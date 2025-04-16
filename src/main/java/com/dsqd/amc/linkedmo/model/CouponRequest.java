package com.dsqd.amc.linkedmo.model;

import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {
    private int coupon_request_id;
    private String cooper_id;
    private String cooper_pw;
    private String site_id;
    private String no_req;
    private String cooper_order;
    private int issue_count;
    private String call_ctn;
    private String sender;
    private String sender_id;
    private String rcv_ctn;
    private String receiver;
    private String send_msg;
    private String valid_start;
    private String valid_end;
    private String pay_id;
    private String booking_no;
    private String site_url;
    private String title;
    private Date request_date;
}
