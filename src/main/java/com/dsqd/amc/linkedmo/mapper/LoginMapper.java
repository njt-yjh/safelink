package com.dsqd.amc.linkedmo.mapper;

import com.dsqd.amc.linkedmo.model.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

    Manager login(Manager data);

    void insertManager(Manager data);

    Manager changePassword(Manager data);

    void deleteManager(@Param("loginid") String loginid);
}
