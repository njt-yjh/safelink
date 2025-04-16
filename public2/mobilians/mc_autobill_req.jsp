<%@page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%
/****************************************************************************************
* 파일명 : mc_req.jsp
* 작성자 : 기술지원팀
* 작성일 : 2016.06
* 용  도 : 휴대폰 자동결제 결제정보 입력 페이지
* 버  전 : 0009
* ---------------------------------------------------------------------------------------
* 가맹점의 소스 임의변경에 따른 책임은 모빌리언스에서 책임을 지지 않습니다.
* 결제실서버 전환시 반드시 모빌리언스 기술지원팀으로 연락바랍니다.
****************************************************************************************/



/*****************************************************************************************
- unique한 거래번호를 위한 거래일시 (밀리세컨드까지 조회)
*****************************************************************************************/
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
String appr_dtm = dateFormat.format(new Date());



/*****************************************************************************************
- 필수 입력 항목
*****************************************************************************************/
String Mode			= "43";		//[   2byte 고정] 휴대폰 자동결제 요청을 위한 고정값. 43 수정불가!!!
String Recordkey	= "";		//[  20byte 이하] 가맹점도메인 (예: www.mcash.co.kr)
String Mrchid		= "";		//[   8byte 고정] 모빌리언스에서 부여한 상점ID. 서비스ID 앞 8자리. (8byte 숫자 형식)
String Svcid		= "";		//[  12byte 고정] 모빌리언스에서 부여한 서비스ID (12byte 숫자 형식)
String Prdtnm		= "";		//[  50byte 이하] 상품명
String Prdtprice	= "";		//[  10byte 이하] 결제요청금액
String USERKEY		= "";		//[  15byte 이하] 휴대폰정보(이통사, 휴대폰번호, 주민번호) 대체용 USERKEY
String AutoBillFlag	= "";		//[   1byte 고정] 자동결제구분. 자동결제 시 "2" 세팅. (0: 해당없음, 1: 최초등록, 2: 2회차이상 거래)
String AutoBillKey	= "";		//[  15byte 이하] 최초 일반결제 시 발급받은 자동결제용 key
String AutoBillDate	= "";		//[   8byte 고정] 자동결제 최초일자. (자동결제 1회차 일반결제일)

String Tradeid		= Svcid + "_" + appr_dtm;	//[4byte 이상, 40byte 이하] 가맹점거래번호. 결제 요청 시 마다 unique한 값을 세팅해야 함.
												//해당 샘플에는 테스트를 위해 {가맹점 서비스ID + 요청일시} 형식으로 세팅하였음.




/*****************************************************************************************
- 선택 입력 항목
*****************************************************************************************/
String ReqOpt		= "";		//[  30byte 이하] USERKEY 요청구분 (AUTOPAY 또는 PARTPAY 세팅 시 결제 완료 후 USERKEY 리턴)
String Email		= "";		//[  30byte 이하] 결제자 e-mail
String Emailflag	= "N";		//[   1byte 고정] 결제 통보 이메일 보내기 여부
String Userid		= "";		//[  50byte 이하] 가맹점 결제자ID
String Prdtcd		= "";		//[  40byte 이하] 상품코드. 자동결제인 경우 상품코드별 SMS문구를 별도 세팅할 때 사용하며 사전에 모빌리언스에 등록이 필요함.
String Item			= "";		//[   8byte 이하] 아이템코드. 미사용 시 반드시 공백으로 세팅.
String Cpcd			= "";		//[  20byte 이하] 리셀러하위상점key. 리셀러 업체인 경우에만 세팅.
String Sellernm		= "";		//[  50byte 이하] 실판매자 이름 (오픈마켓의 경우 실 판매자 정보 필수)
String Sellertel	= "";		//[  15byte 이하] 실판매자 전화번호 (오픈마켓의 경우 실 판매자 정보 필수)

String Commid		= "";		//[   3byte 고정] 이통사. USERKEY 사용시에는 세팅할 필요없음
String No			= "";		//[  11byte 이하] 휴대폰번호. USERKEY 사용시에는 세팅할 필요없음
String Socialno		= "";		//[  13byte 이하] 주민번호. USERKEY 사용시에는 세팅할 필요없음


//이메일이 있으면 flag를 Y로 설정한다.
if(!"".equals(Email)) {
	Emailflag = "Y";
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr"/>
<title>휴대폰 결제 SAMPLE PAGE</title>
<script language="javascript">
function payRequest(){
	document.payForm.submit();
}
</script>
</head>

<body>
<form name="payForm" method="post" action="mc_autobill_result.jsp">
<table border="1" width="100%">
<tr>
	<td align="center" colspan="6"><font size="15pt"><b>휴대폰 자동결제 SAMPLE PAGE</b></font></td>
</tr>
<tr>
	<td colspan="6"><font color="red">&nbsp;빨간색 항목은 필수 값!!!</font></td>
</tr>
<tr>
	<td align="center"><font color="red">결제 요청 구분(43 고정)</font></td>
	<td align="center"><font color="red">*Mode</font></td>
	<td><input type="text" name="Mode" id="Mode" size="30" maxlength="2" value="<%=Mode%>"></td>
	<td align="center"><font color="red">가맹점 도메인</font></td>
	<td align="center"><font color="red">*Recordkey</font></td>
	<td><input type="text" name="Recordkey" id="Recordkey" size="30" maxlength="20" value="<%=Recordkey%>"></td>
</tr>
<tr>
	<td align="center"><font color="red">상점아이디</font></td>
	<td align="center"><font color="red">*Mrchid</font></td>
	<td><input type="text" name="Mrchid" id="Mrchid" size="30" maxlength="8" value="<%=Mrchid%>"></td>
	<td align="center"><font color="red">서비스아이디</font></td>
	<td align="center"><font color="red">*Svcid</font></td>
	<td><input type="text" name="Svcid" id="Svcid" size="30" maxlength="12" value="<%=Svcid%>"></td>
</tr>
<tr>
	<td align="center"><font color="red">상품명</font></td>
	<td align="center"><font color="red">*Prdtnm</font></td>
	<td><input type="text" name="Prdtnm" id="Prdtnm" size="30" maxlength="50" value="<%=Prdtnm%>"></td>
	<td align="center"><font color="red">결제요청금액</font></td>
	<td align="center"><font color="red">*Prdtprice</font></td>
	<td><input type="text" name="Prdtprice" id="Prdtprice" size="30" maxlength="10" value="<%=Prdtprice%>"></td>
</tr>
<tr>
	<td align="center"><font color="red">자동결제 유저키</font></td>
	<td align="center"><font color="red">*USERKEY</font></td>
	<td><input type="text" name="USERKEY" id="USERKEY" size="30" maxlength="15" value="<%=USERKEY%>"></td>
	<td align="center"><font color="red">가맹점거래번호</font></td>
	<td align="center"><font color="red">*Tradeid</font></td>
	<td><input type="text" name="Tradeid" id="Tradeid" size="30" maxlength="40" value="<%=Tradeid%>"></td>
</tr>
<tr>
	<td align="center"><font color="red">자동결제 구분</font></td>
	<td align="center"><font color="red">*AutoBillFlag</font></td>
	<td><input type="text" name="AutoBillFlag" id="AutoBillFlag" size="30" maxlength="1" value="<%=AutoBillFlag%>"></td>
	<td align="center"><font color="red">최초 자동결제 최초일자</font></td>
	<td align="center"><font color="red">*AutoBillDate</font></td>
	<td><input type="text" name="AutoBillDate" id="AutoBillDate" size="30" maxlength="8" value="<%=AutoBillDate%>"></td>
</tr>
<tr>
	<td align="center"><font color="red">자동결제 KEY</font></td>
	<td align="center"><font color="red">*AutoBillKey</font></td>
	<td><input type="text" name="AutoBillKey" id="AutoBillKey" size="30" maxlength="15" value="<%=AutoBillKey%>"></td>
	<td align="center">결제 통보 이메일 보내기 여부</font></td>
	<td align="center">Emailflag</font></td>
	<td><input type="text" name="Emailflag" id="Emailflag" size="30" maxlength="1" value="<%=Emailflag%>"></td>
</tr>
<tr>
	<td align="center">USERKEY 요청 구분</td>
	<td align="center">ReqOpt</td>
	<td><input type="text" name="ReqOpt" id="ReqOpt" size="30" maxlength="" value="<%=ReqOpt%>"></td>
	<td align="center">결제자 이메일</td>
	<td align="center">Email</td>
	<td><input type="text" name="Email" id="Email" size="30" maxlength="30" value="<%=Email%>"></td>
</tr>
<tr>
	<td align="center">사용자 ID</td>
	<td align="center">Userid</td>
	<td><input type="text" name="Userid" id="Userid" size="30" maxlength="20" value="<%=Userid%>"></td>
	<td align="center">상품코드</td>
	<td align="center">Prdtcd</td>
	<td><input type="text" name="Prdtcd" id="Prdtcd" size="30" maxlength="40" value="<%=Prdtcd%>"></td>
</tr>
<tr>
	<td align="center">아이템</td>
	<td align="center">Item</td>
	<td><input type="text" name="Item" id="Item" size="30" maxlength="8" value="<%=Item%>"></td>
	<td align="center">리셀러 하위 상점 key</td>
	<td align="center">Cpcd</td>
	<td><input type="text" name="Cpcd" id="Cpcd" size="30" maxlength="20" value="<%=Cpcd%>"></td>
</tr>
<tr>
	<td align="center">실판매자명</td>
	<td align="center">Sellernm</td>
	<td><input type="text" name="Sellernm" id="Sellernm" size="30" maxlength="50" value="<%=Sellernm%>"></td>
	<td align="center">실판매자 연락처</td>
	<td align="center">Sellertel</td>
	<td><input type="text" name="Sellertel" id="Sellertel" size="30" maxlength="15" value="<%=Sellertel%>"></td>
</tr>
<tr>
	<td align="center">이통사(USERKEY 사용시 공백처리)</td>
	<td align="center">Commid</td>
	<td><input type="text" name="Commid" id="Commid" size="30" maxlength="3" value="<%=Commid%>"></td>
	<td align="center">휴대폰번호(USERKEY 사용시 공백처리)</td>
	<td align="center">No</td>
	<td><input type="text" name="No" id="No" size="30" maxlength="11" value="<%=No%>"></td>
</tr>
<tr>
	<td align="center">주민번호(USERKEY 사용시 공백처리)</td>
	<td align="center">Socialno</td>
	<td colspan="4"><input type="text" name="Socialno" id="Socialno" size="30" maxlength="13" value="<%=Socialno%>"></td>
</tr>
<tr>
	<td align="center" colspan="6">&nbsp;</td>
</tr>
<tr>
	<td align="center" colspan="6"><input type="button" value="결제하기" onclick="payRequest();"></td>
</tr>
</table>
</form>
</body>
</html>
