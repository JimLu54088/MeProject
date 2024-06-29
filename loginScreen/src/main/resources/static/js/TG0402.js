$(document).ready(function () {

	// 获取URL参数
	const urlParams = new URLSearchParams(window.location.search);
	// 获取名为"data"的参数值
	const data = urlParams.get('data');

	// 解析JSON数据
	const jsonData = JSON.parse(data);


	//日期轉換
	function changeDatefromJsontoFormatted(dateString) {
		var momentDate = moment(dateString, "M月 DD, YYYY");
		var formattedDate = momentDate.format("YYYY-MM-DD");
		// console.log(formattedDate); // 输出：2023-05-15

		return formattedDate;
	}

	// console.log("data的值为：" + data);
	// console.log("jsondata的值为：" + jsonData);


	console.log("syainID的值为：" + jsonData[0].SYAIN_ID);



	//         const jsonData1 = [{"SYAIN_ID":36,"FIRST_NAME_KANJI":"A","LAST_NAME_KANJI":"A","FIRST_NAME_KANA":"A","LAST_NAME_KANA":"A","FIRST_NAME_EIGO":"A","LAST_NAME_EIGO":"A","SEIBETU":1,"TANJYOBI":"5月 13, 2023","KOKUSEKI":1,"SYUSSINN":"A","HAIGUSYA":1,"PASSPORT_NUM":"1","PASSPORT_END_DATE":"5月 13, 2023","VISA_KIKAN":1,"VISA_END_DATE":"5月 13, 2023","ZAIRYU_SIKAKU":1,"KOJIN_NUM":"A","ZAIRYU_NUM":"A","SYOZOKU_KAISYA":1,"NYUUSYA_DATE":"5月 13, 2023","TAISYA_DATE":"2023-05-13","SYOKUGYO_KIND":1,"RAINITI_DATE":"5月 13, 2023","BIKOU":"A","YUUBIN":"A","JYUSYO_1":"A","JYUSYO_2":"A","MOYORI_EKI":"A","TEL":"A","EMAIL":"A","WECHAT":"A","LINE":"A","BOKOKU_JYUSYO":"A","BOKOKU_KINNKYUU_RENNRAKU":"A","SAISYUU_GAKUREKI":1,"GAKKOU_NAME":"A","SENNMOM_NAME":"A","SOTUGYO_DATE":"5月 13, 2023","GYUMU_NENSU":1.5,"IT_OS":"A","IT_GENGO":"A","IT_DB":"A","IT_WEB_SERVER":"A","IT_FW":"A","IT_OTHER":"A","IT_BIKOU":"A","DELETE_FLAG":0,"TOUROKUBI":"May 13, 2023, 7:54:47 AM","KOUSINNBI":"May 13, 2023, 7:54:48 AM","ZAISEKI":0,"HIZAISEKI":0}];

	// const firstNameKanji = jsonData1[0].FIRST_NAME_KANJI;
	// console.log("firstNameKanji的值为：" + firstNameKanji);





	// 设置数据到输入框
	$('input[name="SYAIN_ID"]').val(jsonData[0].SYAIN_ID);
	$('input[name="FIRST_NAME_KANJI"]').val(jsonData[0].FIRST_NAME_KANJI);
	$('input[name="LAST_NAME_KANJI"]').val(jsonData[0].LAST_NAME_KANJI);
	$("input[name='FIRST_NAME_KANA']").val(jsonData[0].FIRST_NAME_KANA);
	$("input[name='LAST_NAME_KANA']").val(jsonData[0].LAST_NAME_KANA);
	$("input[name='FIRST_NAME_EIGO']").val(jsonData[0].FIRST_NAME_EIGO);
	$("input[name='LAST_NAME_EIGO']").val(jsonData[0].LAST_NAME_EIGO);
	var previousValueSEIBETU = jsonData[0].SEIBETU;
	$('input[name="SEIBETU"][value="' + previousValueSEIBETU + '"]').prop('checked', true);





	$('#idTANJYOBI').val(changeDatefromJsontoFormatted(jsonData[0].TANJYOBI));


	//國籍另外處理
	//KOKUSEKI......
	$("input[name='SYUSSINN']").val(jsonData[0].SYUSSINN);

	var previousValueHAIGUSYA = jsonData[0].HAIGUSYA;
	$('input[name="HAIGUSYA"][value="' + previousValueHAIGUSYA + '"]').prop('checked', true);


	$("input[name='PASSPORT_NUM']").val(jsonData[0].PASSPORT_NUM);

	$('#idPASSPORT_END_DATE').val(changeDatefromJsontoFormatted(jsonData[0].PASSPORT_END_DATE));
	//visakikan另外處理
	//$('#idVISA_KIKAN').val(jsonData[0].VISA_KIKAN);......
	$('#idVISA_END_DATE').val(changeDatefromJsontoFormatted(jsonData[0].VISA_END_DATE));
	//idZAIRYU_SIKAKU另外處理
	// $('#idZAIRYU_SIKAKU').val(jsonData[0].ZAIRYU_SIKAKU);.....
	$("input[name='KOJIN_NUM']").val(jsonData[0].KOJIN_NUM);
	$("input[name='ZAIRYU_NUM']").val(jsonData[0].ZAIRYU_NUM);
	// idSYOZOKU_KAISYA另外處理
	// $('#idSYOZOKU_KAISYA').val(jsonData[0].SYOZOKU_KAISYA);.......
	
	// 入社日処理
	// $('#idNYUUSYA_DATE').val(changeDatefromJsontoFormatted(jsonData[0].NYUUSYA_DATE));
	$('#idNYUUSYA_DATE').val(jsonData[0].NYUUSYA_DATE);



	// 退社日処理
	if (jsonData[0].TAISYA_DATE !== '') {
		$('input[name="TAISYA_DATE"]').val(jsonData[0].TAISYA_DATE);
	}

	// console.log("jsonTaisya的值为：" + jsonData[0].TAISYA_DATE);

	$('#idRAINITI_DATE').val(changeDatefromJsontoFormatted(jsonData[0].RAINITI_DATE));
	$("textarea[name='BIKOU']").val(jsonData[0].BIKOU);
	$('#zipcode').val(jsonData[0].YUUBIN);
	$('#address_1').val(jsonData[0].JYUSYO_1);
	$('#address_2').val(jsonData[0].JYUSYO_2);
	$('#idMOYORI_EKI').val(jsonData[0].MOYORI_EKI);
	$("input[name='TEL']").val(jsonData[0].TEL);
	$("input[name='EMAIL']").val(jsonData[0].EMAIL);
	$("input[name='WECHAT']").val(jsonData[0].WECHAT);
	$("input[name='LINE']").val(jsonData[0].LINE);
	$("textarea[name='BOKOKU_JYUSYO']").val(jsonData[0].BOKOKU_JYUSYO);
	$("textarea[name='BOKOKU_KINNKYUU_RENNRAKU']").val(jsonData[0].BOKOKU_KINNKYUU_RENNRAKU);

	$("input[name='GAKKOU_NAME']").val(jsonData[0].GAKKOU_NAME);
	$("input[name='SENNMOM_NAME']").val(jsonData[0].SENNMOM_NAME);
	$("input[name='SOTUGYO_DATE']").val(changeDatefromJsontoFormatted(jsonData[0].SOTUGYO_DATE));






	// 发送GET请求获取JSON数据
	$.getJSON('/getsubject2', function (response) {

		//国籍
		var options_kokuseki = response.slice(2, 5); // 选择数组的某几项
		var selectElement_kokuseki = $('#idKOKUSEKI'); // 获取select元素
		var customValue_kokuseki = 1;
		var defaultValue_kokuseki = jsonData[0].KOKUSEKI; // 默认值

		// 遍历选项数据，将其添加到选择框中 for 国籍
		options_kokuseki.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_kokuseki + '">' + option + '</option>');
			if (customValue_kokuseki === defaultValue_kokuseki) {
				optionElement.prop('selected', true);
			}
			selectElement_kokuseki.append(optionElement);
			customValue_kokuseki++; // 递增选项值
		});



		//ビザ期間
		var options_visakikan = response.slice(7, 10); // 选择数组的某几项
		var selectElement_visakikan = $('#idVISA_KIKAN'); // 获取select元素
		var customValue_visakikan = 1;
		var defaultValue_visakikan = jsonData[0].VISA_KIKAN; // 默认值

		// 遍历选项数据，将其添加到选择框中 for ビザ期間
		options_visakikan.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_visakikan + '">' + option + '</option>');
			if (customValue_visakikan === defaultValue_visakikan) {
				optionElement.prop('selected', true);
			}
			selectElement_visakikan.append(optionElement);
			customValue_visakikan++; // 递增选项值
		});





		//在留資格名称

		var options_zairyushikakumeisho = response.slice(10, 18); // 选择数组的某几项
		var selectElement_zairyushikakumeisho = $('#idZAIRYU_SIKAKU'); // 获取select元素
		var customValue_zairyushikakumeisho = 1;
		var defaultValue_zairyushikakumeisho = jsonData[0].ZAIRYU_SIKAKU; // 默认值

		// 遍历选项数据，将其添加到选择框中 for 在留資格名称
		options_zairyushikakumeisho.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_zairyushikakumeisho + '">' + option + '</option>');
			if (customValue_zairyushikakumeisho === defaultValue_zairyushikakumeisho) {
				optionElement.prop('selected', true);
			}
			selectElement_zairyushikakumeisho.append(optionElement);
			customValue_zairyushikakumeisho++; // 递增选项值
		});




		//所属会社

		var options_kaisya = response.slice(0, 2); // 选择数组的某几项
		var selectElement_kaisya = $('#idSYOZOKU_KAISYA'); // 获取select元素
		var customValue_kaisya = 1;
		var defaultValue_kaisya = jsonData[0].SYOZOKU_KAISYA; // 默认值

		// 遍历选项数据，将其添加到选择框中 for 所属会社
		options_kaisya.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_kaisya + '">' + option + '</option>');
			if (customValue_kaisya === defaultValue_kaisya) {
				optionElement.prop('selected', true);
			}
			selectElement_kaisya.append(optionElement);
			customValue_kaisya++; // 递增选项值
		});




		//職業種類


		var options_syokugyoukind = response.slice(18, 24); // 选择数组的某几项
		var selectElement_syokugyoukind = $('#idSYOKUGYO_KIND'); // 获取select元素
		var customValue_syokugyoukind = 1;
		var defaultValue_syokugyoukind = jsonData[0].SYOKUGYO_KIND; // 默认值

		// 遍历选项数据，将其添加到选择框中 for 職業種類
		options_syokugyoukind.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_syokugyoukind + '">' + option + '</option>');
			if (customValue_syokugyoukind === defaultValue_syokugyoukind) {
				optionElement.prop('selected', true);
			}
			selectElement_syokugyoukind.append(optionElement);
			customValue_syokugyoukind++; // 递增选项值
		});


		//最終学歴

		var options_saishuugakureki = response.slice(24, 32); // 选择数组的某几项
		var selectElement_saishuugakureki = $('#idSAISYUU_GAKUREKI'); // 获取select元素
		var customValue_saishuugakureki = 1;
		var defaultValue_saishuugakureki = jsonData[0].SAISYUU_GAKUREKI; // 默认值

		// 遍历选项数据，将其添加到选择框中 for 最終学歴
		options_saishuugakureki.forEach(function (option) {
			var optionElement = $('<option value="' + customValue_saishuugakureki + '">' + option + '</option>');
			if (customValue_saishuugakureki === defaultValue_saishuugakureki) {
				optionElement.prop('selected', true);
			}
			selectElement_saishuugakureki.append(optionElement);
			customValue_saishuugakureki++; // 递增选项值
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
		var SYAIN_ID = $("input[name='SYAIN_ID']").val();
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



		var param = "{\"insertSYAIN_ID\":\"" + SYAIN_ID + "\","
			+ "\"insertFIRST_NAME_KANJI\":\"" + FIRST_NAME_KANJI + "\","
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
			url: '/TG0402upd',
			type: 'post',
			data: param,
			contentType: 'application/json',
		})
			.done(function () {
				alert("変更成功");

				window.open("/getsubject", "_self");


			})
			.fail(function () {
				alert("登録失敗");
			})
			.always(function () {
			});










	})


});
