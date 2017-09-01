package com.focustech.android.components.mt.sdk.android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.commonlibs.util.GeneralUtils;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.IBizInvokeCallback;
import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.ContextHolder;
import com.focustech.android.components.mt.sdk.android.receiver.ConnectivityReceiver;
import com.focustech.android.components.mt.sdk.android.receiver.LaunchReceiver;
import com.focustech.android.components.mt.sdk.android.service.keepalive.MTJobService;
import com.focustech.android.components.mt.sdk.android.service.keepalive.ServiceProtectUtils;
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
import com.focustech.android.components.mt.sdk.android.service.processor.req.ReqLoginProcessor;
import com.focustech.android.components.mt.sdk.communicate.Communication;
import com.focustech.android.components.mt.sdk.core.net.MTConnection;
import com.focustech.android.components.mt.sdk.support.cache.SharedPrefLoginInfo;
import com.focustech.android.components.mt.sdk.util.AsyncLoginControlContent;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SDK核心Service，<b>使用独立进程启动</b></br>
 * 这个Service会持续在后台运行
 * 在配置时候，需要以下的Intent
 * <pre>
 * 1. {@link com.focustech.android.components.mt.sdk.android.IntentAction#ANDROID_SYSTEM_BOOT}
 * 2. {@link com.focustech.android.components.mt.sdk.android.IntentAction#MT_SERVICE_CORE_BOOT}
 * </pre>
 *
 * @author zhangxu
 */
public class MTCoreService extends Service {
    private L l = new L(MTCoreService.class.getSimpleName());
    private static boolean running = false;

    private boolean configurationComplete = false;

    private static MTConnection connection = null;

    private static ScheduledExecutorService threads = null;

    private static MessageService messageService;

    private static DefaultInvokeServiceImpl invokeService;

    public static boolean isRunning;

    public static boolean isRunning() {
        return running;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(MTCoreService.this, SharedPrefLoginInfo.LOGIN_INFO_FILE);
            if (!"".equals(sharedPrefLoginInfo.getString("confFile", ""))) {
                initConf(null, null, false);
            }
        }
    };


    @Override
    public void onCreate() {
        //添加MTCoreService启动日志
        l.i("MTCoreService onCreate---");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //大于等于5.0之后 使用Job
            Intent intent = new Intent();
            intent.setClass(this, MTJobService.class);
            startService(intent);
            l.i("MTCoreService onCreate start  MTJobService---");
        }
        if (!running) {
            handler.sendEmptyMessageDelayed(1, 2000);
            running = true;
            initAIDLBinder();
            ContextHolder.setAndroidContext(getApplicationContext());

            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            MTRuntime.setNetWork(ConnectivityReceiver.getNetwork(cm));

            ServiceProtectUtils.getInstance().startForeground(this);
            ServiceProtectUtils.getInstance().protectService(this);
        }

        AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
    }

    @Override
    public void onDestroy() {
        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.DESTROY, "destroy."));

        startService(new Intent(this, MTCoreService.class));
        try {
            unregisterReceiver(LaunchReceiver.getInstance());
            unregisterReceiver(ConnectivityReceiver.getInstance(connection));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceProtectUtils.getInstance().protectService(this);
        ServiceProtectUtils.getInstance().alarmManagerProtect(this);
        return START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return invokeService;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (null != messageService) {
            AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
            messageService.setIBizInvokeCallback(EMPTY_CALLBACK);
        }

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public synchronized void initConf(String confFile, IBizInvokeCallback callback, boolean initFromApp) {
        l.d("initConf---initFromApp:" + initFromApp);
        l.d("initConf---1");
        final SharedPrefLoginInfo sharedPrefLoginInfo = new SharedPrefLoginInfo(this, SharedPrefLoginInfo.LOGIN_INFO_FILE);
        String localConfFile = sharedPrefLoginInfo.getString("confFile", "");
        if (!initFromApp)
            confFile = localConfFile;
        l.d("initConf---localConfFile:" + localConfFile + "\n confFile:" + confFile);
        if (!localConfFile.equals(confFile)) {
            sharedPrefLoginInfo.saveString("confFile", confFile);
            configurationComplete = false;
        }
        l.d("initConf---2");
        if (messageService != null && configurationComplete) {
            if (initFromApp) {
                messageService.setIBizInvokeCallback(callback);
                try {
                    l.d("initConf  initFromApp--");
                    String userId = SessionManager.getInstance().getUserId();
                    l.d("initConf  initFromApp--userId:" + userId);
                    if (null != SessionManager.getInstance().getCurrent() && GeneralUtils.isNotNullOrEmpty(userId)) {
                        if (ContextHolder.getAndroidContext() != null) {
                            long lastLoginSuccessfulTime = sharedPrefLoginInfo.getLong("lastLoginSuccessfulTime", 0);
                            long thisLoginTime = System.currentTimeMillis();
                            if (thisLoginTime - lastLoginSuccessfulTime > TimeUnit.DAYS.toMillis(60) && lastLoginSuccessfulTime > 0) {
                                if (null != messageService.getBizInvokeCallback()) {
                                    try {
                                        sharedPrefLoginInfo.saveString("loginInfo", "");
                                        sharedPrefLoginInfo.saveBoolean("loginState", false);
                                        AsyncLoginControlContent.cleanContent(ReqLoginProcessor.LOGIN_KEY);
                                        SessionManager.getInstance().setIsOverDue(true);
                                        messageService.getBizInvokeCallback().privateLoginFailed(9005);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    } finally {
                                        // 删除最近登录记录，避免断线后自动登录
                                        SessionManager.getInstance().clear();
                                    }
                                }
                                return;
                            }
                        }
                        String token = SessionManager.getInstance().getCurrent().getToken();
                        String platformData = SessionManager.getInstance().getCurrent().getPlatformData();
                        l.d("initConf  initFromApp--userId:" + userId + "\n token :" + token + "\n platformData:" + platformData);
                        if (null != messageService.getBizInvokeCallback()) {
                            messageService.getBizInvokeCallback().privateLoginSuccessful(userId, token, platformData);
                            messageService.getBizInvokeCallback().privateMyInfoChanged(sharedPrefLoginInfo.getString(UserBase.class.getSimpleName(), ""));
                        }
                    }
                    if (messageService != null && messageService.getBizInvokeCallback() != null) {
                        messageService.getBizInvokeCallback().privateConfigurationComplete();
                        messageService.getBizInvokeCallback().privateNetworkChanged(MTRuntime.getNetwork().name());
                    }
                } catch (Exception e) {
                    Log.e("error", "error", e);
                }
            }
            return;
        }
        l.d("initConf---3");
        configurationComplete = true;
        // 加载sdk配置
        loadSdkConfiguration(confFile);
        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_LOG, "successful."));
        initThreadPool();

        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_THREADS, "successful."));
        initNTP();
        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_NTP, "successful."));

        initConnectionAndService();

        if (messageService != null && callback != null)
            messageService.setIBizInvokeCallback(callback);
        initConnectionToServer();

        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_NET, "successful."));

        registerConnectivityChange();

        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_NET_RECEIVER, "successful."));

        registerLaunchReceiver();

        l.i(LogFormat.format(LogFormat.LogModule.SYSTEM, LogFormat.Operation.INIT_NET_CONNECT, "successful."));

        try {
            if (messageService != null && messageService.getBizInvokeCallback() != null)
                messageService.getBizInvokeCallback().privateNetworkChanged(MTRuntime.getNetwork().name());
        } catch (RemoteException e) {
        }
    }

    /**
     * 注册链接状态改变
     */
    private void registerConnectivityChange() {
        try {
            registerReceiver(ConnectivityReceiver.getInstance(connection), ConnectivityReceiver.getFilters());
        } catch (Throwable e) {
            Log.e("errorregisterConn ", "error", e);
        }
    }

    private void registerLaunchReceiver() {
        try {
            registerReceiver(LaunchReceiver.getInstance(), LaunchReceiver.getFilters());
        } catch (Throwable e) {
            Log.e("errorregisterLaunch", "error", e);
        }
    }


    private void initConnectionToServer() {
        connection.connect();

        l.i(LogFormat.format(LogFormat.LogModule.NET, LogFormat.Operation.CONNECT, "connect true"));
        try {
            if (messageService != null && messageService.getBizInvokeCallback() != null) {
                messageService.getBizInvokeCallback().privateConfigurationComplete();
            }
        } catch (RemoteException e) {
            //
        }
    }

    private void loadSdkConfiguration(String confFile) {
        // 加载assets下的"mt-sdk.properties"文件
        InputStream inputStream = null;

        try {
            inputStream = getApplicationContext().getAssets().open(confFile);
            Properties properties = new Properties();
            properties.load(inputStream);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                System.setProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            Log.e("mt-sdk", "load configuration error", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private void initConnectionAndService() {
        initConnectionAndService(MTRuntime.getServers());
    }

    private void initConnectionAndService(String[] servers) {
        messageService = new MessageService();
        connection = new MTConnection(servers, MTRuntime.getMTHeartbeat(), messageService);
        messageService.setConnection(connection);
        ContextHolder.setMessageService(messageService);
        connection.addListener(SessionManager.getInstance());
    }

    private void initThreadPool() {
        threads = Executors.newScheduledThreadPool(1);
        TaskUtil.initialize(threads);
    }

    private void initNTP() {
        NTPTime.start();
    }

    private void initAIDLBinder() {
        invokeService = new DefaultInvokeServiceImpl(this);
    }


    private static IBizInvokeCallback EMPTY_CALLBACK = new AbstractBizInvokeCallbackAdapter() {

        @Override
        public void privateStudentInfoChange() throws RemoteException {

        }

        @Override
        public String communicate(Communication communication) throws RemoteException {
            return null;
        }

        @Override
        protected void onStudentInfoChange() {

        }

        @Override
        protected void onDeleteFriendGroupSuccessful(String friendGroupId) throws RemoteException {

        }

        @Override
        protected void onConnected() throws RemoteException {

        }

        @Override
        protected void onAutoLoginFailed() throws RemoteException {

        }

        @Override
        protected void onMessageBindingUploadTask(String localMessageId, long taskId) throws RemoteException {

        }

        @Override
        protected void onDiscussionNameChanged(String discussionId, String name) throws RemoteException {

        }

        @Override
        protected void onDiscussionUserSettingChanged(GroupSettingData setting) throws RemoteException {

        }

        @Override
        protected void onDiscussionUserExit(String discussionId, String userId) throws RemoteException {

        }

        @Override
        protected void onDiscussionExitSuccessful(String discussionId) throws RemoteException {

        }

        @Override
        protected void onDiscussionExpired(String discussionId) throws RemoteException {

        }

        @Override
        protected void onDiscussionsChanged(MTGroups data) throws RemoteException {

        }

        @Override
        protected void onDiscussionChanged(MTGroup data) throws RemoteException {

        }

        @Override
        protected void onDiscussionUserChanged(MTGroupUser data) throws RemoteException {

        }

        @Override
        protected void onJoinGroupAgreed(AgreeJoinGroupNotify notify) throws RemoteException {

        }

        @Override
        protected void onJoinGroupDisagreed(DisagreeJoinGroupNotify notify) throws RemoteException {

        }

        @Override
        protected void onJoinGroupProcessed(JoinGroupProcessedNotify notify) throws RemoteException {

        }

        @Override
        protected void onJoinGroupSuccessful(String groupId, String groupName) throws RemoteException {

        }

        @Override
        protected void onGroupUserInviteSucceededToAdmin(InviteUserJoinGroupSucceededToAdminNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserInviteSucceededToTarget(InviteUserJoinGroupSucceededToTargetNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserExit(ExitGroupNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserInviteDisagreedToAdmin(DisagreeInviteUserJoinGroupNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserDeletedToTarget(DeletedFromGroupToTargetNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserInviteAgreedToAdmin(AgreeInviteUserJoinGroupSucceededNotify notify) throws RemoteException {

        }

        @Override
        protected void onGroupUserDeletedToAdmin(DeletedFromGroupToAdminNotify notify) throws RemoteException {

        }

        @Override
        protected void onAddFriendSucceededToTargetNotify(AddAndAgreeFriendSucceededToTargetNotify addAndAgreeFriendSucceededToTargetNotify) throws RemoteException {

        }

        @Override
        protected void onGroupUserTypeChanged(String groupId, List<String> userIds, Messages.UserType userType) throws RemoteException {

        }

        @Override
        protected void onGroupDisabled(String groupId) throws RemoteException {

        }

        @Override
        protected void onGroupUserDeleted(String groupId, List<String> userIds) throws RemoteException {

        }

        @Override
        protected void onUserExitGroup(String groupId, String userId) throws RemoteException {

        }

        @Override
        protected void onGroupUserSettingChanged(GroupSettingData data) throws RemoteException {

        }

        @Override
        protected void onConfigurationComplete() throws RemoteException {

        }

        @Override
        protected void onDisconnected() throws RemoteException {

        }

        @Override
        protected void onNetworkChanged(MTRuntime.Network network) throws RemoteException {

        }

        @Override
        protected void onOperationTimeout(Operation operation, String data) throws RemoteException {

        }

        @Override
        protected void onDeleteAccountSuccessful() throws RemoteException {

        }

        @Override
        protected void onCleanAccountsSuccessful() throws RemoteException {

        }

        @Override
        protected void onLoginSuccessful(String userId, String token, String platformData) throws RemoteException {

        }

        @Override
        protected void onLoginFailed(int code) throws RemoteException {

        }

        @Override
        protected void onLogoutSuccessful(String userId) throws RemoteException {

        }

        @Override
        protected void onKickout(String userId, Messages.Equipment equipment) throws RemoteException {

        }

        @Override
        protected void onMyStatusChanged(Messages.Status status) throws RemoteException {

        }

        @Override
        protected void onMyEquipmentStatusChanged(Messages.Equipment equipment, Messages.Status status) throws RemoteException {

        }

        @Override
        protected void onStatusChanged(String userId, Messages.Equipment equipment, Messages.Status status) throws RemoteException {

        }

        @Override
        protected void onMyInfoChanged(UserBase base) throws RemoteException {

        }

        @Override
        protected void onFriendUserInfoChanged(UserBase base) throws RemoteException {

        }

        @Override
        protected void onMySignatureChanged(String newSignature) throws RemoteException {

        }

        @Override
        protected void onFriendSignatureChanged(String userId, String newSignature) throws RemoteException {

        }

        @Override
        protected void onMyNickNameChanged(String newNickName) throws RemoteException {

        }

        @Override
        protected void onFriendNickNameChanged(String userId, String newNickName) throws RemoteException {

        }

        @Override
        protected void onMyHeadChanged(Messages.HeadType headType, String userHeadId) throws RemoteException {

        }

        @Override
        protected void onFriendHeadChanged(String userId, Messages.HeadType headType, String userHeadId) throws RemoteException {

        }

        @Override
        protected void onUsersPoolChanged(List<UserBase> userBases) throws RemoteException {

        }

        @Override
        protected void onSettingChanged(UserSettingData userSettingData) throws RemoteException {

        }

        @Override
        protected void onUpdateFriendNoDisturbChanged(String userId, Messages.Enable enable) throws RemoteException {

        }

        @Override
        protected void onFriendGroupsChanged(FriendGroups friendGroups) throws RemoteException {

        }

        @Override
        protected void onLocalConversationList(List<ConversationData> data) throws RemoteException {

        }

        @Override
        protected void onLocalConversation(ConversationData data) throws RemoteException {

        }

        @Override
        protected void onLocalConversationMessageList(Messages.RecentContactType type, String contactId, List<MessageData> messages) throws RemoteException {

        }

        @Override
        protected void onLocalExtMessageList(String userId, Messages.MessageType type, List<MessageData> messages) throws RemoteException {

        }

        @Override
        protected void onLocalConversationMessageListOnFetching(Messages.RecentContactType type, String contactId) throws RemoteException {

        }

        @Override
        protected void onHistoryMessageSyncComplete(Messages.RecentContactType type, String contactId) throws RemoteException {

        }

        @Override
        protected void onMessageSendFail(String contactId, int contactType, String localMessageId) throws RemoteException {

        }

        @Override
        protected void onMessageSendSuccessful(String contactId, int contactType, String localMessageId) throws RemoteException {

        }

        @Override
        protected void onMessage(String toUserId, MessageData data) throws RemoteException {

        }

        @Override
        protected void onMessageExt(String toUserId, MessageData data) throws RemoteException {

        }

        @Override
        protected void onMessageSync(String toUserId, Messages.Equipment equipment, MessageData data) throws RemoteException {

        }

        @Override
        protected void onKeyboardInputMessage(String fromUserId) throws RemoteException {

        }

        @Override
        protected void onTaskProcessing(long taskId, long total, long complete) throws RemoteException {

        }

        @Override
        protected void onTaskComplete(long taskId) throws RemoteException {

        }

        @Override
        protected void onTaskFailure(long taskId) throws RemoteException {

        }

        @Override
        protected void onAddFriendSucceededToSrcNotify(AddFriendSucceededToSrcNotify addFriendSucceededToSrcNotify) throws RemoteException {

        }

        @Override
        protected void onAddFriendFailNotify(AddFriendFailNotify addFriendFailNotify) throws RemoteException {

        }

        @Override
        protected void onAddFriendFailUnknownNotify(String targetUserId) throws RemoteException {

        }

        @Override
        protected void onFriendRuleNotify(String targetUserId, Messages.ValidateRule validateRule) throws RemoteException {

        }

        @Override
        protected void onFriendDeleteSuccessful(String friendUserId) throws RemoteException {

        }

        @Override
        protected void onSetGroupRuleSuccessful(GroupRuleData data) throws RemoteException {

        }

        @Override
        protected void onGetGroupRuleSuccessful(GroupRuleData data) throws RemoteException {

        }

        @Override
        protected void onGetGroupUserRuleSuccessful(GroupUserRuleData data) throws RemoteException {

        }

        @Override
        protected void onGroupsChanged(MTGroups groups) throws RemoteException {

        }

        @Override
        protected void onUsersStatusChanged(List<UserStatusData> userStatusDataList) throws RemoteException {

        }

        @Override
        protected void onGroupChanged(MTGroup group) throws RemoteException {

        }

        @Override
        protected void onGroupUserChanged(MTGroupUser groupUser) throws RemoteException {

        }

        @Override
        protected void onUpdateGroupNickNameSettingSuccessful(String groupId, String userId, Messages.Enable setting) throws RemoteException {

        }

        @Override
        protected void onUpdateGroupRemarkSuccessful(String groupId) throws RemoteException {

        }

        @Override
        protected void onGroupNickNameChanged(String groupId, String userId, String newGroupNickName) throws RemoteException {

        }

        @Override
        public void onPullLog(String logId) throws RemoteException {

        }

        @Override
        protected void onSystemNotify(String content) {

        }
    };

}
