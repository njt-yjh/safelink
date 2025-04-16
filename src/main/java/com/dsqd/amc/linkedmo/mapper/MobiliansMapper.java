package com.dsqd.amc.linkedmo.mapper;

import com.dsqd.amc.linkedmo.model.Mobilians;
import com.dsqd.amc.linkedmo.model.MobiliansCancel;
import com.dsqd.amc.linkedmo.model.Subscribe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MobiliansMapper {

    void insertPhone(Mobilians data);
    List<Mobilians> getMobiliansPhoneUser(Subscribe data);
    void updateMobiliansPhoneUser(Mobilians data);
    List<Mobilians> getAutoBillUserList(Mobilians data);
    void updateAutoBillDate(Mobilians data);
    List<Mobilians> getTradeidList(Mobilians data);
    void insertCancel(MobiliansCancel data);
}
