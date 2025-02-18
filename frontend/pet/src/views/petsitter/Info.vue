<template>
  <div class="petsitter-info-container">
    <div v-if="!petSitterInfo" class="loading-state">
      <p>펫시터 정보를 불러오는 중입니다...</p>
    </div>
    <div v-else>
      <!-- 정보 표시 부분 -->
      <div v-if="!isEditing" class="info-card">
        <h2>펫시터 정보</h2>
        
        <div class="info-section">
          <h3>활동 지역</h3>
          <p class="info-text">{{ petSitterInfo.location }}</p>
        </div>

        <div class="info-section">
          <h3>돌봄 가능한 반려동물</h3>
          <div class="tags">
            <span v-for="species in petSitterInfo.species" 
                  :key="species.id" 
                  class="tag">
              {{ species.name }}
            </span>
          </div>
        </div>

        <div class="info-section">
          <h3>제공 서비스</h3>
          <div class="tags">
            <span v-for="service in petSitterInfo.services" 
                  :key="service.serviceId" 
                  class="tag">
              {{ service.name }}
            </span>
          </div>
        </div>

        <div class="info-section">
          <h3>활동 시간</h3>
          <p class="info-text">{{ formatTime(petSitterInfo.startTime) }} ~ {{ formatTime(petSitterInfo.endTime) }}</p>
        </div>

        <div class="info-section">
          <h3>시간당 요금</h3>
          <p class="info-text">{{ formatPrice(petSitterInfo.fee) }}원</p>
        </div>

        <div class="info-section">
          <h3>인증 상태</h3>
          <p :class="['certification-status', certificationClass]">
            {{ certificationText }}
          </p>
        </div>

        <div class="button-section">
          <button @click="startEdit" class="edit-button">정보 수정</button>
          <button @click="deletePetSitter" class="delete-button">펫시터 삭제</button>
        </div>
      </div>

      <!-- 수정 폼 -->
      <div v-else class="register-container">
        <div class="register-form">
          <h2>펫시터 정보 수정</h2>
          <form @submit.prevent="updateInfo">
            <div class="form-section">
              <h3>기본 정보</h3>
              <div class="form-group">
                <label>활동 지역</label>
                <input 
                  type="text" 
                  v-model="editForm.location" 
                  placeholder="활동 가능한 지역을 입력하세요"
                  required
                >
              </div>
            </div>

            <div class="form-section">
              <h3>돌봄 가능한 반려동물</h3>
              <div class="pet-types">
                <label v-for="pet in availablePets" :key="pet.id" class="pet-type-item">
                  <input 
                    type="checkbox" 
                    :value="pet.id" 
                    v-model="editForm.pets"
                  >
                  <span class="checkbox-label">{{ pet.name }}</span>
                </label>
              </div>
            </div>

            <div class="form-section">
              <h3>제공 가능한 서비스</h3>
              <div class="service-types">
                <label v-for="service in availableServices" :key="service.id" class="service-type-item">
                  <input 
                    type="checkbox" 
                    :value="service.id" 
                    v-model="editForm.services"
                  >
                  <span class="checkbox-label">{{ service.name }}</span>
                </label>
              </div>
            </div>

            <div class="form-section">
              <h3>활동 시간</h3>
              <div class="time-range">
                <select v-model="editForm.startTime">
                  <option v-for="hour in 24" :key="`start-${hour-1}`" :value="hour-1">
                    {{ formatTime(hour-1) }}
                  </option>
                </select>
                <span class="time-separator">~</span>
                <select v-model="editForm.endTime">
                  <option v-for="hour in 24" :key="`end-${hour-1}`" :value="hour-1">
                    {{ formatTime(hour-1) }}
                  </option>
                </select>
              </div>
            </div>

            <div class="form-section">
              <h3>시간당 요금</h3>
              <div class="form-group">
                <input 
                  type="number" 
                  v-model="editForm.fee" 
                  placeholder="시간당 요금을 입력하세요"
                  required 
                  min="0"
                >
                <span class="currency">원</span>
              </div>
            </div>

            <div class="button-group">
              <button type="submit" class="submit-button">수정 완료</button>
              <button type="button" @click="cancelEdit" class="cancel-button">취소</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'PetSitterInfo',
  data() {
    return {
      isEditing: false,
      editForm: {
        sitterId: null,
        location: '',
        pets: [],
        services: [],
        startTime: 0,
        endTime: 23,
        fee: 0
      },
      availablePets: [
        { id: '강아지', name: '강아지' },
        { id: '고양이', name: '고양이' },
        { id: '새', name: '새' },
        { id: '파충류', name: '파충류' },
        { id: '기타', name: '기타' }
      ],
      availableServices: [
        { id: '산책', name: '산책' },
        { id: '위탁돌봄', name: '위탁돌봄' },
        { id: '방문돌봄', name: '방문돌봄' }
      ]
    }
  },
  async created() {
    // 컴포넌트 생성 시 펫시터 정보 조회
    await this.$store.dispatch('fetchPetSitterInfo')
  },
  computed: {
    ...mapState(['petSitterInfo']),
    certificationClass() {
      if (!this.petSitterInfo || !this.petSitterInfo.certified) return ''
      return this.petSitterInfo.certified.toLowerCase()
    },
    certificationText() {
      if (!this.petSitterInfo || !this.petSitterInfo.certified) return '미인증'
      const statusMap = {
        PENDING: '인증 대기중',
        CERTIFIED: '인증 완료',
        REJECTED: '인증 거절'
      }
      return statusMap[this.petSitterInfo.certified] || '미인증'
    }
  },
  methods: {
    startEdit() {
      // 현재 정보를 수정 폼에 설정
      this.editForm = {
        sitterId: Number(this.petSitterInfo.id),
        location: this.petSitterInfo.location,
        pets: this.petSitterInfo.species.map(s => s.name),
        services: this.petSitterInfo.services.map(s => s.name),
        startTime: this.petSitterInfo.startTime,
        endTime: this.petSitterInfo.endTime,
        fee: this.petSitterInfo.timeFee
      };
      this.isEditing = true;
      
      console.log('수정 폼 데이터:', this.editForm);
    },
    cancelEdit() {
      this.isEditing = false;
    },
    async updateInfo() {
      try {
        const requestData = {
          sitterId: this.petSitterInfo.petSitterId,  // memberId -> sitterId로 변경
          location: this.editForm.location,
          pets: this.editForm.pets,
          services: this.editForm.services,
          startTime: this.editForm.startTime,
          endTime: this.editForm.endTime,
          fee: this.editForm.fee
        };

        console.log('요청 데이터:', requestData);

        await axios.put('/api/v1/petsitter', requestData);
        this.isEditing = false;
        await this.$store.dispatch('fetchPetSitterInfo');
      } catch (error) {
        console.error('펫시터 정보 수정 실패:', error);
      }
    },
    formatTime(hour) {
      return `${String(hour).padStart(2, '0')}:00`
    },
    formatPrice(price) {
      return price.toLocaleString()
    },
    async deletePetSitter() {
      if (confirm('정말 펫시터를 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        try {
          await axios.delete(`/api/v1/petsitter/${this.petSitterInfo.petSitterId}`);
          this.$store.dispatch('clearPetSitterInfo');
          this.$router.push('/'); // 홈으로 이동
        } catch (error) {
          console.error('펫시터 삭제 실패:', error);
          alert('펫시터 삭제에 실패했습니다.');
        }
      }
    }
  }
}
</script>

<style scoped>
.petsitter-info-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.info-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 2.5rem;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
}

h2 {
  grid-column: 1 / -1;
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
  font-size: 1.8rem;
}

.info-section {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 1.5rem;
}

.info-section h3 {
  color: #333;
  font-size: 1.3rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.info-text {
  font-size: 1.1rem;
  color: #444;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
}

.tag {
  background: #e9ecef;
  padding: 0.8rem 1.5rem;
  border-radius: 25px;
  font-size: 1.1rem;
  color: #495057;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.certification-status {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 500;
}

.certification-status.PENDING {
  background: #ffd700;
  color: #856404;
}

.certification-status.CERTIFIED {
  background: #28a745;
  color: white;
}

.certification-status.REJECTED {
  background: #dc3545;
  color: white;
}

.button-section {
  grid-column: 1 / -1;  /* 버튼 섹션이 전체 너비를 차지하도록 */
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.edit-button, .delete-button {
  padding: 1rem 2rem;
  border: none;
  border-radius: 6px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-button {
  background-color: #007bff;
  color: white;
}

.edit-button:hover {
  background-color: #0056b3;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.delete-button:hover {
  background-color: #c82333;
}

@media (max-width: 768px) {
  .petsitter-info-container {
    padding: 1rem;
  }
  
  .info-card {
    grid-template-columns: 1fr;
    padding: 1.5rem;
    gap: 1rem;
  }
  
  .info-section {
    padding: 1rem;
  }
}

.loading-state {
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.loading-state p {
  color: #666;
  font-size: 1.1rem;
}

.register-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.register-form {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 2rem;
}

.form-section h3 {
  color: #333;
  margin-bottom: 1rem;
  font-size: 1.2rem;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
}

.form-group input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.pet-types, .service-types {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.pet-type-item, .service-type-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.checkbox-label {
  color: #555;
}

.time-range {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.time-range select {
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.time-separator {
  color: #666;
}

.currency {
  margin-left: 0.5rem;
  color: #666;
}

.button-group {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.submit-button, .cancel-button {
  padding: 1rem 2rem;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-button {
  background-color: #28a745;
  color: white;
  flex: 1;
}

.submit-button:hover {
  background-color: #218838;
}

.cancel-button {
  background-color: #6c757d;
  color: white;
  flex: 1;
}

.cancel-button:hover {
  background-color: #5a6268;
}
</style> 