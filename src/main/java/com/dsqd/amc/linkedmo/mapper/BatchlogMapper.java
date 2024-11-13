package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Batch;
import com.dsqd.amc.linkedmo.model.Batchlog;

@Mapper
public interface BatchlogMapper {

	Batchlog getBatchlog(@Param("id") int id);
	
	List<Batchlog> getBatchlogByBatchTxid(@Param("batch_txid") int batch_txid);
	
	List<Batchlog> getBatchlogAll();

    void insertBatchlog(Batchlog data);

    void updateBatchlog(Batchlog data);

    void deleteBatchlog(@Param("id") int id);
    
}
