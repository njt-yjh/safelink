package com.dsqd.amc.linkedmo.model;

import lombok.*;
import net.minidev.json.JSONObject;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiliansAuto {
    private int mobiliansphoneAutoId;
    private String rtn_Resultcd;		//[   4byte 고정] 결과코드
    private String rtn_Resultmsg;		//[ 100byte 이하] 결과메시지
    private String rtn_Actdate;		    //[  14byte 고정] 거래일자
    private String rtn_Autobillkey;		//[  15byte 이하] 자동결제 최초등록 Key
    private String rtn_UserKey;			//[  15byte 이하] USERKEY (Reqopt 설정시 리턴)
    private String rtn_Commid;			//[   3byte 고정] 이통사
    private String rtn_Emailflag;		//[   1byte 고정] 이메일 통보여부
    private String rtn_Mobilid;			//[  15byte 이하] 모빌리언스 거래번호
    private String rtn_Mode;			//[   2byte 고정] 결제모드
    private String rtn_Prdtprice;		//[  10byte 이하] 상품 거래금액
    private String rtn_Recordkey;		//[  20byte 이하] 사이트 URL
    private String rtn_Remainamt;		//[  10byte 이하] 잔여한도
    private String rtn_Svcid;			//[  12byte 고정] 서비스ID
    private String rtn_Tradeid;			//[  40byte 이하] 가맹점거래번호
    private String rtn_Payeremail;	    //[  30byte 고정] 결제자 e-mail은 리턴되지 않으므로 요청 파라미터를 보여준다.
    private String rtn_No;			    //[  11byte 이하] 휴대폰번호는 리턴되지 않으므로 요청 파라미터를 보여준다.
    private String rtn_Prdtnm;		    //[  13byte 이하] 상품명은 리턴되지 않으므로 요청 파라미터를 보여준다.
}
