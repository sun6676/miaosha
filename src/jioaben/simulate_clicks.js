import axios from 'axios';

async function fetchItemIds() {
    try {
        // 替换为你的实际API URL
        const response = await axios.get('http://localhost:8080/api/seckill/items');

        // 假设每个item对象都有一个id属性
        return response.data.map(item => item.id);
    } catch (error) {
        console.error('获取商品列表失败:', error);
        return [];
    }
}

async function simulateClicks(itemIds, totalRequests) {
    let successCount = 0;
    let failureCount = 0;

    for (let i = 0; i < totalRequests; i++) {
        // 随机选择一个商品ID
        const itemId = itemIds[Math.floor(Math.random() * itemIds.length)];
        const message = `查看详情被点击 - 商品ID: ${itemId}`; // 使用随机选择的商品ID

        try {
            await axios.post('http://localhost:8080/api/send-message', { message });
            successCount++;
            if (i % 1000 === 0) { // 每1000条消息打印一次状态
                console.log(`已发送 ${i + 1} 条消息`);
            }
        } catch (error) {
            failureCount++;
            console.error(`发送关于商品ID ${itemId} 的消息时出错:`, error);
        }
    }

    console.log('所有消息发送完成');
    console.log(`成功发送的消息总数: ${successCount}`);
    console.log(`发送失败的消息总数: ${failureCount}`);
    console.log(`总的尝试次数: ${totalRequests}`);
}

async function main() {
    const itemIds = await fetchItemIds();
    if (itemIds.length > 0) {
        const totalRequests = 10000; // 设置需要模拟的点击次数
        await simulateClicks(itemIds, totalRequests);
    } else {
        console.log('没有获取到任何商品ID，无法进行模拟点击。');
    }
}

main().then(() => {
    console.log('模拟点击结束');
});