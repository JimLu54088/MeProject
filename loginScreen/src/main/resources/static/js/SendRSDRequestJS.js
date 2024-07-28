$(document).ready(function () {
    // 在页面加载时检查是否存在令牌
    let token = sessionStorage.getItem('token');
    if (token) {
        console.log("token : " + token)
        // 解析JWT获取payload部分
        let payload = JSON.parse(atob(token.split('.')[1]));

        // 提取过期时间（exp），JWT中的时间是以秒为单位的，需要转换为毫秒
        let tokenExp = payload.exp * 1000; // 转换为毫秒
        let currentTimestamp = Date.now();

        // 检查是否过期
        if (currentTimestamp > tokenExp) {
            // 令牌已过期，重定向到登录页面或执行其他逻辑
            window.location.href = '/DGLogin.html';
        } else {
            // 令牌未过期，可以将其用于后续请求的身份验证
            // 例如设置请求头： headers: { 'Authorization': 'Bearer ' + token }
        }
    } else {
        // 如果没有令牌，重定向到登录页面
        window.location.href = '/DGLogin.html';
    }

    document.getElementById('sendRequestBtn').addEventListener('click', function () {
        var requestJson = document.getElementById('requestJson').value;

         $.ajax({
                type: "POST",
                url: "/sendRSDRequestLink", // 替換為你的後端API URL
                contentType: "application/json",
                data: requestJson,
                success: function(response) {
                    document.getElementById('responseJson').value = JSON.stringify(response, null, 4);
                },
                error: function(xhr, status, error) {
                    document.getElementById('responseJson').value = 'Error: ' + xhr.responseText;
                }
            });
    });

    $('#cleartextareaButton').click(function() {
        $('textarea').val('');
    });
});
