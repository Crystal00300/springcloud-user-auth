package com.example.service;

import com.example.vo.*;


public interface UserService {

    //獲取簡訊驗證碼
    boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo);

    //簡訊登入
    LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo);

    //登入退出
    boolean loginExit(LoginExitReqVo loginExitReqVo);

}
