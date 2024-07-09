package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.DataMapper;
import com.dsqd.amc.linkedmo.model.Data;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class DataService {
    private SqlSessionFactory sqlSessionFactory;

    public DataService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }

    public void insertData(Data data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DataMapper mapper = session.getMapper(DataMapper.class);
            mapper.insertData(data);
            session.commit();
        }
    }

    public void updateData(Data data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DataMapper mapper = session.getMapper(DataMapper.class);
            mapper.updateData(data);
            session.commit();
        }
    }

    public Data getDataById(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DataMapper mapper = session.getMapper(DataMapper.class);
            return mapper.getDataById(id);
        }
    }

    public void deleteData(int id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            DataMapper mapper = session.getMapper(DataMapper.class);
            mapper.deleteData(id);
            session.commit();
        }
    }
}
