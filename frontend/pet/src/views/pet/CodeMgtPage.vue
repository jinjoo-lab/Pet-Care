<template>
  <div class="code-management-container">
    <h1>코드 관리</h1>

    <div class="code-group">
      <h2>코드 그룹</h2>
      <div class="code-group-header">
        <button @click="openAddGroupModal" class="add-button">+추가</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>그룹명</th>
            <th>그룹ID</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="group in codeGroups" :key="group.id">
            <td @click="selectGroup(group)">{{ group.name }}</td>
            <td>{{ group.id }}</td>
            <td>
              <button @click.stop="deleteGroup(group)" class="delete-button">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="code-detail" v-if="selectedGroup">
      <h2>코드상세: {{ selectedGroup.name }}</h2>
      <div class="code-detail-header">
        <button @click="addCode" class="add-button">+추가</button>
        <button @click="deleteCode" class="delete-button">삭제</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>순서</th>
            <th>코드명</th>
            <th>코드ID</th>
            <th>코드설명</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="code in selectedGroup.codes" :key="code.id">
            <td>{{ code.order }}</td>
            <td>{{ code.name }}</td>
            <td>{{ code.id }}</td>
            <td>{{ code.description }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 코드 그룹 추가 모달 -->
    <div v-if="isAddGroupModalOpen" class="modal">
      <h2>코드 그룹 추가</h2>
      <div class="form-group">
        <label for="groupName">그룹명</label>
        <input type="text" id="groupName" v-model="newGroupName" />
      </div>
      <button @click="saveGroup" class="save-button">저장</button>
      <button @click="closeAddGroupModal" class="cancel-button">취소</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'CodeMgtPage',
  data() {
    return {
      codeGroups: [], // 코드 그룹 데이터
      selectedGroup: null, // 선택된 그룹
      isAddGroupModalOpen: false, // 모달 열림 상태
      newGroupName: '' // 새 그룹 이름
    }
  },
  async created() {
    await this.fetchCodeGroups(); // 컴포넌트 생성 시 코드 그룹 가져오기
  },
  methods: {
    async fetchCodeGroups() {
      try {
        const response = await axios.get('/api/v1/codes/groups');
        this.codeGroups = response.data; // 응답 데이터를 코드 그룹에 저장
      } catch (error) {
        console.error('코드 그룹 가져오기 실패:', error);
      }
    },
    openAddGroupModal() {
      this.isAddGroupModalOpen = true; // 모달 열기
    },
    closeAddGroupModal() {
      this.isAddGroupModalOpen = false; // 모달 닫기
      this.newGroupName = ''; // 입력 필드 초기화
    },
    async saveGroup() {
      if (!this.newGroupName) {
        alert('그룹명을 입력하세요.');
        return;
      }

      const requestData = {
        name: this.newGroupName
      };

      try {
        const response = await axios.post('/api/v1/codes/groups', requestData);
        console.log('그룹 추가 성공:', response.data);
        this.codeGroups.push(response.data); // 새 그룹 추가
        this.closeAddGroupModal(); // 모달 닫기
      } catch (error) {
        console.error('그룹 추가 실패:', error);
      }
    },
    selectGroup(group) {
      this.selectedGroup = group; // 선택된 그룹 설정
    },
    deleteGroup(group) {
      if (!group) {
        alert('삭제할 그룹을 선택하세요.');
        return;
      }

      // 삭제 로직 추가
      console.log('삭제할 그룹:', group);
      // API 요청을 통해 그룹 삭제 구현
    },
    addCode() {
      // 코드 추가 로직
    },
    deleteCode() {
      // 선택된 코드 삭제 로직 추가
      console.log('삭제할 코드:', this.selectedGroup.codes);
      // API 요청을 통해 코드 삭제 구현
    }
  }
}
</script>

<style scoped>
.code-management-container {
  padding: 2rem;
  max-width: 1200px; /* 너비 조정 */
  margin: 0 auto; /* 중앙 정렬 유지 */
  background-color: #f9f9f9; /* 배경색 추가 */
  border-radius: 8px; /* 모서리 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

h1 {
  text-align: left; /* 제목 왼쪽 정렬 */
  margin-bottom: 1rem; /* 제목 아래 여백 추가 */
}

.code-group {
  margin-bottom: 2rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: white; /* 배경색 추가 */
}

.code-group-header {
  display: flex;
  justify-content: flex-end; /* 버튼을 오른쪽으로 정렬 */
  margin-bottom: 1rem; /* 버튼 아래 여백 추가 */
  gap: 1rem; /* 버튼 간격 추가 */
}

.code-detail-header {
  display: flex;
  justify-content: flex-end; /* 버튼을 오른쪽으로 정렬 */
  margin-bottom: 1rem; /* 버튼 아래 여백 추가 */
  gap: 1rem; /* 버튼 간격 추가 */
}

.add-button, .save-button, .cancel-button, .delete-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.add-button:hover, .save-button:hover, .cancel-button:hover, .delete-button:hover {
  background-color: #0056b3;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 0.5rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}
</style> 