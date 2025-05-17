import { createRouter, createWebHistory } from 'vue-router';
import ItemList from '../components/ItemList.vue';
import SeckillDetail from '../components/SeckillDetail.vue';
import OrderConfirm from '../components/OrderConfirm.vue';

const routes = [
    {
        path: '/',
        name: 'ItemList',
        component: ItemList
    },
    {
        path: '/seckill/:itemId',
        name: 'SeckillDetail',
        component: SeckillDetail
    },
    {
        path: '/confirm-order',
        name: 'OrderConfirm',
        component: OrderConfirm
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
