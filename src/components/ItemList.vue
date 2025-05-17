<template>
    <div class="product-list">
        <h1>商品列表</h1>
        <ul>
            <li v-for="(item, index) in paginatedItems" :key="item.id" class="product-item">
                <!-- 添加了 'index-number' 类 -->
                <span class="index-number">{{ (currentPage - 1) * pageSize + index + 1 }}</span>
                {{ item.name }} - 库存: {{ item.stock }}
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
<script>
import axios from 'axios';

export default {
    name: "ItemList",
    data() {
        return {
            items: [],
            pageSize: 5, // 每页显示的商品数量
            currentPage: 1,
        };
    },
    computed: {
        paginatedItems() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.items.slice(start, end);
        },
        totalPages() {
            return Math.ceil(this.items.length / this.pageSize);
        }
    },
    created() {
        console.log('ItemList component created');
        axios.get('http://localhost:8080/api/seckill/items')
            .then(response => {
                console.log('Items fetched:', response.data);
                this.items = response.data;
            })
            .catch(error => {
                console.error('获取商品列表失败:', error);
            });
    },
    methods: {
        prevPage() {
            if (this.currentPage > 1) {
                this.currentPage--;
            }
        },
        nextPage() {
            if (this.currentPage < this.totalPages) {
                this.currentPage++;
            }
        },
        viewDetail(itemId) {
            const message = `查看详情被点击 - 商品ID: ${itemId}`;
            axios.post('http://localhost:8080/api/send-message', { message })
                .then(() => {
                    // 成功发送消息后跳转到详情页
                    this.$router.push({ name: 'SeckillDetail', params: { itemId } });
                })
                .catch(error => {
                    console.error('发送消息失败:', error);
                });
        }
    }
};
</script>

<style scoped>
.product-list {
    font-family: Arial, sans-serif;
}

h1 {
    color: #333;
}

ul {
    list-style-type: none;
    padding: 0;
}

.product-item {
    margin-bottom: 10px;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

/* 新增的样式 */
.index-number {
    margin-right: 20px; /* 调整这个值以增加间距 */
    font-weight: bold; /* 可选：使序号更加突出 */
}

.product-item:hover {
    background-color: #f9f9f9;
}

a {
    text-decoration: none;
    color: blue;
}

.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
}

.pagination button {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
    margin: 0 5px;
}

.pagination button[disabled] {
    background-color: #ccc;
    cursor: not-allowed;
}

.pagination span {
    margin: 0 10px;
}
</style>