<template>
  <div class="pet-list-container">
    <div class="header-section">
      <h2>내 반려동물</h2>
      <button @click="showAddPetForm = true" class="add-pet-btn">반려동물 등록</button>
    </div>

    <div class="pets-section">
      <div v-if="!pets.length" class="no-pets">
        등록된 반려동물이 없습니다.
      </div>
      <div v-else class="pet-list">
        <div v-for="pet in pets" :key="pet.id" class="pet-card">
          <div class="pet-image">
            <img :src="pet.photo" :alt="pet.name">
          </div>
          <div class="pet-info">
            <h3>{{ pet.name }}</h3>
            <p>종류: {{ pet.petType }}</p>
            <p>나이: {{ pet.age }}살</p>
            <p>크기: {{ pet.petSize }}</p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showAddPetForm" class="modal-overlay">
      <div class="modal-content">
        <h3>반려동물 등록</h3>
        <form @submit.prevent="handleAddPet" class="pet-form">
          <div class="form-group">
            <label for="petName">이름</label>
            <input type="text" id="petName" v-model="newPet.name" required>
          </div>
          <div class="form-group">
            <label for="petType">종류</label>
            <select id="petType" v-model="newPet.petType" required>
              <option value="DOG">강아지</option>
              <option value="CAT">고양이</option>
            </select>
          </div>
          <div class="form-group">
            <label for="petAge">나이</label>
            <input type="number" id="petAge" v-model="newPet.age" required>
          </div>
          <div class="form-group">
            <label for="petPhoto">사진 URL</label>
            <input type="text" id="petPhoto" v-model="newPet.photo" required>
          </div>
          <div class="form-group">
            <label for="petSize">크기</label>
            <select id="petSize" v-model="newPet.size" required>
              <option value="SMALL">작음</option>
              <option value="MEDIUM">중간</option>
              <option value="LARGE">큼</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="submit" class="submit-btn">등록</button>
            <button type="button" class="cancel-btn" @click="showAddPetForm = false">취소</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PetList',
  data() {
    return {
      pets: [],
      showAddPetForm: false,
      newPet: {
        name: '',
        petType: 'DOG',
        age: 0,
        photo: '',
        size: 'SMALL'
      }
    }
  },
  methods: {
    async handleAddPet() {
      try {
        const requestData = {
          ...this.newPet,
          memberId: this.$store.state.userInfo.id
        }

        await axios.post('/api/v1/pets', requestData)
        console.log('반려동물 등록 성공')
        this.showAddPetForm = false
        this.fetchPets()
      } catch (error) {
        console.error('반려동물 등록 실패:', error)
      }
    },
    async fetchPets() {
      try {
        const memberId = this.$store.state.userInfo.id
        const response = await axios.get(`/api/v1/pets/${memberId}`)
        this.pets = response.data
        console.log('반려동물 목록 불러오기 성공:', this.pets)
      } catch (error) {
        console.error('반려동물 목록 불러오기 실패:', error)
      }
    }
  },
  mounted() {
    this.fetchPets()
  }
}
</script>

<style scoped>
.pet-list-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.add-pet-btn {
  padding: 0.75rem 1.5rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s;
}

.add-pet-btn:hover {
  background: #0056b3;
}

.pets-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 2rem;
}

.no-pets {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.pet-list {
  display: grid;
  gap: 2rem;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
}

.pet-card {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.pet-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.pet-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-info {
  padding: 1.5rem;
}

.pet-info h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.pet-type {
  color: #666;
  margin-bottom: 0.5rem;
}

.pet-details {
  color: #666;
  margin-bottom: 1rem;
}

.pet-description {
  color: #444;
  font-size: 0.9rem;
  line-height: 1.5;
}

.pet-actions {
  display: flex;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background: #f8f9fa;
}

.edit-btn, .delete-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  flex: 1;
}

.edit-btn {
  background: #28a745;
  color: white;
}

.edit-btn:hover {
  background: #218838;
}

.delete-btn {
  background: #dc3545;
  color: white;
}

.delete-btn:hover {
  background: #c82333;
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

.pet-form {
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

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.submit-btn, .cancel-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover, .cancel-btn:hover {
  background-color: #0056b3;
}

@media (max-width: 768px) {
  .pet-list-container {
    padding: 1rem;
  }

  .pet-list {
    grid-template-columns: 1fr;
  }

  .pet-actions {
    flex-direction: column;
  }
}
</style> 