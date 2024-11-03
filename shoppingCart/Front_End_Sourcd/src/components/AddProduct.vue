<template>
	<div className="flex justify-center min-h-screen items-center bg-gray-100">
		<form
			method="post"
			role="form"
			className="bg-white p-6 rounded-lg shadow-md w-full max-w-md"
			@submit.prevent="handleSubmit"
		>
			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Name </label>
				<input
					placeholder="Enter product name"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="name"
					v-model="name"
				/>
			</div>

			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Description </label>
				<input
					placeholder="Enter product description"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="description"
					v-model="description"
				/>
			</div>

			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Price </label>
				<input
					placeholder="Enter product price"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="price"
					v-model="price"
				/>
			</div>

			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Image URL </label>
				<input
					placeholder="Enter product image URL"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="image"
					v-model="image"
				/>
			</div>

			<div className="mb-4">
				<label className="text-gray-700 font-bold mb-2"> Category </label>
				<input
					placeholder="Enter product category"
					type="text"
					className="shadow border rounded w-full py-2 px-3 text-gray-700"
					name="category"
					v-model="category"
				/>
			</div>

			<button
				type="submit"
				className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
			>
				Add
			</button>
		</form>
	</div>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

export default {
	setup() {
		const router = useRouter();
		//使用pinia
		const authStore = useAuthStore();
		//取得表單的內容
		const name = ref("");
		const description = ref("");
		const price = ref("");
		const image = ref("");
		const category = ref("");

		//處理按下表單送出後的事件
		const handleSubmit = async () => {
			try {
				const token = authStore.token;
				if(!name.value || !description.value || !price.value || !image.value || !category.value || isNaN(price.value)){
					throw new Error("商品資料格式錯誤");
				}
				//我們在SpringBoot的設定，為了一次接收多筆產品資料，使用了Product[]，因此data需要多出[]，就算只傳一筆也要加[]
				const data = [{
					name: name.value,
					description: description.value,
					price: parseInt(price.value),
					image: image.value,
					category: category.value,
				}];
				//傳送建立商品的請求
				const response = await axios.post(
					"http://localhost:8080/api/product/",
					data,
					{
						//將token放入header中
						headers: {
							'Authorization': `Bearer ${token}`
						}
					}
				);
				//顯示成功訊息
				console.log(response.data);
				router.push("/");
			} catch (error) {
				alert("Error");
				console.log(error);
			}
		};

		return {
			name,
			description,
			price,
			image,
			category,
			handleSubmit,
		};
	},
};
</script>
