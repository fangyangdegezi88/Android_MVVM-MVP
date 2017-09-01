package com.focustech.android.components.mt.sdk.android.db;

import com.focustech.android.components.mt.sdk.android.db.gen.Account;

/**
 * 账号接口
 *
 * @author zhangxu
 */
public interface IAccountService {
    long ACTION_EMPTY = 0;
    long ACTION_LOGOUT = 1;
    long ACTION_KICKOUT = 2;

    /**
     * 账号是否存在
     *
     * @param userId
     * @return
     */
    boolean exist(String userId);

    void delete(String userId);

    void clean();

    /**
     * 创建一个新账号
     *
     * @param account
     * @return
     */
    long add(Account account);

    /**
     * 更新签名
     *
     * @param newSignature
     * @return
     */
    boolean updateSignature(String newSignature);

    /**
     * 更新签名
     *
     * @param newNickName
     * @return
     */
    boolean updateNickName(String newNickName);

    /**
     * 更新用户头像图片
     *
     * @param userHeadType 头像类型
     * @param userHeadId   头像ID
     * @return
     */
    boolean updateHead(long userHeadType, String userHeadId);

    /**
     * 不存在创建
     *
     * @param account
     * @return
     */
    void addOrUpdate(Account account);

    /**
     * 更新最后的行为
     *
     * @param userId
     * @param action
     */
    void updateLastAction(String userId, long action);

    /**
     * 获取账号
     *
     * @param userId
     * @return
     */
    Account getAccount(String userId);

    /**
     * 获取账号
     *
     * @param userName
     * @return
     */
    Account getAccountByName(String userName);
}
