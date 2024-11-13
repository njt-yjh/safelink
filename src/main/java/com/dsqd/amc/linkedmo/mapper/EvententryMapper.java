package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Evententry;

@Mapper
public interface EvententryMapper {

	Evententry getEvententryById(@Param("txid") int txid);
	
	List<Evententry> getAllEvententry();

	void insertEvententry(Evententry data);

	void updateEvententry(Evententry data);

	void deleteEvententry(@Param("txid") int txid);
}
