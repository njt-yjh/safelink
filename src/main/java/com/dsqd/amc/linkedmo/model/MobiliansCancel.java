package com.dsqd.amc.linkedmo.model;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobiliansCancel {
    private String mrchid;
    private String svcid;
    private String tradeid;
    private String prdtprice;
    private String mobilid;
    private String result;
}
