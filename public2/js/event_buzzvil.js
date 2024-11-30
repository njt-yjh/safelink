/*
//현재 시간을 기록하는 함수
function saveEntryTime() {
    // 현재 시간을 가져오기
    var currentTime = new Date().toISOString();
    // sessionStorage에 현재 시간 저장
    sessionStorage.setItem('evententrydt', currentTime);
}

// 현재 시간과 기록된 시간의 차이를 분로 계산하는 함수 
function calculateTimeDifferenceInMinutes() { 
	// 기록된 시간을 가져오기 
	var recordedTime = sessionStorage.getItem("evententrydt"); 
	if (!recordedTime) { 
		console.log('No recorded time found in sessionStorage.'); 
		return 0; 
	} 
	// 현재 시간을 가져오기 
	var currentTime = new Date(); 
	// 기록된 시간을 Date 객체로 변환 
	var recordedDate = new Date(recordedTime); 
	// 시간 차이를 밀리초(ms)로 계산 
	var timeDifferenceInMs = currentTime - recordedDate; 
	// 밀리초를 분로 변환 
	var timeDifferenceInMinutes = timeDifferenceInMs / 1000 / 60 ; 
	return timeDifferenceInMinutes; 
}
*/


function createAndSubmitForm(actionUrl, method, formData) {
    // 새로운 form 태그 생성
    const form = document.createElement('form');
    form.action = actionUrl;
    form.method = method;
    form.style.display = 'none'; // 보이지 않게 설정

    // formData 객체의 키-값 쌍을 사용하여 입력 필드 생성
    for (const key in formData) {
        if (formData.hasOwnProperty(key)) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = formData[key];
            form.appendChild(input);
        }
    }

    // 폼을 body에 추가하고 제출
    document.body.appendChild(form);
    form.submit();
}

function setBuzzEntry(mobileno) {
	var eventon = sessionStorage.getItem("event");
	var bz_tracking_id = sessionStorage.getItem("bz_tracking_id");
	var evententrydt = sessionStorage.getItem("evententrydt");
	var bz_checkcode = $('#checkcode').val();
	
	const actionUrl = '/api/v1.0/event/buzzvilHtml';
	const method = 'POST';
	const formData = {
	    bz_mobileno: mobileno,
	    bz_tracking_id: bz_tracking_id,
	    bz_checkcode: bz_checkcode,
	    bz_recordType: 'javascript'
	};

	// 함수 호출
	createAndSubmitForm(actionUrl, method, formData);
}

// 이벤트 등록완료를 요청하는 함수
function setBuzzEntry2(mobileno) {
	// 이벤트 참여사항 확인
	var eventon = sessionStorage.getItem("event");
	var bz_tracking_id = sessionStorage.getItem("bz_tracking_id");
	var evententrydt = sessionStorage.getItem("evententrydt");
	
	var rstCode = 200;
	console.log("event : " + eventon);

	// 이벤트 참여시간을 확인 30분이 지났으면 무효
	let due_min = calculateTimeDifferenceInMinutes(evententrydt);
	if (eventon == "on" && due_min < 30) {
		var formData = {
		    bz_tracking_id: bz_tracking_id,
			mobileno: mobileno,
			recordType: 'javascript'
		};
		console.log("SEND Buzzvil ENTRY DATA..." + formData.bz_tracking_id)
		// AJAX 호출 예제
		makeAjaxCall_buzz(formData).then(rstCode => {
			window.bzq("track", { action: "bz_action_complete", redirect_url: "./subscribe_done.html"});
			console.log("event buzzvil : result code["+rstCode+"]");
			alert('가입이 완료되었습니다.');
			return rstCode;
		}).catch(rstCode => {
			return rstCode;
		}).finally(() => { 
			console.log("페이지전환");
		});
	} else {
		
	}
}

function makeAjaxCall_buzz(formData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/event/buzzvil',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            caches: false, 
            success: function (response) {
                var data = JSON.parse(response);
                if (data.resCode === "E00") {
                    sessionStorage.removeItem("event");
                    sessionStorage.removeItem("offercode");
                    sessionStorage.removeItem("evententrydt"); 
                    sessionStorage.removeItem("bz_tracking_id");
                    sessionStorage.removeItem("referer");
                    resolve(200);
                } else {
                    resolve(999);
                }
            },
            error: function (xhr, status, error) {
                console.log('Error occurred:', status, error);
                reject(999);
            }
        });
    });
}


$(document).ready(function(){
	//파라미터를 받아서 참여사코드를 세팅함
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams != "") {
		var offercode = urlParams.get("offercode");

		// Buzzvil 마케팅용 세팅
		if (offercode == "12") {
			
			var bz_tracking_id = urlParams.get("bz_tracking_id");
			var referer = document.referrer;
			
			if (bz_tracking_id != null && bz_tracking_id != '') {
				sessionStorage.setItem("event", "on");
				sessionStorage.setItem("offercode", offercode);
				saveEntryTime(); //evententrydt
				sessionStorage.setItem("bz_tracking_id", bz_tracking_id);
				sessionStorage.setItem("referer", referer);
				
				console.log('event set [' + sessionStorage.getItem("event") + ']');
				console.log('offercode set sessionStorage [' + sessionStorage.getItem("offercode") + ']');
				console.log('bz_tracking_id set sessionStorage [' + sessionStorage.getItem("bz_tracking_id") + ']');
				console.log('evententrydt set sessionStorage [' + sessionStorage.getItem("evententrydt") + ']');
				console.log('referer set sessionStorage [' + sessionStorage.getItem("referer") + ']');
				
			} else {
				console.log("reset event data")
				sessionStorage.removeItem("event");
				sessionStorage.removeItem("offercode");
				sessionStorage.removeItem("evententrydt"); 
				sessionStorage.removeItem("bz_tracking_id");
				sessionStorage.removeItem("referer");
			}
		}
	}

});
