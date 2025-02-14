<template>
  <div class="register-container">
    <div class="register-form card">
      <h2>펫시터 등록</h2>
      <form @submit.prevent="handleSubmit">
        <!-- 위치 입력 -->
        <div class="form-group">
          <label for="location">활동 지역</label>
          <input 
            type="text" 
            id="location" 
            v-model="formData.location"
            required
            placeholder="예: 서울시 강남구"
          >
        </div>

        <!-- 돌봄 가능한 반려동물 종류 -->
        <div class="form-group">
          <label>돌봄 가능한 반려동물</label>
          <div class="checkbox-group pets">
            <div class="pet-type">
              <input 
                type="checkbox" 
                id="dog"
                v-model="formData.petTypes" 
                value="강아지"
              >
              <label for="dog">강아지</label>
            </div>
            <div class="pet-type">
              <input 
                type="checkbox" 
                id="cat"
                v-model="formData.petTypes" 
                value="고양이"
              >
              <label for="cat">고양이</label>
            </div>
            <div class="pet-type">
              <input 
                type="checkbox" 
                id="bird"
                v-model="formData.petTypes" 
                value="새"
              >
              <label for="bird">새</label>
            </div>
            <div class="pet-type">
              <input 
                type="checkbox" 
                id="reptile"
                v-model="formData.petTypes" 
                value="파충류"
              >
              <label for="reptile">파충류</label>
            </div>
            <div class="pet-type">
              <input 
                type="checkbox" 
                id="other"
                v-model="formData.petTypes" 
                value="기타"
              >
              <label for="other">기타</label>
            </div>
          </div>
        </div>

        <!-- 주요 서비스 -->
        <div class="form-group">
          <label>제공 가능한 서비스</label>
          <div class="checkbox-group">
            <label class="checkbox-label">
              <input 
                type="checkbox" 
                v-model="formData.services" 
                value="산책"
              > 산책
            </label>
            <label class="checkbox-label">
              <input 
                type="checkbox" 
                v-model="formData.services" 
                value="방문돌봄"
              > 방문돌봄
            </label>
            <label class="checkbox-label">
              <input 
                type="checkbox" 
                v-model="formData.services" 
                value="위탁돌봄"
              > 위탁돌봄
            </label>
          </div>
        </div>

        <!-- 돌봄 가능 시간 -->
        <div class="form-group time-range">
          <label>돌봄 가능 시간</label>
          <div class="time-inputs">
            <div class="time-input">
              <label for="startTime">시작 시간</label>
              <select 
                id="startTime" 
                v-model="formData.startTime"
                required
              >
                <option v-for="hour in 24" :key="`start-${hour}`" :value="hour-1">
                  {{ String(hour-1).padStart(2, '0') }}:00
                </option>
              </select>
            </div>
            <span class="time-separator">~</span>
            <div class="time-input">
              <label for="endTime">종료 시간</label>
              <select 
                id="endTime" 
                v-model="formData.endTime"
                required
              >
                <option v-for="hour in 24" :key="`end-${hour}`" :value="hour-1">
                  {{ String(hour-1).padStart(2, '0') }}:00
                </option>
              </select>
            </div>
          </div>
        </div>

        <!-- 요금 설정 -->
        <div class="form-group">
          <label for="fee">시간당 요금 (원)</label>
          <input 
            type="number" 
            id="fee" 
            v-model="formData.fee"
            required
            min="0"
            step="1000"
            placeholder="예: 15000"
          >
        </div>

        <div v-if="submitStatus.error" class="error-message">
          {{ submitStatus.error }}
        </div>
        <button 
          type="submit" 
          class="submit-btn" 
          :disabled="!isFormValid || submitStatus.loading"
        >
          {{ submitStatus.loading ? '등록 중...' : '펫시터 등록하기' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'PetsitterRegister',
  data() {
    return {
      formData: {
        location: '',
        petTypes: [],
        services: [],
        startTime: 9,
        endTime: 18,
        fee: null
      },
      submitStatus: {
        loading: false,
        error: null
      }
    }
  },
  computed: {
    ...mapState(['userInfo']),
    isFormValid() {
      return this.formData.location && 
             this.formData.petTypes.length > 0 && 
             this.formData.services.length > 0 &&
             this.formData.fee > 0 &&
             this.formData.startTime < this.formData.endTime
    }
  },
  methods: {
    async handleSubmit() {
      if (!this.isFormValid) return

      this.submitStatus.loading = true
      this.submitStatus.error = null

      try {
        const requestData = {
          memberId: this.userInfo.id,
          location: this.formData.location,
          pets: this.formData.petTypes,
          services: this.formData.services,
          startTime: parseInt(this.formData.startTime),
          endTime: parseInt(this.formData.endTime),
          fee: parseInt(this.formData.fee)
        }

        const response = await axios.post('/api/v1/petsitter', requestData)
        
        // store에 펫시터 정보 저장
        this.$store.dispatch('registerPetSitter', response.data)
        
        // 등록 성공 시 스케줄 페이지로 이동
        this.$router.push('/petsitter/info')
      } catch (error) {
        console.error('펫시터 등록 실패:', error)
        this.submitStatus.error = '펫시터 등록에 실패했습니다. 다시 시도해주세요.'
      } finally {
        this.submitStatus.loading = false
      }
    }
  }
}
</script>

<style scoped>
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

h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

.form-group {
  margin-bottom: 1.5rem;
}

.checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-top: 0.5rem;
}

.checkbox-group.pets {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  margin-top: 0.5rem;
}

.pet-type {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 100px;
}

.pet-type input[type="checkbox"] {
  margin: 0;
}

.pet-type label {
  margin: 0;
  cursor: pointer;
  white-space: nowrap;
}

.time-range {
  margin-top: 1.5rem;
}

.time-inputs {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-top: 0.5rem;
}

.time-input {
  flex: 1;
}

.time-separator {
  margin: 0 0.5rem;
  color: #666;
}

select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  background-color: white;
}

.submit-btn {
  width: 100%;
  padding: 1rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  margin-top: 1rem;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background: #0056b3;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.error-message {
  color: #dc3545;
  margin-top: 1rem;
  text-align: center;
}

@media (max-width: 768px) {
  .register-container {
    padding: 1rem;
  }

  .checkbox-group {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 1rem;
  }

  .checkbox-label {
    min-width: auto;
  }

  .time-inputs {
    flex-direction: column;
    gap: 1rem;
  }

  .time-separator {
    display: none;
  }
}
</style> 