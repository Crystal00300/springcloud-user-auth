package com.example.dao;

import com.example.entity.UserSmsCode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSmsCodeDao {

    int insert(UserSmsCode userSmsCode);

    UserSmsCode selectByMobileNo(String mobileNo);
}
