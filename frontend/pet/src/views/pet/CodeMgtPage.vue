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
        <button @click="openAddCodeModal" class="add-button">+추가</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>코드명</th>
            <th>코드ID</th>
            <th>코드상태</th>
            <th>변경</th>
            <th>삭제</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="code in codeDetails" :key="code.id">
            <td>{{ code.name }}</td>
            <td>{{ code.id }}</td>
            <td>{{ code.isActive ? '활성' : '비활성' }}</td>
            <td>
              <button @click.stop="openEditCodeModal(code)" class="edit-button">변경</button>
            </td>
            <td>
              <button @click.stop="deleteCode(code.id)" class="delete-button">삭제</button>
            </td>
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

    <!-- 코드 추가 모달 -->
    <div v-if="isAddCodeModalOpen" class="modal">
      <h2>코드 추가</h2>
      <div class="form-group">
        <label for="codeName">코드명</label>
        <input type="text" id="codeName" v-model="newCodeName" />
      </div>
      <div class="form-group">
        <label for="codeStatus">코드 상태</label>
        <select id="codeStatus" v-model="newCodeStatus">
          <option :value="true">활성</option>
          <option :value="false">비활성</option>
        </select>
      </div>
      <button @click="saveCode" class="save-button">저장</button>
      <button @click="closeAddCodeModal" class="cancel-button">취소</button>
    </div>

    <!-- 코드 변경 모달 -->
    <div v-if="isEditCodeModalOpen" class="modal">
      <h2>코드 변경</h2>
      <div class="form-group">
        <label for="editCodeName">코드명</label>
        <input type="text" id="editCodeName" v-model="editCodeName" />
      </div>
      <div class="form-group">
        <label for="editCodeStatus">코드 상태</label>
        <select id="editCodeStatus" v-model="editCodeStatus">
          <option :value="true">활성</option>
          <option :value="false">비활성</option>
        </select>
      </div>
      <button @click="updateCode" class="save-button">저장</button>
      <button @click="closeEditCodeModal" class="cancel-button">취소</button>
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
      codeDetails: [], // 코드 상세 데이터
      isAddGroupModalOpen: false, // 모달 열림 상태
      newGroupName: '', // 새 그룹 이름
      isAddCodeModalOpen: false, // 코드 추가 모달 열림 상태
      newCodeName: '', // 새 코드 이름
      newCodeStatus: true, // 새 코드 상태 (기본값: 활성)
      isEditCodeModalOpen: false, // 코드 변경 모달 열림 상태
      editCodeName: '', // 변경할 코드 이름
      editCodeStatus: true, // 변경할 코드 상태 (기본값: 활성)
      codeToEdit: null // 변경할 코드 정보
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
        // 각 그룹의 codes 배열 초기화
        this.codeGroups.forEach(group => {
          group.codes = group.codes || []; // codes가 없으면 빈 배열로 초기화
        });
      } catch (error) {
        console.error('코드 그룹 가져오기 실패:', error);
      }
    },
    async fetchCodeDetails(groupId) {
      try {
        const response = await axios.get(`/api/v1/codes/details/list/${groupId}`);
        this.codeDetails = response.data; // 응답 데이터를 코드 상세에 저장
        console.log('코드 상세 가져오기 성공:', this.codeDetails); // 디버깅용 로그
      } catch (error) {
        console.error('코드 상세 가져오기 실패:', error);
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
      this.fetchCodeDetails(group.id); // 선택된 그룹의 코드 상세 가져오기
    },
    openAddCodeModal() {
      this.isAddCodeModalOpen = true; // 코드 추가 모달 열기
    },
    closeAddCodeModal() {
      this.isAddCodeModalOpen = false; // 코드 추가 모달 닫기
      this.newCodeName = ''; // 입력 필드 초기화
      this.newCodeStatus = true; // 상태 초기화
    },
    async saveCode() {
      if (!this.selectedGroup) {
        alert('코드 그룹을 선택하세요.');
        return;
      }

      const requestData = {
        isActive: this.newCodeStatus, // 코드 상태
        name: this.newCodeName, // 코드 이름
        codeGroupId: this.selectedGroup.id // 선택된 그룹 ID
      };

      try {
        const response = await axios.post('/api/v1/codes/details', requestData);
        console.log('코드 추가 성공:', response.data);
        this.codeDetails.push(response.data); // 새 코드 추가
        this.closeAddCodeModal(); // 모달 닫기
      } catch (error) {
        console.error('코드 추가 실패:', error);
      }
    },
    openEditCodeModal(code) {
      this.codeToEdit = code; // 변경할 코드 설정
      this.editCodeName = code.name; // 코드명 설정
      this.editCodeStatus = code.isActive; // 코드 상태 설정
      this.isEditCodeModalOpen = true; // 코드 변경 모달 열기
    },
    closeEditCodeModal() {
      this.isEditCodeModalOpen = false; // 코드 변경 모달 닫기
      this.editCodeName = ''; // 입력 필드 초기화
      this.editCodeStatus = true; // 상태 초기화
    },
    async updateCode() {
      if (!this.codeToEdit) {
        alert('변경할 코드를 선택하세요.');
        return;
      }

      const requestData = {
        isActive: this.editCodeStatus, // 코드 상태
        name: this.editCodeName, // 코드 이름
        codeDetailId: this.codeToEdit.id // 선택된 코드 ID
      };

      try {
        const response = await axios.put('/api/v1/codes/details', requestData);
        console.log('코드 변경 성공:', response.data);
        const index = this.codeDetails.findIndex(code => code.id === this.codeToEdit.id);
        if (index !== -1) {
          this.codeDetails[index] = response.data; // 변경된 코드로 업데이트
        }
        this.closeEditCodeModal(); // 모달 닫기
      } catch (error) {
        console.error('코드 변경 실패:', error);
      }
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
    deleteCode(code) {
      if (!code) {
        alert('삭제할 코드를 선택하세요.');
        return;
      }

      // 삭제 로직 추가
      console.log('삭제할 코드:', code);
      // API 요청을 통해 코드 삭제 구현
      axios.delete(`/api/v1/codes/details/${code}`).then(() => {
        this.codeDetails = this.codeDetails.filter(c => c.id !== code); // 코드 목록에서 삭제
      }).catch(error => {
        console.error('코드 삭제 실패:', error);
      });
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