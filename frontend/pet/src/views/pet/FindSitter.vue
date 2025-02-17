<template>
  <div class="find-sitter-container">
    <div class="filter-section">
      <div class="filter-group">
        <!-- 위치 필터 -->
        <div class="filter-item">
          <button class="filter-btn" :class="{ active: activeFilter === 'location' }" @click="toggleFilter('location')">
            위치
          </button>
          <div v-if="activeFilter === 'location'" class="filter-dropdown">
            <div class="search-box">
              <input 
                type="text" 
                v-model="filters.location"
                placeholder="주소를 입력하세요"
                class="location-input"
                required
              >
              <button class="search-address-btn" @click="handleSearch">주소 검색</button>
            </div>
            <span v-if="!filters.location" class="error-message">위치를 입력하세요.</span>
          </div>
        </div>

        <!-- 일자 필터 -->
        <div class="filter-item">
          <button class="filter-btn" :class="{ active: activeFilter === 'date' }" @click="toggleFilter('date')">
            일자
          </button>
          <div v-if="activeFilter === 'date'" class="filter-dropdown calendar">
            <DatePicker
              v-model="filters.date"
              :min-date="new Date()"
              :masks="masks"
              :attributes="attributes"
              is-expanded
              locale="ko"
            />
          </div>
        </div>

        <!-- 시작 시간 필터 -->
        <div class="filter-item">
          <button class="filter-btn" :class="{ active: activeFilter === 'startTime' }" @click="toggleFilter('startTime')">
            시작 시간
          </button>
          <div v-if="activeFilter === 'startTime'" class="filter-dropdown">
            <select v-model="filters.startTime" class="time-select">
              <option v-for="hour in 24" :key="`start-${hour}`" :value="hour-1">
                {{ String(hour-1).padStart(2, '0') }}:00
              </option>
            </select>
          </div>
        </div>

        <!-- 종료 시간 필터 -->
        <div class="filter-item">
          <button class="filter-btn" :class="{ active: activeFilter === 'endTime' }" @click="toggleFilter('endTime')">
            종료 시간
          </button>
          <div v-if="activeFilter === 'endTime'" class="filter-dropdown">
            <select v-model="filters.endTime" class="time-select">
              <option v-for="hour in 24" :key="`end-${hour}`" :value="hour-1">
                {{ String(hour-1).padStart(2, '0') }}:00
              </option>
            </select>
          </div>
        </div>

        <button class="search-btn" @click="handleSearch">검색</button>
      </div>
    </div>

    <div class="services-section">
      <div class="service-list">
        <div v-for="service in services" :key="service.id" class="service-card">
          <div class="service-info">
            <h3>{{ service.sitter.memberResponse.name }}의 서비스</h3>
            <div class="service-details">
              <p>시작 시간: {{ formatTime(service.startTime) }}</p>
              <p>종료 시간: {{ formatTime(service.endTime) }}</p>
              <p>시간당 요금: {{ formatPrice(service.timeFee) }}원</p>
              <p>반려동물 종류: 
                <span v-for="species in service.sitter.species" :key="species.id">
                  {{ species.name }}<span v-if="!$last">, </span>
                </span>
              </p>
              <p>제공 서비스: 
                <span v-for="care in service.sitter.services" :key="care.serviceId">
                  {{ care.name }}<span v-if="!$last">, </span>
                </span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { DatePicker } from 'v-calendar'
import axios from 'axios'
import { format, zonedTimeToUtc } from 'date-fns-tz'

export default {
  name: 'FindSitter',
  components: {
    DatePicker
  },
  data() {
    return {
      activeFilter: '',
      filters: {
        location: '',
        date: new Date(),
        startTime: 9,
        endTime: 18
      },
      services: [],
      masks: {
        input: 'YYYY-MM-DD'
      },
      attributes: [
        {
          key: 'today',
          highlight: true,
          dates: new Date()
        }
      ]
    }
  },
  methods: {
    toggleFilter(filterName) {
      this.activeFilter = this.activeFilter === filterName ? '' : filterName
    },
    async handleSearch() {
      if (!this.filters.location) {
        alert('위치를 입력하세요.');
        return;
      }

      if (!this.filters.date) {
        alert('일자를 선택하세요.');
        return;
      }

      const selectedDate = zonedTimeToUtc(this.filters.date, 'Asia/Seoul');
      const formattedDate = format(selectedDate, 'yyyy-MM-dd');

      const requestData = {
        location: this.filters.location,
        date: formattedDate,
        startTime: this.filters.startTime,
        endTime: this.filters.endTime
      }

      try {
        const response = await axios.post('/api/v1/schedule/list', requestData);
        console.log('응답 데이터:', response.data); // 응답 데이터 확인

        if (!response.data || response.data.length === 0) {
          console.error('검색 결과가 없습니다.');
          return;
        }

        this.services = response.data; // 검색 결과를 services에 저장
      } catch (error) {
        console.error('검색 실패:', error);
      }
    },
    formatPrice(price) {
      return price.toLocaleString()
    },
    formatTime(hour) {
      return `${String(hour).padStart(2, '0')}:00`
    }
  },
  mounted() {
    // 외부 클릭시 필터 닫기
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.activeFilter = ''
      }
    })
  }
}
</script>

<style scoped>
.find-sitter-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.filter-section {
  background: white;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.filter-group {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 0.75rem 1.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.filter-btn:hover {
  border-color: #007bff;
  color: #007bff;
}

.filter-btn.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.search-btn {
  padding: 0.75rem 2rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  margin-left: auto;
}

.search-btn:hover {
  background: #0056b3;
}

.services-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  max-height: 600px;
  overflow-y: auto;
}

.service-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.service-card {
  padding: 1.5rem;
  border: 1px solid #eee;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.service-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.service-info h3 {
  margin: 0 0 1rem 0;
  color: #333;
}

.service-details {
  color: #666;
}

.service-meta {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
  color: #333;
  font-weight: 500;
}

/* 스크롤바 스타일링 */
.services-section::-webkit-scrollbar {
  width: 8px;
}

.services-section::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.services-section::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.services-section::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.filter-item {
  position: relative;
}

.filter-dropdown {
  position: absolute;
  top: calc(100% + 0.5rem);
  left: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  z-index: 1000;
  min-width: 200px;
}

.search-box {
  display: flex;
  gap: 0.5rem;
}

.location-input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.search-address-btn {
  padding: 0.5rem 1rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.time-select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.service-options {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.service-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.calendar {
  padding: 1rem;
  min-width: 300px;
}

/* v-calendar 커스텀 스타일 */
:deep(.vc-container) {
  border: none;
}

:deep(.vc-header) {
  padding-top: 0.5rem;
}

:deep(.vc-weeks) {
  padding: 0.5rem 0;
}

@media (max-width: 768px) {
  .find-sitter-container {
    padding: 1rem;
  }

  .filter-group {
    flex-wrap: nowrap;
    overflow-x: auto;
    padding-bottom: 0.5rem;
  }

  .filter-btn {
    white-space: nowrap;
  }

  .filter-dropdown {
    position: fixed;
    top: auto;
    bottom: 0;
    left: 0;
    right: 0;
    border-radius: 16px 16px 0 0;
    padding: 1.5rem;
  }
}

.error-message {
  color: #dc3545;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}
</style> 