import axios from 'axios';

// 配置请求信息
const placeOrderUrl = 'http://localhost:8080/api/seckill/orders'; // 下单API URL
const itemId = 19; // 固定的商品ID

async function simulateOrdersForUser(userId, totalRequests) {
    let successCount = 0;
    let failureCount = 0;

    for (let i = 0; i < totalRequests; i++) {
        try {
            const response = await axios.post(placeOrderUrl, {
                userId: userId,
                itemId: itemId
            });
            if(response.status === 200 || response.status === 201) { // 根据实际情况调整状态码判断
                successCount++;
            } else {
                failureCount++;
                console.error(`下单失败 - 商品ID: ${itemId}, 用户ID: ${userId}, 状态码: ${response.status}`);
            }cd
        } catch (error) {
            failureCount++;
            console.error(`发送关于商品ID ${itemId} 的下单请求时出错:`, error.message);
        }
    }

    return { userId, successCount, failureCount };
}

async function main() {
    const totalUsers = 10; // 总共10个用户
    const requestsPerUser = 500; // 每个用户下单5次，总共50次
    const userPromises = [];

    for (let userId = 1; userId <= totalUsers; userId++) {
        userPromises.push(simulateOrdersForUser(userId, requestsPerUser));
    }

    const results = await Promise.all(userPromises);

    console.log('所有下单尝试完成');
    results.forEach(result => {
        console.log(`用户ID: ${result.userId}, 成功下单数: ${result.successCount}, 失败下单数: ${result.failureCount}`);
    });

    const totalSuccesses = results.reduce((sum, result) => sum + result.successCount, 0);
    const totalFailures = results.reduce((sum, result) => sum + result.failureCount, 0);
    const totalRequests = results.reduce((sum, result) => sum + result.successCount + result.failureCount, 0);

    console.log(`总的成功的下单总数: ${totalSuccesses}`);
    console.log(`总的失败的下单总数: ${totalFailures}`);
    console.log(`总的尝试次数: ${totalRequests}`);
}

main().then(() => {
    console.log('模拟下单结束');
});