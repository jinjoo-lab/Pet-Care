<template>
  <div class="schedule-container">
    <div class="week-navigator">
      <button @click="movePreviousWeek" class="nav-btn">
        <i class="fas fa-chevron-left"></i>
      </button>
      <div class="week-dates">
        <div 
          v-for="date in weekDates" 
          :key="date.date"
          class="date-item"
          :class="{ active: selectedDate === date.date }"
          @click="selectDate(date.date)"
        >
          <span class="date">{{ formatDate(date.date) }}</span>
          <span class="day">({{ date.day }})</span>
        </div>
      </div>
      <button @click="moveNextWeek" class="nav-btn">
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <div v-if="selectedDate" class="schedule-details">
      <h3>{{ formatFullDate(selectedDate) }} 일정</h3>
      <div class="schedule-list">
        <div v-if="!filteredSchedules.length" class="no-schedule">
          예약된 일정이 없습니다.
        </div>
        <div v-for="schedule in filteredSchedules" :key="schedule.id" class="schedule-item">
          <div class="time">
            {{ formatTime(schedule.startTime) }} ~ {{ formatTime(schedule.endTime) }}
          </div>
          <div class="status-actions">
            <button 
              v-if="schedule.status === '예약요청'" 
              class="action-btn accept"
              @click="handleAccept(schedule.id)"
            >
              수락
            </button>
            <button 
              v-if="schedule.status === '예약요청'"
              class="action-btn reject"
              @click="handleReject(schedule.id)"
            >
              거절
            </button>
            <button 
              class="action-btn"
              :class="schedule.status"
              @click="showReservationDetail(schedule)"
            >
              {{ schedule.status }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="schedule-actions">
      <button @click="showAddScheduleForm = true" class="add-schedule-btn">
        일정 등록
      </button>
    </div>

    <div v-if="showAddScheduleForm" class="modal-overlay">
      <div class="modal-content">
        <h3>일정 등록</h3>
        <form @submit.prevent="handleAddSchedule" class="schedule-form">
          <div class="form-group">
            <label for="scheduleDate">날짜</label>
            <input 
              type="date" 
              id="scheduleDate"
              v-model="newSchedule.date"
              required
            >
          </div>
          
          <div class="form-group">
            <label>제공 서비스</label>
            <div class="checkbox-group">
              <label class="checkbox-label">
                <input 
                  type="checkbox" 
                  v-model="newSchedule.services" 
                  value="산책"
                > 산책
              </label>
              <label class="checkbox-label">
                <input 
                  type="checkbox" 
                  v-model="newSchedule.services" 
                  value="방문돌봄"
                > 방문 돌봄
              </label>
              <label class="checkbox-label">
                <input 
                  type="checkbox" 
                  v-model="newSchedule.services" 
                  value="위탁돌봄"
                > 위탁 돌봄
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label for="startTime">시작 시간</label>
            <select 
              id="startTime" 
              v-model="newSchedule.startTime"
              required
            >
              <option v-for="hour in 24" :key="`start-${hour}`" :value="hour-1">
                {{ String(hour-1).padStart(2, '0') }}:00
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="endTime">종료 시간</label>
            <select 
              id="endTime" 
              v-model="newSchedule.endTime"
              required
            >
              <option v-for="hour in 24" :key="`end-${hour}`" :value="hour-1">
                {{ String(hour-1).padStart(2, '0') }}:00
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="price">시간당 요금 (원)</label>
            <input 
              type="number" 
              id="price" 
              v-model="newSchedule.price"
              required
              min="0"
              step="1000"
              placeholder="예: 15000"
            >
          </div>

          <div class="modal-actions">
            <button type="submit" class="submit-btn" :disabled="!isScheduleFormValid">등록</button>
            <button 
              type="button" 
              @click="showAddScheduleForm = false" 
              class="cancel-btn"
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 예약 상세 정보 모달 -->
    <div v-if="selectedReservation" class="modal-overlay">
      <div class="modal-content reservation-detail">
        <h3>예약 상세 정보</h3>
        <div class="detail-content">
          <div class="detail-item">
            <span class="label">예약자</span>
            <span>{{ selectedReservation.userName }}</span>
          </div>
          <div class="detail-item">
            <span class="label">반려동물</span>
            <span>{{ selectedReservation.petName }} ({{ selectedReservation.petType }})</span>
          </div>
          <div class="detail-item">
            <span class="label">서비스</span>
            <span>{{ selectedReservation.services.join(', ') }}</span>
          </div>
          <div class="detail-item">
            <span class="label">시간</span>
            <span>{{ formatTime(selectedReservation.startTime) }} ~ {{ formatTime(selectedReservation.endTime) }}</span>
          </div>
          <div class="detail-item">
            <span class="label">요금</span>
            <span>{{ selectedReservation.price.toLocaleString() }}원</span>
          </div>
          <div class="detail-item">
            <span class="label">특이사항</span>
            <p class="note">{{ selectedReservation.note || '없음' }}</p>
          </div>
        </div>
        <div class="modal-actions">
          <button class="close-btn" @click="selectedReservation = null">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { format, addDays, startOfWeek } from 'date-fns'
import { ko } from 'date-fns/locale'

export default {
  name: 'PetsitterSchedule',
  data() {
    return {
      weekStart: startOfWeek(new Date(), { weekStartsOn: 1 }),
      selectedDate: format(new Date(), 'yyyy-MM-dd'),
      schedules: [
        {
          id: 1,
          startTime: 9,
          endTime: 11,
          status: '예약요청',
          services: ['산책', '방문돌봄'],
          price: 15000,
          userName: '김철수',
          petName: '멍멍이',
          petType: '강아지',
          note: '산책을 매우 좋아해요'
        },
        {
          id: 2,
          startTime: 11,
          endTime: 13,
          status: '예약요청',
          services: ['위탁돌봄'],
          price: 20000,
          userName: '이영희',
          petName: '나비',
          petType: '고양이',
          note: '낯을 많이 가리는 편이에요'
        },
        {
          id: 3,
          startTime: 13,
          endTime: 15,
          status: '예약거절',
          services: ['산책'],
          price: 12000
        },
        {
          id: 4,
          startTime: 15,
          endTime: 17,
          status: '예약확정',
          services: ['방문돌봄', '산책'],
          price: 18000,
          userName: '박지민',
          petName: '해피',
          petType: '강아지',
          note: '간식을 매우 좋아합니다'
        },
        {
          id: 5,
          startTime: 17,
          endTime: 19,
          status: '예약요청',
          services: ['위탁돌봄'],
          price: 25000,
          userName: '최수진',
          petName: '달이',
          petType: '고양이',
          note: '특별한 약을 복용중입니다'
        },
        {
          id: 6,
          startTime: 10,
          endTime: 12,
          status: '예약확정',
          services: ['산책'],
          price: 13000
        },
        {
          id: 7,
          startTime: 14,
          endTime: 16,
          status: '예약요청',
          services: ['방문돌봄'],
          price: 16000
        },
        {
          id: 8,
          startTime: 16,
          endTime: 18,
          status: '예약확정',
          services: ['위탁돌봄', '산책'],
          price: 22000
        },
        {
          id: 9,
          startTime: 12,
          endTime: 14,
          status: '예약거절',
          services: ['방문돌봄'],
          price: 14000
        },
        {
          id: 10,
          startTime: 18,
          endTime: 20,
          status: '예약요청',
          services: ['위탁돌봄', '방문돌봄'],
          price: 23000
        }
      ],
      showAddScheduleForm: false,
      newSchedule: {
        date: '',
        services: [],
        startTime: 9,
        endTime: 18,
        price: null
      },
      selectedReservation: null
    }
  },
  computed: {
    weekDates() {
      return Array.from({ length: 7 }, (_, i) => {
        const date = addDays(this.weekStart, i)
        return {
          date: format(date, 'yyyy-MM-dd'),
          day: format(date, 'E', { locale: ko }).slice(0, 1) // 요일 한글자
        }
      })
    },
    isScheduleFormValid() {
      return (
        this.newSchedule.date &&
        this.newSchedule.services.length > 0 &&
        this.newSchedule.startTime !== null &&
        this.newSchedule.endTime !== null &&
        this.newSchedule.price > 0
      )
    },
    filteredSchedules() {
      return this.schedules.filter(schedule => schedule.status !== '예약거절')
    }
  },
  methods: {
    formatDate(date) {
      return format(new Date(date), 'M.d')
    },
    formatFullDate(date) {
      return format(new Date(date), 'yyyy년 M월 d일')
    },
    formatTime(time) {
      return `${String(time).padStart(2, '0')}:00`
    },
    selectDate(date) {
      this.selectedDate = date
      this.fetchSchedules(date)
    },
    movePreviousWeek() {
      this.weekStart = addDays(this.weekStart, -7)
    },
    moveNextWeek() {
      this.weekStart = addDays(this.weekStart, 7)
    },
    async fetchSchedules(date) {
      try {
        const response = await axios.get(`/api/v1/petsitter/schedules/${date}`)
        this.schedules = response.data
      } catch (error) {
        console.error('일정 조회 실패:', error)
      }
    },
    async handleAccept(scheduleId) {
      try {
        await axios.post(`/api/v1/petsitter/schedules/${scheduleId}/accept`)
        await this.fetchSchedules(this.selectedDate)
      } catch (error) {
        console.error('예약 수락 실패:', error)
      }
    },
    async handleReject(scheduleId) {
      try {
        await axios.post(`/api/v1/petsitter/schedules/${scheduleId}/reject`)
        await this.fetchSchedules(this.selectedDate)
      } catch (error) {
        console.error('예약 거절 실패:', error)
      }
    },
    async handleAddSchedule() {
      try {
        await axios.post('/api/v1/petsitter/schedules', this.newSchedule)
        this.showAddScheduleForm = false
        await this.fetchSchedules(this.selectedDate)
      } catch (error) {
        console.error('일정 등록 실패:', error)
      }
    },
    showReservationDetail(reservation) {
      this.selectedReservation = reservation;
    }
  },
  mounted() {
    this.fetchSchedules(this.selectedDate)
  }
}
</script>

<style scoped>
.schedule-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.week-navigator {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.week-dates {
  display: flex;
  gap: 1rem;
  flex: 1;
  justify-content: space-between;
}

.date-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.date-item:hover {
  background-color: #f0f0f0;
}

.date-item.active {
  background-color: #007bff;
  color: white;
}

.nav-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.5rem;
  color: #666;
}

.nav-btn:hover {
  color: #333;
}

.schedule-details {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-height: 500px;
  overflow-y: auto;
}

.schedule-list {
  margin-top: 1rem;
}

.no-schedule {
  text-align: center;
  color: #666;
  padding: 2rem;
}

.schedule-item {
  display: flex;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #eee;
  gap: 1rem;
  min-height: 60px;
}

.time {
  flex: 1;
  font-weight: 500;
}

.status-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  font-size: 0.9rem;
  min-width: 80px;
}

.action-btn:disabled {
  cursor: default;
}

.action-btn.accept {
  background-color: #28a745;
  color: white;
}

.action-btn.reject {
  background-color: #dc3545;
  color: white;
}

.action-btn.예약요청 {
  background-color: #ffd700;
  cursor: default;
}

.action-btn.예약확정 {
  background-color: #28a745;
  color: white;
  cursor: default;
}

.schedule-actions {
  margin-top: 2rem;
  display: flex;
  justify-content: center;
}

.add-schedule-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-schedule-btn:hover {
  background-color: #0056b3;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
}

.schedule-form {
  margin-top: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.submit-btn,
.cancel-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn {
  background-color: #28a745;
  color: white;
}

.submit-btn:hover {
  background-color: #218838;
}

.cancel-btn {
  background-color: #6c757d;
  color: white;
}

.cancel-btn:hover {
  background-color: #5a6268;
}

.checkbox-group {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: nowrap;
  gap: 0;
  margin-top: 0.5rem;
  width: 100%;
  max-width: 350px;
  margin-left: 0;
  margin-right: auto;
}

.checkbox-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  white-space: nowrap;
  min-width: fit-content;
  padding-right: 10px;
}

.checkbox-label input[type="checkbox"] {
  margin: 0;
  margin-right: 6px;
}

@media (max-width: 768px) {
  .schedule-container {
    padding: 1rem;
  }

  .week-dates {
    gap: 0.5rem;
  }

  .date-item {
    padding: 0.5rem;
  }

  .schedule-item {
    flex-direction: row;
    align-items: center;
  }

  .status-actions {
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }

  .action-btn {
    width: auto;
    margin-top: 0;
  }

  .modal-content {
    width: 95%;
    padding: 1.5rem;
  }

  .modal-actions {
    flex-direction: column;
  }

  .submit-btn,
  .cancel-btn {
    width: 100%;
  }

  .checkbox-group {
    max-width: 300px;
  }
  
  .checkbox-label {
    font-size: 0.9rem;
  }

  .schedule-details {
    max-height: 400px;
    padding: 1rem;
  }
}

/* 스크롤바 스타일링 */
.schedule-details::-webkit-scrollbar {
  width: 8px;
}

.schedule-details::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.schedule-details::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.schedule-details::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.reservation-detail .detail-content {
  margin: 1.5rem 0;
}

.detail-item {
  margin-bottom: 1rem;
  display: flex;
  gap: 1rem;
}

.detail-item .label {
  font-weight: 500;
  min-width: 80px;
  color: #666;
}

.detail-item .note {
  margin: 0;
  white-space: pre-line;
}

.close-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.close-btn:hover {
  background-color: #5a6268;
}
</style> 