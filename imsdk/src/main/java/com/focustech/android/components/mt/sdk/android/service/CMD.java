package com.focustech.android.components.mt.sdk.android.service;


import com.focustech.android.components.mt.sdk.android.service.processor.KnownProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalAfterLoginSuccessfulProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalCleanAccountProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalDeleteAccountProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalDeleteExtMessageListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalDeleteExtMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetConversationListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetConversationMessageListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetDiscussionsProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetExtMessageListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetFriendGroupsProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalGetGroupsProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalUpdateConversationSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.local.LocalUpdateUserExtProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.DiscussionMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.DiscussionMessageSyncProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.FriendMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.FriendMessageSyncProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.GroupMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.GroupMessageSyncProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.msg.KeyboardInputMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyActiveStatusChangeProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyDeleteFriendProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyFriendInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyGroupDisabledProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyJoinGroupProcessedProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyKickoutProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyMessageHasBeenReadProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyReceiptProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyStudentInfoChangeProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyUpdateUserStatusProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyUserHeadProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyUserNickNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.nty.NtyUserSignatureProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqAddFriendAnswerProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqAddFriendProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqAddOfflineFileProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqCreateDiscussionProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqCreateGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDeleteFriendGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDeleteFriendProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDeleteGroupUserProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDisableGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDiscussionExpiredProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDiscussionListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDiscussionUpdateNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqDiscussionUserInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqExitDiscussionProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqExitGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqFetchMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqFriendGroupsProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqFriendListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetDiscussionUserStatusProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetFriendRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetGroupRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetGroupUserRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetGroupUserStatusProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetLastChatTimeProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetSingleDiscussionInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetSysNtyProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGetUsersInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGroupListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGroupUserInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqGroupUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqInviteDiscussionProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqInviteUserJoinGroupAnswerProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqInviteUserJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqJoinGroupAnswerProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLogoutProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqMessageHasBeenReadProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqReconnectProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqSetGroupAdminProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqSetGroupRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateDiscussionUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateFriendNoDisturbProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateGroupInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateGroupNickNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateGroupNickNameSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateGroupRemarkProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateGroupUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserHeadProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserNickNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserSignatureProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqUpdateUserStatusProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspCreateGroupSuccessfulProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDeleteFriendGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDeleteFriendProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDeleteGroupUserProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionInvalidProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionUserInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspDiscussionUserSettingListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspExitDiscussionProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspExitGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFetchDiscussionMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFetchGroupMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFetchMessageProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFriendGroupsProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFriendInfoEndProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFriendInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspFriendsInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGetFriendRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGetGroupRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGetGroupUserRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGetLastChatTimeProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupDisabledProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupUserInfoListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspGroupUserSettingListProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspLoginProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspReconnectProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspSetGroupAdminProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspSetGroupRuleProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateDiscussionNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateDiscussionUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateFriendNoDisturbProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateGroupNickNameProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateGroupNickNameSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateGroupRemarkProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUpdateGroupUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUserInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUserSettingProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.rsp.RspUsersInfoProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddAndAgreeFriendSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddFriendFailProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddFriendProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddFriendSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddFriendWithoutValidateSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAddedFriendSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAgreeInviteUserJoinGroupSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyAgreeJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyDeleteGroupUserToAdminProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyDeleteGroupUserToTargetProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyDisagreeInviteUserJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyDisagreeJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyDiscussionExpiredProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyExitGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyGroupDisabledProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyInviteUserJoinGroupProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyInviteUserJoinGroupSucceededToAdminProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyInviteUserJoinGroupSucceededToTargetProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyJoinGroupSucceededProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyJoinGroupValidateProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyNewProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyPullLogProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtySetGroupAdminProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.SysNtyTransmissionProcessor;
import com.focustech.android.components.mt.sdk.android.service.processor.sys.TransmissionProcessor;

/**
 * 报文信令，包含指定的处理器
 *
 * @author zhangxu
 */
public enum CMD {
    // -----------------主动请求
    // 登陆请求
    REQ_LOGIN("LoginReq", new ReqLoginProcessor()),
    // 重新链接请求
    REQ_RECONNECT("ReconnectReq", new ReqReconnectProcessor()),
    // 退出登陆
    REQ_LOGOUT("LogoutReq", new ReqLogoutProcessor()),
    // 更新用户状态
    REQ_UPDATE_USER_STATUS("UpdateUserStatusReq", new ReqUpdateUserStatusProcessor()),
    // 更新用户信息
    REQ_UPDATE_USER_INFO("UpdateUserInfoReq", new ReqUpdateUserInfoProcessor()),
    // 更新签名
    REQ_UPDATE_USER_SIGNATURE("UpdateUserSignatureReq", new ReqUpdateUserSignatureProcessor()),
    // 更新签名
    REQ_UPDATE_USER_NICKNAME("UpdateUserNickNameReq", new ReqUpdateUserNickNameProcessor()),
    // 更新头像
    REQ_UPDATE_USER_HEAD("UserHeadReq", new ReqUpdateUserHeadProcessor()),
    // 获取用户头像设置
    REQ_GET_USER_SETTING("UserSettingReq", new ReqGetUserSettingProcessor()),
    // 更新用户设置
    REQ_UPDATE_USER_SETTING("UpdateUserSettingReq", new ReqUpdateUserSettingProcessor()),
    // 拉取好友分组
    REQ_FRIEND_GROUPS("FriendGroupsReq", new ReqFriendGroupsProcessor()),
    // 删除好友分组
    REQ_DELETE_FRIEND_GROUP("DeleteFriendGroupReq", new ReqDeleteFriendGroupProcessor()),
    // 拉取好友信息
    REQ_FRIENDS("FriendsReq", new ReqFriendListProcessor()),
    // 拉取好友信息
    REQ_USERS_INFO("UsersInfoReq", new ReqGetUsersInfoProcessor()),
    // 删除好友
    REQ_DELETE_FRIEND("DeleteFriendReq", new ReqDeleteFriendProcessor()),
    //  设置免打扰
    REQ_UPDATE_FRIEND_NODISTURB("UpdateFriendNoDisturbReq", new ReqUpdateFriendNoDisturbProcessor()),
    // 拉取消息
    REQ_FETCH_MESSAGE("FetchMessageReq", new ReqFetchMessageProcessor()),
    // 拉取所有的最后阅读时间
    REQ_GET_LAST_CHAT_TIME("GetLastChatTimeReq", new ReqGetLastChatTimeProcessor()),
    // 消息已经完成读取
    REQ_MESSAGE_HAS_BEEN_READ("MessageHasBeenReadReq", new ReqMessageHasBeenReadProcessor()),
    // 拉取系统消息
    REQ_GET_SYS_NTY("GetSysNtyReq", new ReqGetSysNtyProcessor()),
    // 增加一个离线文件
    REQ_ADD_OFFLINE_FILE("AddOfflineFileReq", new ReqAddOfflineFileProcessor()),
    // 拉取好友
    REQ_GET_FRIEND_RULE("GetFriendRuleReq", new ReqGetFriendRuleProcessor()),
    // 添加好友
    REQ_ADD_FRIEND("AddFriendReq", new ReqAddFriendProcessor()),
    // 添加好友应答
    REQ_ADD_FRIEND_ANSWER("AddFriendAnswerReq", new ReqAddFriendAnswerProcessor()),
    // 设置群规则
    REQ_SET_GROUP_RULE("SetGroupRuleReq", new ReqSetGroupRuleProcessor()),
    // 获取群规则
    REQ_GET_GROUP_RULE("GetGroupRuleReq", new ReqGetGroupRuleProcessor()),
    // 获取群规则
    REQ_GET_GROUP_USER_RULE("GetGroupUserRuleReq", new ReqGetGroupUserRuleProcessor()),
    // 获取群规则
    REQ_GET_GROUP_USER_STATUS("GetGroupUserStatusReq", new ReqGetGroupUserStatusProcessor()),
    // 拉取群
    REQ_GROUP_LIST("GroupsReq", new ReqGroupListProcessor()),
    // 拉取
    REQ_GROUP_USER_INFO_LIST("GroupUserInfoReq", new ReqGroupUserInfoListProcessor()),
    // 拉取单个群用户信息
    REQ_GROUP_USER_INFO("GroupSingleUserInfoReq", new ReqGroupUserInfoProcessor()),
    // 创建群
    REQ_GROUP_CREATE("AddGroupReq", new ReqCreateGroupProcessor()),
    // 设置在群众的昵称设置
    REQ_GROUP_UPDATE_NICKNAME_SETTING("UpdateGroupNickNameSettingReq", new ReqUpdateGroupNickNameSettingProcessor()),
    REQ_GROUP_UPDATE_REMARK("UpdateGroupRemarkReq", new ReqUpdateGroupRemarkProcessor()),
    REQ_GROUP_UPDATE_NICKNAME("UpdateGroupNickNameReq", new ReqUpdateGroupNickNameProcessor()),
    REQ_GROUP_UPDATE_USER_SETTING("UpdateGroupUserSettingReq", new ReqUpdateGroupUserSettingProcessor()),
    REQ_GROUP_EXIT("ExitGroupReq", new ReqExitGroupProcessor()),
    REQ_GROUP_UPDATE_INFO("UpdateGroupInfoReq", new ReqUpdateGroupInfoProcessor()),
    REQ_GROUP_INVITE_JOIN_TO("InviteUserJoinGroupReq", new ReqInviteUserJoinGroupProcessor()),
    REQ_GROUP_INVITE_USER_JOIN_ANSWER("InviteUserJoinGroupRsp", new ReqInviteUserJoinGroupAnswerProcessor()),
    REQ_GROUP_DELETE_USER("DeleteGroupUserReq", new ReqDeleteGroupUserProcessor()),
    REQ_GROUP_DISABLE("DisableGroupReq", new ReqDisableGroupProcessor()),
    REQ_GROUP_SET_ADMIN("SetGroupAdminReq", new ReqSetGroupAdminProcessor()),
    REQ_GROUP_JOIN("JoinGroupReq", new ReqJoinGroupProcessor()),
    REQ_GROUP_JOIN_ANSWER("JoinGroupRsp", new ReqJoinGroupAnswerProcessor()),

    // 拉取群
    REQ_DISCUSSION_LIST("DiscussionsReq", new ReqDiscussionListProcessor()),
    // 拉取
    REQ_DISCUSSION_USERS_INFO_LIST("DiscussionUserInfosReq", new ReqDiscussionUserInfoListProcessor()),
    REQ_DISCUSSION_CREATE("AddDiscussionReq", new ReqCreateDiscussionProcessor()),
    REQ_DISCUSSION_INVITE("InviteDiscussionReq", new ReqInviteDiscussionProcessor()),
    REQ_DISCUSSION_EXPIRED("DiscussionExpiredReq", new ReqDiscussionExpiredProcessor()),
    REQ_DISCUSSION_EXIT("ExitDiscussionReq", new ReqExitDiscussionProcessor()),
    REQ_DISCUSSION_UPDATE_USER_SETTING("UpdateDiscussionUserSettingReq", new ReqUpdateDiscussionUserSettingProcessor()),
    REQ_DISCUSSION_UPDATE_NAME("UpdateDiscussionNameReq", new ReqDiscussionUpdateNameProcessor()),
    REQ_DISCUSSION_GET_USER_STATUS("GetDiscussionUserStatusReq", new ReqGetDiscussionUserStatusProcessor()),
    REQ_DISCUSSION_GET_INFO("GetSingleDiscussionInfoReq", new ReqGetSingleDiscussionInfoProcessor()),

    // -----------------服务器响应
    // 登陆响应
    RSP_LOGIN("LoginRsp", new RspLoginProcessor()),
    // 重新链接响应
    RSP_RECONNECT("ReconnectRsp", new RspReconnectProcessor()),
    // 用户信息响应
    RSP_USER_INFO("UserInfoRsp", new RspUserInfoProcessor()),
    // 多个用户信息响应
    RSP_USERS_INFO("UsersInfoRsp", new RspUsersInfoProcessor()),
    // 设置响应
    RSP_USER_SETTING("UserSettingRsp", new RspUserSettingProcessor()),
    // 好友分组响应
    RSP_FRIEND_GROUPS("FriendGroupsRsp", new RspFriendGroupsProcessor()),
    // 好友信息响应
    RSP_FRIENDS_INFO("FriendsInfoRsp", new RspFriendsInfoProcessor()),
    // 好友信息响应
    RSP_FRIEND_INFO("FriendInfoRsp", new RspFriendInfoProcessor()),
    // 好友信息完毕相应
    RSP_FRIEND_INFO_END("FriendInfoEndRsp", new RspFriendInfoEndProcessor()),
    // 获取用户验证规则响应
    RSP_GET_FRIEND_RULE("GetFriendRuleRsp", new RspGetFriendRuleProcessor()),
    // 删除好友分组响应
    RSP_DELETE_FRIEND_GROUP("DeleteFriendGroupRsp", new RspDeleteFriendGroupProcessor()),
    // 删除好友响应
    RSP_DELETE_FRIEND("DeleteFriendRsp", new RspDeleteFriendProcessor()),
    // 更新免打扰设置响应
    RSP_UPDATE_FRIEND_NODISTURB("UpdateFriendNoDisturbRsp", new RspUpdateFriendNoDisturbProcessor()),
    // 拉取好友消息相应
    RSP_FETCH_MESSAGE("FetchMessageRsp", new RspFetchMessageProcessor()),
    // 拉取群消息相应
    RSP_FETCH_GROUP_MESSAGE("FetchGroupMessageRsp", new RspFetchGroupMessageProcessor()),
    // 拉讨论组群消息相应
    RSP_FETCH_DISCUSSION_MESSAGE("FetchDiscussionMessageRsp", new RspFetchDiscussionMessageProcessor()),
    // 拉取所有的最后阅读时间响应
    RSP_GET_LAST_CHAT_TIME("GetLastChatTimeRsp", new RspGetLastChatTimeProcessor()),
    // 设置群规则响应
    RSP_GROUP_SET_RULE("SetGroupRuleRsp", new RspSetGroupRuleProcessor()),
    // 获取群规则
    RSP_GROUP_GET_RULE("GetGroupRuleRsp", new RspGetGroupRuleProcessor()),
    // 获取群规则
    RSP_GROUP_GET_USER_RULE("GetGroupUserRuleRsp", new RspGetGroupUserRuleProcessor()),
    // 拉取群
    RSP_GROUPS("GroupsRsp", new RspGroupListProcessor()),
    // 群信息返回
    RSP_GROUP("GroupRsp", new RspGroupProcessor()),
    // 群详细信息
    RSP_GROUP_INFO_LIST("GroupsInfoRsp", new RspGroupInfoListProcessor()),
    // 多个群用户信息
    RSP_GROUP_USER_INFO_LIST("GroupUserInfosRsp", new RspGroupUserInfoListProcessor()),
    // 群用户信息返回响应
    RSP_GROUP_USER_INFO("GroupUserInfoRsp", new RspGroupUserInfoProcessor()),
    // 创建群成功
    RSP_GROUP_CREATE_SUCCESSFUL("CreateGroupSuccessfulRsp", new RspCreateGroupSuccessfulProcessor()),
    RSP_GROUP_UPDATE_NICKNAME_SETTING("UpdateGroupNickNameSettingRsp", new RspUpdateGroupNickNameSettingProcessor()),
    RSP_GROUP_UPDATE_REMARK("UpdateGroupRemarkRsp", new RspUpdateGroupRemarkProcessor()),
    RSP_GROUP_UPDATE_NICKNAME("UpdateGroupNickNameRsp", new RspUpdateGroupNickNameProcessor()),
    RSP_GROUP_UPDATE_USER_SETTING("UpdateGroupUserSettingRsp", new RspUpdateGroupUserSettingProcessor()),
    RSP_GROUP_USER_SETTING_LIST("GroupUserSettingRsp", new RspGroupUserSettingListProcessor()),
    RSP_GROUP_USER_EXIT("ExitGroupRsp", new RspExitGroupProcessor()),
    RSP_GROUP_INFO("GroupInfoRsp", new RspGroupInfoProcessor()),
    RSP_GROUP_DELETE_USER("DeleteGroupUserRsp", new RspDeleteGroupUserProcessor()),
    RSP_GROUP_SET_ADMIN("SetGroupAdminRsp", new RspSetGroupAdminProcessor()),
    RSP_GROUP_DISABLED("GroupDisableRsp", new RspGroupDisabledProcessor()),

    RSP_DISCUSSION_LIST("DiscussionsRsp", new RspDiscussionListProcessor()),
    RSP_DISCUSSION_INFO_LIST("DiscussionsInfoRsp", new RspDiscussionInfoListProcessor()),
    RSP_DISCUSSION_USER_INFO_LIST("DiscussionUserInfosRsp", new RspDiscussionUserInfoListProcessor()),
    RSP_DISCUSSION_INFO("DiscussionInfoRsp", new RspDiscussionInfoProcessor()),
    RSP_DISCUSSION_USER_INFO("DiscussionUserInfoRsp", new RspDiscussionUserInfoProcessor()),
    RSP_DISCUSSION_USER_EXIT("ExitDiscussionRsp", new RspExitDiscussionProcessor()),
    RSP_DISCUSSION_INVALID("DiscussionInvalidRsp", new RspDiscussionInvalidProcessor()),
    RSP_DISCUSSION_UPDATE_USER_SETTING("UpdateDiscussionUserSettingRsp", new RspUpdateDiscussionUserSettingProcessor()),
    RSP_DISCUSSION_USER_SETTING_LIST("DiscussionUserSettingRsp", new RspDiscussionUserSettingListProcessor()),
    RSP_DISCUSSION_UPDATE_NAME("UpdateDiscussionNameRsp", new RspUpdateDiscussionNameProcessor()),

    // -----------------服务器通知
    //学生信息改变
    NTY_STUDENT_CHANGE("StudentInfoChangeNty", new NtyStudentInfoChangeProcessor()),
    // 被踢下线
    NTY_KICK_OUT("KickoutNty", new NtyKickoutProcessor()),
    // 系统消息通知
    NTY_NEW_SYS("NewSysNty", new SysNtyNewProcessor()),
    // 回执
    NTY_RECEIPT("ReceptNty", new NtyReceiptProcessor()),
    // 状态变更通知
    NTY_UPDATE_USER_STATUS("UpdateUserStatusNty", new NtyUpdateUserStatusProcessor()),
    // 用户信息变更通知
    NTY_USER_INFO("UserInfoNty", new NtyUserInfoProcessor()),
    // 用户签名变更
    NTY_USER_SIGNATURE("UserSignatureNty", new NtyUserSignatureProcessor()),
    // 用户昵称变更
    NTY_USER_NICKNAME("UserNickNameNty", new NtyUserNickNameProcessor()),
    // 用户头像变更
    NTY_USER_HEAD("UserHeadNty", new NtyUserHeadProcessor()),
    // 设备激活通知
    NTY_ACTIVE_STATUS_CHANGE("ActiveStatusChangeNty", new NtyActiveStatusChangeProcessor()),
    // 消息已读标示
    NTY_MESSAGE_HAS_BEEN_READ("MessageHasBeenReadNty", new NtyMessageHasBeenReadProcessor()),
    // 好友信息通知
    NTY_FRIEND_INFO("FriendInfoNty", new NtyFriendInfoProcessor()),
    // 删除好友响应
    NTY_DELETE_FRIEND("DeleteFriendNty", new NtyDeleteFriendProcessor()),
    NTY_DISABLE_GROUP("DisableGroupNty", new NtyGroupDisabledProcessor()),
    NTY_JOIN_GROUP_PROCESSED("JoinGroupProcessedNty", new NtyJoinGroupProcessedProcessor()),


    // -------------------系统消息
    SYS_NTY_ADD_FRIEND("AddFriendSysNty", new SysNtyAddFriendProcessor()),
    // 添加好友成功 system -> src
    SYS_NTY_ADD_FRIEND_SUCCEEDED("AddFriendSucceededSysNty", new SysNtyAddFriendSucceededProcessor()),
    // 无需认证，加好友成功
    SYS_NTY_ADD_FRIEND_WITHOUT_VALIDATE_SUCCEEDED("AddFriendWithoutValidateSucceededSysNty", new SysNtyAddFriendWithoutValidateSucceededProcessor()),
    // 被他人添加为好友通知
    SYS_NTY_ADDED_FRIEND_SUCCEEDED("AddedFriendSucceededSysNty", new SysNtyAddedFriendSucceededProcessor()),
    // target操作添加并同意
    SYS_NTY_ADDED_AND_AGREE_FRIEND_SUCCEEDED_TO_TARGET("AddAndAgreeFriendSucceededSysNty", new SysNtyAddAndAgreeFriendSucceededProcessor()),

    // 拉取用户日志
    SYS_NTY_PULL_LOG("PullLogSysNty", new SysNtyPullLogProcessor()),
    // 透传系统通知
    SYS_NTY_TRANSMISSION("TransmissionSysNty", new SysNtyTransmissionProcessor()),
    SYS_NTY_TRANSMISSION_ONE("Transmission", new TransmissionProcessor()),
    // 添加好友失败
    SYS_NTY_ADD_FRIEND_FAIL("AddFriendFailSysNty", new SysNtyAddFriendFailProcessor()),
    SYS_NTY_EXIT_GROUP("ExitGroupSysNty", new SysNtyExitGroupProcessor()),
    SYS_NTY_INVITE_USER_JOIN_GROUP("InviteUserJoinGroupSysNty", new SysNtyInviteUserJoinGroupProcessor()),
    SYS_NTY_INVITE_USER_JOIN_GROUP_SUCCEEDED("InviteUserJoinGroupSucceededSysNty", new SysNtyInviteUserJoinGroupSucceededToAdminProcessor()),
    SYS_NTY_INVITE_USER_JOIN_GROUP_SUCCEEDED_TO_USER("InviteUserJoinGroupSucceededToUserSysNty", new SysNtyInviteUserJoinGroupSucceededToTargetProcessor()),
    SYS_NTY_AGREE_INVITE_USER_JOIN_GROUP_SUCCEEDED("AgreeInviteUserJoinGroupSucceededSysNty", new SysNtyAgreeInviteUserJoinGroupSucceededProcessor()),
    SYS_NTY_DISAGREE_INVITE_USER_JOIN_GROUP_SUCCEEDED("DisagreeInviteUserJoinGroupSysNty", new SysNtyDisagreeInviteUserJoinGroupProcessor()),
    SYS_NTY_DELETE_GROUP_USER_TO_TARGET("DeleteGroupUserSysNty", new SysNtyDeleteGroupUserToTargetProcessor()),
    SYS_NTY_DELETE_GROUP_USER_TO_ADMIN("DeleteGroupUserToAdminSysNty", new SysNtyDeleteGroupUserToAdminProcessor()),
    SYS_NTY_GROUP_DISABLED("DisableGroupSysNty", new SysNtyGroupDisabledProcessor()),
    SYS_NTY_SET_GROUP_ADMIN("SetGroupAdminSysNty", new SysNtySetGroupAdminProcessor()),
    SYS_NTY_JOIN_GROUP_VALIDATE("JoinGroupValidateSysNty", new SysNtyJoinGroupValidateProcessor()),
    SYS_NTY_JOIN_GROUP_SUCCEEDED("JoinGroupSucceededSysNty", new SysNtyJoinGroupSucceededProcessor()),
    SYS_NTY_JOIN_GROUP_AGREE("AgreeJoinGroupSysNty", new SysNtyAgreeJoinGroupProcessor()),
    SYS_NTY_JOIN_GROUP_DISAGREE("DisagreeJoinGroupSysNty", new SysNtyDisagreeJoinGroupProcessor()),
    SYS_NTY_DISCUSSION_EXPIRED("DiscussionExpiredSysNty", new SysNtyDiscussionExpiredProcessor()),

    // --------------------消息
    // 消息
    MESSAGE("Message", new FriendMessageProcessor()),
    // 群消息
    GROUP_MESSAGE("GroupMessage", new GroupMessageProcessor()),
    // 群消息
    DISCUSSION_MESSAGE("DiscussionMessage", new DiscussionMessageProcessor()),

    // 同步消息
    MESSAGE_SYNC("MessageSync", new FriendMessageSyncProcessor()),
    // 同步消息
    GROUP_MESSAGE_SYNC("GroupMessageSync", new GroupMessageSyncProcessor()),
    // 同步消息
    DISCUSSION_MESSAGE_SYNC("DiscussionMessageSync", new DiscussionMessageSyncProcessor()),

    // 键盘输入
    KEYBOARD_INPUT_MESSAGE("KeyboardInputMessage", new KeyboardInputMessageProcessor()),

    // -----------------本地操作
    // 本地登陆成功后的处理
    LOCAL_DELETE_ACCOUNT("", new LocalDeleteAccountProcessor()),
    // 本地登陆成功后的处理
    LOCAL_CLEAN_ACCOUNT("", new LocalCleanAccountProcessor()),
    // 本地登陆成功后的处理
    LOCAL_AFTER_LOGIN_SUCCESSFUL("", new LocalAfterLoginSuccessfulProcessor()),
    // 拉取本地会话列表
    LOCAL_GET_CONVERSATION_LIST("", new LocalGetConversationListProcessor()),
    // 跟新本地扩展信息
    LOCAL_UPDATE_USER_EXT("", new LocalUpdateUserExtProcessor()),
    // 更新会话设置
    LOCAL_UPDATE_CONVERSATION("", new LocalUpdateConversationSettingProcessor()),
    // 拉取本地会话聊天记录
    LOCAL_GET_CONVERSATION_MESSAGE_LIST("", new LocalGetConversationMessageListProcessor()),
    // 拉取本地扩展消息
    LOCAL_GET_EXT_MESSAGE_LIST("", new LocalGetExtMessageListProcessor()),
    // 删除扩展消息
    LOCAL_DELETE_EXT_MESSAGE("", new LocalDeleteExtMessageProcessor()),
    LOCAL_DELETE_EXT_MESSAGE_LIST("", new LocalDeleteExtMessageListProcessor()),
    // 拉取本地好友分组列表
    LOCAL_GET_FRIEND_GROUPS("", new LocalGetFriendGroupsProcessor()),
    // 拉取本地群组
    LOCAL_GET_GROUPS("", new LocalGetGroupsProcessor()),
    // 拉取本地讨论组
    LOCAL_GET_DISCUSSIONS("", new LocalGetDiscussionsProcessor()),


    KNOWN("known", new KnownProcessor()),;

    private String value;
    private CMDProcessor processor;

    CMD(String value, CMDProcessor processor) {
        this.value = value;
        this.processor = processor;
    }

    public String getValue() {
        return value;
    }

    public CMDProcessor getProcessor() {
        return processor;
    }

    public static CMD parse(String cmdValue) {
        CMD value = CMD.KNOWN;

        for (CMD cmd : values()) {
            if (cmd.getValue().equalsIgnoreCase(cmdValue)) {
                value = cmd;
                break;
            }
        }

        return value;
    }
}
