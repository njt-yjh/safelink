package com.dsqd.amc.linkedmo.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dsqd.amc.linkedmo.config.MyBatisConfig;
import com.dsqd.amc.linkedmo.mapper.BlocknumberMapper;
import com.dsqd.amc.linkedmo.model.Blocknumber;


public class BlocknumberService {
	
//	List<Blocknumber> getAllBlocknumber();
//	Blocknumber getBlocknumberById(@Param("id") int id);
//	Blocknumber getBlocknumberByMobileno(@Param("mobileno") String mobileno); // 현재 status가 A인 데이터만 조회
//	Blocknumber getBlocknumberByMobileno2(@Param("mobileno") String mobileno); // status 상관없이 조회 
//	Blocknumber getBlocknumberByMobilenoSKT(@Param("mobileno") String mobileno); // SKT 전용, 현재 status가 A인 데이터만 조회
//	Blocknumber getBlocknumberByMobilenoKT(@Param("mobileno") String mobileno); // KT 전용, 현재 status가 A인 데이터만 조회
//	Blocknumber getBlocknumberByMobilenoLGU(@Param("mobileno") String mobileno); // LGU 전용, 현재 status가 A인 데이터만 조회
//    void insertBlocknumber(Blocknumber blocknumber);
//    void updateBlocknumber(Blocknumber blocknumber);
//    void deleteBlocknumber(Blocknumber blocknumber);
//    void deleteBlocknumberByMobileno(Blocknumber blocknumber);

	private SqlSessionFactory sqlSessionFactory;

	public BlocknumberService() {
		this.sqlSessionFactory = MyBatisConfig.getSqlSessionFactory();
	}

	public List<Blocknumber> getAllBlocknumber() { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getAllBlocknumber(); 
		}
	}
	
	public Blocknumber getBlocknumberById(int id) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberById(id); 
		}
	}

	public Blocknumber getBlocknumberByMobileno(String mobileno) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberByMobileno(mobileno); 
		}
	}
	public Blocknumber getBlocknumberByMobileno2(String mobileno) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberByMobileno2(mobileno); 
		}
	}
	public Blocknumber getBlocknumberByMobilenoSKT(String mobileno) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberByMobilenoSKT(mobileno); 
		}
	}
	public Blocknumber getBlocknumberByMobilenoKT(String mobileno) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberByMobilenoKT(mobileno); 
		}
	}
	public Blocknumber getBlocknumberByMobilenoLGU(String mobileno) { 
		try (SqlSession session = sqlSessionFactory.openSession()) { 
			BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
			return mapper.getBlocknumberByMobilenoLGU(mobileno); 
		}
	}
    
    public void insertBlocknumber(Blocknumber data) { 
    	try (SqlSession session = sqlSessionFactory.openSession()) { 
    		BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
    		mapper.insertBlocknumber(data); 
    		session.commit(); 
    	} 
    } 
    
    public void updateBlocknumber(Blocknumber data) { 
    	try (SqlSession session = sqlSessionFactory.openSession()) { 
    		BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
    		mapper.updateBlocknumber(data); 
    		session.commit(); 
    	} 
    } 
    
    public void deleteBlocknumber(Blocknumber data) { 
    	try (SqlSession session = sqlSessionFactory.openSession()) { 
    		BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
    		mapper.deleteBlocknumber(data); 
    		session.commit(); 
    	} 
    } 
    
    public void deleteBlocknumberByMobileno(Blocknumber data) { 
    	try (SqlSession session = sqlSessionFactory.openSession()) { 
    		BlocknumberMapper mapper = session.getMapper(BlocknumberMapper.class); 
    		mapper.deleteBlocknumberByMobileno(data); 
    		session.commit(); 
    	} 
    } 
    
}
