$(document).ready(function () {

    //반응형 초기화
    winH = 0
    winW = 0
    var delay = 500;

    //높이,너비를 윈도우의 창의 높이로 설정
    winH = $(window).innerHeight();
    winW = $(window).innerWidth();
    winResize(); //초기값설정

    //반응형 자동크기 설정
    $(window).resize(function () {
        winResize();
    });
    
    function winResize() {
        winH = $(window).innerHeight();
        winW = $(window).innerWidth();
    }
	
	function closeWindows() {
		window.open('about:blank', '_self').close();
	}

	// 통신사 선택 
	$('#service_join_wrap .service_j_btn').on('click', function(e){
		e.stopPropagation();
		e.preventDefault();
		
		$('#service_join_wrap .service_j_btn').removeClass('selected');
		$(this).addClass('selected');
		
		$('#spcode').val($(this).attr('id'));
		console.log($('#spcode').val());
   });
	
	// 취소처리 
	$('.cancel_btn').on("click", function(){
		// 입력데이터 검증
		
		var telcoSelected = $('#spcode').val().length > 0;
		var phoneNumber = $.trim($('#phoneNumber').val());
		var phoneValid1 = /^010/.test(phoneNumber);
		var phoneValid2 = /^010[1-9]/.test(phoneNumber);
		var phoneValid3 = /^010\d{8}/.test(phoneNumber);
		var cancelAgree = $('#cancelAgree').is(':checked');
		
		if (!telcoSelected) {
            alert('통신사를 선택해주세요.');
            return;
        }
        
		if (!phoneValid1) {
			alert('전화번호는 010으로 시작해야 합니다.');
            $('#phoneNumber').focus();
            return;
        }
		
		if (!phoneValid2) {
		    //$('#phoneNumberFeedback').show();
			alert('전화번호는 숫자로 입력하여 주세요.');
		    $('#phoneNumber').focus();
		    return;
		}
		
		if (!phoneValid3) {
			alert('전화번호를 다시 한번 확인해 주세요.');
		    $('#phoneNumber').focus();
		    return;
		}

        if (!cancelAgree) {
            alert('서비스 취소확인에 동의하셔야 합니다.');
            return;
        }

        $.ajax({
            url: '/api/v1.0/cancel',
            method: 'POST',
            contentType: 'application/json',
			caches: false, 
            data: JSON.stringify({ carrier: telcoSelected, phone: phoneNumber, cancelAgree: cancelAgree }),
            success: function(response) {
				var data = JSON.parse(response);
                if (data.code === 200) {
                    alert('해지 요청이 정상 처리되었습니다.');
                    closeWindows();
                } else if (data.code === 400) {
					alert('가입 내역을찾을 수 없습니다. 확인 후 다시 입력바랍니다. ');
				} else {
                    alert('해지 요청 처리에 실패했습니다.');
                }
            },
            error: function() {
                alert('해지 요청 처리에 실패했습니다.');
            }
        });
	});

});//style.js
