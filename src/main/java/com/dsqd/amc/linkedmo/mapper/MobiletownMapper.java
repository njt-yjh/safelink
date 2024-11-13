package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Mobiletown;

@Mapper
public interface MobiletownMapper {

	Mobiletown getMobiletown(@Param("txid") int txid);
	
	List<Mobiletown> getMobiletownAll();

	List<Mobiletown> getMobiletownByMobileid(String rcverphone);

    void insertMobiletown(Mobiletown data);

    void updateMobiletown(Mobiletown data);

    void deleteMobiletown(@Param("txid") int txid);
    
}
