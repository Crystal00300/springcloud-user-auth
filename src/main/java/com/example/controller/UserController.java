package com.example.controller;

import com.example.common.ApiResponse;
import com.example.common.ResultCode;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.example.vo.GetSmsCodeReqVo;
import com.example.vo.LoginByMobileReqVo;
import com.example.vo.LoginByMobileResVo;
import com.example.vo.LoginExitReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * 獲取簡訊驗證碼介面
     *
     * @param reqId
     * @param mobileNo
     * @return
     */
    @RequestMapping(value = "getCode", method = RequestMethod.POST)
    public Boolean getSmsCode(@RequestParam("reqId") String reqId, @RequestParam("mobileNo") String mobileNo) {
        GetSmsCodeReqVo getSmsCodeReqVo = GetSmsCodeReqVo.builder().reqId(reqId).mobileNo(mobileNo).build();
        boolean result = userService.getSmsCode(getSmsCodeReqVo);
        return result;
    }

    /**
     * 簡訊驗證碼登入介面
     *
     * @param reqId
     * @param mobileNo
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "loginByMobile", method = RequestMethod.POST)
    public ApiResponse loginByMobile(@RequestParam("reqId") String reqId, @RequestParam("mobileNo") String mobileNo, @RequestParam("smsCode") String smsCode) {

        LoginByMobileReqVo loginByMobileReqVo = LoginByMobileReqVo.builder().reqId(reqId).mobileNo(mobileNo).smsCode(smsCode).build();
        LoginByMobileResVo loginByMobileResVo = userServiceImpl.loginByMobile(loginByMobileReqVo);
        return ApiResponse.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc(), loginByMobileResVo);
    }

    /**
     * 登出介面
     *
     * @param userId
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "loginExit", method = RequestMethod.POST)
    public Boolean loginExit(@RequestParam("userId") String userId, @RequestParam("accessToken") String accessToken) {
        LoginExitReqVo loginExitReqVo = LoginExitReqVo.builder().userId(userId).accessToken(accessToken).build();
        boolean result = userServiceImpl.loginExit(loginExitReqVo);
        return result;
    }
}
