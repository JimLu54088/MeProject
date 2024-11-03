<template>
	<div
		class="flex items-center justify-between border-b border-gray-200 py-4 font-bold"
	>
		<div class="flex items-center">
			<div class="w-16 h-16"></div>
			<div class="flex-1 text-gray-700">商品</div>
		</div>
		<div class="flex items-center space-x-4">
			<span class="w-28 text-gray-700">單價</span>
			<span class="w-20 text-gray-700">數量</span>
			<span class="w-16 text-gray-700">總計</span>
			<span class="w-16 text-gray-700">操作</span>
		</div>
	</div>

	<div
		v-for="item in items"
		:key="item.id"
		class="flex items-center justify-between border-b border-gray-200 py-4"
	>
		<div class="flex items-center">
			<img
				:src="item.product.image"
				:alt="item.product.name"
				class="w-16 h-16 object-cover rounded-lg shadow-sm mr-4"
			/>
			<div class="flex-1">
				<h2 class="text-lg font-bold text-gray-900">{{ item.product.name }}</h2>
			</div>
		</div>
		<div class="flex items-center space-x-4">
			<span class="w-16 text-gray-700">{{ item.product.price }}</span>
			<div class="flex items-center space-x-2">
				<button
					class="bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded"
					@click="updateQuantity(item.id, item.product.id, item.quantity - 1)"
				>
					-
				</button>
				<input
					type="text"
					v-model="item.quantity"
					class="border border-gray-300 p-2 w-16 text-center"
					@change="updateQuantity(item.id, item.product.id, item.quantity)"
				/>
				<button
					class="bg-blue-500 hover:bg-blue-600 text-white px-2 py-1 rounded"
					@click="updateQuantity(item.id, item.product.id, item.quantity + 1)"
				>
					+
				</button>
			</div>
			<span class="w-16 text-gray-700">{{
				item.product.price * item.quantity
			}}</span>
			<button
				class="w-16 bg-red-500 hover:bg-red-600 text-white px-2 py-1 rounded"
				@click="updateQuantity(item.id, item.product.id, 0)"
			>
				刪除
			</button>
		</div>
	</div>

	<div class="text-right mr-4 text-lg">
		總金額 ({{ totalQuantity }} 個商品):$ {{ totalPrice }}
		<br />
		<button
			class="bg-orange-500 hover:bg-orange-600 text-white px-2 py-1 rounded"
			@click="checkoutHandler()"
		>
			Checkout
		</button>
	</div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";
import axios from "axios";
import { computed, onMounted, ref } from "vue";

const authStore = useAuthStore();
const isAuthenticated = computed(() => authStore.isAuthenticated);
const items = ref([]);

const totalPrice = ref(0);
const totalQuantity = ref(0);

const getCartItems = async () => {
	try {
		const response = await axios.get("http://localhost:8080/api/cart/", {
			headers: {
				Authorization: `Bearer ${authStore.token}`,
			},
		});
		items.value = response.data.cartItems.sort((a, b) => a.id - b.id);
		totalPrice.value = response.data.totalPrice;
		totalQuantity.value = response.data.totalQuantity;
		console.log(response.data);
	} catch (error) {
		console.log(error);
	}
};

const updateQuantity = async (itemId, productId, quantity) => {
	try {
		// 如果數量小於等於0，則刪除商品
		if (quantity <= 0) {
			const response = await axios.delete(
				`http://localhost:8080/api/cartItem/${itemId}`,
				{
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				}
			);
			console.log(response.data);
		}
		//更新商品數量
		else {
			const data = {
				productId: productId,
				quantity: quantity,
			};
			const response = await axios.put(
				`http://localhost:8080/api/cartItem/${itemId}`,
				data,
				{
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				}
			);
			console.log(response.data);
		}
		//重新計算總金額與總數量
		getCartItems();
	} catch (error) {
		console.log(error);
	}
};

//建立支付Session，並轉移到Stripe支付頁面
const checkoutHandler = async () => {
	
	const response = await axios.get(
				"http://localhost:8080/api/order/create_session",
				{
					headers: {
						Authorization: `Bearer ${authStore.token}`,
					},
				}
			);
	window.location.href = "" + response.data.url;
};

onMounted(() => {
	if (isAuthenticated.value) {
		getCartItems();
	}
});
</script>
