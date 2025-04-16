package com.dsqd.amc.linkedmo.model;

import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private int coupon_response_id;
    private String action;
    private String engine_id;
    private String rt;
    private String rtmsg;
    private String cooper_order;
    private int issue_count;
    private String no_cpn;
    private String no_auth;
    private String cpn_pw;
    private String ts_id;
    private Date response_date;
}
