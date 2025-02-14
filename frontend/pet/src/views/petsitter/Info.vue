<template>
  <div class="petsitter-info-container">
    <div v-if="!petSitterInfo" class="loading-state">
      <p>펫시터 정보를 불러오는 중입니다...</p>
    </div>
    <div v-else class="info-card">
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
        <button @click="handleEdit" class="edit-button">정보 수정</button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'PetSitterInfo',
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
    formatTime(hour) {
      return `${String(hour).padStart(2, '0')}:00`
    },
    formatPrice(price) {
      return price.toLocaleString()
    },
    handleEdit() {
      this.$router.push('/petsitter/edit')
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
  margin-top: 2rem;
  text-align: center;
}

.edit-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 1rem 3rem;
  border-radius: 6px;
  font-size: 1.2rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
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
</style> 