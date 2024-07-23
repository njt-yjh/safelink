package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Batch;

@Mapper
public interface BatchMapper {

	Batch getBatchByTxid(@Param("txid") int id);
	
	List<Batch> getBatchByCode(String code);
	
	List<Batch> getBatchAll();

    void insertSBatch(Batch data);

    void updateBatch(Batch data);

    void deleteBatch(@Param("txid") int id);
    
}
