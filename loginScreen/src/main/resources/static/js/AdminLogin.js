$(document).ready(function() {
    var maxAttempts = 3;  // 最大嘗試次數
    var attemptCount = 0;  // 目前嘗試次數
    var countdownTimeout;  // 倒計時計時器

    $("#adminloginForm").on("submit", function(event) {
        event.preventDefault();  // Prevent the form from submitting via the browser

        var username = $("#username").val();
        var password = $("#password").val();

        $.ajax({
            type: "POST",
            url: "/Adminlogin",
            contentType: "application/json",
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                 // 登录成功，保存令牌到本地存储
                 sessionStorage.setItem('admintoken', response.admintoken);
                window.location.href = "/AdminaddNewUser.html";  // Redirect to B.html
            },
            error: function(error) {
                alert("Login failed!");
                attemptCount++;
                var remainingAttempts = maxAttempts - attemptCount;

                if (remainingAttempts > 0) {
                    $("#message").text("你還有 " + remainingAttempts + " 次機會");
                } else {
                    $("#message").text("超過最大嘗試次數，請稍後再試");
                    startCountdownAndDisableForm(10);  // 啟動倒計時 n 秒
                }
            }
        });
    });

    // 開始倒計時並禁用表單
    function startCountdownAndDisableForm(seconds) {
        // 禁用表單中的所有輸入字段和按鈕
        $("#adminloginForm input, #adminloginForm button").prop("disabled", true);

        startCountdown(seconds);
    }

    // 倒計時結束後啟用表單
    function enableFormAfterCountdown() {
        // 啟用表單中的所有輸入字段和按鈕
        $("#adminloginForm input, #adminloginForm button").prop("disabled", false);
    }

    // 倒計時函數
    function startCountdown(seconds) {
        var timer = seconds;

        // 更新倒計時消息
        updateMessage(timer);

        countdownTimeout = setInterval(function() {
            timer--;

            // 更新倒計時消息
            updateMessage(timer);

            if (timer <= 0) {
                clearInterval(countdownTimeout);
                enableFormAfterCountdown();  // 倒計時結束後啟用表單
                $("#message").text("");  // 清空消息
                attemptCount = 0;  // 重置嘗試次數
            }
        }, 1000);
    }

    // 更新倒計時消息
    function updateMessage(time) {
        $("#message").text("請稍候 " + time + " 秒後重新嘗試");
    }
});
