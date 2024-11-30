package com.dsqd.amc.linkedmo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsqd.amc.linkedmo.GlobalCache;

public class TestMobileno {
	// 테스트폰 적용여부 확인
	private GlobalCache cache = GlobalCache.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(TestMobileno.class);

	public boolean isTestphone(String mobileno) {
		// 등록된 프로퍼티 확인
		boolean bRet = false;
		String pcnt = cache.getAsString("test.mobileno.count");
		if (pcnt == null && "".equals(pcnt)) return bRet;
		try {
			int cnt = Integer.parseInt(pcnt);
			for (int i=1; i<(cnt+1); i++) {
				String Tmobileno = cache.getAsString("test.mobileno."+i);
				if (Tmobileno.equals(mobileno)) return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("TestPhone Check ERROR [{}]", e.getMessage());
		}
		return bRet;
	}
}
