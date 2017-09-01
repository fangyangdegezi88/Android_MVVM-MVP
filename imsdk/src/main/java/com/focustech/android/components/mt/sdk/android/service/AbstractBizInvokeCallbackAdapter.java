package com.focustech.android.components.mt.sdk.android.service;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.UserSettingData;
import com.focustech.android.components.mt.sdk.android.service.pojo.conversation.ConversationData;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddAndAgreeFriendSucceededToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendFailNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.AddFriendSucceededToSrcNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.friend.FriendGroups;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeInviteUserJoinGroupSucceededNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.AgreeJoinGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DeletedFromGroupToAdminNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DeletedFromGroupToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeInviteUserJoinGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeJoinGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.ExitGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupRuleData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupSettingData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserRuleData;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToAdminNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.InviteUserJoinGroupSucceededToTargetNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.JoinGroupProcessedNotify;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.android.components.mt.sdk.android.service.pojo.status.UserStatusData;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 业务调用回调适配器
 *
 * @author zhangxu
 * @modify zhangzeyu 2016/4/5 新增透传系统通知返回
 */
public abstract class AbstractBizInvokeCallbackAdapter extends IBizInvokeCallback.Stub {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractBizInvokeCallbackAdapter.class);
    @Override
    public void privateConnected() throws RemoteException {
        onConnected();
    }

    protected abstract void onConnected() throws RemoteException;

    @Override
    public void privateConfigurationComplete() throws RemoteException {
        onConfigurationComplete();
    }

    protected abstract void onConfigurationComplete() throws RemoteException;

    @Override
    public void privateDisconnected() throws RemoteException {
        onDisconnected();
    }

    protected abstract void onDisconnected() throws RemoteException;

    @Override
    public void privateNetworkChanged(String data) throws RemoteException {
        onNetworkChanged(MTRuntime.Network.valueOf(data));
    }

    protected abstract void onNetworkChanged(MTRuntime.Network network) throws RemoteException;

    @Override
    public void privateOperationTimeout(String operation, String data) throws RemoteException {
        onOperationTimeout(Operation.valueOf(operation), data);
    }

    protected abstract void onOperationTimeout(Operation operation, String data) throws RemoteException;

    @Override
    public void privateDeleteAccountSuccessful() throws RemoteException {
        onDeleteAccountSuccessful();
    }

    protected abstract void onDeleteAccountSuccessful() throws RemoteException;

    @Override
    public void privateCleanAccountsSuccessful() throws RemoteException {
        onCleanAccountsSuccessful();
    }

    protected abstract void onCleanAccountsSuccessful() throws RemoteException;

    @Override
    public void privateLoginSuccessful(String userId, String token, String platformData) throws RemoteException {
        onLoginSuccessful(userId, token, platformData);
    }

    protected abstract void onLoginSuccessful(String userId, String token, String platformData) throws RemoteException;

    @Override
    public void privateLoginFailed(int code) throws RemoteException {
        onLoginFailed(code);
    }

    protected abstract void onLoginFailed(int code) throws RemoteException;

    @Override
    public void privateAutoLoginFailed() throws RemoteException {
        onAutoLoginFailed();
    }

    protected abstract void onAutoLoginFailed() throws RemoteException;

    @Override
    public void privateLogoutSuccessful(String userId) throws RemoteException {
        onLogoutSuccessful(userId);
    }

    protected abstract void onLogoutSuccessful(String userId) throws RemoteException;

    @Override
    public void privateKickout(String userId, String equipment) throws RemoteException {
        onKickout(userId, Messages.Equipment.valueOf(equipment));
    }

    protected abstract void onKickout(String userId, Messages.Equipment equipment) throws RemoteException;

    @Override
    public void privateMyStatusChanged(String status) throws RemoteException {
        onMyStatusChanged(Messages.Status.valueOf(status));
    }

    protected abstract void onMyStatusChanged(Messages.Status status) throws RemoteException;

    @Override
    public void privateMyEquipmentStatusChanged(String equipment, String status) throws RemoteException {
        onMyEquipmentStatusChanged(Messages.Equipment.valueOf(equipment), Messages.Status.valueOf(status));
    }

    protected abstract void onMyEquipmentStatusChanged(Messages.Equipment equipment, Messages.Status status) throws RemoteException;

    @Override
    public void privateStatusChanged(String userId, String equipment, String status) throws RemoteException {
        onStatusChanged(userId, Messages.Equipment.valueOf(equipment), Messages.Status.valueOf(status));
    }

    protected abstract void onStatusChanged(String userId, Messages.Equipment equipment, Messages.Status status) throws RemoteException;

    @Override
    public void privateMyInfoChanged(String data) throws RemoteException {
        onMyInfoChanged(JSONObject.parseObject(data, UserBase.class));
    }

    protected abstract void onMyInfoChanged(UserBase base) throws RemoteException;

    @Override
    public void privateFriendUserInfoChanged(String data) throws RemoteException {
        onFriendUserInfoChanged(JSONObject.parseObject(data, UserBase.class));
    }

    protected abstract void onFriendUserInfoChanged(UserBase base) throws RemoteException;

    @Override
    public void privateMySignatureChanged(String newSignature) throws RemoteException {
        onMySignatureChanged(newSignature);
    }

    protected abstract void onMySignatureChanged(String newSignature) throws RemoteException;

    @Override
    public void privateFriendSignatureChanged(String userId, String newSignature) throws RemoteException {
        onFriendSignatureChanged(userId, newSignature);
    }

    protected abstract void onFriendSignatureChanged(String userId, String newSignature) throws RemoteException;

    @Override
    public void privateMyNickNameChanged(String newNickName) throws RemoteException {
        onMyNickNameChanged(newNickName);
    }

    protected abstract void onMyNickNameChanged(String newNickName) throws RemoteException;

    @Override
    public void privateFriendNickNameChanged(String userId, String newNickName) throws RemoteException {
        onFriendNickNameChanged(userId, newNickName);
    }

    protected abstract void onFriendNickNameChanged(String userId, String newNickName) throws RemoteException;

    @Override
    public void privateMyHeadChanged(int userHeadType, String userHeadId) throws RemoteException {
        onMyHeadChanged(Messages.HeadType.valueOf(userHeadType), userHeadId);
    }

    protected abstract void onMyHeadChanged(Messages.HeadType headType, String userHeadId) throws RemoteException;

    @Override
    public void privateFriendHeadChanged(String userId, int userHeadType, String userHeadId) throws RemoteException {
        onFriendHeadChanged(userId, Messages.HeadType.valueOf(userHeadType), userHeadId);
    }

    protected abstract void onFriendHeadChanged(String userId, Messages.HeadType headType, String userHeadId) throws RemoteException;

    @Override
    public void privateUsersPoolChanged(String data) throws RemoteException {
        onUsersPoolChanged(JSONObject.parseArray(data, UserBase.class));
    }

    protected abstract void onUsersPoolChanged(List<UserBase> userBases) throws RemoteException;

    @Override
    public void privateSettingChanged(String data) throws RemoteException {
        onSettingChanged(JSONObject.parseObject(data, UserSettingData.class));
    }

    protected abstract void onSettingChanged(UserSettingData userSettingData) throws RemoteException;

    @Override
    public void privateUpdateFriendNoDisturbChanged(String userId, String enable) throws RemoteException {
        onUpdateFriendNoDisturbChanged(userId, Messages.Enable.valueOf(enable));
    }

    protected abstract void onUpdateFriendNoDisturbChanged(String userId, Messages.Enable enable) throws RemoteException;

    @Override
    public void privateFriendGroupsChanged(String data) throws RemoteException {
        onFriendGroupsChanged(JSONObject.parseObject(data, FriendGroups.class));
    }

    protected abstract void onFriendGroupsChanged(FriendGroups friendGroups) throws RemoteException;

    @Override
    public void privateLocalConversationList(String data) throws RemoteException {
        onLocalConversationList(JSONObject.parseArray(data, ConversationData.class));
    }

    protected abstract void onLocalConversationList(List<ConversationData> data) throws RemoteException;

    @Override
    public void privateLocalConversation(String data) throws RemoteException {
        onLocalConversation(JSONObject.parseObject(data, ConversationData.class));
    }

    protected abstract void onLocalConversation(ConversationData data) throws RemoteException;

    @Override
    public void privateLocalConversationMessageList(String contactType, String contactId, String data) throws RemoteException {
        onLocalConversationMessageList(Messages.RecentContactType.valueOf(contactType), contactId, JSONObject.parseArray(data, MessageData.class));
    }

    protected abstract void onLocalConversationMessageList(Messages.RecentContactType type, String contactId, List<MessageData> messages) throws RemoteException;

    @Override
    public void privateLocalExtMessageList(String userId, String type, String data) throws RemoteException {
        onLocalExtMessageList(userId, Messages.MessageType.valueOf(type), JSONObject.parseArray(data, MessageData.class));
    }

    protected abstract void onLocalExtMessageList(String userId, Messages.MessageType type, List<MessageData> messages) throws RemoteException;

    @Override
    public void privateLocalConversationMessageListOnFetching(String contactType, String contactId) throws RemoteException {
        onLocalConversationMessageListOnFetching(Messages.RecentContactType.valueOf(contactType), contactId);
    }

    protected abstract void onLocalConversationMessageListOnFetching(Messages.RecentContactType type, String contactId) throws RemoteException;

    @Override
    public void privateHistoryMessageSyncComplete(String contactType, String contactId) throws RemoteException {
        onHistoryMessageSyncComplete(Messages.RecentContactType.valueOf(contactType), contactId);
    }

    protected abstract void onHistoryMessageSyncComplete(Messages.RecentContactType type, String contactId) throws RemoteException;

    @Override
    public void privateMessageBindingUploadTask(String localMessageId, long taskId) throws RemoteException {
        onMessageBindingUploadTask(localMessageId, taskId);
    }

    protected abstract void onMessageBindingUploadTask(String localMessageId, long taskId) throws RemoteException;

    @Override
    public void privateMessageSendFail(String contactId, int contactType, String localMessageId) throws RemoteException {
        onMessageSendFail(contactId, contactType, localMessageId);
    }

    protected abstract void onMessageSendFail(String contactId, int contactType, String localMessageId) throws RemoteException;

    @Override
    public void privateMessageSendSuccessful(String contactId, int contactType, String localMessageId) throws RemoteException {
        onMessageSendSuccessful(contactId, contactType, localMessageId);
    }

    protected abstract void onMessageSendSuccessful(String contactId, int contactType, String localMessageId) throws RemoteException;

    @Override
    public void privateMessage(String toUserId, String data) throws RemoteException {
        onMessage(toUserId, JSONObject.parseObject(data, MessageData.class));
    }

    protected abstract void onMessage(String toUserId, MessageData data) throws RemoteException;

    @Override
    public void privateMessageExt(String toUserId, String data) throws RemoteException {
        onMessageExt(toUserId, JSONObject.parseObject(data, MessageData.class));
    }

    protected abstract void onMessageExt(String toUserId, MessageData data) throws RemoteException;

    @Override
    public void privateMessageSync(String toUserId, String equipment, String data) throws RemoteException {
        onMessageSync(toUserId, Messages.Equipment.valueOf(equipment), JSONObject.parseObject(data, MessageData.class));
    }

    protected abstract void onMessageSync(String toUserId, Messages.Equipment equipment, MessageData data) throws RemoteException;

    @Override
    public void privateKeyboardInputMessage(String fromUserId) throws RemoteException {
        onKeyboardInputMessage(fromUserId);
    }

    protected abstract void onKeyboardInputMessage(String fromUserId) throws RemoteException;

    @Override
    public void privateTaskProcessing(long taskId, long total, long complete) throws RemoteException {
        onTaskProcessing(taskId, total, complete);
    }

    protected abstract void onTaskProcessing(long taskId, long total, long complete) throws RemoteException;

    @Override
    public void privateTaskComplete(long taskId) throws RemoteException {
        onTaskComplete(taskId);
    }

    protected abstract void onTaskComplete(long taskId) throws RemoteException;

    @Override
    public void privateTaskFailure(long taskId) throws RemoteException {
        onTaskFailure(taskId);
    }

    protected abstract void onTaskFailure(long taskId) throws RemoteException;

    @Override
    public void privateAddFriendSucceededToSrcNotify(String addFriendSucceededToSrcNotify) throws RemoteException {
        onAddFriendSucceededToSrcNotify(JSONObject.parseObject(addFriendSucceededToSrcNotify, AddFriendSucceededToSrcNotify.class));
    }

    protected abstract void onAddFriendSucceededToSrcNotify(AddFriendSucceededToSrcNotify addFriendSucceededToSrcNotify) throws RemoteException;

    @Override
    public void privateAddFriendSucceededToTargetNotify(String addAndAgreeFriendSucceededToTargetNotify) throws RemoteException {
        onAddFriendSucceededToTargetNotify(JSONObject.parseObject(addAndAgreeFriendSucceededToTargetNotify, AddAndAgreeFriendSucceededToTargetNotify.class));
    }

    protected abstract void onAddFriendSucceededToTargetNotify(AddAndAgreeFriendSucceededToTargetNotify addAndAgreeFriendSucceededToTargetNotify) throws RemoteException;

    @Override
    public void privateAddFriendFailNotify(String addFriendFailNotify) throws RemoteException {
        onAddFriendFailNotify(JSONObject.parseObject(addFriendFailNotify, AddFriendFailNotify.class));
    }

    protected abstract void onAddFriendFailNotify(AddFriendFailNotify addFriendFailNotify) throws RemoteException;

    @Override
    public void privateAddFriendFailUnknownNotify(String targetUserId) throws RemoteException {
        onAddFriendFailUnknownNotify(targetUserId);
    }

    protected abstract void onAddFriendFailUnknownNotify(String targetUserId) throws RemoteException;

    @Override
    public void privateFriendRuleNotify(String targetUserId, String rule) throws RemoteException {
        onFriendRuleNotify(targetUserId, Messages.ValidateRule.valueOf(rule));
    }

    protected abstract void onFriendRuleNotify(String targetUserId, Messages.ValidateRule validateRule) throws RemoteException;

    @Override
    public void privateFriendDeleteSuccessful(String friendUserId) throws RemoteException {
        onFriendDeleteSuccessful(friendUserId);
    }

    protected abstract void onFriendDeleteSuccessful(String friendUserId) throws RemoteException;

    @Override
    public void privateSetGroupRuleSuccessful(String data) throws RemoteException {
        onSetGroupRuleSuccessful(JSONObject.parseObject(data, GroupRuleData.class));
    }

    protected abstract void onSetGroupRuleSuccessful(GroupRuleData data) throws RemoteException;

    @Override
    public void privateGetGroupRuleSuccessful(String data) throws RemoteException {
        onGetGroupRuleSuccessful(JSONObject.parseObject(data, GroupRuleData.class));
    }

    protected abstract void onGetGroupRuleSuccessful(GroupRuleData data) throws RemoteException;

    @Override
    public void privateGetGroupUserRuleSuccessful(String data) throws RemoteException {
        onGetGroupUserRuleSuccessful(JSONObject.parseObject(data, GroupUserRuleData.class));
    }

    protected abstract void onGetGroupUserRuleSuccessful(GroupUserRuleData data) throws RemoteException;

    @Override
    public void privateGroupsChanged(String data) throws RemoteException {
        onGroupsChanged(JSONObject.parseObject(data, MTGroups.class));
    }

    protected abstract void onGroupsChanged(MTGroups groups) throws RemoteException;

    @Override
    public void privateUsersStatusChanged(String data) throws RemoteException {
        onUsersStatusChanged(JSONObject.parseArray(data, UserStatusData.class));
    }

    protected abstract void onUsersStatusChanged(List<UserStatusData> userStatusDataList) throws RemoteException;

    @Override
    public void privateGroupChanged(String data) throws RemoteException {
        onGroupChanged(JSONObject.parseObject(data, MTGroup.class));
    }

    protected abstract void onGroupChanged(MTGroup group) throws RemoteException;

    @Override
    public void privateGroupUserChanged(String data) throws RemoteException {
        onGroupUserChanged(JSONObject.parseObject(data, MTGroupUser.class));
    }

    protected abstract void onGroupUserChanged(MTGroupUser groupUser) throws RemoteException;

    @Override
    public void privateUpdateGroupNickNameSettingSuccessful(String groupId, String userId, String setting) throws RemoteException {
        onUpdateGroupNickNameSettingSuccessful(groupId, userId, Messages.Enable.valueOf(setting));
    }

    protected abstract void onUpdateGroupNickNameSettingSuccessful(String groupId, String userId, Messages.Enable setting) throws RemoteException;

    @Override
    public void privateUpdateGroupRemarkSuccessful(String groupId) throws RemoteException {
        onUpdateGroupRemarkSuccessful(groupId);
    }

    protected abstract void onUpdateGroupRemarkSuccessful(String groupId) throws RemoteException;

    @Override
    public void privateGroupNickNameChanged(String groupId, String userId, String newGroupNickName) throws RemoteException {
        onGroupNickNameChanged(groupId, userId, newGroupNickName);
    }

    protected abstract void onGroupNickNameChanged(String groupId, String userId, String newGroupNickName) throws RemoteException;

    @Override
    public void privateGroupUserSettingChanged(String data) throws RemoteException {
        onGroupUserSettingChanged(JSONObject.parseObject(data, GroupSettingData.class));
    }

    protected abstract void onGroupUserSettingChanged(GroupSettingData data) throws RemoteException;

    @Override
    public void privateUserExitGroup(String groupId, String userId) throws RemoteException {
        onUserExitGroup(groupId, userId);
    }

    protected abstract void onUserExitGroup(String groupId, String userId) throws RemoteException;

    @Override
    public void privateGroupUserDeleted(String groupId, String userIds) throws RemoteException {
        onGroupUserDeleted(groupId, JSONObject.parseArray(userIds, String.class));
    }

    protected abstract void onGroupUserDeleted(String groupId, List<String> userIds) throws RemoteException;

    @Override
    public void privateGroupDisabled(String groupId) throws RemoteException {
        onGroupDisabled(groupId);
    }

    protected abstract void onGroupDisabled(String groupId) throws RemoteException;

    @Override
    public void privateGroupUserTypeChanged(String groupId, String userIds, int userType) throws RemoteException {
        onGroupUserTypeChanged(groupId, JSONObject.parseArray(userIds, String.class), Messages.UserType.valueOf(userType));
    }

    protected abstract void onGroupUserTypeChanged(String groupId, List<String> userIds, Messages.UserType userType) throws RemoteException;

    @Override
    public void privateGroupUserInviteAgreedToAdmin(String data) throws RemoteException {
        onGroupUserInviteAgreedToAdmin(JSONObject.parseObject(data, AgreeInviteUserJoinGroupSucceededNotify.class));
    }

    protected abstract void onGroupUserInviteAgreedToAdmin(AgreeInviteUserJoinGroupSucceededNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserInviteDisagreedToAdmin(String data) throws RemoteException {
        onGroupUserInviteDisagreedToAdmin(JSONObject.parseObject(data, DisagreeInviteUserJoinGroupNotify.class));
    }

    protected abstract void onGroupUserInviteDisagreedToAdmin(DisagreeInviteUserJoinGroupNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserDeletedToAdmin(String data) throws RemoteException {
        onGroupUserDeletedToAdmin(JSONObject.parseObject(data, DeletedFromGroupToAdminNotify.class));
    }

    protected abstract void onGroupUserDeletedToAdmin(DeletedFromGroupToAdminNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserDeletedToTarget(String data) throws RemoteException {
        onGroupUserDeletedToTarget(JSONObject.parseObject(data, DeletedFromGroupToTargetNotify.class));
    }

    protected abstract void onGroupUserDeletedToTarget(DeletedFromGroupToTargetNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserExit(String data) throws RemoteException {
        onGroupUserExit(JSONObject.parseObject(data, ExitGroupNotify.class));
    }

    protected abstract void onGroupUserExit(ExitGroupNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserInviteSucceededToAdmin(String data) throws RemoteException {
        onGroupUserInviteSucceededToAdmin(JSONObject.parseObject(data, InviteUserJoinGroupSucceededToAdminNotify.class));
    }

    protected abstract void onGroupUserInviteSucceededToAdmin(InviteUserJoinGroupSucceededToAdminNotify notify) throws RemoteException;

    @Override
    public void privateGroupUserInviteSucceededToTarget(String data) throws RemoteException {
        onGroupUserInviteSucceededToTarget(JSONObject.parseObject(data, InviteUserJoinGroupSucceededToTargetNotify.class));
    }

    protected abstract void onGroupUserInviteSucceededToTarget(InviteUserJoinGroupSucceededToTargetNotify notify) throws RemoteException;

    @Override
    public void privateJoinGroupSuccessful(String groupId, String groupName) throws RemoteException {
        onJoinGroupSuccessful(groupId, groupName);
    }

    protected abstract void onJoinGroupSuccessful(String groupId, String groupName) throws RemoteException;

    @Override
    public void privateJoinGroupProcessed(String data) throws RemoteException {
        onJoinGroupProcessed(JSONObject.parseObject(data, JoinGroupProcessedNotify.class));
    }

    protected abstract void onJoinGroupProcessed(JoinGroupProcessedNotify notify) throws RemoteException;

    @Override
    public void privateJoinGroupAgreed(String data) throws RemoteException {
        onJoinGroupAgreed(JSONObject.parseObject(data, AgreeJoinGroupNotify.class));
    }

    protected abstract void onJoinGroupAgreed(AgreeJoinGroupNotify notify) throws RemoteException;

    @Override
    public void privateJoinGroupDisagreed(String data) throws RemoteException {
        onJoinGroupDisagreed(JSONObject.parseObject(data, DisagreeJoinGroupNotify.class));
    }

    protected abstract void onJoinGroupDisagreed(DisagreeJoinGroupNotify notify) throws RemoteException;

    @Override
    public void privateDiscussionsChanged(String data) throws RemoteException {
        onDiscussionsChanged(JSONObject.parseObject(data, MTGroups.class));
    }

    protected abstract void onDiscussionsChanged(MTGroups data) throws RemoteException;

    @Override
    public void privateDiscussionChanged(String data) throws RemoteException {
        onDiscussionChanged(JSONObject.parseObject(data, MTGroup.class));
    }

    protected abstract void onDiscussionChanged(MTGroup data) throws RemoteException;

    @Override
    public void privateDiscussionUserChanged(String data) throws RemoteException {
        onDiscussionUserChanged(JSONObject.parseObject(data, MTGroupUser.class));
    }

    protected abstract void onDiscussionUserChanged(MTGroupUser data) throws RemoteException;

    @Override
    public void privateDiscussionExpired(String discussionId) throws RemoteException {
        onDiscussionExpired(discussionId);
    }

    protected abstract void onDiscussionExpired(String discussionId) throws RemoteException;

    @Override
    public void privateDiscussionUserExit(String discussionId, String userId) throws RemoteException {
        onDiscussionUserExit(discussionId, userId);
    }

    protected abstract void onDiscussionUserExit(String discussionId, String userId) throws RemoteException;

    @Override
    public void privateDiscussionExitSuccessful(String discussionId) throws RemoteException {
        onDiscussionExitSuccessful(discussionId);
    }

    protected abstract void onDiscussionExitSuccessful(String discussionId) throws RemoteException;

    @Override
    public void privateDiscussionUserSettingChanged(String data) throws RemoteException {
        onDiscussionUserSettingChanged(JSONObject.parseObject(data, GroupSettingData.class));
    }

    protected abstract void onDiscussionUserSettingChanged(GroupSettingData setting) throws RemoteException;

    @Override
    public void privateDiscussionNameChanged(String discussionId, String name) throws RemoteException {
        onDiscussionNameChanged(discussionId, name);
    }

    protected abstract void onDiscussionNameChanged(String discussionId, String name) throws RemoteException;

    @Override
    public void privateDeleteFriendGroupSuccessful(String friendGroupId) throws RemoteException {
        onDeleteFriendGroupSuccessful(friendGroupId);
    }

    protected abstract void onDeleteFriendGroupSuccessful(String friendGroupId) throws RemoteException;

    @Override
    public void privatePullLog(String logId) throws RemoteException {
        onPullLog(logId);
    }

    protected abstract void onPullLog(String logId) throws RemoteException;

    @Override
    public void privateStudentInfoChange() throws RemoteException {
        onStudentInfoChange();
    }

    protected abstract void onStudentInfoChange();

    @Override
    public void privateSystemNotify(String content) throws RemoteException {
        onSystemNotify(content);
    }

    protected abstract void onSystemNotify(String content);
}
