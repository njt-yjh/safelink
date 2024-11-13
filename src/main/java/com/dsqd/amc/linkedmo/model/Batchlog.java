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

public class Batchlog {
	private int id;
	private int batch_txid;
	private String type;
	private String logdata;
	private String actcode;
	private String actorname;
	private String comment;
	private Date created_at;
	private Date updated_at;
}

