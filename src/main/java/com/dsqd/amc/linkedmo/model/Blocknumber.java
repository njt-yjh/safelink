package com.dsqd.amc.linkedmo.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blocknumber {

	private int id; // 자동생성 
	private String spcode; // 통신사 구분값, ALL / SKT / KT / LGU
	private String status; // 상태값, A - 차단상태, C - 차단해제상태
	private String mobileno;
	private Date createat; // 생성일시
	private Date canceledat; // 삭제일시
	private String usernameofoper; // 조작자
	private String remark; // 비고작성 
}
