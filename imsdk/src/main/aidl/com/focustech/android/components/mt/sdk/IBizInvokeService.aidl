// IMTBizService.aidl
package com.focustech.android.components.mt.sdk;

import com.focustech.android.components.mt.sdk.IBizInvokeCallback;

// Declare any non-default types here with import statements
interface IBizInvokeService {
    /**
     * 配置启动
     *
     * @param confFile 配置文件
     * @param callback 回调
     */
    void syncStart(String confFile, IBizInvokeCallback callback);

    /**
     * 获取账号
     */
    String syncGetAccountList();

    /**
    * 同步后台状态信息（登录状态）
    *
    * 如果后台已经登录，则将登陆结果返回给前台
    */
    void syncUserStatus();

    /**
    *   清除用户状态
    **/
    void syncClearUserStatus();

    /**
     * 聊天多媒体消息下载URL
     *
     * @param fileId 文件唯一ID
     */
    String syncGetChatImageDownloadURL(String fileId);

    /**
     * 聊天语音消息下载URL
     *
     * @param fileId 文件唯一ID
     */
    String syncGetAudioImageDownloadURL(String fileId);

    /**
     * 头像图片下载URL
     *
     * @param fileId 文件唯一ID
     */
    String syncGetHeadImageDownloadURL(String fileId);

    /**
     * 获取图片占位符
     */
    String syncGetImagePlaceholder();

    /**
     * 删除本地账号
     *
     */
    void asyncDeleteAccount(String userId);

    /**
     * 删除本地账号
     *
     */
    void asyncCleanAccounts();

    /**
     * 修改账号
     *
     * @param
     */
    void syncUpdateAccount(String account);

    /**
     * 获取NTP时间
     */
    long syncGetNtpTime();

    /**
     * 指定userId是否在线
     *
     * @param userId
     */
    boolean syncIsOnline(String userId);

    /**
     * 登陆
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.LoginData
     */
    void asyncLogin(String data);

    /**
     * 退出登陆
     */
    void asyncLogout();
    /**
     * 收取系统消息
     */
    void asyncGetSysNty();
    /**
     * 异步更新自己的状态
     *
     * @param status
     */
    void asyncUpdateUserStatus(String status);

    /**
     * 更新用户信息
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase
     */
    void asyncUpdateUserInfo(String data);

    /**
     * 更新用户设置
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData
     */
    void asyncUpdateUserSetting(String data);

    /**
     * 修改昵称
     *
     * @param newNickName
     */
    void asyncUpdateMyNickName(String newNickName);

    /**
     * 修改个性签名
     *
     * @param newSignature
     */
    void asyncUpdateMySignature(String newSignature);

    /**
     * 获取用户详情
     *
     * @param userIds 用户IDList，转换为JSON ARRAY
     */
    void asyncGetUserInfo(String userIds);

    /**
     * 更新本地用户扩展信息
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase
     */
    void asyncUpdateLocalUserExt(String data);

    /**
     * 获取本地会话列表
     */
    void asyncGetLocalConversationList();

    /**
     * 获取本地扩展消息
     *
     * @param data
     */
    void asyncGetLocalExtMessageList(String data);

    /**
     * 获取本地扩展消息
     *
     * @param svrMsgId 消息唯一ID
     */
    void asyncDeleteLocalExtMessage(String svrMsgId);

    /**
     * 获取本地扩展消息列表
     *
     * @param type 消息类型
     */
    void asyncDeleteLocalExtMessageList(int type);

    /**
     * 获取本地好友分组列表
     */
    void asyncGetLocalFriendGroups();

    /**
     * 删除好友分组
     *
     * @param friendGroupId 好友分组ID
     */
    void asyncDeleteFriendGroup(String friendGroupId);

    /**
     * 删除好友
     *
     * @param friendUserId 好友用户ID
     */
    void asyncDeleteFriend(String friendUserId);

    /**
     * 获取本地群组
     */
    void asyncGetLocalGroups();

    /**
     * 获取本地讨论组
     */
    void asyncGetLocalDiscussions();

    /**
      * 激活聊天窗口
      *
      * @param contactType           联系类型
      * @param contactId             联系ID
      */
    void asyncActiveConversation(String contactType, String contactId);

     /**
      * 去激活聊天窗口
      *
      * @param contactType           联系类型
      * @param contactId             联系ID
      */
    void asyncInActiveConversation(String contactType, String contactId);

    /**
     * 异步拉取本地会话的聊天记录
     *
     * @param contactType           联系类型
     * @param contactId             联系ID
     * @param earliestTimestamp  当前会话中的最后一个消息最早的时间戳
     * @param count                 拉取数目
     */
    void asyncGetLocalConversationMessageList(String contactType, String contactId, long earliestTimestamp, int count);

    /**
     * 异步更新会话设置
     *
     * @param setting               设置 com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationSetting
     */
    void asyncUpdateConversationSetting(String setting);

    /**
     * 设置好友免打扰
     *
     * @param userId 好友ID
     * @param enable 开关
     */
    void asyncUpdateFriendNoDisturb(String userId, String enable);

    /**
     * 异步拉取消息
     */
    void asyncFetchMessages();

    /**
     * 异步拉取消息
     * @param fetchData
     */
    void asyncFetchGroupMessages(String fetchData);

    /**
     * 发送消息
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.MessageData
     */
    void asyncSendMessage(String data);

    /**
     * 发送离线文件
     *
     * @param fileName 文件路径
     * @param userId   目标用户
     * @return taskId   此次任务的ID
     */
    long asyncSendOfflineFileTo(String fileName, String userId);

    /**
     * 获取目标用户的验证规则
     *
     * @param targetUserId 目标好友的ID
     */
    void asyncGetFriendRule(String targetUserId);

    /**
     * 增加一个好友, src -> system
     *
     * @param addFriendAction com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAction
     */
    void asyncAddFriend(String addFriendAction);

    /**
     * 加好友应答,target -> system
     *
     * @param answer com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAnswer
     */
    void asyncAddFriendAnswer(String answer);

    /**
     * 拉取群组列表
     */
    void asyncGetGroupList();

    /**
     * 拉取群组用户
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserQueryData
     */
    void asyncGetGroupUser(String data);

    /**
     * 设置群规则
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData
     */
    void asyncSetGroupRule(String data);

    /**
     * 设置群规则
     *
     * @param groupId
     */
    void asyncGetGroupRule(String groupId);

    /**
     * 设置群用户规则
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData
     */
    void asyncGetGroupUserRule(String data);

    /**
     * 更新群昵称设置
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser
     */
    void asyncUpdateGroupNickNameSetting(String data);

    /**
     * 更新群备注
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup
     */
    void asyncUpdateGroupRemark(String data);

    /**
     * 更新群昵称
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser
     */
    void asyncUpdateGroupNickName(String data);

    /**
     * 更新群用户设置
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupSettingData
     */
    void asyncUpdateGroupUserSetting(String data);

    /**
     * 用户主动退出群
     *
     * @param groupId
     */
    void asyncExitGroup(String groupId);

    /**
     * 停用群
     *
     * @param groupId
     */
    void asyncDiableGroup(String groupId);

    /**
     * 更新群信息
     *
     * @param groupId
     */
    void asyncUpdateGroupInfo(String data);

    /**
     * 创建群
     * @param data
     */
    void asyncCreateGroup(String data);

    /**
     * 邀请用户加入群
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteJoinToGroupData
     */
    void asyncInviteJoinToGroup(String data);

    /**
     * 应答邀请加入群
     *
     * @param data 应答 com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupAnswer
     */
    void asyncAnswerInviteJoinToGroup(String data);


    /**
     * 创建讨论组
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.AddDiscussionData
     */
    void asyncCreateDiscussion(String data);

    /**
     * 退出讨论组
     *
     * @param discussionId 讨论组ID
     */
    void asyncExitDiscussion(String discussionId);
}
