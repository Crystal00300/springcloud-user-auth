package com.example.dao;

import com.example.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {

    UserInfo selectByMobileNo(String mobileNo);

    int insert(UserInfo userInfo);

    int updateById(UserInfo userInfo);

}
