package com.dsqd.amc.linkedmo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dsqd.amc.linkedmo.model.Marketing;

@Mapper
public interface MarketingMapper {

	Marketing getMarketingById(@Param("id") int id);
	
	Marketing getMarketingByEventcd(@Param("eventcd") String eventcd);

	Marketing getMarketingByKeycode(@Param("keycode") String keycode);
	
	List<Marketing> getAllMarketing();

	void insertMarketing(Marketing data);

	void updateMarketing(Marketing data);

	void deleteMarketing(@Param("id") int id);
}
