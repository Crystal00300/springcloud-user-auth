package com.example.service;

import com.example.common.ErrorCode;
import com.example.dao.UserInfoDao;
import com.example.dao.UserSmsCodeDao;
import com.example.entity.UserInfo;
import com.example.entity.UserSmsCode;
import com.example.vo.GetSmsCodeReqVo;
import com.example.vo.LoginByMobileReqVo;
import com.example.vo.LoginByMobileResVo;
import com.example.BizException;
import com.example.vo.LoginExitReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserSmsCodeDao userSmsCodeDao;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 獲取簡訊驗證碼
     *
     * @param getSmsCodeReqVo
     * @return
     */
    @Override
    public boolean getSmsCode(GetSmsCodeReqVo getSmsCodeReqVo) {
        // 1. 隨機生成六位簡訊驗證碼
        String smsCode = String.format("%06d", (int) (Math.random() * 1000000));

        // 2. 封裝驗證碼資訊到 UserSmsCode 物件
        UserSmsCode userSmsCode = UserSmsCode.builder().mobileNo(getSmsCodeReqVo.getMobileNo()) // 用戶手機號碼
                .smsCode(smsCode)                        // 生成的六位驗證碼
                .sendTime(new Timestamp(System.currentTimeMillis())) // 發送時間
                .createTime(new Timestamp(System.currentTimeMillis())) // 創建時間
                .build();

        // 3. 將驗證碼資訊存入資料庫
        userSmsCodeDao.insert(userSmsCode);
        log.info("smsCode: {}, 取得驗證碼", smsCode);

        // TODO: 真實場景中，需呼叫第三方簡訊認證平台，實現真正的簡訊發送功能

        return true; // 表示驗證碼生成和存儲成功
    }

    /**
     * 簡訊驗證碼登入
     *
     * @param loginByMobileReqVo
     * @return
     * @throws BizException
     */
    @Override
    public LoginByMobileResVo loginByMobile(LoginByMobileReqVo loginByMobileReqVo) throws BizException {
        // 1. 檢查驗證碼
        UserSmsCode userSmsCode = userSmsCodeDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userSmsCode == null || !userSmsCode.getSmsCode().equals(loginByMobileReqVo.getSmsCode())) {
            throw new BizException(ErrorCode.INVALID_SMS_CODE); // 驗證碼不存在或錯誤
        }

        // 2. 判斷使用者是否已經註冊
        UserInfo userInfo = userInfoDao.selectByMobileNo(loginByMobileReqVo.getMobileNo());
        if (userInfo == null) {
            // 隨機生成使用者 ID 並完成系統預設註冊流程
            String userId = String.valueOf((int) (Math.random() * 1000000 + 1));
            userInfo = UserInfo.builder().userId(userId).mobileNo(loginByMobileReqVo.getMobileNo()).isLogin(1) // 登入狀態設置為 1
                    .loginTime(new Timestamp(new Date().getTime())).build();
            userInfoDao.insert(userInfo); // 新增用戶
        } else {
            // 如果用戶已存在，更新登入狀態和時間
            userInfo.setIsLogin(1); // 設置登入狀態為 1
            userInfo.setLoginTime(new Timestamp(new Date().getTime()));
            userInfoDao.updateById(userInfo); // 更新用戶記錄
        }
        log.info("使用者資訊: {}", userInfo);

        // 3. 生成訪問令牌 (accessToken)
        String accessToken = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        redisTemplate.opsForValue().set(accessToken, userInfo, 30, TimeUnit.DAYS); // 存入 Redis，設置有效期 30 天
        log.info("存入 Redis 的 accessToken: {}, 對應的用戶資訊: {}", accessToken, userInfo);

        // TODO: 可再加入 Redis 過期的處理邏輯

        // 封裝回應參數
        LoginByMobileResVo loginByMobileResVo = LoginByMobileResVo.builder().userId(userInfo.getUserId()).accessToken(accessToken).build();

        log.info("生成並存儲的 accessToken 是: {}", accessToken);

        // 4. 返回結果
        return loginByMobileResVo;
    }

    /**
     * 登出操作
     *
     * @param loginExitReqVo
     * @return
     */
    @Override
    public boolean loginExit(LoginExitReqVo loginExitReqVo) {
        try {
            // 1: 打印登出請求的 accessToken
            log.info("登出操作開始，傳入的 accessToken 是: {}", loginExitReqVo.getAccessToken());

            // 2: 從 Redis 中查詢 accessToken 對應的使用者資料
            UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(loginExitReqVo.getAccessToken());
            if (userInfo == null) {
                // 如果 token 無效或已過期，記錄警告並返回失敗
                log.warn("登出失敗，無效的訪問令牌：{}", loginExitReqVo.getAccessToken());
                return false;
            }

            // 3: 驗證傳入的 userId 與 token 中的 userId 是否匹配
            if (!userInfo.getUserId().equals(loginExitReqVo.getUserId())) {
                log.warn("登出失敗，訪問令牌與使用者 ID 不匹配。token: {}, userId: {}", loginExitReqVo.getAccessToken(), loginExitReqVo.getUserId());
                return false;
            }

            // 4: 刪除 Redis 中的 accessToken
            redisTemplate.delete(loginExitReqVo.getAccessToken());
            log.info("登出成功，已刪除 accessToken: {}", loginExitReqVo.getAccessToken());
            return true; // 登出成功

        } catch (
                Exception e) {
            // 其他異常並記錄錯誤
            log.error("登出操作失敗，異常信息：{}", e.getMessage(), e);
            return false;
        }
    }
}
