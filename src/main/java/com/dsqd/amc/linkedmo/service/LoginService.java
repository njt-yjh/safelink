package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.LoginMapper;
import com.dsqd.amc.linkedmo.model.Manager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class LoginService {
    private SqlSessionFactory sqlSessionFactory;

    public LoginService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public Manager login(Manager data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LoginMapper mapper = session.getMapper(LoginMapper.class);
            return mapper.login(data);
        }
    }
    
    public Manager write(Manager data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LoginMapper mapper = session.getMapper(LoginMapper.class);
            return mapper.write(data);
        }
    }
    
    public Manager fail(Manager data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            LoginMapper mapper = session.getMapper(LoginMapper.class);
            return mapper.login(data);
        }
    }

    public void insertManager(Manager data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	LoginMapper mapper = session.getMapper(LoginMapper.class);
            mapper.insertManager(data);
            session.commit();
        }
    }

    public Manager changePassword(Manager data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	LoginMapper mapper = session.getMapper(LoginMapper.class);
            Manager result = mapper.changePassword(data);
            session.commit();
            return result;
        }
    }

    public void deleteManager(String loginid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	LoginMapper mapper = session.getMapper(LoginMapper.class);
            mapper.deleteManager(loginid);
            session.commit();
        }
    }
}
