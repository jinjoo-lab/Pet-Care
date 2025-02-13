<template>
  <div class="signup-container">
    <h2>회원가입</h2>
    <form @submit.prevent="handleSubmit" class="signup-form">
      <!-- 이름 입력 -->
      <div class="form-group">
        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          v-model="formData.name"
          required
          placeholder="이름을 입력하세요"
        />
      </div>

      <!-- 이메일 입력 -->
      <div class="form-group">
        <label for="email">이메일</label>
        <div class="email-group">
          <input
            type="email"
            id="email"
            v-model="formData.email"
            required
            placeholder="이메일을 입력하세요"
            :class="{ verified: isEmailVerified }"
          />
          <button
            type="button"
            @click="checkEmailDuplicate"
            class="verify-btn"
            :disabled="!formData.email"
          >
            중복확인
          </button>
        </div>
        <span
          class="email-status"
          v-if="emailMessage"
          :class="{ success: isEmailVerified }"
        >
          {{ emailMessage }}
        </span>
      </div>

      <!-- 비밀번호 입력 -->
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input
          type="password"
          id="password"
          v-model="formData.password"
          required
          placeholder="비밀번호를 입력하세요"
        />
      </div>

      <!-- 비밀번호 확인 -->
      <div class="form-group">
        <label for="passwordConfirm">비밀번호 확인</label>
        <input
          type="password"
          id="passwordConfirm"
          v-model="formData.passwordConfirm"
          required
          placeholder="비밀번호를 다시 입력하세요"
        />
        <span
          class="password-match"
          v-if="formData.passwordConfirm"
          :class="{ error: !isPasswordMatch }"
        >
          {{ passwordMatchMessage }}
        </span>
      </div>

      <!-- 전화번호 입력 -->
      <div class="form-group">
        <label for="phone">전화번호</label>
        <input
          type="tel"
          id="phone"
          v-model="formData.phone"
          required
          placeholder="전화번호를 입력하세요"
        />
      </div>

      <!-- 제출 버튼 -->
      <button type="submit" class="submit-btn" :disabled="!isFormValid">
        회원가입
      </button>
    </form>
  </div>
</template>
  
  <script>
import axios from "axios";

export default {
  name: "SignUp",
  data() {
    return {
      formData: {
        name: "",
        email: "",
        password: "",
        passwordConfirm: "",
        phone: "",
      },
      isEmailVerified: false,
      emailMessage: "",
    };
  },
  computed: {
    isPasswordMatch() {
      return this.formData.password === this.formData.passwordConfirm;
    },
    passwordMatchMessage() {
      if (!this.formData.passwordConfirm) return "";
      return this.isPasswordMatch
        ? "비밀번호가 일치합니다."
        : "비밀번호가 일치하지 않습니다.";
    },
    isFormValid() {
      return (
        this.formData.name &&
        this.formData.email &&
        this.isEmailVerified &&
        this.formData.password &&
        this.isPasswordMatch &&
        this.formData.phone
      );
    },
  },
  methods: {
    async checkEmailDuplicate() {
      if (!this.formData.email) return;

      try {
        const response = await axios.get(
          `/api/v1/members/duplicate/${this.formData.email}`
        );
        const isDuplicate = response.data.isDuplicate;

        if (isDuplicate) {
          this.isEmailVerified = false;
          this.emailMessage = "이미 사용 중인 이메일입니다.";
        } else {
          this.isEmailVerified = true;
          this.emailMessage = "사용 가능한 이메일입니다.";
        }
      } catch (error) {
        this.isEmailVerified = false;
        this.emailMessage = "이메일 중복 확인 중 오류가 발생했습니다.";
        console.error("이메일 중복 확인 실패:", error);
      }
    },
    async handleSubmit() {
      if (!this.isFormValid) return;

      try {
        const response = await axios.post("/api/v1/members", {
          email: this.formData.email,
          password: this.formData.password,
          name: this.formData.name,
          phone: this.formData.phone,
        });

        console.log("회원가입 성공:", response.data);
        this.$router.push("/login");
      } catch (error) {
        console.error("회원가입 실패:", error);
        // 에러 처리 로직 추가 가능
      }
    },
  },
};
</script>
  
  <style scoped>
.signup-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  margin-left: 15%; /* 더 왼쪽으로 이동 (20%에서 15%로 변경) */
  padding: 2rem;
}

.signup-form {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  padding: 2rem;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #007bff;
}

.email-group {
  display: flex;
  gap: 1rem;
}

.email-group input {
  flex: 1;
}

.verify-btn {
  padding: 0 1rem;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
}

.verify-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.email-status {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.875rem;
  color: #dc3545;
}

.email-status.success {
  color: #28a745;
}

.password-match {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.875rem;
  color: #28a745;
}

.password-match.error {
  color: #dc3545;
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
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background: #0056b3;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

input.verified {
  border-color: #28a745;
}

@media (max-width: 768px) {
  .signup-container {
    margin-left: auto; /* 모바일에서는 중앙 정렬 */
    padding: 1rem;
  }
}
</style>