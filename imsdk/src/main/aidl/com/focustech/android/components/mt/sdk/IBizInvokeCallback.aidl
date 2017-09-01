// BizInvokeCallback.aidl
package com.focustech.android.components.mt.sdk;

import com.focustech.android.components.mt.sdk.communicate.Communication;
// Declare any non-default types here with import statements

interface IBizInvokeCallback {
    /**
     * 配置完成，业务在这个回调中做事情
     */
    void privateConfigurationComplete();

    /**
     * 网络掉线
     */
    void privateDisconnected();

    /**
     * 重新连接上服务器
     */
    void privateConnected();

    /**
     * 网络状态切换
     *
     * @param data com.focustech.android.components.mt.sdk.MTRuntime.Network
     */
    void privateNetworkChanged(String data);

    /**
     * 操作超时
     *
     * @param operation 操作
     * @param 超时附加数据，一般是userId
     */
    void privateOperationTimeout(String operation, String data);

    /**
     * 删除本地账号成功
     */
    void privateDeleteAccountSuccessful();

    /**
     * 清除本地账号成功
     */
    void privateCleanAccountsSuccessful();

    /**
     * 登录成功
     *
     * @param userId   登陆用户的userId，后续所有操作基于这个userId
     * @param token    登陆token
     * @param platformData 平台相关信息
     */
    void privateLoginSuccessful(String userId, String token, String platformData);

    /**
     * 登录失败
     *
     * @param code 错误码
     */
    void privateLoginFailed(int code);

    /**
     * 自动登录失败
     */
    void privateAutoLoginFailed();

    /**
     * 登录失败
     *
     * @param userId   退出登陆的userId
     */
    void privateLogoutSuccessful(String userId);

    /**
     * 被迫下线
     *
     * @param userId      userId
     * @param equipment   设备
     */
    void privateKickout(String userId, String equipment);

    /**
     * 自己的状态变更了
     *
     * @param status 新状态
     * @param
     */
    void privateMyStatusChanged(String status);

    /**
     * 自己的其他设备状态变更了
     *
     * @param equipment 设备
     * @param status    新状态
     */
    void privateMyEquipmentStatusChanged(String equipment, String status);

    /**
     * 用户状态变更了
     *
     * @param userId    用户
     * @param equipment 设备
     * @param status    状态
     */
    void privateStatusChanged(String userId, String equipment, String status);

    /**
     * 自己的信息变更了
     *
     * @param data 数据 com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase
     */
    void privateMyInfoChanged(String data);

    /**
     * 好友基本信息变更了
     *
     * @param data 数据 com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase
     */
    void privateFriendUserInfoChanged(String data);

    /**
     * 自己的签名变更了
     *
     * @param newSignature 签名内容
     */
    void privateMySignatureChanged(String newSignature);

    /**
     * 好友的签名变更了
     *
     * @param userId       用户
     * @param newSignature 签名内容
     */
    void privateFriendSignatureChanged(String userId, String newSignature);

    /**
     * 自己的昵称变更了
     *
     * @param newNickName 新昵称
     */
    void privateMyNickNameChanged(String newNickName);

    /**
     * 好友的昵称变更了
     *
     * @param userId       用户
     * @param newNickName  新昵称
     */
    void privateFriendNickNameChanged(String userId, String newNickName);

    /**
     * 自己的头像变更了
     *
     * @param userHeadType 头像类型
     * @param userHeadId   头像ID
     */
    void privateMyHeadChanged(int userHeadType, String userHeadId);

    /**
     * 好友的昵称变更了
     *
     * @param userId       用户
     * @param userHeadType 头像类型
     * @param userHeadId   头像ID
     */
    void privateFriendHeadChanged(String userId, int userHeadType, String userHeadId);

    /**
     * 用户池发生变化
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase JsonArray
     */
    void privateUsersPoolChanged(String data);

    /**
     * 设置变更了
     *
     * @param data 设置 com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData
     */
    void privateSettingChanged(String data);

    /**
     * 设置好友免打扰成功
     *
     * @param userId 好友ID
     * @param enable 开关
     */
    void privateUpdateFriendNoDisturbChanged(String userId, String enable);

    /**
     * 联系人列表更新了
     *
     * @param data 数据 com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups
     */
    void privateFriendGroupsChanged(String data);

    /**
     * 删除好友分组
     *
     * @param friendGroupId 好友分组ID
     */
    void privateDeleteFriendGroupSuccessful(String friendGroupId);

    /**
     * 本地会话列表
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationData JSON Array
     */
    void privateLocalConversationList(String data);

    /**
     * 新本地会话
     *
     * @param data        com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationData JSON
     */
    void privateLocalConversation(String data);

    /**
     * 本地会话消息列表
     *
     * @param contactType 联系类型
     * @param contactId   联系ID
     * @param data        com.focustech.android.components.mt.sdk.android.service.pojo.MessageData JSON Array
     */
    void privateLocalConversationMessageList(String contactType, String contactId, String data);

    /**
     * 本地自定义消息扩展
     *
     * @param userId 用户
     * @param type   消息类型
     * @param data   com.focustech.android.components.mt.sdk.android.service.pojo.MessageData JSON Array
     */
    void privateLocalExtMessageList(String userId, String type, String data);

    /**
     * 本地会话消息列表
     *
     * @param contactType 联系类型
     * @param contactId   联系ID
     */
    void privateLocalConversationMessageListOnFetching(String contactType, String contactId);

    /**
     * 历史没有收到的消息同步完成
     *
     * @param contactType 联系类型
     * @param contactId 联系ID
     */
    void privateHistoryMessageSyncComplete(String contactType, String contactId);

    /**
     * 消息发送成功
     *
     * @param localMessageId 本地消息唯一ID
     * @param taskId 上传任务号，后续根据该任务号接收上传进度。
     */
    void privateMessageBindingUploadTask(String localMessageId, long taskId);

    /**
     * 消息发送失败
     *
     * @param contactId 联系ID
     * @param contactType
     * @param localMessageId 本地消息唯一消息ID
     */
    void privateMessageSendFail(String contactId, int contactType, String localMessageId);

    /**
     * 消息发送成功
     *
     * @param contactId 联系ID
     * @param contactType
     * @param localMessageId 本地消息唯一ID
     */
    void privateMessageSendSuccessful(String contactId, int contactType, String localMessageId);

    /**
     * 个人消息
     *
     * @param toUserId 好友ID
     * @param data 消息内容 com.focustech.android.components.mt.sdk.android.service.pojo.MessageData
     */
    void privateMessage(String toUserId, String data);

    /**
     * 消息扩展，客户端自己处理
     *
     * @param toUserId 好友ID
     * @param data 消息内容 com.focustech.android.components.mt.sdk.android.service.pojo.MessageData
     */
    void privateMessageExt(String toUserId, String data);

    /**
     * 同步消息
     *
     * @param toUserId 好友ID
     * @param equipment 设备
     * @param data 消息内容 com.focustech.android.components.mt.sdk.android.service.pojo.MessageData
     */
    void privateMessageSync(String toUserId, String equipment, String data);

    /**
     * 正在输入
     *
     * @param userId 好友ID
     */
    void privateKeyboardInputMessage(String fromUserId);

    /**
     * 任务处理中
     *
     * @param taskId   任务ID
     * @param total    任务总计总标记
     * @param complete 任务已经完成标记
     */
    void privateTaskProcessing(long taskId, long total, long complete);

    /**
     * 任务完成
     *
     * @param taskId   任务ID
     */
    void privateTaskComplete(long taskId);

    /**
     * 任务失败
     *
     * @param taskId   任务ID
     */
    void privateTaskFailure(long taskId);

    /**
     * 添加好友通知成功, system -> src
     *
     * @param addFriendSucceededToSrcNotify com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendSucceededToSrcNotify
     */
    void privateAddFriendSucceededToSrcNotify(String addFriendSucceededToSrcNotify);

    /**
     * 同意并添加好友通知成功, system -> target
     *
     * @param addFriendSucceededToSrcNotify com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddAndAgreeFriendSucceededToTargetNotify
     */
    void privateAddFriendSucceededToTargetNotify(String addAndAgreeFriendSucceededToTargetNotify);

    /**
     * 添加好友失败的通知, system -> src
     *
     * @param addFriendFailNotify com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendFailNotify
     */
    void privateAddFriendFailNotify(String addFriendFailNotify);

    /**
     * 添加好友失败，未知的用户, system -> src
     *
     * @param targetUserId 用户ID
     */
    void privateAddFriendFailUnknownNotify(String targetUserId);

    /**
     * 好友规则, system -> src
     *
     * @param targetUserId 好友ID
     * @param rule 规则
     */
    void privateFriendRuleNotify(String targetUserId, String rule);

    /***
     * 删除好友成功
     *
     * @param friendUserId 好友ID
     */
    void privateFriendDeleteSuccessful(String friendUserId);

    /**
     * 设置群规则成功
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData
     */
    void privateSetGroupRuleSuccessful(String data);

    /**
     * 获取群规则成功
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData
     */
    void privateGetGroupRuleSuccessful(String data);

    /**
     * 获取群用户规则成功
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData
     */
    void privateGetGroupUserRuleSuccessful(String data);

    /**
     * 一组用户的状态发生了变更
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.status.UserStatusData JSON ARRAY
     */
    void privateUsersStatusChanged(String data);

    /**
     * 群组信息变更
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups
     */
    void privateGroupsChanged(String data);

    /**
     * 群组信息变更
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup
     */
    void privateGroupChanged(String data);

    /**
     * 群用户信息变更
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser
     */
    void privateGroupUserChanged(String data);

    /**
     * 更新群昵称设置成功
     *
     * @param groupId
     * @param userId
     */
    void privateUpdateGroupNickNameSettingSuccessful(String groupId, String userId, String setting);

    /**
     * 更新群备注成功
     *
     * @param groupId
     */
    void privateUpdateGroupRemarkSuccessful(String groupId);

    /**
     * 群用户昵称改变
     *
     * @param groupId
     * @param userId
     */
    void privateGroupNickNameChanged(String groupId, String userId, String newGroupNickName);

    /**
     * 群用户设置变更
     *
     * @param data
     */
    void privateGroupUserSettingChanged(String data);

    /**
     * 用户退出群通知->非管理员、群主和操作人。
     *
     * @param data
     */
    void privateUserExitGroup(String groupId, String userId);

    /**
     * 群用户被删除
     *
     * @param groupId 群ID
     * @param userIds 用户ID JSON ARRAY
     */
    void privateGroupUserDeleted(String groupId, String userIds);

    /**
     * 群被停用了
     *
     * @param groupId 群ID
     */
    void privateGroupDisabled(String groupId);

    /**
     * 群用户类型变化了
     *
     * @param groupId 群ID
     */
    void privateGroupUserTypeChanged(String groupId, String userIds, int userType);

    /**
     * 用户同意邀请入群
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeInviteUserJoinGroupSucceededNotify
     */
    void privateGroupUserInviteAgreedToAdmin(String data);

    /**
     * 用户不同意邀请入群
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeInviteUserJoinGroupSucceededNotify
     */
    void privateGroupUserInviteDisagreedToAdmin(String data);

    /**
     * 群用户被删除
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeInviteUserJoinGroupSucceededNotify
     */
    void privateGroupUserDeletedToAdmin(String data);

    /**
     * 群用户被删除
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.DeletedFromGroupToTargetNotify
     */
    void privateGroupUserDeletedToTarget(String data);

    /**
     * 用户退出群
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.ExitGroupNotify
     */
    void privateGroupUserExit(String data);

    /**
     * 用户邀请加入成功
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToAdminNotify
     */
    void privateGroupUserInviteSucceededToAdmin(String data);

    /**
     * 用户邀请加入成功
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToTargetNotify
     */
    void privateGroupUserInviteSucceededToTarget(String data);

    /**
     * 申请加入群成功
     *
     * @param groupId
     * @param groupName
     */
    void privateJoinGroupSuccessful(String groupId, String groupName);

    /**
     * 申请加入群已经被其他人处理
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.com.focustech.android.components.mt.sdk.android.service.pojo.group.JoinGroupProcessedNotify
     */
    void privateJoinGroupProcessed(String data);

    /**
     * 申请加入群已经被其他人处理
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeJoinGroupNotify
     */
    void privateJoinGroupAgreed(String data);

    /**
     * 申请加入群已经被其他人处理
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeJoinGroupNotify
     */
    void privateJoinGroupDisagreed(String data);

    /**
     * 讨论组信息变更
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups
     */
    void privateDiscussionsChanged(String data);

    /**
     * 讨论组信息变更
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup
     */
    void privateDiscussionChanged(String data);

    /**
     * 讨论组用户信息变更
     *
     * @param data com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser
     */
    void privateDiscussionUserChanged(String data);

    /**
     * 讨论组过期
     *
     * @param discussionId
     */
    void privateDiscussionExpired(String discussionId);

    void privateDiscussionUserExit(String discussionId, String userId);

    void privateDiscussionExitSuccessful(String discussionId);

    /**
     * 群用户设置变更
     *
     * @param data
     */
    void privateDiscussionUserSettingChanged(String data);

    void privateDiscussionNameChanged(String discussionId, String name);

    /**
     * 拉取用户日志
     *
     * @param logId 日志
     */
    void privatePullLog(String logId);

    /**
    *  学生信息改变
    */
    void privateStudentInfoChange();

    /**
     * 透传系统消息
     *
     * @param content 与服务器协商好的系统通知内容
     */
    void privateSystemNotify(String content);



    /**
     * add 2016.10.24 新的通讯方式
     *
     *
     * @param Communication 通讯类
     */
    String communicate(inout Communication communication);

}
