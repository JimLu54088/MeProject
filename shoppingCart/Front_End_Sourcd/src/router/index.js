import AddProduct from "@/components/AddProduct.vue"
import CartPage from "@/components/CartPage.vue"
import CheckoutSuccess from "@/components/CheckoutSuccess.vue"
import HomePage from "@/components/HomePage.vue"
import LoginForm from "@/components/LoginForm.vue"
import RegisterForm from "@/components/RegisterForm.vue"
import UserOrder from "@/components/UserOrder.vue"
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/signup',
      name: 'signup',
      component: RegisterForm
    },
    {
      path: '/login',
      name: 'login',
      component: LoginForm
    },
    {
      path: '/add',
      name: 'add',
      component: AddProduct
    },
    {
      path: '/',
      name: 'home',
      component: HomePage
    },
    {
      path: '/cart',
      name: 'cart',
      component: CartPage
    },
    {
      path: '/checkout/success',
      name: 'checkout-success',
      component: CheckoutSuccess
    },
    {
      path: '/order',
      name: 'order',
      component: UserOrder
    }
  ]
})

export default router
