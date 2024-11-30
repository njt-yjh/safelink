package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.BatchMapper;
import com.dsqd.amc.linkedmo.model.Batch;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class BatchService {

	private SqlSessionFactory sqlSessionFactory;

    public BatchService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
/*
	Batch getBatch(@Param("txid") int id);
	List<Batch> getBatchByBatchid(String batchid);
	List<Batch> getBatchToday();
	List<Batch> getBatchTodayByBatchid(String batchid);
	List<Batch> getBatchAll();
    void insertBatch(Batch data);
    void updateBatch(Batch data);
    void deleteBatch(@Param("txid") int id);
 */
    public Batch getBatch(int id) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
            return mapper.getBatch(id);
        }
    }
    
    public List<Batch> getBatch(String batchid) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
            return mapper.getBatchByBatchid(batchid);
        }
    }
    
    public List<Batch> getBatchToday() {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
            return mapper.getBatchToday();
        }
    }
        
    public List<Batch> getBatchToday(String batchid) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
            return mapper.getBatchTodayByBatchid(batchid);
        }
    }

    public List<Batch> getBatchAll() {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
    		return mapper.getBatchAll();
    	}
    }
    
    public void insertBatch(Batch data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchMapper mapper = session.getMapper(BatchMapper.class);
            mapper.insertBatch(data);
            session.commit();
        }
    }
    
    public void updateBatch(Batch data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchMapper mapper = session.getMapper(BatchMapper.class);
            mapper.updateBatch(data);
            session.commit();
        }
    }
    
    public void deleteBatch(int txid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchMapper mapper = session.getMapper(BatchMapper.class);
            mapper.deleteBatch(txid);
            session.commit();
        }
    }
    
    public int pingQuery() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	BatchMapper mapper = session.getMapper(BatchMapper.class);
        	return mapper.pingQuery();
        }
    }
}