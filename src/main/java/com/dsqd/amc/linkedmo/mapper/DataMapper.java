package com.dsqd.amc.linkedmo.mapper;

import com.dsqd.amc.linkedmo.model.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataMapper {

    Data getDataById(@Param("id") int id);

    void insertData(Data data);

    void updateData(Data data);

    void deleteData(@Param("id") int id);
}
