package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.BatchlogMapper;
import com.dsqd.amc.linkedmo.model.Batchlog;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class BatchlogService {

	private SqlSessionFactory sqlSessionFactory;

    public BatchlogService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
/*
	Batchlog getBatchlog(@Param("id") int id);
	List<Batchlog> getBatchlogByBatchTxid(@Param("batch_txid") int batch_txid);
	List<Batchlog> getBatchlogAll();
    void insertBatchlog(Batchlog data);
    void updateBatchlog(Batchlog data);
    void deleteBatchlog(@Param("id") int id);
 */
    public Batchlog getBatchlog(int id) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
            return mapper.getBatchlog(id);
        }
    }
    
    public List<Batchlog> getBatchlogByBatchTxid(int batch_txid) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
            return mapper.getBatchlogByBatchTxid(batch_txid);
        }
    }

    public List<Batchlog> getBatchlogAll() {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
    		return mapper.getBatchlogAll();
    	}
    }
    
    public void insertBatchlog(Batchlog data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
            mapper.insertBatchlog(data);
            session.commit();
        }
    }
    
    public void updateBatch(Batchlog data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
            mapper.updateBatchlog(data);
            session.commit();
        }
    }
    
    public void deleteBatch(int txid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchlogMapper mapper = session.getMapper(BatchlogMapper.class);
            mapper.deleteBatchlog(txid);
            session.commit();
        }
    }
}