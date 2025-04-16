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
public class Mobilians {
    private int mobiliansphoneId;
    private String resultcd;    //결과코드
    private String resultmsg;   //결과메세지
    private String autobillkey; //[가맹점DB 필수저장]자동결제 최초등록키
    private String commid;  //통신사 코드
    private String mobilid; //[가맹점DB 필수저장]모빌리언스 거래번호
    private String mrchid;  //상점ID
    private String mstr;    //가맹점 전달 콜백 변수
    private String no;  //휴대폰번호
    private String prdtnm;  //[가맹점DB 필수저장]상품명
    private String prdtprice;   //[가맹점DB 필수저장]상품가격
    private String signdate;    //[가맹점DB 필수저장]결제일자
    private String svcid;   //[가맹점DB 필수저장]서비스ID
    private String tradeid; //[가맹점DB 필수저장]상점거래번호
    private String userid;  //사용자ID
    private String userkey; //[가맹점DB 필수저장]휴대폰정보 대체용 키
    private String cashGb;  //결제수단(MC 리턴)
    private String payeremail; ////결제자 이메일
    private String autoyn;
    private String ezkey;
    private String msg;
    private String result;
    private Date autobillDate;

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobiliansphoneId", mobiliansphoneId);
        jsonObject.put("resultcd", resultcd);
        jsonObject.put("resultmsg", resultmsg);
        jsonObject.put("autobillkey", autobillkey);
        jsonObject.put("commid", commid);
        jsonObject.put("mobilid", mobilid);
        jsonObject.put("mrchid", mrchid);
        jsonObject.put("mstr", mstr);
        jsonObject.put("no", no);
        jsonObject.put("prdtnm", prdtnm);
        jsonObject.put("prdtprice", prdtprice);
        jsonObject.put("signdate", signdate);
        jsonObject.put("svcid", svcid);
        jsonObject.put("tradeid", tradeid);
        jsonObject.put("userid", userid);
        jsonObject.put("userkey", userkey);
        jsonObject.put("cashGb", cashGb);
        jsonObject.put("payeremail", payeremail);
        jsonObject.put("autoyn", autoyn);
        jsonObject.put("ezkey", ezkey);
        return jsonObject.toJSONString();
    }
}
