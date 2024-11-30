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
function setEventEntry(mobileno) {
	alert('가입이 완료되었습니다.');
	closeWindows(); 
	return;
}

