package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.SubscribeMapper;
import com.dsqd.amc.linkedmo.model.Data;
import com.dsqd.amc.linkedmo.model.Subscribe;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class SubscribeService {

	private SqlSessionFactory sqlSessionFactory;

    public SubscribeService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public void insertSubscribe(Subscribe data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            mapper.insertSubscribe(data);
            session.commit();
        }
    }

    public void updateSubscribe(Subscribe data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            mapper.updateSubscribe(data);
            session.commit();
        }
    }

    public Subscribe getSubscribeById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            return mapper.getSubscribeById(id);
        }
    }
    
    public List <Subscribe> getSubscribeByMobileno(Subscribe data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            return mapper.getSubscribeByMobileno(data);
        }
    }
    
    public List <Subscribe> getSubscribAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            return mapper.getSubscribeAll();
        }
    }

    public void deleteSubscribe(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	SubscribeMapper mapper = session.getMapper(SubscribeMapper.class);
            mapper.deleteSubscribe(id);
            session.commit();
        }
    }
}
