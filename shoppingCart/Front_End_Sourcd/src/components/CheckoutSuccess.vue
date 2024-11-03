<template>
	<div class="flex items-center justify-center min-h-screen bg-green-100">
		<div class="bg-white p-6 rounded-lg shadow-lg text-center">
			<h2 class="text-2xl font-bold mb-2">Payment Successful</h2>
			<p class="text-gray-700">Thank you for your purchase!</p>
			<button
				class="mt-4 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                @click="router.push('/')"
			>
				Back To Home
			</button>
		</div>
	</div>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();

</script>
