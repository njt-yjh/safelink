package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.mapper.MobiliansAutoMapper;
import com.dsqd.amc.linkedmo.mapper.MobiliansMapper;
import com.dsqd.amc.linkedmo.model.Mobilians;
import com.dsqd.amc.linkedmo.model.MobiliansAuto;
import com.dsqd.amc.linkedmo.model.MobiliansCancel;
import com.dsqd.amc.linkedmo.model.Subscribe;
import com.dsqd.amc.linkedmo.naru.SubscribeNaru;
import com.dsqd.amc.linkedmo.util.TestMobileno;
import com.mobilians.mc01_v0005.AckParam;
import com.mobilians.mc01_v0005.McashManager;
import com.mobilians.mcCancel_v0001.MC_Cancel;
import net.minidev.json.JSONObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class MobiliansService {
    private static final Logger logger = LoggerFactory.getLogger(MobiliansService.class);
    private SqlSessionFactory sqlSessionFactory;

    public MobiliansService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public void insertPhone(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            mapper.insertPhone(data);
            session.commit();
        }
    }

    public List<Mobilians> getMobiliansPhoneUser(Subscribe data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            return mapper.getMobiliansPhoneUser(data);
        }
    }

    public void updateMobiliansPhoneUser(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            mapper.updateMobiliansPhoneUser(data);
            session.commit();
        }
    }

    public List<Mobilians> getAutoBillUserList(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            return mapper.getAutoBillUserList(data);
        }
    }

    public void insertAuto(MobiliansAuto data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansAutoMapper mapper = session.getMapper(MobiliansAutoMapper.class);
            mapper.insertAuto(data);
            session.commit();
        }
    }

    public void updateAutoBillDate(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            mapper.updateAutoBillDate(data);
            session.commit();
        }
    }

    public List<Mobilians> getTradeidList(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            return mapper.getTradeidList(data);
        }
    }

    public MobiliansAuto getAutobillKey(Mobilians data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansAutoMapper mapper = session.getMapper(MobiliansAutoMapper.class);
            return mapper.getAutobillKey(data);
        }
    }

    public void insertCancel(MobiliansCancel data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MobiliansMapper mapper = session.getMapper(MobiliansMapper.class);
            mapper.insertCancel(data);
            session.commit();
        }
    }

    /*
    * KG모빌리언스 결제 완료 값에서 Mobilians 객체값 Set
    * 리턴 받은 객체값 때문에 아래와 같이 따로 넣어줌.....첫글자가 대문자만 아니였으면....
    * */
    public Mobilians setMobilians(JSONObject jsonObject) {
        Mobilians mobilians = new Mobilians();

        mobilians.setResultcd(jsonObject.getAsString("Resultcd"));
        mobilians.setResultmsg(jsonObject.getAsString("Resultmsg"));
        mobilians.setAutobillkey(jsonObject.getAsString("AutoBillKey"));
        mobilians.setCommid(jsonObject.getAsString("Commid"));
        mobilians.setMobilid(jsonObject.getAsString("Mobilid"));
        mobilians.setMrchid(jsonObject.getAsString("Mrchid"));
        mobilians.setMstr(jsonObject.getAsString("MSTR"));
        mobilians.setNo(jsonObject.getAsString("No"));
        mobilians.setPrdtnm(jsonObject.getAsString("Prdtnm"));
        mobilians.setPrdtprice(jsonObject.getAsString("Prdtprice"));
        mobilians.setSigndate(jsonObject.getAsString("Signdate"));
        mobilians.setSvcid(jsonObject.getAsString("Svcid"));
        mobilians.setTradeid(jsonObject.getAsString("Tradeid"));
        mobilians.setUserid(jsonObject.getAsString("Userid"));
        mobilians.setUserkey(jsonObject.getAsString("USERKEY"));
        mobilians.setCashGb(jsonObject.getAsString("CASH_GB"));
        mobilians.setPayeremail(jsonObject.getAsString("Payeremail"));
        mobilians.setAutoyn(jsonObject.getAsString("Autoyn"));
        mobilians.setEzkey(jsonObject.getAsString("Ezkey"));

        return mobilians;
    }
    /*
    * KG 모빌리언스 결제 완료 값에서 MSTR 객체 값 Split
    * 해당 값들은 서비스 가입 DB 저장시 필요한 값
    * */
    public void splitMstr(String mstr, Subscribe data) {
        for(String item : mstr.split("\\|")) {
            String [] keyValue = item.split("=");

            switch (keyValue[0]) {
                case "offercode" :
                    data.setOffercode(keyValue[1]);
                    break;
                case "agree1" :
                    data.setAgree1(Boolean.parseBoolean(keyValue[1]));
                    break;
                case "agree2" :
                    data.setAgree2(Boolean.parseBoolean(keyValue[1]));
                    break;
                case "agree3" :
                    data.setAgree3(Boolean.parseBoolean(keyValue[1]));
                    break;
                case "spcode" :
                    data.setSpcode(keyValue[1]);
            }
        }
    }

    public Date setAutobillDate(String signdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(signdate, formatter);
        LocalDate toSignDate = localDateTime.toLocalDate();
        return Date.from(toSignDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /*
    * KG모빌리언스 휴대폰 자동 결제 배치
    * */
    public void autoPayBatch() {
        Properties properties = new Properties();
        try {
            properties.load(Resources.getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mobilians mobilians = new Mobilians();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date today = formatter.parse(formatter.format(new Date()));
            mobilians.setAutobillDate(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //1. 자동 결제일 도래 사용자 조회
        List<Mobilians> autobillUserList = getAutoBillUserList(mobilians);

        for(Mobilians user : autobillUserList) {
            //2, 결제 정보 Set(반복문)
            String Mode			= "43";	//[   2byte 고정] 휴대폰 자동결제 요청을 위한 고정값. 43 수정불가!!!
            // 테스트 userKey = uk0080842250306
            String Phoneid		= user.getUserkey();	//[  15byte 이하] 휴대폰정보(이통사, 휴대폰번호, 주민번호) 대체용 USERKEY
            String Svcid		= user.getSvcid();	//[  12byte 고정] 모빌리언스에서 부여한 서비스ID (12byte 숫자 형식)
            String Mrchid		= user.getMrchid();	//[   8byte 고정] 모빌리언스에서 부여한 상점ID. 서비스ID 앞 8자리. (8byte 숫자 형식)
            String Tradeid		= user.getNo()+new Date().getTime();	//[4byte 이상, 40byte 이하] 가맹점거래번호. 결제 요청 시 마다 unique한 값을 세팅해야 함.
            String Prdtnm		= "휴대폰약속번호 서비스";	//[  50byte 이하] 상품명
            String Prdtprice	= "1650";	//[  10byte 이하] 결제요청금액
            String Email		= user.getPayeremail();	//[  30byte 이하] 결제자 e-mail
            String Emailflag	= "";	//[   1byte 고정] 결제 통보 이메일 보내기 여부
            String Recordkey	= properties.getProperty("mobilians.sever.url");	//[  20byte 이하] 가맹점도메인 (예: www.mcash.co.kr)
            String ReqOpt		= "";	//[  30byte 이하] USERKEY 요청구분
            String AutoBillFlag	= "2";	//[   1byte 고정] 자동결제구분. 자동결제 시 "2" 세팅. (0: 해당없음, 1: 최초등록, 2: 2회차이상 거래)
            // 테스트 autobillKey = bk2478942250306
            String AutoBillKey	= user.getAutobillkey();	//[  15byte 이하] 최초 일반결제 시 발급받은 자동결제용 key
            // 테스트 signdate = 20250306102857
            String AutoBillDate	= user.getSigndate();	//[   8byte 고정] 자동결제 최초일자. (자동결제 1회차 일반결제일)
            String Commid		= "";	//[   3byte 고정] 이통사. USERKEY 사용시에는 세팅할 필요없음
            String No			= "";	//[  11byte 이하] 휴대폰번호. USERKEY 사용시에는 세팅할 필요없음
            String Socialno		= "";	//[  13byte 이하] 주민번호. USERKEY 사용시에는 세팅할 필요없음
            String Userid		= "";	//[  20byte 이하] 가맹점 결제자ID
            String Prdtcd		= "";	//[  40byte 이하] 상품코드. 자동결제인 경우 상품코드별 SMS문구를 별도 세팅할 때 사용하며 사전에 모빌리언스에 등록이 필요함.
            String Item			= "";	//[   8byte 이하] 아이템코드. 미사용 시 반드시 공백으로 세팅.
            String Cpcd			= "";	//[  20byte 이하] 리셀러하위상점key. 리셀러 업체인 경우에만 세팅.
            String Sellernm		= "";	//[  50byte 이하] 실판매자 이름 (오픈마켓의 경우 실 판매자 정보 필수)
            String Sellertel	= "";	//[  15byte 이하] 실판매자 전화번호 (오픈마켓의 경우 실 판매자 정보 필수)
            String Userip		= "";				//[  15byte 이하] 사용자IP

            //3. KG 모빌리언스 자동 결제 통신
            McashManager mm = new McashManager();

            mm.setServerInfo(
                properties.getProperty("mobilians.server"),	//서버 IP
                properties.getProperty("mobilians.switchserver"),	//스위치 IP
                Integer.parseInt(properties.getProperty("mobilians.payport")),	//서버 PORT
                properties.getProperty("mobilians.log"),		//로그 디렉토리 경로
                "0",		//가맹점 KEY 구분(0: 상점ID, 1: 사용자정의KEY1, 2: 사용자정의KEY2)
                "",		//사용자정의KEY 사용 시 가맹점 KEY 값
                "UTF-8",	//가맹점측 서버 인코딩셋. "" or null 인경우 EUC-KR
                "info"	//로그레벨. "" or null 가능
            );

            try {
                /*********************************************************************************
                 * 결제 통신
                 *********************************************************************************/
                AckParam ap = mm.McashApprv(
                    Mode,			//01. 결제모드
                    Recordkey,		//02. 사이트URL
                    Mrchid,			//03. 상점ID
                    Svcid,			//04. 서비스ID
                    No,				//05. 핸드폰 번호
                    Socialno,		//06. 주민번호
                    "",				//07. 가맹점 사용자 주민번호 (미사용)
                    Userid,			//08. 사용자ID
                    ReqOpt,			//09. 사용자KEY 요청구분
                    Email,			//10. 사용자 email
                    Phoneid,		//11. USERKEY (기존 Phoneid 파라미터)
                    Tradeid,		//12. 가맹점 거래번호
                    Prdtcd,			//13. 가맹점 상품코드
                    Prdtnm,			//14. 가맹점 상품명
                    Prdtprice,		//15. 가맹점 상품 가격
                    "",				//16. SMS 인증번호
                    Commid,			//17. 이통사
                    Emailflag,		//18. 결제 통보 이메일 보내기 여부
                    Item,			//19. 사이트URL
                    "",				//20. SMS, ARS 구분
                    Userip,			//21. 사용자 IP
                    Cpcd,			//22. 가맹점 코드
                    "",				//23. 모빌리언스 거래번호
                    "",				//24. 안심결제 비밀번호
                    Sellernm,		//25. 판매자명
                    Sellertel,		//26. 판매자 연락처
                    "",				//27. SKT 캐리어핀 사용여부
                    "",				//28. SKT 캐리어핀 비밀번호
                    AutoBillFlag,	//29. 자동결제구분 (0: 해당없음, 1: 최초등록, 2: 2회차이상 거래)
                    AutoBillDate,	//30. 자동결제 최초일자 (2 회차이상 거래시 최초자동결제 거래일자)
                    AutoBillKey		//31. 자동결제 KEY (발급받은 자동결제 key)
                );

                /*********************************************************************************
                 * 결과 리턴
                 *********************************************************************************/
                String rtn_Resultcd		= ap.getResultcd();		//[   4byte 고정] 결과코드
                String rtn_Resultmsg	= ap.getResultmsg();	//[ 100byte 이하] 결과메시지
                String rtn_Actdate		= ap.getActdate();		//[  14byte 고정] 거래일자
                String rtn_AutoBillKey	= ap.getAutoBillKey();	//[  15byte 이하] 자동결제 최초등록 Key
                String rtn_UserKey		= ap.getPhoneid();		//[  15byte 이하] USERKEY (Reqopt 설정시 리턴)
                String rtn_Commid		= ap.getCommid();		//[   3byte 고정] 이통사
                String rtn_Emailflag	= ap.getEmailflag();	//[   1byte 고정] 이메일 통보여부
                String rtn_Mobilid		= ap.getMobilid();		//[  15byte 이하] 모빌리언스 거래번호
                String rtn_Mode			= ap.getMode();			//[   2byte 고정] 결제모드
                String rtn_Prdtprice	= ap.getPrdtprice();	//[  10byte 이하] 상품 거래금액
                String rtn_Recordkey	= ap.getRecordkey();	//[  20byte 이하] 사이트 URL
                String rtn_Remainamt	= ap.getRemainamt();	//[  10byte 이하] 잔여한도
                String rtn_Svcid		= ap.getSvcid();		//[  12byte 고정] 서비스ID
                String rtn_Tradeid		= ap.getTradeid();		//[  40byte 이하] 가맹점거래번호
                String rtn_Payeremail	= Email;		//[  30byte 고정] 결제자 e-mail은 리턴되지 않으므로 요청 파라미터를 보여준다.
                String rtn_No			= No;			//[  11byte 이하] 휴대폰번호는 리턴되지 않으므로 요청 파라미터를 보여준다.
                String rtn_Prdtnm		= Prdtnm;		//[  13byte 이하] 상품명은 리턴되지 않으므로 요청 파라미터를 보여준다.

                //자동 결제 결과 저장 로직
                MobiliansAuto mobiliansAuto = new MobiliansAuto();
                mobiliansAuto.setRtn_Resultcd(rtn_Resultcd);
                mobiliansAuto.setRtn_Resultmsg(rtn_Resultmsg);
                mobiliansAuto.setRtn_Actdate(rtn_Actdate);
                mobiliansAuto.setRtn_Autobillkey(AutoBillKey);
                mobiliansAuto.setRtn_UserKey(rtn_UserKey);
                mobiliansAuto.setRtn_Commid(rtn_Commid);
                mobiliansAuto.setRtn_Emailflag(rtn_Emailflag);
                mobiliansAuto.setRtn_Mobilid(rtn_Mobilid);
                mobiliansAuto.setRtn_Mode(rtn_Mode);
                mobiliansAuto.setRtn_Prdtprice(rtn_Prdtprice);
                mobiliansAuto.setRtn_Recordkey(rtn_Recordkey);
                mobiliansAuto.setRtn_Remainamt(rtn_Remainamt);
                mobiliansAuto.setRtn_Svcid(rtn_Svcid);
                mobiliansAuto.setRtn_Tradeid(rtn_Tradeid);
                mobiliansAuto.setRtn_Payeremail(rtn_Payeremail);
                mobiliansAuto.setRtn_No(rtn_No);
                mobiliansAuto.setRtn_Prdtnm(rtn_Prdtnm);

                insertAuto(mobiliansAuto);

                if("0000".equals(rtn_Resultcd) && rtn_Mode.equals("53") && rtn_Mobilid != null) {
                    // 자동 결제 성공
                    user.setAutobillDate(setAutobillDate(rtn_Actdate));

                    updateAutoBillDate(user);
                } else {
                    // 자동 결제 실패
                    // 별도 테이블에 저장???? 매일 배치 후 확인 요청???
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(Mobilians mobilians) {
        Properties properties = new Properties();
        try {
            properties.load(Resources.getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Mrchid="";     	//모빌리언스가 부여한 상점아이디 8자리
        String Svcid="";       	//모빌리언스가 부여한 서비스아이디 12자리
        String Tradeid = ""; 	//결제된 상점거래번호
        String Prdtprice = ""; 	//결제된 금액
        String Mobilid = "";    //결제된 모빌리언스 거래번호

        // 1. mobiliansphone_auto 테이블에서 자동 결제 내역 조회(autobillKey로 조회?)
        MobiliansAuto mobiliansAuto = getAutobillKey(mobilians);

        if(mobiliansAuto != null) {
            // 2. 자동 결제 내역 있으면 가장 최근 결제 내역 환불 요청
            Mrchid = mobilians.getMrchid();
            Svcid = mobilians.getSvcid();
            Tradeid = mobiliansAuto.getRtn_Tradeid();

            long refundAmount = getRefundAmount(mobiliansAuto.getRtn_Actdate());

            // 만약 사용한 일수가 많아 환불액이 음수가 되는 경우 0원 처리
            if(refundAmount < 0) {
                return;
            }

            Prdtprice = String.valueOf(refundAmount);
            Mobilid = mobiliansAuto.getRtn_Mobilid();
        } else {
            // 2. 자동 결제 내역 없으면 mobiliansphone 결제 내역 환불 요청
            Mrchid = mobilians.getMrchid();
            Svcid = mobilians.getSvcid();
            Tradeid = mobilians.getTradeid();

            long refundAmount = getRefundAmount(mobilians.getSigndate());

            // 만약 사용한 일수가 많아 환불액이 음수가 되는 경우 0원 처리
            if(refundAmount < 0) {
                return;
            }

            Prdtprice = String.valueOf(refundAmount);
            Mobilid = mobilians.getMobilid();
        }


        /*Mrchid    = "24110815";
        Svcid     = "241108151152";
        Tradeid   = "010452731431741239610105";
        Prdtprice = "1300";
        Mobilid   = "5002498961";*/

        MC_Cancel cancel = new MC_Cancel();
        cancel.setConfigFileDir("application.properties");

        cancel.setServerInfo(
            properties.getProperty("mobilians.server"),   // 메인서버아이피
            Integer.parseInt(properties.getProperty("mobilians.cancelport")),    // 서버포트
            properties.getProperty("mobilians.switchserver"),   // 백업서버아이피
            30000,   // 전문수신타임아웃
            properties.getProperty("mobilians.log")      // 로그경로
        );

        /***************************************************************
         결제취소요청
         ****************************************************************/
        try {
            String res_cd = cancel.cancel_attempt(Mrchid, Svcid, Tradeid, Prdtprice, Mobilid);

            MobiliansCancel mobiliansCancel = new MobiliansCancel();
            mobiliansCancel.setMrchid(Mrchid);
            mobiliansCancel.setSvcid(Svcid);
            mobiliansCancel.setTradeid(Tradeid);
            mobiliansCancel.setPrdtprice(Prdtprice);
            mobiliansCancel.setMobilid(Mobilid);
            mobiliansCancel.setResult(res_cd);

            insertCancel(mobiliansCancel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getRefundAmount(String actDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime payDate = LocalDateTime.parse(actDate, formatter);
        LocalDate nowDate = LocalDate.now();
        // 결제일부터 현재까지 사용한 일 수 계산
        long daysUsed = ChronoUnit.DAYS.between(payDate.toLocalDate(), nowDate);

        // 하루당 금액 55원으로 사용한 금액 계산
        long usedAmount = daysUsed * 55;

        // 총 서비스 금액 1650원에서 사용한 금액을 뺀 환불액 계산
        return 1650 - usedAmount;
    }
}
