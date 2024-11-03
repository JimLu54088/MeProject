<template>
	<div v-if="isAuthenticated" class="p-4 bg-gray-50 min-h-screen flex">
		<div class="w-1/4 p-4">
			<div class="p-4 bg-gray-100 rounded-lg shadow-md">
				<div class="mb-4">
					<label class="block text-gray-700 font-bold mb-2">Price</label>
					<div class="flex items-center">
						<input
							placeholder="min"
							type="number"
							class="border p-2 mb-2 w-full mr-2 rounded"
							v-model="minPrice"
						/>
						<span class="mx-2">to</span>
						<input
							placeholder="max"
							type="number"
							class="border p-2 mb-2 w-full ml-2 rounded"
							v-model="maxPrice"
						/>
					</div>
				</div>
				<div class="mb-4">
					<label class="block text-gray-700 font-bold mb-2">Sort by:</label>
					<select class="border p-2 mb-4 w-full rounded" v-model="sortBy">
						<option value="">Default</option>
						<option value="price_low">Price : Low to High</option>
						<option value="price_high">Price : High to Low</option>
					</select>
				</div>
				<div class="mb-4">
					<label class="block text-gray-700 font-bold mb-2">Category:</label>
					<div v-for="category in categories" :key="category" class="mb-2">
						<input
							type="radio"
							:value="category"
							v-model="categoryType"
							class="mr-2"
						/>
						<label class="text-gray-700">{{
							category ? category : "All"
						}}</label>
					</div>
				</div>
				<button
					class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-full"
					@click="filterHandler"
				>
					Filter
				</button>
			</div>
		</div>
		<div class="w-3/4 p-4">
			<div
				v-for="product in products"
				:key="product.id"
				class="flex items-center border-b border-gray-200 py-4"
			>
				<img
					:src="product.image"
					:alt="product.name"
					class="w-16 h-16 object-cover rounded-lg shadow-sm mr-4"
				/>
				<div class="flex-1">
					<h2 class="text-lg font-bold text-gray-900">{{ product.name }}</h2>
					<p class="text-gray-700 text-sm">{{ product.description }}</p>
					<p class="text-green-600 font-semibold text-sm mt-1">
						Price: {{ product.price }}
					</p>
					<p class="text-gray-500 text-xs mt-1">
						Category: {{ product.category }}
					</p>
					<button
						class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
						@click="cartHandler(product.id)"
					>
						Add To Cart
					</button>
				</div>
			</div>

			<div
				v-if="showBubble"
				class="fixed top-8 left-1/2 transform -translate-x-1/2 bg-green-500 text-white px-4 py-2 rounded-lg shadow-lg transition-opacity duration-500 ease-in-out"
				@click="closeBubble"
			>
				Added to Cart!
			</div>

			<div class="flex justify-center">
				<vue-awesome-paginate
					:total-items="totalElement"
					:items-per-page="parseInt(pageSize)"
					:max-pages-shown="5"
					v-model="currentPage"
					@click="onClickHandler"
				/>
				<select
					class="ml-4 bg-gray-200"
					name="pageSize"
					id="pageSize"
					v-model="pageSize"
					@change="onChangeHandler"
				>
					<option value="5">5</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
				</select>
			</div>
		</div>
	</div>
	<div v-else class="p-4">
		<p class="text-red-600 font-medium">Please log in to see the products.</p>
	</div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";
import { computed, onMounted, ref } from "vue";
import axios from "axios";

const authStore = useAuthStore();
const isAuthenticated = computed(() => authStore.isAuthenticated);
//將商品資料提供給template
const products = ref([]);

const pageSize = ref(5);
const totalElement = ref(1);
const currentPage = ref(1);

const showBubble = ref(false);


const categories = ref([]);

const getAllcategories = async () => {
	try {
		const response = await axios.post(
			//資料庫從0開始，網頁從1開始，所以要-1
			`http://localhost:8080/api/product/getCategoryInFilter`,
			{}, // 第二個參數應該是請求的資料，這裡是空的物件
			{
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			}
		);
		//把取得的category資料放入categories
		categories.value = response.data;
		console.log("All categories from DB::", categories.value);
	} catch (error) {
		console.log(error);
	}
};


const categoryType = ref("");
const minPrice = ref("");
const maxPrice = ref("");
const sortBy = ref("");

const getProducts = async (min, max, category, sort, page, size) => {
	try {
		const response = await axios.get(
			//資料庫從0開始，網頁從1開始，所以要-1
			`http://localhost:8080/api/product/?minPrice=${min}&maxPrice=${max}&category=${category}&sort=${sort}&pageNumber=${
				page - 1
			}&pageSize=${parseInt(size)}`,
			{
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			}
		);
		//把取得的商品資料放入products
		products.value = response.data.content;
		totalElement.value = response.data.totalElements;
		console.log(response.data);
	} catch (error) {
		console.log(error);
	}
};
const onClickHandler = () => {
	getProducts(
		minPrice.value,
		maxPrice.value,
		categoryType.value,
		sortBy.value,
		currentPage.value,
		pageSize.value
	);
};

const onChangeHandler = () => {
	currentPage.value = 1;
	getProducts(
		minPrice.value,
		maxPrice.value,
		categoryType.value,
		sortBy.value,
		currentPage.value,
		pageSize.value
	);
};

const filterHandler = () => {
	currentPage.value = 1;
	getProducts(
		minPrice.value,
		maxPrice.value,
		categoryType.value,
		sortBy.value,
		currentPage.value,
		pageSize.value
	);
};

const addToCart = async (id) => {
	try {
		const data = {
			productId: id,
			quantity: 1,
		};

		const response = await axios.put(
			"http://localhost:8080/api/cart/add",
			data,
			{
				headers: {
					Authorization: `Bearer ${authStore.token}`,
				},
			}
		);
		showBubble.value = true;
		setTimeout(() => {
			showBubble.value = false;
		}, 2000);
		console.log(response.data);
	} catch (error) {
		console.log(error);
	}
};

const cartHandler = (id) => {
	addToCart(id);
};

const closeBubble = () => {
	showBubble.value = false;
};

//等待template載入完成，再把商品資料放入template
onMounted(() => {
	if (isAuthenticated.value) {
		getProducts(
			minPrice.value,
			maxPrice.value,
			categoryType.value,
			sortBy.value,
			currentPage.value,
			pageSize.value
		);
		getAllcategories();
	}
});
</script>

<style>
.pagination-container {
	display: flex;

	column-gap: 10px;
}

.paginate-buttons {
	height: 40px;

	width: 40px;

	border-radius: 20px;

	cursor: pointer;

	background-color: rgb(242, 242, 242);

	border: 1px solid rgb(217, 217, 217);

	color: black;
}

.paginate-buttons:hover {
	background-color: #d8d8d8;
}

.active-page {
	background-color: #3498db;

	border: 1px solid #3498db;

	color: white;
}

.active-page:hover {
	background-color: #2988c8;
}
</style>
