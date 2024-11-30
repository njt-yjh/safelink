package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Batch;

@Mapper
public interface BatchMapper {

	Batch getBatch(@Param("txid") int txid);
	
	List<Batch> getBatchByBatchid(String batchid);
	
	List<Batch> getBatchToday();
	
	List<Batch> getBatchTodayByBatchid(String batchid);
	
	List<Batch> getBatchAll();

    void insertBatch(Batch data);

    void updateBatch(Batch data);

    void deleteBatch(@Param("txid") int txid);
    
    int pingQuery();
    
}
