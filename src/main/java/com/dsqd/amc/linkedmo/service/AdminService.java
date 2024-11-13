package com.dsqd.amc.linkedmo.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.mapper.BatchMapper;
import com.dsqd.amc.linkedmo.model.Batch;

public class AdminService {

	private SqlSessionFactory sqlSessionFactory;

    public AdminService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
    
    public List<Batch> getBatchAll() {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		BatchMapper mapper = session.getMapper(BatchMapper.class);
    		return mapper.getBatchAll();
    	}
    }
}
