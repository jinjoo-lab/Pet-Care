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
              >
              <button class="search-address-btn">주소 검색</button>
            </div>
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

        <!-- 서비스 종류 필터 -->
        <div class="filter-item">
          <button class="filter-btn" :class="{ active: activeFilter === 'services' }" @click="toggleFilter('services')">
            서비스 종류
          </button>
          <div v-if="activeFilter === 'services'" class="filter-dropdown">
            <div class="service-options">
              <label class="service-option">
                <input 
                  type="checkbox" 
                  v-model="filters.services" 
                  value="산책"
                > 산책
              </label>
              <label class="service-option">
                <input 
                  type="checkbox" 
                  v-model="filters.services" 
                  value="방문돌봄"
                > 방문 돌봄
              </label>
              <label class="service-option">
                <input 
                  type="checkbox" 
                  v-model="filters.services" 
                  value="위탁돌봄"
                > 위탁 돌봄
              </label>
            </div>
          </div>
        </div>

        <button class="search-btn" @click="handleSearch">검색</button>
      </div>
    </div>

    <div class="services-section">
      <div class="service-list">
        <div v-for="service in services" :key="service.id" class="service-card">
          <div class="service-info">
            <h3>{{ service.title }}</h3>
            <div class="service-details">
              <p>{{ service.description }}</p>
              <div class="service-meta">
                <span class="location">{{ service.location }}</span>
                <span class="price">{{ formatPrice(service.price) }}원</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { DatePicker } from 'v-calendar'

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
        endTime: 18,
        services: []
      },
      services: [
        {
          id: 1,
          title: '강아지 산책 전문 펫시터',
          description: '반려동물과 함께하는 즐거운 산책 시간을 제공합니다.',
          location: '서울시 강남구',
          price: 15000
        },
        {
          id: 2,
          title: '고양이 방문 돌봄',
          description: '편안한 우리 집에서 안전하게 돌봐드립니다.',
          location: '서울시 서초구',
          price: 20000
        },
        {
          id: 3,
          title: '24시간 위탁 돌봄',
          description: '넓은 공간에서 자유롭게 지내실 수 있습니다.',
          location: '서울시 송파구',
          price: 35000
        },
        {
          id: 4,
          title: '노견 전문 펫시터',
          description: '노령견의 특성을 고려한 맞춤 돌봄 서비스를 제공합니다.',
          location: '서울시 강동구',
          price: 25000
        },
        {
          id: 5,
          title: '장기 위탁 전문',
          description: '장기 여행이나 출장시 안심하고 맡기실 수 있습니다.',
          location: '서울시 마포구',
          price: 30000
        },
        {
          id: 6,
          title: '퍼피 전문 펫시터',
          description: '활발한 강아지를 위한 맞춤 케어 서비스입니다.',
          location: '서울시 영등포구',
          price: 22000
        },
        {
          id: 7,
          title: '산책 & 놀이 전문',
          description: '매일 다른 코스로 즐거운 산책을 제공합니다.',
          location: '서울시 용산구',
          price: 18000
        },
        {
          id: 8,
          title: '고양이 전문 펫시터',
          description: '고양이의 특성을 이해하는 전문 돌봄 서비스입니다.',
          location: '서울시 성동구',
          price: 23000
        }
      ],
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
    handleSearch() {
      // 검색 로직 구현
      console.log('검색 필터:', this.filters)
    },
    formatPrice(price) {
      return price.toLocaleString()
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
</style> 