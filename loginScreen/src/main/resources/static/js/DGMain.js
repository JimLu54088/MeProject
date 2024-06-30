$(document).ready(function () {
    // 发送GET请求获取JSON数据
    $.getJSON('/getdropDownSelect', function (response) {
        console.log('Response:', response); // 添加日志记录

        // 确认数据成功接收
        if (response && response.length > 0) {
            var options_DGMain = response;
            var selectElement_kaisya = $('#redirectDropdown');  // 获取select元素

            // 遍历选项数据，将其添加到选择框中
            options_DGMain.forEach(function (option) {
                selectElement_kaisya.append('<option value="' + option.item_no + '">' + option.item_name + '</option>');
            });
        } else {
            console.error('No options received or response format is incorrect');
        }
    }).fail(function (jqxhr, textStatus, error) {
        var err = textStatus + ", " + error;
        console.error("Request Failed: " + err);
    });


	   // 监听按钮点击事件
	   $('#DGMainSeawrch').on('click', function () {
		var selectedValue = $('#redirectDropdown').val();
		if (selectedValue == "2") {
			window.location.href = '/DGlogin.html';
		} 
	});







});
