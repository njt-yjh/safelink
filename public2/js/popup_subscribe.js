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


    // popup에서 allcheck 기능
	// allcheck 체크박스를 클릭했을 때
    $('#allcheck').on('change', function() {
        var isChecked = $(this).is(':checked');
        // agree1, agree2, agree3 체크박스의 상태를 allcheck의 상태와 동일하게 설정
        $('input[name="agree1"], input[name="agree2"], input[name="agree3"]').prop('checked', isChecked);
    });

    // agree1, agree2, agree3 체크박스를 클릭했을 때
    $('input[name="agree1"], input[name="agree2"], input[name="agree3"]').on('change', function() {
        // 모든 agree 체크박스가 체크된 상태인지 확인
        var allChecked = $('input[name="agree1"]').is(':checked') &&
                         $('input[name="agree2"]').is(':checked') &&
                         $('input[name="agree3"]').is(':checked');
        // allcheck 체크박스의 상태를 모든 agree 체크박스의 상태에 따라 설정
        $('#allcheck').prop('checked', allChecked);
    });
	
	// 가입처리 
	$('.agree_btn').on("click", function(){
		// 입력데이터 검증
		
		var telcoSelected = $('#spcode').val().length > 0;
		var phoneNumber = $.trim($('#phoneNumber').val());
		var phoneValid1 = /^010/.test(phoneNumber);
		var phoneValid2 = /^010[1-9]/.test(phoneNumber);
		var phoneValid3 = /^010\d{8}/.test(phoneNumber);
		var allAgreed = $('#agree1').is(':checked') && $('#agree2').is(':checked') && $('#agree3').is(':checked');
		
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

        if (!allAgreed) {
            alert('서비스약관, 개인정보수집동의, 제3자정보제공동의는 꼭 필요한 내용입니다.');
            if (!$('#agree1').is(':checked')) {
                $('#agree1').focus();
            } else if (!$('#agree2').is(':checked')) {
                $('#agree2').focus();
            } else if (!$('#agree3').is(':checked')) {
                $('#agree3').focus();
            }
            return;
        }
				
		var formData = {
		    spcode: $('#spcode').val(),
		    mobileno: phoneNumber,
			offercode: $('#offercode').val(),
		    agree1: $('#agree1').is(':checked'),
		    agree2: $('#agree2').is(':checked'),
		    agree3: $('#agree3').is(':checked')
		};
		
		$.ajax({
            type: 'POST',
            url: '/api/v1.0/subscribe',
            data: JSON.stringify(formData),
            contentType: 'application/json',
			caches: false, 
           	//dataType: 'json', 
            success: function (response) {
				var data = JSON.parse(response);
                if (data.code === 200) {
                    alert('가입이 완료되었습니다.');
					closeWindows();
                    //$('#subscriptionModal').modal('hide');
				} else if (data.code === 901) {
					alert('휴대폰약속번호 서비스에 가입이 된 전화번호입니다.[901]');
				} else if (data.code === 923) {
						alert('휴대폰약속번호 서비스에 가입이 불가한 전화번호입니다.[923]');
                } else {
                    alert(data.msg);
                }
            },
            complete: function () {
                //hideLoading();
            }
        });
	});

});//style.js
