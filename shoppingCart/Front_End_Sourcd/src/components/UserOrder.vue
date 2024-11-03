<template>
	<div class="container mx-auto p-6">
		<h1 class="text-3xl font-extrabold text-gray-800 mb-8 text-center">
			User Orders
		</h1>

        <!--等待資料載入時，會轉圈圈-->
		<div v-if="loading" class="flex justify-center items-center h-48">
			<div
				class="w-16 h-16 border-4 border-gray-300 border-t-4 border-t-blue-500 rounded-full animate-spin"
			></div>
		</div>

        <!--資料載入後，顯示資料-->
		<div v-else class="bg-white shadow-lg rounded-lg overflow-hidden">
			<table class="min-w-full leading-normal">
				<thead>
					<tr>
						<th
							class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
						>
							Session ID
						</th>
						<th
							class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
						>
							Amount
						</th>
						<th
							class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
						>
							Status
						</th>
					</tr>
				</thead>
				<tbody>
					<tr
						v-for="order in orders"
						:key="order.id"
						class="border-b border-gray-200 bg-white hover:bg-gray-50 transition duration-150"
					>
						<td class="px-5 py-5 text-sm">
							<div class="flex items-center">
								<div class="ml-3">
									<p class="text-gray-900 whitespace-no-wrap">
										{{ order.sessionId }}
									</p>
								</div>
							</div>
						</td>
						<td class="px-5 py-5 text-sm">
							<p class="text-gray-900 whitespace-no-wrap">
								$TWD {{ order.amount }}
							</p>
						</td>
						<td class="px-5 py-5 text-sm">
							<span
								:class="{
									'inline-block px-3 py-1 font-semibold text-green-800 bg-green-200 rounded-full':
										order.status === 'paid',
									'inline-block px-3 py-1 font-semibold text-yellow-800 bg-yellow-200 rounded-full':
										order.status === 'unpaid',
								}"
							>
								{{ order.status }}
							</span>
							<button
								v-if="order.status === 'unpaid'"
								@click="goToPaymentSession(order.url)"
								class="bg-green-500 hover:bg-green-600 text-white px-4 py-1 rounded"
							>
								Click Here to Pay
							</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</template>

<script setup>
import axios from "axios";
import { useAuthStore } from "@/stores/auth";
import { onMounted, ref } from "vue";

const authStore = useAuthStore();
const orders = ref([]);
const loading = ref(true);

const getOrders = async () => {
	try {
		const response = await axios.get(
			`http://localhost:8080/api/order/find_order`,
			{
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			}
		);
		orders.value = response.data;
		console.log(response.data);
	} catch (error) {
		console.log(error);
	} finally {
		loading.value = false;
	}
};

const goToPaymentSession = (url) => {
	window.location.href = url;
};

onMounted(() => {
	getOrders();
});
</script>
