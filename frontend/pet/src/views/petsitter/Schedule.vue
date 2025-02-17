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
      <div v-for="schedule in filteredSchedules" :key="schedule.id" class="schedule-item">
        <div class="time">
          {{ formatTime(schedule.startTime) }} ~ {{ formatTime(schedule.endTime) }}
        </div>
        <div class="schedule-actions">
          <button @click="editSchedule(schedule)" class="edit-btn">일정 변경</button>
          <button @click="deleteSchedule(schedule.id)" class="delete-btn">일정 삭제</button>
        </div>
        <div class="reservations">
          <div v-for="reservation in schedule.reservations" :key="reservation.id" class="reservation-card">
            <div class="reservation-info">
              <span :class="['status', getStatusClass(reservation.status)]">{{ getStatusKorean(reservation.status) }}</span>
              <span class="member">{{ reservation.member.name }}</span>
              <span class="pets">{{ reservation.pets.map(pet => pet.name).join(', ') }}</span>
              <span class="time">{{ formatTime(reservation.startTime) }} ~ {{ formatTime(reservation.endTime) }}</span>
            </div>
            <div class="reservation-actions">
              <button v-if="reservation.status === 'REQUEST'" @click="approveReservation(reservation.id, schedule.id)" class="accept-btn">수락</button>
              <button v-if="reservation.status === 'REQUEST'" @click="rejectReservation(reservation.id, schedule.id)" class="reject-btn">거절</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="schedule-actions" v-if="!filteredSchedules.length">
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
            <label for="fee">시간당 요금 (원)</label>
            <input 
              type="number" 
              id="fee"
              v-model="newSchedule.fee"
              required
              placeholder="예: 15000"
            >
          </div>

          <div class="modal-actions">
            <button type="submit" class="submit-btn">등록</button>
            <button type="button" class="cancel-btn" @click="showAddScheduleForm = false">취소</button>
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

    <div v-if="selectedSchedule" class="schedule-actions">
      <button @click="showUpdateModal" class="update-button">일정 변경</button>
    </div>

    <!-- 일정 변경 모달 -->
    <div v-if="isUpdateModalOpen" class="modal-overlay">
      <div class="update-modal">
        <h2>일정 변경</h2>
        <div class="form-group">
          <label>시작 시간</label>
          <select v-model="updateForm.startTime" class="time-select">
            <option v-for="hour in 24" :key="`start-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>종료 시간</label>
          <select v-model="updateForm.endTime" class="time-select">
            <option v-for="hour in 24" :key="`end-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>
        <div class="modal-buttons">
          <button @click="updateSchedule" class="submit-button">변경하기</button>
          <button @click="closeUpdateModal" class="cancel-button">취소</button>
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
      selectedDate: null,
      schedules: [],
      showAddScheduleForm: false,
      newSchedule: {
        date: '',
        startTime: 0,
        endTime: 0,
        fee: 0
      },
      selectedReservation: null,
      isUpdateModalOpen: false,
      updateForm: {
        id: null,
        startTime: 0,
        endTime: 23
      }
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
        this.newSchedule.startTime !== null &&
        this.newSchedule.endTime !== null &&
        this.newSchedule.fee > 0
      )
    },
    filteredSchedules() {
      return this.schedules.filter(schedule => schedule.date === this.selectedDate)
    },
    petSitterId() {
      return this.$store.state.userInfo.id
    }
  },
  methods: {
    async selectDate(date) {
      this.selectedDate = date
      await this.fetchSchedules()
    },
    async fetchSchedules() {
      if (!this.selectedDate) return

      try {
        const formattedDate = format(new Date(this.selectedDate), 'yyyy-MM-dd')
        const response = await axios.get(`/api/v1/schedule/list/${this.petSitterId}/${formattedDate}`)
        this.schedules = response.data
        console.log('스케줄 불러오기 성공:', this.schedules)
      } catch (error) {
        console.error('스케줄 불러오기 실패:', error)
        // 에러 처리 로직 추가 가능
      }
    },
    formatDate(date) {
      return format(new Date(date), 'MM.dd')
    },
    formatFullDate(date) {
      return format(new Date(date), 'yyyy년 M월 d일')
    },
    formatTime(hour) {
      return `${String(hour).padStart(2, '0')}:00`
    },
    movePreviousWeek() {
      this.weekStart = addDays(this.weekStart, -7)
    },
    moveNextWeek() {
      this.weekStart = addDays(this.weekStart, 7)
    },
    async approveReservation(reservationId, scheduleId) {
      try {
        const requestData = {
          reservationId,
          scheduleId,
          petSitterId: this.$store.state.userInfo.id
        }
        await axios.patch('/api/v1/reservations/approve', requestData)
        console.log('예약 수락 성공')
        this.fetchSchedules()
      } catch (error) {
        console.error('예약 수락 실패:', error)
      }
    },
    async rejectReservation(reservationId, scheduleId) {
      try {
        const requestData = {
          reservationId,
          scheduleId,
          petSitterId: this.$store.state.userInfo.id
        }
        await axios.patch('/api/v1/reservations/reject', requestData)
        console.log('예약 거절 성공')
        this.fetchSchedules()
      } catch (error) {
        console.error('예약 거절 실패:', error)
      }
    },
    async handleAddSchedule() {
      try {
        const requestData = {
          petSitterId: this.petSitterId,
          date: this.newSchedule.date,
          startTime: this.newSchedule.startTime,
          endTime: this.newSchedule.endTime,
          timeFee: this.newSchedule.fee
        }

        const response = await axios.post('/api/v1/schedule', requestData)
        console.log('일정 등록 성공:', response.data)
        this.showAddScheduleForm = false
        await this.fetchSchedules()
      } catch (error) {
        console.error('일정 등록 실패:', error)
      }
    },
    showReservationDetail(reservation) {
      this.selectedReservation = reservation;
    },
    getStatusKorean(status) {
      const statusMap = {
        REQUEST: '요청',
        APPROVE: '승인',
        PAID: '결제 완료',
        REJECT: '거절',
        CANCEL: '취소'
      }
      return statusMap[status] || '알 수 없음'
    },
    getStatusClass(status) {
      return {
        REQUEST: 'status-request',
        APPROVE: 'status-approve',
        PAID: 'status-paid',
        REJECT: 'status-reject',
        CANCEL: 'status-cancel'
      }[status] || ''
    },
    editSchedule(schedule) {
      this.selectedSchedule = schedule;
      this.showUpdateModal();
    },
    async deleteSchedule(scheduleId) {
      try {
        await axios.delete(`/api/v1/schedule/${scheduleId}`)
        console.log('일정 삭제 성공')
        await this.fetchSchedules()
      } catch (error) {
        console.error('일정 삭제 실패:', error)
      }
    },
    showUpdateModal() {
      this.updateForm.id = this.selectedSchedule.id;
      this.updateForm.startTime = this.selectedSchedule.startTime;
      this.updateForm.endTime = this.selectedSchedule.endTime;
      this.isUpdateModalOpen = true;
    },
    closeUpdateModal() {
      this.isUpdateModalOpen = false;
      this.updateForm = {
        id: null,
        startTime: 0,
        endTime: 23
      };
    },
    async updateSchedule() {
      try {
        const response = await axios.put('/api/v1/schedule', this.updateForm);
        this.closeUpdateModal();
        await this.fetchSchedules();
      } catch (error) {
        console.error('일정 변경 실패:', error);
      }
    }
  },
  mounted() {
    this.fetchSchedules()
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

.schedule-item {
  margin-bottom: 2rem;
}

.time {
  font-weight: 500;
}

.schedule-actions {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.edit-btn, .delete-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-btn:hover, .delete-btn:hover {
  background-color: #0056b3;
}

.reservations {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.reservation-card {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.reservation-info {
  display: flex;
  gap: 1rem;
}

.status {
  font-weight: 500;
}

.status-request {
  color: #ffc107;
}

.status-approve {
  color: #28a745;
}

.status-paid {
  color: #17a2b8;
}

.status-reject {
  color: #dc3545;
}

.status-cancel {
  color: #6c757d;
}

.reservation-actions {
  display: flex;
  gap: 0.5rem;
}

.accept-btn, .reject-btn {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.accept-btn:hover {
  background-color: #218838;
}

.reject-btn {
  background-color: #dc3545;
}

.reject-btn:hover {
  background-color: #c82333;
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

.update-modal {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.time-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.submit-button {
  padding: 0.5rem 1.5rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button {
  padding: 0.5rem 1.5rem;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.update-button {
  padding: 0.5rem 1rem;
  background-color: #17a2b8;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 1rem;
}

.schedule-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
}
</style> 