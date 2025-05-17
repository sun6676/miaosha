<template>
    <div>
        <h1>{{ item.name }}</h1>
        <p>库存: {{ realStock }}</p>
        <p>秒杀开始时间: {{ formattedStartTime }}</p>
        <button
            @click="debouncedHandleSeckill"
            :disabled="isSubmitting"
        >
            {{ buttonText }}
        </button>
        <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import { useRoute } from 'vue-router';
import { ref, computed, onMounted } from 'vue';
import router from "@/router/index.js";

export default {
    name: "SeckillDetail",
    setup() {
        const route = useRoute();
        const itemId = route.params.itemId;

        const item = ref({});
        const realStock = ref(0); // 使用独立的库存状态
        const isSubmitting = ref(false); // 按钮禁用状态
        const errorMessage = ref(""); // 错误信息

        // 获取商品详情
        const fetchItem = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/seckill/items/${itemId}`, {
                    headers: {
                        'Cache-Control': 'no-cache',
                    },
                });
                item.value = response.data;
                realStock.value = response.data.stock; // 初始化库存
            } catch (error) {
                console.error('获取商品详情失败:', error);
                errorMessage.value = '无法加载商品信息，请稍后再试。';
            }
        };

        // 处理秒杀逻辑
        const handleSeckill = async () => {
            isSubmitting.value = true;
            errorMessage.value = "";

            try {
                const response = await axios.post(
                    'http://localhost:8080/api/seckill/orders',
                    { userId: 1, itemId: item.value.id },
                    { timeout: 5000 }
                );

                if (response.data.status === 'success') {
                    realStock.value = response.data.stock;
                    alert(`下单成功！订单号：${response.data.orderId}`);
                    router.push({ name: 'OrderConfirm' });
                } else {
                    handleBusinessError(response.data);
                }
            } catch (error) {
                if (error.response) {
                    const status = error.response.status;
                    if ([400, 401, 409, 429].includes(status)) {
                        // 传递完整的错误响应数据
                        handleBusinessError({
                            ...error.response.data,
                            status: status // 确保状态码可用
                        });
                    } else {
                        handleSystemError(error);
                    }
                } else {
                    handleSystemError(error);
                }
            } finally {
                isSubmitting.value = false;
            }
        };

        // 防抖函数
        const debounce = (func, delay) => {
            let timer = null;
            return function (...args) {
                if (timer) clearTimeout(timer);
                timer = setTimeout(() => {
                    func.apply(this, args);
                }, delay);
            };
        };

        // 防抖处理秒杀函数
        const debouncedHandleSeckill = debounce(handleSeckill, 500); // 防抖时间为 1 秒

        // 更新后的 handleBusinessError 方法
        const handleBusinessError = (data) => {
            let errorMsg = '下单失败：';
            switch (data.status) { // 使用 HTTP 状态码判断
                case 400:
                    errorMsg += data.message || '请求参数错误';
                    break;
                case 409:
                    errorMsg += data.message || '库存不足，商品已售罄';
                    break;
                case 429:
                    errorMsg += data.message || '请求过于频繁';
                    break;
                default:
                    errorMsg += data.message || '未知业务错误';
            }
            errorMessage.value = errorMsg;
        };

        // 处理系统错误
        const handleSystemError = (error) => {
            if (error.response) {
                // 服务器响应了状态码，但不是2xx
                switch (error.response.status) {
                    case 500:
                        errorMessage.value = '服务器繁忙，请稍后再试。';
                        break;
                    case 403:
                        errorMessage.value = '活动尚未开始或已结束。';
                        break;
                    default:
                        errorMessage.value = `系统错误，状态码：${error.response.status}`;
                }
            } else if (error.request) {
                // 请求已发出，但没有收到响应
                errorMessage.value = '网络连接异常，请检查您的网络。';
            } else {
                // 其他错误
                errorMessage.value = '发生未知错误，请稍后再试。';
            }
        };

        // 计算属性：格式化开始时间
        const formattedStartTime = computed(() => {
            return item.value.startTime
                ? new Date(item.value.startTime).toLocaleString()
                : '未开始';
        });

        // 按钮文本
        const buttonText = computed(() => isSubmitting.value ? '抢购中...' : '立即秒杀');

        onMounted(fetchItem);

        return {
            item,
            realStock,
            formattedStartTime,
            debouncedHandleSeckill, // 返回防抖后的函数
            isSubmitting,
            errorMessage,
            buttonText
        };
    }
};
</script>

<style scoped>
button {
    padding: 12px 24px;
    border: none;
    border-radius: 4px;
    background-color: #4CAF50;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
}

button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

.error-message {
    margin-top: 10px;
    color: red;
    font-size: 14px;
}
</style>