package com.focustech.android.components.mt.sdk.android.service;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.FileType;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.IBizInvokeService;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.db.DBHelper;
import com.focustech.android.components.mt.sdk.android.db.gen.Account;
import com.focustech.android.components.mt.sdk.android.service.pojo.FetchData;
import com.focustech.android.components.mt.sdk.android.service.pojo.FileData;
import com.focustech.android.components.mt.sdk.android.service.pojo.GetConversationMessageListData;
import com.focustech.android.components.mt.sdk.android.service.pojo.GetExtMessageListData;
import com.focustech.android.components.mt.sdk.android.service.pojo.LoginData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.NoDisturbData;
import com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationSetting;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationStatusData;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAction;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendAnswer;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.AddDiscussionData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupSettingData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserQueryData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteJoinToGroupData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupAnswer;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 默认业务调用Service实现
 *
 * @author zhangxu
 */
public class DefaultInvokeServiceImpl extends IBizInvokeService.Stub {
    private static Logger logger;
    private static final Void VOID = null;
    private MTCoreService service;
    private IBizInvokeCallback mCallback;

    public DefaultInvokeServiceImpl(MTCoreService service) {
        this.service = service;
    }

    @Override
    public void syncStart(String confFile, IBizInvokeCallback callback) throws RemoteException {
        mCallback = callback;
        service.initConf(confFile, callback, true);
        logger = LoggerFactory.getLogger(DefaultInvokeServiceImpl.class);
    }

    @Override
    public String syncGetAccountList() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}"), Operation.ACCOUNT_GET_ALL);
        }

        List<Account> accounts = DBHelper.getInstance().getAccountDao().loadAll();
        return JSONObject.toJSONString(accounts);
    }

    @Override
    public void syncUserStatus() throws RemoteException {
        if (SessionManager.getInstance().getIsOverDue()) {
            mCallback.privateLoginFailed(9005);
            return;
        }
        SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(ContextHolder.getAndroidContext(), SharedPrefLoginInfo.LOGIN_INFO_FILE);
        if (SessionManager.getInstance().isKickOut()) {     //判断是否处于被踢状态  == 当前不是登录

            String mtModel = sharedPrefLoginInfo.getString(MTModel.class.getSimpleName(), "");

            if (null != mtModel && mtModel.length() >= 0) {
                MTModel mtModel1 = JSONObject.parseObject(mtModel, MTModel.class);
                if (mtModel1.getCurrent() != null)
                    mCallback.privateKickout(mtModel1.getCurrent().getUserId(), Messages.Equipment.MOBILE_ANDROID.toString());
            }
            return;
        }

        if (SessionManager.getInstance().getIsUserOrPsdError()) {
            mCallback.privateLoginFailed(10001);
            return;
        }
        if (SessionManager.getInstance().getCurrent() != null && SessionManager.getInstance().getUserId() != null) {
            mCallback.privateLoginSuccessful(SessionManager.getInstance().getUserId(),
                    SessionManager.getInstance().getCurrent().getToken(), SessionManager.getInstance().getCurrent().getPlatformData());
            mCallback.privateMyInfoChanged(sharedPrefLoginInfo.getString(UserBase.class.getSimpleName(), ""));
        }
    }

    @Override
    public void syncClearUserStatus() throws RemoteException {
        SessionManager.getInstance().setKickOut(false);
        SessionManager.getInstance().setIsOverDue(false);
        SessionManager.getInstance().setIsUserOrPsdError(false);
    }

    @Override
    public String syncGetChatImageDownloadURL(String fileId) throws RemoteException {
        return MTRuntime.getFileDownloadUrl(FileType.FILE_TYPE_PICTURE, fileId);
    }

    @Override
    public String syncGetAudioImageDownloadURL(String fileId) throws RemoteException {
        return MTRuntime.getFileDownloadUrl(FileType.FILE_TYPE_VOICE, fileId);
    }

    @Override
    public String syncGetHeadImageDownloadURL(String fileId) throws RemoteException {
        return MTRuntime.getFileDownloadUrl(FileType.FILE_TYPE_HEAD, fileId);
    }

    @Override
    public String syncGetImagePlaceholder() throws RemoteException {
        return MTRuntime.getPicTag();
    }

    @Override
    public void asyncDeleteAccount(String userId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.ACCOUNT_DELETE, userId);
        }

        CMD.LOCAL_DELETE_ACCOUNT.getProcessor().request(userId);
    }

    @Override
    public void asyncCleanAccounts() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}"), Operation.ACCOUNT_DELETE);
        }

        CMD.LOCAL_CLEAN_ACCOUNT.getProcessor().request(VOID);
    }

    @Override
    public void syncUpdateAccount(String account) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.ACCOUNT_UPDATE, account);
        }

        DBHelper.getAccountService().addOrUpdate(JSONObject.parseObject(account, Account.class));
    }

    @Override
    public long syncGetNtpTime() throws RemoteException {
        return NTPTime.now();
    }

    @Override
    public boolean syncIsOnline(String userId) throws RemoteException {
        return null != userId && userId.equals(SessionManager.getInstance().getUserId());
    }

    @Override
    public void asyncLogin(String data) throws RemoteException {
        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.LOGIN, data);
        }

        CMD.REQ_LOGIN.getProcessor().request(JSONObject.parseObject(data, LoginData.class));
    }

    @Override
    public void asyncLogout() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.LOGOUT);
        }

        CMD.REQ_LOGOUT.getProcessor().request(VOID);
    }

    @Override
    public void asyncGetSysNty() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.GET_SYS_NTY);
        }
        CMD.REQ_GET_SYS_NTY.getProcessor().request(VOID);
    }

    @Override
    public void asyncUpdateUserStatus(String status) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}"), Operation.UPDATE_USER_STATUS, status);
        }

        CMD.REQ_UPDATE_USER_STATUS.getProcessor().request(Messages.Status.valueOf(status));
    }

    @Override
    public void asyncUpdateUserInfo(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_USER_INFO, data);
        }

        CMD.REQ_UPDATE_USER_INFO.getProcessor().request(JSONObject.parseObject(data, UserBase.class));
    }

    @Override
    public void asyncUpdateUserSetting(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_USER_SETTING, data);
        }

        CMD.REQ_UPDATE_USER_SETTING.getProcessor().request(JSONObject.parseObject(data, UserSettingData.class));
    }

    @Override
    public void asyncUpdateMyNickName(String newNickName) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_USER_NICKNAME, newNickName);
        }

        CMD.REQ_UPDATE_USER_NICKNAME.getProcessor().request(newNickName);
    }

    @Override
    public void asyncUpdateMySignature(String newSignature) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_USER_SIGNATURE, newSignature);
        }

        CMD.REQ_UPDATE_USER_SIGNATURE.getProcessor().request(newSignature);
    }

    @Override
    public void asyncGetUserInfo(String userIds) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GET_USERS_INFO, userIds);
        }

        CMD.REQ_USERS_INFO.getProcessor().request(JSONObject.parseArray(userIds, String.class));
    }


    @Override
    public void asyncUpdateFriendNoDisturb(String userId, String enable) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}, {}")
                    , Operation.UPDATE_NO_DISTURB, userId, enable);
        }

        NoDisturbData data = new NoDisturbData();
        data.setUserId(userId);
        data.setNoDisturb(Messages.Enable.valueOf(enable));

        CMD.REQ_UPDATE_FRIEND_NODISTURB.getProcessor().request(data);
    }

    @Override
    public void asyncUpdateLocalUserExt(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_UPDATE_USER_EXT, data);
        }

        CMD.LOCAL_UPDATE_USER_EXT.getProcessor().request(JSONObject.parseObject(data, UserBase.class));
    }

    @Override
    public void asyncGetLocalConversationList() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_GET_CONVERSATION_LIST, "");
        }

        CMD.LOCAL_GET_CONVERSATION_LIST.getProcessor().request(VOID);
    }

    @Override
    public void asyncGetLocalConversationMessageList(String contactType, String contactId, long lastMessageTimestamp, int count) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}, {}, {}, {}")
                    , Operation.LOCAL_GET_CONVERSATION_MESSAGE_LIST
                    , contactType, contactId, lastMessageTimestamp, count);
        }

        GetConversationMessageListData data = new GetConversationMessageListData();
        data.setContactType(Messages.RecentContactType.valueOf(contactType));
        data.setContactId(contactId);
        data.setEarliestTimestamp(lastMessageTimestamp);
        data.setCount(count);

        CMD.LOCAL_GET_CONVERSATION_MESSAGE_LIST.getProcessor().request(data);
    }

    @Override
    public void asyncGetLocalExtMessageList(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_GET_EXT_MESSAGE_LIST, data);
        }

        CMD.LOCAL_GET_EXT_MESSAGE_LIST.getProcessor().request(JSONObject.parseObject(data, GetExtMessageListData.class));
    }

    @Override
    public void asyncDeleteLocalExtMessage(String svrMsgId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_DELETE_EXT_MESSAGE, svrMsgId);
        }

        CMD.LOCAL_DELETE_EXT_MESSAGE.getProcessor().request(svrMsgId);
    }

    @Override
    public void asyncDeleteLocalExtMessageList(int type) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_DELETE_EXT_MESSAGE_LIST, type);
        }

        CMD.LOCAL_DELETE_EXT_MESSAGE_LIST.getProcessor().request(type);
    }

    @Override
    public void asyncGetLocalFriendGroups() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_GET_FRIEND_GROUPS, "");
        }

        CMD.LOCAL_GET_FRIEND_GROUPS.getProcessor().request(VOID);
    }

    @Override
    public void asyncDeleteFriendGroup(String friendGroupId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.DELETE_FRIEND_GROUP, friendGroupId);
        }

        CMD.REQ_DELETE_FRIEND_GROUP.getProcessor().request(friendGroupId);
    }

    @Override
    public void asyncGetLocalGroups() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_GET_GROUPS, "");
        }

        CMD.LOCAL_GET_GROUPS.getProcessor().request(VOID);
    }

    @Override
    public void asyncGetLocalDiscussions() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_GET_DISCUSSIONS, "");
        }

        CMD.LOCAL_GET_DISCUSSIONS.getProcessor().request(VOID);
    }

    @Override
    public void asyncActiveConversation(String contactType, String contactId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}, {}")
                    , Operation.ACTIVE_CONVERSATION, contactType, contactId);
        }

        ConversationStatusData data = new ConversationStatusData();
        data.setType(Messages.RecentContactType.valueOf(contactType));
        data.setContactId(contactId);
        data.setActive(true);
        CMD.REQ_MESSAGE_HAS_BEEN_READ.getProcessor().request(data);
    }

    @Override
    public void asyncInActiveConversation(String contactType, String contactId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}, {}")
                    , Operation.INACTIVE_CONVERSATION, contactType, contactId);
        }

        ConversationStatusData data = new ConversationStatusData();
        data.setType(Messages.RecentContactType.valueOf(contactType));
        data.setContactId(contactId);
        data.setActive(false);
        CMD.REQ_MESSAGE_HAS_BEEN_READ.getProcessor().request(data);
    }

    @Override
    public void asyncFetchMessages() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.FETCH_MESSAGE, "");
        }

        CMD.REQ_FETCH_MESSAGE.getProcessor().request(null);
    }

    @Override
    public void asyncFetchGroupMessages(String fetchData) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.FETCH_MESSAGE, fetchData);
        }

        CMD.REQ_FETCH_MESSAGE.getProcessor().request(JSONObject.parseObject(fetchData, FetchData.class));
    }

    @Override
    public void asyncSendMessage(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.SEND_MESSAGE, data);
        }

        MessageData messageData = JSONObject.parseObject(data, MessageData.class);

        CMD cmd;

        if (messageData.getContactType() == Messages.RecentContactType.PERSON_VALUE) {
            cmd = CMD.MESSAGE;
        } else if (messageData.getContactType() == Messages.RecentContactType.GROUP_VALUE) {
            cmd = CMD.GROUP_MESSAGE;
        } else {
            cmd = CMD.DISCUSSION_MESSAGE;
        }

        cmd.getProcessor().request(messageData);
    }

    @Override
    public long asyncSendOfflineFileTo(String fileName, String userId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}, {}")
                    , Operation.SEND_OFFLINE_FILE, fileName, userId);
        }

        FileData data = new FileData();
        data.setContactId(userId);
        data.setFileName(fileName);
        return (long) CMD.REQ_ADD_OFFLINE_FILE.getProcessor().request(data);
    }

    @Override
    public void asyncGetFriendRule(String targetUserId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GET_FRIEND_RULE, targetUserId);
        }

        CMD.REQ_GET_FRIEND_RULE.getProcessor().request(targetUserId);

    }

    @Override
    public void asyncAddFriend(String addFriendAction) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.ADD_FRIEND, addFriendAction);
        }

        CMD.REQ_ADD_FRIEND.getProcessor().request(JSONObject.parseObject(addFriendAction, AddFriendAction.class));
    }

    @Override
    public void asyncAddFriendAnswer(String answer) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.ADD_FRIEND_ANSWER, answer);
        }

        CMD.REQ_ADD_FRIEND_ANSWER.getProcessor().request(JSONObject.parseObject(answer, AddFriendAnswer.class));
    }

    @Override
    public void asyncUpdateConversationSetting(String setting) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.LOCAL_UPDATE_CONVERSATION_SETTING
                    , setting);
        }

        CMD.LOCAL_UPDATE_CONVERSATION.getProcessor().request(JSONObject.parseObject(setting, ConversationSetting.class));
    }

    @Override
    public void asyncSetGroupRule(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.SET_GROUP_RULE, data);
        }

        CMD.REQ_SET_GROUP_RULE.getProcessor().request(JSONObject.parseObject(data, GroupRuleData.class));
    }

    @Override
    public void asyncGetGroupRule(String groupId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GET_GROUP_RULE, groupId);
        }

        CMD.REQ_GET_GROUP_RULE.getProcessor().request(groupId);
    }

    @Override
    public void asyncGetGroupUserRule(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GET_GROUP_USER_RULE, data);
        }

        CMD.REQ_GET_GROUP_USER_RULE.getProcessor().request(JSONObject.parseObject(data, GroupUserRuleData.class));
    }

    @Override
    public void asyncGetGroupList() throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GET_GROUP_LIST, "");
        }

        CMD.REQ_GROUP_LIST.getProcessor().request(null);
    }

    @Override
    public void asyncGetGroupUser(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GROUP_USER_INFO, data);
        }

        CMD.REQ_GROUP_USER_INFO.getProcessor().request(JSONObject.parseObject(data, GroupUserQueryData.class));
    }

    @Override
    public void asyncUpdateGroupNickNameSetting(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_GROUP_NICKNAME_SETTING, data);
        }

        CMD.REQ_GROUP_UPDATE_NICKNAME_SETTING.getProcessor().request(JSONObject.parseObject(data, MTGroupUser.class));
    }

    @Override
    public void asyncUpdateGroupRemark(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_GROUP_REMARK, data);
        }

        CMD.REQ_GROUP_UPDATE_REMARK.getProcessor().request(JSONObject.parseObject(data, MTGroup.class));
    }

    @Override
    public void asyncUpdateGroupNickName(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_GROUP_NICKNAME, data);
        }

        CMD.REQ_GROUP_UPDATE_NICKNAME.getProcessor().request(JSONObject.parseObject(data, MTGroupUser.class));
    }

    @Override
    public void asyncUpdateGroupUserSetting(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_GROUP_USER_SETTING, data);
        }

        CMD.REQ_GROUP_UPDATE_USER_SETTING.getProcessor().request(JSONObject.parseObject(data, GroupSettingData.class));
    }

    @Override
    public void asyncExitGroup(String groupId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GROUP_EXIT, groupId);
        }

        CMD.REQ_GROUP_EXIT.getProcessor().request(groupId);
    }

    @Override
    public void asyncUpdateGroupInfo(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.UPDATE_GROUP_INFO, data);
        }

        CMD.REQ_GROUP_UPDATE_INFO.getProcessor().request(JSONObject.parseObject(data, MTGroup.class));
    }

    @Override
    public void asyncDiableGroup(String groupId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.DISABLE_GROUP, groupId);
        }

        CMD.REQ_GROUP_DISABLE.getProcessor().request(groupId);
    }

    @Override
    public void asyncCreateGroup(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.GROUP_CREATE, data);
        }

        CMD.REQ_GROUP_CREATE.getProcessor().request(JSONObject.parseObject(data, MTGroup.class));
    }

    @Override
    public void asyncInviteJoinToGroup(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.INVITE_JOIN_TO_GROUP, data);
        }

        CMD.REQ_GROUP_INVITE_JOIN_TO.getProcessor().request(JSONObject.parseObject(data, InviteJoinToGroupData.class));
    }

    @Override
    public void asyncAnswerInviteJoinToGroup(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.JOIN_GROUP_ANSWER, data);
        }

        CMD.REQ_GROUP_INVITE_USER_JOIN_ANSWER.getProcessor().request(JSONObject.parseObject(data, InviteUserJoinGroupAnswer.class));
    }

    @Override
    public void asyncCreateDiscussion(String data) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.CREATE_DISCUSSION, data);
        }

        CMD.REQ_DISCUSSION_CREATE.getProcessor().request(JSONObject.parseObject(data, AddDiscussionData.class));
    }

    @Override
    public void asyncExitDiscussion(String discussionId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.DISCUSSION_EXIT, discussionId);
        }

        CMD.REQ_DISCUSSION_EXIT.getProcessor().request(discussionId);
    }

    @Override
    public void asyncDeleteFriend(String friendUserId) throws RemoteException {
        if (logger.isInfoEnabled()) {
            logger.info(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.INVOKE, "operation:{}, data:{}")
                    , Operation.DELETE_FRIEND, friendUserId);
        }

        CMD.REQ_DELETE_FRIEND.getProcessor().request(friendUserId);
    }
}
