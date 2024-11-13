package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.DataMapper;
import com.dsqd.amc.linkedmo.mapper.MarketingMapper;
import com.dsqd.amc.linkedmo.model.Data;
import com.dsqd.amc.linkedmo.model.Marketing;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class MarketingService {
    private SqlSessionFactory sqlSessionFactory;

    /*
    Marketing getMarketingById(@Param("id") int id);
	Marketing getMarketingByEventcd(@Param("eventcd") String eventcd);
	Marketing getMarketingByKeycode(@Param("keycode") String keycode);
	List<Marketing> getAllMarketing();
	void insertMarketing(Marketing data);
	void updateMarketing(Marketing data);
	void deleteMarketing(@Param("id") int id);
     */

    public MarketingService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
    
    public Marketing getMarketingById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            return mapper.getMarketingById(id);
        }
    }
    
    public Marketing getMarketingByEventcd(String eventcd) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            return mapper.getMarketingByEventcd(eventcd);
        }
    }
    
    public Marketing getMarketingByKeycode(String keycode) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            return mapper.getMarketingByKeycode(keycode);
        }
    }
    
    public List<Marketing> getAllMarketing() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            return mapper.getAllMarketing();
        }
    }
    
    public void insertMarketing(Marketing data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            mapper.insertMarketing(data);
            session.commit();
        }
    }

    public void updateMarketing(Marketing data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            mapper.updateMarketing(data);
            session.commit();
        }
    }

    public void deleteMarketing(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MarketingMapper mapper = session.getMapper(MarketingMapper.class);
            mapper.deleteMarketing(id);
            session.commit();
        }
    }
}
