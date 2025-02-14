<template>
  <div class="pet-list-container">
    <div class="header-section">
      <h2>내 반려동물</h2>
      <button class="add-pet-btn">반려동물 등록</button>
    </div>

    <div class="pets-section">
      <div v-if="!pets.length" class="no-pets">
        등록된 반려동물이 없습니다.
      </div>
      <div v-else class="pet-list">
        <div v-for="pet in pets" :key="pet.id" class="pet-card">
          <div class="pet-image">
            <img :src="pet.imageUrl" :alt="pet.name">
          </div>
          <div class="pet-info">
            <h3>{{ pet.name }}</h3>
            <p class="pet-type">{{ pet.type }} / {{ pet.breed }}</p>
            <p class="pet-details">{{ pet.age }}세 / {{ pet.gender }}</p>
            <p class="pet-description">{{ pet.description }}</p>
          </div>
          <div class="pet-actions">
            <button class="edit-btn">수정</button>
            <button class="delete-btn">삭제</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PetList',
  data() {
    return {
      pets: [
        {
          id: 1,
          name: '멍멍이',
          type: '강아지',
          breed: '골든리트리버',
          age: 3,
          gender: '남아',
          description: '산책을 매우 좋아하는 활발한 강아지입니다.',
          imageUrl: '/images/default-pet.png'
        },
        {
          id: 2,
          name: '나비',
          type: '고양이',
          breed: '러시안블루',
          age: 2,
          gender: '여아',
          description: '조용하고 애교가 많은 고양이입니다.',
          imageUrl: '/images/default-pet.png'
        }
      ]
    }
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