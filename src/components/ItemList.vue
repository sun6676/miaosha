<template>
    <div class="product-list">
        <h1>商品列表</h1>
        <ul>
            <li v-for="(item, index) in paginatedItems" :key="item.id" class="product-item">
                <span class="index-number">{{ (currentPage - 1) * pageSize + index + 1 }}</span>
                {{ item.name }} - 价格: ¥{{ item.price }} - 库存: {{ item.stock }}
                <a href="#" @click.prevent="viewDetail(item.id)">查看详情</a>
            </li>
        </ul>
        <div class="pagination">
            <button :disabled="currentPage === 1" @click="prevPage">上一页</button>
            <span>{{ currentPage }} / {{ totalPages }}</span>
            <button :disabled="currentPage === totalPages" @click="nextPage">下一页</button>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// 响应式数据
const items = ref([])
const pageSize = ref(5)
const currentPage = ref(1)

// 计算属性
const paginatedItems = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return items.value.slice(start, end)
})

const totalPages = computed(() => Math.ceil(items.value.length / pageSize.value))

// 方法
const prevPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--
    }
}

const nextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++
    }
}

const viewDetail = async (itemId) => {
    try {
        const message = `查看详情被点击 - 商品ID: ${itemId}`
        await axios.post('http://localhost:8080/api/send-message', { message })
        router.push({ name: 'SeckillDetail', params: { itemId } })
    } catch (error) {
        console.error('发送消息失败:', error)
    }
}

// 生命周期钩子
onMounted(async () => {
    try {
        console.log('ItemList component created')
        const response = await axios.get('http://localhost:8080/api/seckill/items')
        console.log('Items fetched:', response.data)
        items.value = response.data
    } catch (error) {
        console.error('获取商品列表失败:', error)
    }
})
</script>

<style scoped>
.product-list {
    font-family: Arial, sans-serif;
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

h1 {
    color: #333;
    text-align: center;
    margin-bottom: 20px;
}

ul {
    list-style-type: none;
    padding: 0;
}

.product-item {
    margin-bottom: 10px;
    padding: 15px;
    border: 1px solid #ddd;
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #fff;
    transition: all 0.3s ease;
}

.index-number {
    margin-right: 20px;
    font-weight: bold;
    color: #666;
    min-width: 30px;
    display: inline-block;
    text-align: right;
}

.product-item:hover {
    background-color: #f9f9f9;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    transform: translateY(-2px);
}

a {
    text-decoration: none;
    color: #42b983;
    font-weight: bold;
    padding: 5px 10px;
    border-radius: 4px;
    transition: all 0.2s ease;
}

a:hover {
    background-color: #f0f0f0;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 30px;
    gap: 15px;
}

.pagination button {
    padding: 8px 16px;
    background-color: #42b983;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.2s ease;
}

.pagination button:hover:not([disabled]) {
    background-color: #3aa876;
    transform: translateY(-1px);
}

.pagination button[disabled] {
    background-color: #cccccc;
    cursor: not-allowed;
    opacity: 0.7;
}

.pagination span {
    margin: 0 10px;
    font-weight: bold;
    color: #666;
}
</style>
