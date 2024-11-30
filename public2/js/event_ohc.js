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

// 이벤트 등록완료를 요청하는 함수
function setOHCEntry(mobileno) {
	// 이벤트 참여사항 확인
	var eventon = sessionStorage.getItem("event");
	var m_id = sessionStorage.getItem("m_id");
	var ohvalue = sessionStorage.getItem("ohvalue");
	var evententrydt = sessionStorage.getItem("evententrydt");
	
	var rstCode = 200;
	console.log("event : " + eventon);

	// 이벤트 참여시간을 확인 30분이 지났으면 무효
	let due_min = calculateTimeDifferenceInMinutes(evententrydt);
	if (eventon == "on" && due_min < 30) {
		var formData = {
		    m_id: m_id,
		    ohvalue: ohvalue,
			mobileno: mobileno,
		};
		console.log("SEND OHC ENTRY DATA..." + formData.ohvalue)
		// AJAX 호출 예제
		makeAjaxCall(formData).then(rstCode => {
			console.log("event ohc : result code["+rstCode+"]");
			alert('가입이 완료되었습니다.');
			return rstCode;
		}).catch(rstCode => {
			return rstCode;
		}).finally(() => { 
			closeWindows();
		});
	}
}

function makeAjaxCall(formData) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/event/ohc',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            caches: false, 
            success: function (response) {
                var data = JSON.parse(response);
                if (data.resCode === "E00") {
                    sessionStorage.removeItem("event");
                    sessionStorage.removeItem("offercode");
                    sessionStorage.removeItem("evententrydt"); 
                    sessionStorage.removeItem("m_id");
                    sessionStorage.removeItem("ohvalue");
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

				
		// Ohpoint 마케팅용 세팅
		if (offercode == "11") {
		
			var m_id = urlParams.get("m_id");
			var ohvalue = urlParams.get("ohvalue");
			var referer = document.referrer;
			
			if (m_id != null && m_id != '' && ohvalue != null && ohvalue != '') {
				sessionStorage.setItem("event", "on")
				sessionStorage.setItem("offercode", offercode);
				saveEntryTime(); //evententrydt
				sessionStorage.setItem("m_id", m_id);
				sessionStorage.setItem("ohvalue", ohvalue);
				sessionStorage.setItem("referer", referer);
				
				console.log('event set [' + sessionStorage.getItem("event") + ']');
				console.log('offercode set [' + sessionStorage.getItem("offercode") + ']');
				console.log('m_id set sessionStorage [' + sessionStorage.getItem("m_id") + ']');
				console.log('ohvalue set sessionStorage [' + sessionStorage.getItem("ohvalue") + ']');
				console.log('evententrydt set sessionStorage [' + sessionStorage.getItem("evententrydt") + ']');
				console.log('referer set sessionStorage [' + sessionStorage.getItem("referer") + ']');
				
			} else {
				console.log("reset event data")
				sessionStorage.removeItem("event");
				sessionStorage.removeItem("offercode");
				sessionStorage.removeItem("evententrydt"); 
				sessionStorage.removeItem("m_id");
				sessionStorage.removeItem("ohvalue");
				sessionStorage.removeItem("referer");
			}
		}
	}

});
