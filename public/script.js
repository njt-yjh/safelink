$(document).ready(function () {
    function toggleAgreeAll() {
        $('.agree-check').prop('checked', $('#agreeAll').is(':checked'));
    }

    function updateAgreeAll() {
        $('#agreeAll').prop('checked', $('.agree-check:checked').length === $('.agree-check').length);
    }

    function showLoading() {
        $('.loading').show();
    }

    function hideLoading() {
        $('.loading').hide();
    }

    function handleFormSubmit(event) {
        event.preventDefault();

        var form = $('#subscriptionForm')[0];
        if (!form.checkValidity()) {
            event.stopPropagation();
            form.classList.add('was-validated');
            return;
        }

        var telcoSelected = $('input[name="telco"]:checked').length > 0;
        var phoneNumber = $('#phoneNumber').val();
        var phoneValid = /^010[1-9]\d{7,8}$/.test(phoneNumber);
        var allAgreed = $('#agree1').is(':checked') && $('#agree2').is(':checked') && $('#agree3').is(':checked');

        if (!telcoSelected) {
            alert('통신사를 선택해주세요.');
            $('input[name="telco"]').first().focus();
            return;
        }
        
        if (!phoneValid) {
            $('#phoneNumberFeedback').show();
            $('#phoneNumber').focus();
            return;
        }

        if (!allAgreed) {
            alert('모든 동의 항목에 체크해주세요.');
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
            spcode: $('input[name="telco"]:checked').val(),
            mobileno: $('#phoneNumber').val(),
            agree1: $('#agree1').is(':checked'),
            agree2: $('#agree2').is(':checked'),
            agree3: $('#agree3').is(':checked')
        };
        
        showLoading();
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/subscribe',
            data: JSON.stringify(formData),
            contentType: 'application/json',
           	//dataType: 'json', 
            success: function (response) {
				var data = JSON.parse(response);
                if (data.code === 200) {
                    alert('가입이 완료되었습니다.');
                    $('#subscriptionModal').modal('hide');
                } else {
                    alert(data.msg);
                }
            },
            complete: function () {
                hideLoading();
            }
        });
    }

    function validatePhoneNumber() {
        var phoneNumber = $('#phoneNumber').val();
        phoneNumber = phoneNumber.replace(/[^0-9]/g, '');
        $('#phoneNumber').val(phoneNumber);
    }

    function checkPhoneNumber() {
        var phoneNumber = $('#phoneNumber').val();
        var isValid = /^010[1-9]\d{7,8}$/.test(phoneNumber);
        if (!isValid) {
            $('#phoneNumberFeedback').show();
        } else {
            $('#phoneNumberFeedback').hide();
        }
    }

    function loadAgreementContent(fileName) {
        $.get(fileName, function (data) {
            var lines = data.split('\n');
            var title = lines[0];
            var content = lines.slice(1).join('<br/>');
            $('#agreeModalLabel').text(title);
            $('#agreeModalBody').html(content);
            $('#agreeModal').modal('show');
        });
    }

    $('.agree-label').on('click', function () {
        var fileName = $(this).data('file');
        if (fileName) {
            loadAgreementContent(fileName);
        }
    });

    $('#agreeAll').on('click', toggleAgreeAll);
    $('.agree-check').on('click', updateAgreeAll);
    $('#subscriptionForm').on('submit', handleFormSubmit);
    
    $('#phoneNumber').on('input', validatePhoneNumber);
    $('#phoneNumber').on('focusout', checkPhoneNumber);
    
    
    // 해지 요청 처리
    $('#cancelSubmit').on('click', function() {
        let carrier = $('input[name="carrier"]:checked').val();
        let phone = $('#cancelPhone').val();
        let cancelAgree = $('#cancelAgree').is(':checked');

        if (!carrier || !phone || !cancelAgree) {
            alert('모든 항목을 입력하고 동의해야 합니다.');
            return;
        }

        $.ajax({
            url: '/api/v1.0/cancel',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ carrier: carrier, phone: phone, cancelAgree: cancelAgree }),
            success: function(response) {
				var data = JSON.parse(response);
                if (data.code === 200) {
                    alert('해지 요청이 정상 처리되었습니다.');
                    $('#cancelModal').modal('hide');
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
});
