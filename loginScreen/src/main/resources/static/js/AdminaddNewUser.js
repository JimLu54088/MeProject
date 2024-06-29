$(document).ready(function() {
    var maxAttempts = 3;  // 最大嘗試次數
    var attemptCount = 0;  // 目前嘗試次數
    var countdownTimeout;  // 倒計時計時器

    $("#adminInsertUser").on("submit", function(event) {
        event.preventDefault();  // Prevent the form from submitting via the browser

        var username = $("#username").val();
        var password = $("#password").val();
        var reEnteredpassword = $("#reEnteredpassword").val();

        $.ajax({
            type: "POST",
            url: "/AdminAddNewUser",
            contentType: "application/json",
            data: JSON.stringify({ username: username, password: password, reEnteredpassword: reEnteredpassword }),
            success: function(response) {
                alert("Insert successfully");
                window.location.href = "/main.html";  
            },
            error: function(xhr, status, error) {

                        // 获取后端返回的错误消息
                        var errorMessage = xhr.responseText;
                        $("#message").text(errorMessage).css("color", "red");;

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
