package com.dsqd.amc.linkedmo.service;

import com.dsqd.amc.linkedmo.mapper.MobiletownMapper;
import com.dsqd.amc.linkedmo.model.Mobiletown;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dsqd.amc.linkedmo.config.MyBatisConfig;

public class MobiletownService {

	private SqlSessionFactory sqlSessionFactory;

    public MobiletownService() {
        this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
    }
/*
	Mobiletown getMobiletown(@Param("txid") int txid);
	List<Mobiletown> getMobiletownAll();
	List<Mobiletown> getMobiletownByMobileid(String rcverphone);
    void insertMobiletown(Mobiletown data);
    void updateMobiletown(Mobiletown data);
    void deleteMobiletown(@Param("txid") int txid);
 */
    public Mobiletown getMobiletown(int txid) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		MobiletownMapper mapper = session.getMapper(MobiletownMapper.class);
            return mapper.getMobiletown(txid);
        }
    }
    
    public List<Mobiletown> getMobiletownByMobileid(String rcverphone) {
    	try (SqlSession session = sqlSessionFactory.openSession()) {
    		MobiletownMapper mapper = session.getMapper(MobiletownMapper.class);
            return mapper.getMobiletownByMobileid(rcverphone);
        }
    }
        
    public void insertMobiletown(Mobiletown data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MobiletownMapper mapper = session.getMapper(MobiletownMapper.class);
            mapper.insertMobiletown(data);
            session.commit();
        }
    }
    
    public void updateMobiletown(Mobiletown data) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MobiletownMapper mapper = session.getMapper(MobiletownMapper.class);
            mapper.updateMobiletown(data);
            session.commit();
        }
    }
    
    public void deleteMobiletown(int txid) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
        	MobiletownMapper mapper = session.getMapper(MobiletownMapper.class);
            mapper.deleteMobiletown(txid);
            session.commit();
        }
    }
}