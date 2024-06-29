$(function () {


	// 发送GET请求获取JSON数据
	$.getJSON('/getsubject2', function (response) {

		//国籍
		var options_kokuseki = response.slice(2, 5);  // 选择数组的某幾項
		var selectElement_kokuseki = $('#idKOKUSEKI');  // 获取select元素
		var customValue_kokuseki = 1;


		// 遍历选项数据，将其添加到选择框中for 国籍
		options_kokuseki.forEach(function (option) {

			selectElement_kokuseki.append('<option value="' + customValue_kokuseki + '">' + option + '</option>');
			customValue_kokuseki++;  // 遞增選項值
		});


		//ビザ期間
		var options_visakikan = response.slice(7, 10);  // 选择数组的某幾項
		var selectElement_visakikan = $('#idVISA_KIKAN');  // 获取select元素
		var customValue_visakikan = 1;


		// 遍历选项数据，将其添加到选择框中for ビザ期間
		options_visakikan.forEach(function (option) {

			selectElement_visakikan.append('<option value="' + customValue_visakikan + '">' + option + '</option>');
			customValue_visakikan++;  // 遞增選項值
		});




		//在留資格名称
		var options_zairyushikakumeisho = response.slice(10, 18);  // 选择数组的某幾項
		var selectElement_zairyushikakumeisho = $('#idZAIRYU_SIKAKU');  // 获取select元素
		var customValue_zairyushikakumeisho = 1;


		// 遍历选项数据，将其添加到选择框中for 在留資格名称
		options_zairyushikakumeisho.forEach(function (option) {

			selectElement_zairyushikakumeisho.append('<option value="' + customValue_zairyushikakumeisho + '">' + option + '</option>');
			customValue_zairyushikakumeisho++;  // 遞增選項值
		});




		//所属会社
		var options_kaisya = response.slice(0, 2);  // 选择数组的某幾項
		var selectElement_kaisya = $('#idSYOZOKU_KAISYA');  // 获取select元素
		var customValue_kaisya = 1;


		// 遍历选项数据，将其添加到选择框中for 所属会社
		options_kaisya.forEach(function (option) {

			selectElement_kaisya.append('<option value="' + customValue_kaisya + '">' + option + '</option>');
			customValue_kaisya++;  // 遞增選項值
		});



		//職業種類
		var options_syokugyoukind = response.slice(18, 24);  // 选择数组的某幾項
		var selectElement_syokugyoukind = $('#idSYOKUGYO_KIND');  // 获取select元素
		var customValue_syokugyoukind = 1;


		// 遍历选项数据，将其添加到选择框中for 職業種類
		options_syokugyoukind.forEach(function (option) {

			selectElement_syokugyoukind.append('<option value="' + customValue_syokugyoukind + '">' + option + '</option>');
			customValue_syokugyoukind++;  // 遞增選項值
		});



		//最終学歴
		var options_saishuugakureki = response.slice(24, 32);  // 选择数组的某幾項
		var selectElement_saishuugakureki = $('#idSAISYUU_GAKUREKI');  // 获取select元素
		var customValue_saishuugakureki = 1;


		// 遍历选项数据，将其添加到选择框中for 最終学歴
		options_saishuugakureki.forEach(function (option) {

			selectElement_saishuugakureki.append('<option value="' + customValue_saishuugakureki + '">' + option + '</option>');
			customValue_saishuugakureki++;  // 遞增選項值
		});








	});


	//****************************************
	//「登録」ボタン押下
	//****************************************
	$("#register").on("submit", function (e) {

		// HTMLでの送信をキャンセル
		e.preventDefault();

		/****************************
		 *取引先Mainデータ取得
		*****************************/

		//inputタグのnameプロパティよりデータ取得
		var FIRST_NAME_KANJI = $("input[name='FIRST_NAME_KANJI']").val();
		var LAST_NAME_KANJI = $("input[name='LAST_NAME_KANJI']").val();


		var FIRST_NAME_KANA = $("input[name='FIRST_NAME_KANA']").val();
		var LAST_NAME_KANA = $("input[name='LAST_NAME_KANA']").val();
		var FIRST_NAME_EIGO = $("input[name='FIRST_NAME_EIGO']").val();
		var LAST_NAME_EIGO = $("input[name='LAST_NAME_EIGO']").val();
		var SEIBETU = $('#idSEIBETU').val();
		var TANJYOBI = $('#idTANJYOBI').val();
		var KOKUSEKI = $('#idKOKUSEKI').val();
		var SYUSSINN = $("input[name='SYUSSINN']").val();
		var HAIGUSYA = $('#idHAIGUSYA').val();
		var PASSPORT_NUM = $("input[name='PASSPORT_NUM']").val();
		var PASSPORT_END_DATE = $('#idPASSPORT_END_DATE').val();
		var VISA_KIKAN = $('#idVISA_KIKAN').val();
		var VISA_END_DATE = $('#idVISA_END_DATE').val();
		var ZAIRYU_SIKAKU = $('#idZAIRYU_SIKAKU').val();
		var KOJIN_NUM = $("input[name='KOJIN_NUM']").val();
		var ZAIRYU_NUM = $("input[name='ZAIRYU_NUM']").val();
		var SYOZOKU_KAISYA = $('#idSYOZOKU_KAISYA').val();
		var NYUUSYA_DATE = $('#idNYUUSYA_DATE').val();
		var TAISYA_DATE = $('#idTAISYA_DATE').val();
		var SYOKUGYO_KIND = $('#idSYOKUGYO_KIND').val();
		var RAINITI_DATE = $('#idRAINITI_DATE').val();
		var BIKOU = $("textarea[name='BIKOU']").val();
		var YUUBIN = $('#zipcode').val();
		var JYUSYO_1 = $('#address_1').val();
		var JYUSYO_2 = $('#address_2').val();
		var MOYORI_EKI = $('#idMOYORI_EKI').val();
		var TEL = $("input[name='TEL']").val();
		var EMAIL = $("input[name='EMAIL']").val();
		var WECHAT = $("input[name='WECHAT']").val();
		var LINE = $("input[name='LINE']").val();
		var BOKOKU_JYUSYO = $("textarea[name='BOKOKU_JYUSYO']").val();
		var BOKOKU_KINNKYUU_RENNRAKU = $("textarea[name='BOKOKU_KINNKYUU_RENNRAKU']").val();
		var SAISYUU_GAKUREKI = $('#idSAISYUU_GAKUREKI').val();
		var GAKKOU_NAME = $("input[name='GAKKOU_NAME']").val();
		var SENNMOM_NAME = $("input[name='SENNMOM_NAME']").val();
		var SOTUGYO_DATE = $("input[name='SOTUGYO_DATE']").val();

		// console.log("seibetsu的值为：" + SEIBETU);



		var param = "{\"insertFIRST_NAME_KANJI\":\"" + FIRST_NAME_KANJI + "\","
			+ "\"insertLAST_NAME_KANJI\":\"" + LAST_NAME_KANJI + "\","
			+ "\"insertFIRST_NAME_KANA\":\"" + FIRST_NAME_KANA + "\","
			+ "\"insertLAST_NAME_KANA\":\"" + LAST_NAME_KANA + "\","
			+ "\"insertFIRST_NAME_EIGO\":\"" + FIRST_NAME_EIGO + "\","
			+ "\"insertLAST_NAME_EIGO\":\"" + LAST_NAME_EIGO + "\","
			+ "\"insertSEIBETU\":\"" + SEIBETU + "\","
			+ "\"insertTANJYOBI\":\"" + TANJYOBI + "\","
			+ "\"insertKOKUSEKI\":\"" + KOKUSEKI + "\","
			+ "\"insertSYUSSINN\":\"" + SYUSSINN + "\","
			+ "\"insertHAIGUSYA\":\"" + HAIGUSYA + "\","
			+ "\"insertPASSPORT_NUM\":\"" + PASSPORT_NUM + "\","
			+ "\"insertPASSPORT_END_DATE\":\"" + PASSPORT_END_DATE + "\","
			+ "\"insertVISA_KIKAN\":\"" + VISA_KIKAN + "\","
			+ "\"insertVISA_END_DATE\":\"" + VISA_END_DATE + "\","
			+ "\"insertZAIRYU_SIKAKU\":\"" + ZAIRYU_SIKAKU + "\","
			+ "\"insertKOJIN_NUM\":\"" + KOJIN_NUM + "\","
			+ "\"insertZAIRYU_NUM\":\"" + ZAIRYU_NUM + "\","
			+ "\"insertSYOZOKU_KAISYA\":\"" + SYOZOKU_KAISYA + "\","
			+ "\"insertNYUUSYA_DATE\":\"" + NYUUSYA_DATE + "\","
			+ "\"insertTAISYA_DATE\":\"" + TAISYA_DATE + "\","
			+ "\"insertSYOKUGYO_KIND\":\"" + SYOKUGYO_KIND + "\","
			+ "\"insertRAINITI_DATE\":\"" + RAINITI_DATE + "\","
			+ "\"insertBIKOU\":\"" + BIKOU + "\","
			+ "\"insertYUUBIN\":\"" + YUUBIN + "\","
			+ "\"insertJYUSYO_1\":\"" + JYUSYO_1 + "\","
			+ "\"insertJYUSYO_2\":\"" + JYUSYO_2 + "\","
			+ "\"insertMOYORI_EKI\":\"" + MOYORI_EKI + "\","
			+ "\"insertTEL\":\"" + TEL + "\","
			+ "\"insertEMAIL\":\"" + EMAIL + "\","
			+ "\"insertWECHAT\":\"" + WECHAT + "\","
			+ "\"insertLINE\":\"" + LINE + "\","
			+ "\"insertBOKOKU_JYUSYO\":\"" + BOKOKU_JYUSYO + "\","
			+ "\"insertBOKOKU_KINNKYUU_RENNRAKU\":\"" + BOKOKU_KINNKYUU_RENNRAKU + "\","
			+ "\"insertSAISYUU_GAKUREKI\":\"" + SAISYUU_GAKUREKI + "\","
			+ "\"insertGAKKOU_NAME\":\"" + GAKKOU_NAME + "\","
			+ "\"insertSENNMOM_NAME\":\"" + SENNMOM_NAME + "\","
			+ "\"insertSOTUGYO_DATE\":\"" + SOTUGYO_DATE + "\"}";

		console.log("Data的值为：" + param);


		// 送信
		$.ajax({
			url: '/TG0401Insert',
			type: 'post',
			data: param,
			contentType: 'application/json',
		})
			.done(function () {
				alert("登録成功");
				
				 window.open("/getsubject", "_self");
				

			})
			.fail(function () {
				alert("登録失敗");
			})
			.always(function () {
			});
	})


});
