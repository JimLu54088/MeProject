$(function () {

	var rownumber;   // 在外部定义rownumber变量








	function validateCheckbox() {
		if (!$('#ZAISEKI').is(':checked') && !$('#HIZAISEKI').is(':checked')) {
			alert("在籍と非在籍がいずれにしても、１つのチェックが必須です。");
			return false;
		}

		return true;
	}




	// 发送GET请求获取JSON数据
	$.getJSON('/getsubject2', function (response) {

		//所属会社
		var options_kaisya = response.slice(0, 2);  // 选择数组的某幾項
		var selectElement_kaisya = $('#syozokuKaisyaSelect');  // 获取select元素
		var customValue_kaisya = 1;


		// 遍历选项数据，将其添加到选择框中for 所属会社
		options_kaisya.forEach(function (option) {

			selectElement_kaisya.append('<option value="' + customValue_kaisya + '">' + option + '</option>');
			customValue_kaisya++;  // 遞增選項值
		});


		//職業種類
		var options_gyomukind = response.slice(18, 24);  // 选择数组的某几项
		var selectElement_gyomukind = $('#syokugyoKindSelect');  // 获取select元素
		var customValue_gyomukind = 1;

		// 遍历选项数据，将其添加到选择框中for 職業種類
		options_gyomukind.forEach(function (option, index) {
			var selected = '';
			if (index === 3) {  // 第四个选项（索引从0开始）
				selected = 'selected';
			}
			selectElement_gyomukind.append('<option value="' + customValue_gyomukind + '" ' + selected + '>' + option + '</option>');
			customValue_gyomukind++;  // 递增选项值
		});






	});


	//取得每頁顯示行數
	$.getJSON('/getpagingrows', function (response1) {
		// 处理第一个请求的响应数据

		var [row] = response1.slice(0, 1);
		rownumber = parseInt(row); // 将rownumber的值赋值为response1中的值


		console.log(rownumber);
	});












	//****************************************
	//「検索」ボタン押下
	//****************************************
	$(document).on('click', '#syainkanrisearch', function () {


		if (validateCheckbox()) {


			var intSYOZOKU_KAISYA = $('#syozokuKaisyaSelect').val();
			//console.log("strSYOZOKU_KAISYA的值为：" + intSYOZOKU_KAISYA);
			var shainName = $('input[name="shainName"]').val();
			//console.log("shainName的值为：" + shainName);
			var intSYOKUGYO_KIND = $('#syokugyoKindSelect').val();

			var intZAISEKI = $('#ZAISEKI').is(':checked') ? 1 : 0;
			var intHIZAISEKI = $('#HIZAISEKI').is(':checked') ? 1 : 0;




			//console.log("ZAISEKI的值为：" + intZAISEKI);

			//console.log("HIZAISEKI的值为：" + intHIZAISEKI);

			// 创建一个包含数据的对象
			var data = {
				intSYOZOKU_KAISYA: intSYOZOKU_KAISYA,
				shainName: shainName,
				intSYOKUGYO_KIND: intSYOKUGYO_KIND,
				intZAISEKI: intZAISEKI,
				intHIZAISEKI: intHIZAISEKI,
			};











			$.ajax({
				url: '/list', //アクセスするURL
				type: 'post', //post or get
				data: data,//アクセスするときに必要なデータを記載
				//contentType: 'application/json',
			})
				.done(function (response) {
					//通信成功時の処理
					//成功したとき実行したいスクリプトを記載

					// 該当するデータが無かった場合
					if (!response) {
						alert('該当するデータはありませんでした');
						return;
					}




					function modifyTaisyaDate(date) {
						if (date === undefined) {
							return ''; // 将 undefined 转换为空白字符串
						}
						return formatNyuushaDate(date); // 如果不是 undefined，则返回原始值  formatNyuushaDate這個方法對nyuusha taisha都適用
					}

					function formatNyuushaDate(dateString) {
						var date = new Date(dateString);

						var year = date.getFullYear();
						var month = (date.getMonth() + 1).toString().padStart(2, '0');
						var day = date.getDate().toString().padStart(2, '0');

						return year + '年' + month + '月' + day + '日';
					}



				





					//json文字列をjsonオブジェクトに切り替え
					const json = JSON.parse(response);







					var page_number = 1;
					var records_per_page = rownumber;
					var count = json.length;
					var total_page = Math.ceil(count / records_per_page);
					$.fn.displayPaginationButtons = function () {
						var buttons_text = '<a href="#" onClick = "javascript:$.fn.prevPage();">&laquo;</a>';
						var active = '';
						for (var i = 1; i <= total_page; i++) {
							if (i == 1) {
								active = 'active';
							} else {
								active = '';
							}
							buttons_text = buttons_text + '<a href="#" id = "page_index' + i + '" onClick="javascript:$.fn.changePageIndex(' + i + ');" class="page_index' + active + '">' + i + '</a>';
						}
						buttons_text = buttons_text + '<a href="#" onClick="javascript:$.fn.nextPage();">&raquo;</a>';
						$(".pagination-buttons").text('');
						$(".pagination-buttons").append(buttons_text);
					}
					$.fn.displayPaginationButtons();

					//画面に検索件数を設定
					$('#cnt').text("件数：" + count + "件");





					$.fn.displayTableData = function () {
						var start_index = (page_number - 1) * records_per_page;
						var end_index = start_index + (records_per_page - 1);
						end_index = (end_index >= count) ? count - 1 : end_index;
						var inner_html = '';

						for (var i = start_index; i <= end_index; i++) {

							
							var formattedNyuushaDate = formatNyuushaDate(json[i].NYUUSYA_DATE);
							var modifiedTaisyaDate = modifyTaisyaDate(json[i].TAISYA_DATE);


							inner_html = inner_html + '<tr>' +
								'<td>' + json[i].KAISHANAME + '</td>' +
								'<td>' + json[i].FIRST_NAME_KANJI + "　" + json[i].LAST_NAME_KANJI + '</td>' +
								'<td>' + json[i].sname + '</td>' +
								'<td>' + json[i].GYOMUKIND + '</td>' +
								'<td>' + formattedNyuushaDate + '</td>' +
								'<td>' + modifiedTaisyaDate + '</td>' +
								'<td><button class=\'btnUpdate\' id=' + json[i].SYAIN_ID + '>更新</button>&nbsp;<button class=\'btnDelete\' id=' + json[i].SYAIN_ID + '>削除</button></td>' +
								'</tr>';








						}

						// //前回検索結果をクリア
						// var tBody = $("#tb1");
						// tBody.empty();
						$("table tbody tr").remove();
						$("table tbody").append(inner_html);
						$(".page_index").removeClass('active');
						$(".page_index" + page_number).addClass('active');
						$(".pagination-details").text('Showing ' + (start_index + 1) + ' to ' + (end_index + 1) + ' of ' + count + ' entries.');
					}
					$.fn.nextPage = function () {
						if (page_number < total_page) { // 检查当前页码是否小于总页数
							page_number++;
							$.fn.displayTableData();
						}
					}

					$.fn.prevPage = function () {
						if (page_number > 1) { // 检查当前页码是否大于1
							page_number--;
							$.fn.displayTableData();
						}
					}

					$.fn.changePageIndex = function (index) {
						page_number = parseInt(index);
						$.fn.displayTableData();

					}

					$("table-size").change(function () {
						var tab_size = $(this).val();
						page_number = 1;
						records_per_page = parseInt(tab_size);
						total_page = Math.ceil(count / records_per_page);
						$.fn.displayPaginationButtons();
						$.fn.displayTableData();
					});

					$.fn.displayTableData();

				})
				.fail(function () {
					//通信失敗時の処理
					//失敗したときに実行したいスクリプトを記載
					alert("TG0400Search error");
				})
				.always(function () {
				});
		}



	});

	//****************************************
	//「削除」ボタン押下
	//****************************************
	$(document).on('click', '.btnDelete', function () {
		var parent = $(this).parent().parent();
		var name = parent.children('td:nth-child(2)');
		if (confirm('以下の社員名情報を削除しますか？\r\n社員名：' + name.html())) {
			var id = $(this).attr('id');
			$.ajax({
				url: '/TG0400Delete/' + id,
				type: 'DELETE',

			})
				.done(function () {
					$('#' + id).parent().parent().remove();
				})
				.fail(function () {
					alert("del error");
				})
				.always(function () {
				});
		}
	});

	//****************************************
	//「更新」ボタン押下
	//****************************************
	$(document).on('click', '.btnUpdate', function () {

		var id = $(this).attr('id');
		console.log("id的值为：" + id);
		// var param = JSON.stringify({ "SYAIN_ID": id });

		var param = {
			SYAIN_ID: id,

		};

		$.ajax({
			url: '/TG0400Change', //アクセスするURL
			type: 'post', //post or get
			data: param, //アクセスするときに必要なデータを記載
			// contentType: 'application/json',
		})
			// .done(function (response) {

			// 	//	json = JSON.stringify(response);


			// 	//	res.p



			// 	alert(response);




			// 	// 該当するデータが無かった場合
			// 	if (!response) {
			// 		alert('該当するデータはありませんでした');
			// 		return;
			// 	}

			// 	$.ajax({
			// 		url: '/TG0402',
			// 		type: 'get',
			// 		data: response, //アクセスするときに必要なデータを記載
			// 		contentType: 'application/json',
			// 	})
			// 		.done(function () {
			// 		})
			// 		.fail(function () {
			// 			alert("更新 error");
			// 		})
			// 		.always(function () {
			// 		});












			// })
			// .fail(function () {
			// })
			// .always(function () {
			// });

			.done(function (response) {

              	// alert(response);




				// 該当するデータが無かった場合
				if (!response) {
					alert('該当するデータはありませんでした');
					return;
				}





				// 将从后端获取的数据作为参数发送到TG0402页面
				// var data = encodeURIComponent(JSON.stringify(response));
				var data = encodeURIComponent(response);
				window.location.href = '/TG0402?data=' + data;
			})
			.fail(function () {
				alert('请求失败');
			});




	});

	//****************************************
	//「新規登録」ボタン押下
	//****************************************
	$(document).on('click', '#idBtnTG0400Addnew', function () {
		window.open("/TG0400Addnew", "_self");
	});

});