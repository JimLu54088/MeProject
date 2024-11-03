<template>
	<div className="flex justify-center min-h-screen items-center bg-gray-100">
		<form
			method="post"
			role="form"
			className="bg-white p-6 rounded-lg shadow-md w-full max-w-md"
			@submit.prevent="handleSubmit"
		>
			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Email </label>
				<input
					placeholder="Enter email address"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="email"
					autocomplete="current-email"
					v-model="email"
				/>
			</div>

			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Password </label>
				<input
					placeholder="Enter password"
					type="password"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="password"
					autocomplete="current-password"
					v-model="password"
				/>
			</div>

			<button
				type="submit"
				className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
			>
				Login
			</button>
		</form>
	</div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

export default {
  setup() {
    const router = useRouter();
	//使用pinia
    const authStore = useAuthStore();
	//取得表單的email和password
    const email = ref('');
    const password = ref('');

	//處理按下表單送出後的事件
    const handleSubmit = async () => {
      try {
		//傳送登入請求
        const response = await axios.post('http://localhost:8080/auth/login', {
          email: email.value,
          password: password.value,
        });
		//顯示登入成功訊息
        console.log(response.data);
		//設定token
        authStore.setToken(response.data.jwt);
        router.push('/');
      } catch (error) {
		alert("Invalid email or password");
        console.log(error);
      }
    };

    return {
      email,
      password,
      handleSubmit,
    };
  },
};
</script>
