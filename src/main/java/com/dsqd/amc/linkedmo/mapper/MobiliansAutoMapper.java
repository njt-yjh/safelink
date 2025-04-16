package com.dsqd.amc.linkedmo.mapper;

import com.dsqd.amc.linkedmo.model.Mobilians;
import com.dsqd.amc.linkedmo.model.MobiliansAuto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MobiliansAutoMapper {

    void insertAuto(MobiliansAuto data);
    MobiliansAuto getAutobillKey(Mobilians data);
}
