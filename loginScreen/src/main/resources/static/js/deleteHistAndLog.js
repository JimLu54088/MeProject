$(document).ready(function () {
 

    $('#deleteHistAndLog').click(function () {
		
		$.ajax({
                url: '/deleteHistAndLog',
                type: 'GET',
                //data: formData,
                //processData: false,
                //contentType: false,
                success: function (response) {
                    alert("Delete hist table and old logs successfully!!");
					window.location.href = "/main.html";
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Delete hist table and old logs Failed!! " + jqXHR.responseText);
					window.location.href = "/main.html";
                }
            });
		
        
    });


});
