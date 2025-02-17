<template>
  <div class="find-sitter-container">
    <h1>시터 찾기</h1>
    
    <!-- 내 예약 목록 섹션 -->
    <div class="my-reservations-section">
      <h2>내 요청 목록</h2>
      <div class="reservations-table-container">
        <table class="reservations-table">
          <thead>
            <tr>
              <th>날짜</th>
              <th>시작 시간</th>
              <th>종료 시간</th>
              <th>상태</th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reservation in myReservations" :key="reservation.id">
              <td>{{ formatDate(reservation.schedule.date) }}</td>
              <td>{{ formatTime(reservation.startTime) }}</td>
              <td>{{ formatTime(reservation.endTime) }}</td>
              <td>
                <span :class="['status-badge', getStatusClass(reservation.status)]">
                  {{ getStatusText(reservation.status) }}
                </span>
              </td>
              <td class="action-buttons">
                <template v-if="['REQUEST', 'APPROVE'].includes(reservation.status)">
                  <button 
                    @click="cancelReservation(reservation.id)" 
                    class="cancel-action-button"
                  >
                    취소
                  </button>
                </template>
                <template v-if="reservation.status === 'APPROVE'">
                  <button 
                    @click="processPayment(reservation)" 
                    class="payment-button"
                  >
                    결제
                  </button>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 검색 조건 섹션 -->
    <div class="search-section">
      <div class="search-row">
        <div class="search-field">
          <label>위치</label>
          <input type="text" v-model="location" placeholder="서울시 강남구">
        </div>
        
        <div class="search-field">
          <label>날짜</label>
          <input type="date" v-model="date">
        </div>

        <div class="search-field">
          <label>시작 시간</label>
          <select v-model="startTime">
            <option v-for="hour in 24" :key="`start-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>

        <div class="search-field">
          <label>종료 시간</label>
          <select v-model="endTime">
            <option v-for="hour in 24" :key="`end-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>
      </div>
      <div class="button-row">
        <button @click="search" class="search-button">검색</button>
      </div>
    </div>

    <!-- 검색 결과 섹션 -->
    <div class="search-results-container">
      <div v-if="filteredResults.length > 0" class="search-results">
        <div v-for="schedule in filteredResults" :key="schedule.id" class="sitter-card" @click="selectSchedule(schedule)">
          <div class="sitter-info">
            <div class="card-header">
              <h3>{{ schedule.sitter.memberResponse.name }}의 서비스</h3>
              <button class="request-button" @click.stop="requestService(schedule)">요청</button>
            </div>
            <div class="service-details">
              <div class="details-grid">
                <div class="detail-row">
                  <span class="detail-label">위치:</span>
                  <span>{{ schedule.sitter.location }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">시작 시간:</span>
                  <span>{{ formatTime(schedule.startTime) }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">종료 시간:</span>
                  <span>{{ formatTime(schedule.endTime) }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">시간당 요금:</span>
                  <span>{{ schedule.timeFee ? formatPrice(schedule.timeFee) : 0 }}원</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">돌봄 가능:</span>
                  <span>{{ schedule.sitter.species.map(s => s.name).join(', ') }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">제공 서비스:</span>
                  <span>{{ schedule.sitter.services.map(s => s.name).join(', ') }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else-if="searched" class="no-results">
        검색 결과가 없습니다.
      </div>
    </div>

    <!-- 예약 요청 모달 -->
    <div v-if="isRequestModalOpen" class="modal-overlay">
      <div class="request-modal">
        <h2>예약 요청</h2>
        <div class="form-group">
          <label>시작 시간</label>
          <select v-model="reservationForm.startTime" class="time-select">
            <option v-for="hour in 24" :key="`start-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>종료 시간</label>
          <select v-model="reservationForm.endTime" class="time-select">
            <option v-for="hour in 24" :key="`end-${hour-1}`" :value="hour-1">
              {{ formatTime(hour-1) }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>반려동물 선택</label>
          <div class="pets-scroll-container">
            <div class="pet-grid">
              <div 
                v-for="pet in myPets" 
                :key="pet.id" 
                class="pet-card"
                :class="{ 'selected': reservationForm.petList.includes(pet.id) }"
                @click="togglePetSelection(pet.id)"
              >
                <div class="pet-card-content">
                  <div v-if="pet.photo" class="pet-photo">
                    <img :src="pet.photo" :alt="pet.name">
                  </div>
                  <div v-else class="pet-photo default">
                    <span>🐾</span>
                  </div>
                  <span class="pet-name">{{ pet.name }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="form-group description-group">
          <label>요청 설명</label>
          <textarea 
            v-model="reservationForm.description" 
            rows="4" 
            placeholder="특이사항이나 요청사항을 입력해주세요."
          ></textarea>
        </div>
        <div class="modal-buttons">
          <button @click="submitReservation" class="submit-button">예약 요청</button>
          <button @click="closeRequestModal" class="cancel-button">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  name: 'FindSitter',
  data() {
    return {
      location: '',
      date: '',
      startTime: 0,
      endTime: 23,
      searchResults: [],
      searched: false,
      myReservations: [], // 내 예약 목록
      isRequestModalOpen: false,
      selectedSchedule: null,
      myPets: [],
      reservationForm: {
        startTime: 0,
        endTime: 0,
        petList: [],
        description: '',
        date: '',
        memberId: null,
        scheduleId: null
      },
      selectedPets: [],
      selectedServices: [],
      availablePets: [
        { id: '강아지', name: '강아지' },
        { id: '고양이', name: '고양이' },
        { id: '새', name: '새' },
        { id: '파충류', name: '파충류' },
        { id: '기타', name: '기타' }
      ],
      availableServices: [
        { id: '산책', name: '산책' },
        { id: '목욕', name: '목욕' },
        { id: '돌봄', name: '돌봄' }
      ]
    }
  },
  async created() {
    await this.fetchMyReservations();
  },
  methods: {
    async search() {
      try {
        const filters = {
          location: this.location,
          date: this.date,
          startTime: this.startTime,
          endTime: this.endTime
        };

        const response = await axios.post('/api/v1/schedule/list', filters);
        this.searchResults = response.data;
        this.searched = true;
      } catch (error) {
        console.error('시터 검색 실패:', error);
      }
    },
    selectSchedule(schedule) {
      // 스케줄 선택 시 처리할 로직
      console.log('Selected schedule:', schedule);
    },
    formatTime(hour) {
      return `${String(hour).padStart(2, '0')}:00`;
    },
    formatPrice(price) {
      if (!price && price !== 0) return 0;
      return price.toLocaleString();
    },
    async requestService(schedule) {
      this.selectedSchedule = schedule;
      this.reservationForm.scheduleId = schedule.id;
      this.reservationForm.date = this.date;
      await this.fetchMyPets();
      this.isRequestModalOpen = true;
    },
    async fetchMyReservations() {
      try {
        const memberId = this.userInfo.id;
        const response = await axios.get(`/api/v1/reservations/list/${memberId}`);
        this.myReservations = response.data;
      } catch (error) {
        console.error('예약 목록 가져오기 실패:', error);
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    },
    getStatusText(status) {
      const statusMap = {
        REQUEST: '요청',
        APPROVE: '승인',
        PAID: '결제 완료',
        REJECT: '거절',
        CANCEL: '취소'
      };
      return statusMap[status] || status;
    },
    getStatusClass(status) {
      const statusClassMap = {
        REQUEST: 'status-request',
        APPROVE: 'status-approve',
        PAID: 'status-paid',
        REJECT: 'status-reject',
        CANCEL: 'status-cancel'
      };
      return statusClassMap[status] || '';
    },
    async fetchMyPets() {
      try {
        const memberId = this.userInfo.id;
        const response = await axios.get(`/api/v1/pets/${memberId}`);
        this.myPets = response.data;
      } catch (error) {
        console.error('반려동물 목록 가져오기 실패:', error);
      }
    },
    togglePetSelection(petId) {
      const index = this.reservationForm.petList.indexOf(petId);
      if (index === -1) {
        this.reservationForm.petList.push(petId);
      } else {
        this.reservationForm.petList.splice(index, 1);
      }
    },
    closeRequestModal() {
      this.isRequestModalOpen = false;
      this.resetReservationForm();
    },
    resetReservationForm() {
      this.reservationForm = {
        startTime: 0,
        endTime: 0,
        petList: [],
        description: '',
        date: '',
        memberId: null,
        scheduleId: null
      };
    },
    async submitReservation() {
      try {
        // 현재 로그인한 사용자의 ID 설정
        this.reservationForm.memberId = this.userInfo.id;
        
        // 요청 전에 필수 데이터가 모두 있는지 확인
        if (!this.reservationForm.memberId || 
            !this.reservationForm.scheduleId || 
            !this.reservationForm.date || 
            this.reservationForm.petList.length === 0) {
          alert('모든 필수 정보를 입력해주세요.');
          return;
        }

        await axios.post('/api/v1/reservations', this.reservationForm);
        this.closeRequestModal();
        // 예약 성공 후 예약 목록 새로고침
        await this.fetchMyReservations();
      } catch (error) {
        console.error('예약 요청 실패:', error);
        alert('예약 요청에 실패했습니다.');
      }
    },
    async cancelReservation(reservationId) {
      try {
        const cancelRequest = {
          userId: this.userInfo.id,
          reservationId: reservationId
        };
        
        await axios.patch('/api/v1/reservations/cancel', cancelRequest);
        await this.fetchMyReservations();
      } catch (error) {
        console.error('예약 취소 실패:', error);
      }
    },
    async processPayment(reservation) {
      try {
        // 결제 금액 계산
        const price = reservation.schedule.timeFee * 
          (reservation.endTime - reservation.startTime);
        
        // 결제 처리
        const response = await this.$payment(price);
        
        // 결제 성공 시 서버에 결제 완료 상태 업데이트
        if (response.success) {
          const paidRequest = {
            userId: this.$store.state.userInfo.id,  // 로그인된 사용자 ID
            reservationId: reservation.id
          };
          
          await axios.patch('/api/v1/reservations/paid', paidRequest);
          await this.fetchMyReservations(); // 목록 새로고침
        }
      } catch (error) {
        console.error('결제 처리 실패:', error);
      }
    }
  },
  computed: {
    ...mapState(['userInfo']),
    filteredResults() {
      // 체크박스 선택과 관계없이 검색 결과 그대로 반환
      return this.searchResults;
    }
  }
}
</script>

<style scoped>
.find-sitter-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.search-section {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 2rem;
}

.search-row {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
  align-items: flex-end;
}

.search-field {
  flex: 1;
}

.search-field label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.search-field input,
.search-field select {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.pet-types-row, .services-row {
  margin-bottom: 1rem;
  padding: 0.5rem 0;
}

.pet-types-row label, .services-row label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.checkbox-group {
  display: flex;
  gap: 2rem;
  flex-wrap: nowrap;
  justify-content: flex-start;
  padding: 0.5rem 0;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  white-space: nowrap;
  background-color: #f8f9fa;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  transition: all 0.2s;
}

.checkbox-label:hover {
  background-color: #e9ecef;
}

.checkbox-label input[type="checkbox"] {
  width: 16px;
  height: 16px;
}

.button-row {
  display: flex;
  justify-content: center;
  margin-top: 1.5rem;
}

.search-button {
  min-width: 200px;
}

.search-results-container {
  max-height: 70vh;
  overflow-y: auto;
  padding: 1rem;
}

.search-results {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
}

.sitter-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 400px;
}

.sitter-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.service-details {
  margin-top: 1rem;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.5rem 1rem;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.card-header h3 {
  margin: 0;
}

.request-button {
  padding: 0.5rem 1.5rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.request-button:hover {
  background-color: #218838;
}

.detail-row {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  padding: 0.3rem 0;
}

.detail-label {
  font-weight: bold;
  min-width: 100px;
}

.time-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 100px;
  cursor: pointer;
}

.no-results {
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.my-reservations-section {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.my-reservations-section h2 {
  margin-top: 0;
  margin-bottom: 1rem;
}

.reservations-table-container {
  overflow-x: auto;
}

.reservations-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 600px;
}

.reservations-table th,
.reservations-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.reservations-table th {
  background-color: #f8f9fa;
  font-weight: bold;
}

.reservations-table td {
  vertical-align: middle;
  height: 60px;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 80px;
  height: 28px;
}

.status-request {
  background-color: #ffd700;
  color: #000;
}

.status-approve {
  background-color: #28a745;
  color: white;
}

.status-paid {
  background-color: #17a2b8;
  color: white;
}

.status-reject {
  background-color: #dc3545;
  color: white;
}

.status-cancel {
  background-color: #6c757d;
  color: white;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  min-height: 36px;
}

.cancel-action-button,
.payment-button {
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.cancel-action-button {
  padding: 0.25rem 0.75rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: background-color 0.2s;
}

.cancel-action-button:hover {
  background-color: #c82333;
}

.payment-button {
  padding: 0.25rem 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: background-color 0.2s;
}

.payment-button:hover {
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

.request-modal {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.pets-scroll-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 1rem;
}

.pet-grid {
  display: inline-flex;
  gap: 1rem;
  padding: 0.5rem 0;
  min-width: min-content;
}

.pet-card {
  flex: 0 0 100px;
  aspect-ratio: 1;
  border: 2px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  overflow: hidden;
}

.pet-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pet-card.selected {
  border-color: #28a745;
  background-color: #e8f5e9;
}

.pet-card-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
}

.pet-photo {
  width: 60%;
  aspect-ratio: 1;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}

.pet-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-photo.default {
  background-color: #e9ecef;
  font-size: 1.5rem;
}

.pet-name {
  font-size: 0.9rem;
  font-weight: 500;
  text-align: center;
  word-break: break-word;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.submit-button {
  padding: 0.5rem 1.5rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #218838;
}

.cancel-button {
  padding: 0.5rem 1.5rem;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-button:hover {
  background-color: #5a6268;
}

.description-group {
  margin-top: 2rem;
}

.description-group textarea {
  width: 100%;
  min-height: 120px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: vertical;
  font-size: 0.95rem;
  line-height: 1.5;
}

.description-group textarea:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.1);
}

.pets-scroll-container::-webkit-scrollbar {
  height: 8px;
}

.pets-scroll-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.pets-scroll-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.pets-scroll-container::-webkit-scrollbar-thumb:hover {
  background: #666;
}
</style>