package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.DataMapper;
import com.dsqd.amc.linkedmo.mapper.EvententryMapper;
import com.dsqd.amc.linkedmo.mapper.MarketingMapper;
import com.dsqd.amc.linkedmo.model.Data;
import com.dsqd.amc.linkedmo.model.Evententry;
import com.dsqd.amc.linkedmo.model.Marketing;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class EvententryService {
    private SqlSessionFactory sqlSessionFactory;

    /*
	Evententry getEvententryById(@Param("txid") int txid);
	List<Evententry> getAllEvententry();
	void insertEvententry(Evententry data);
	void updateEvententry(Evententry data);
	void deleteEvententry(@Param("txid") int txid);
     */

    public EvententryService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
    
    public Evententry getEvententryById(int txid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            return mapper.getEvententryById(txid);
        }
    }
  
    public List<Evententry> getAllEvententry() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            return mapper.getAllEvententry();
        }
    }
    
    public List<Evententry> getAllEvententry2() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            return mapper.getAllEvententry2();
        }
    }
    
    public void insertEvententry(Evententry data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            mapper.insertEvententry(data);
            session.commit();
        }
    }

    public void updateMarketing(Evententry data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            mapper.updateEvententry(data);
            session.commit();
        }
    }

    public void deleteMarketing(int txid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            mapper.deleteEvententry(txid);
            session.commit();
        }
    }
    
    public int getSubcribeIDByMobileno(String mobileno) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
        	EvententryMapper mapper = session.getMapper(EvententryMapper.class);
            return mapper.getSubcribeIDByMobileno(mobileno);
        }
    }
}
