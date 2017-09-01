package com.focustech.android.components.mt.sdk.android.service.processor;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.pojo.MTModel;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroup;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroupUser;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.MTGroups;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 抽象实现
 *
 * @author zhangxu
 */
public abstract class AbstractGroupProcessor<PARAM, RETURN, INNER> extends AbstractMessageProcessor<PARAM, RETURN, INNER> {
    protected void updateGroupUser(Messages.GroupUserInfoRsp rsp) throws RemoteException {
        MTModel model = getSessionManager().getCurrent();

        if (null == model) {
            return;
        }

        MTGroups groups = getSessionManager().getCurrent().getGroups();
        final MTGroupUser groupUser = new MTGroupUser(rsp);
        final MTGroup group = groups.getGroup(rsp.getGroupId());
        final String userId = getSessionManager().getUserId();

        if (null != group) {
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    group.addOrUpdateGroupUser(groupUser);
                    getGroupService().addOrUpdateGroupUser(userId, group.getGroupUser(groupUser.getGroupUserId()), IGroupService.FEATURE_FOREVER);
                    updateLastTimestamp(userId, group.getGroupId(), LastTimestampType.DATA_GROUP_USER_INFO);
                }
            });
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateGroupUserChanged(JSONObject.toJSONString(group.getGroupUser(rsp.getUserId())));
            }
        }
    }

    protected void updateDiscussionUser(Messages.DiscussionUserInfoRsp rsp) throws RemoteException {
        MTModel model = getSessionManager().getCurrent();

        if (null == model) {
            return;
        }

        MTGroups groups = getSessionManager().getCurrent().getDiscussions();
        final MTGroupUser groupUser = new MTGroupUser(rsp);
        final MTGroup group = groups.getGroup(rsp.getDiscussionId());
        final String userId = getSessionManager().getUserId();

        if (null != group) {
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    group.addOrUpdateGroupUser(groupUser);
                    getGroupService().addOrUpdateGroupUser(userId, group.getGroupUser(groupUser.getGroupUserId()), IGroupService.FEATURE_TEMP);
                    updateLastTimestamp(userId, group.getGroupId(), LastTimestampType.DATA_DISCUSSION_USER_INFO);
                }
            });
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateDiscussionUserChanged(JSONObject.toJSONString(group.getGroupUser(rsp.getUserId())));
            }
        }
    }

    protected void updateGroupInfo(Messages.MyGroupInfoRsp rsp) throws RemoteException {
        MTGroup group;
        MTModel model = getSessionManager().getCurrent();

        if (null != model) {
            final String userId = model.getCurrent().getUserId();

            group = model.getGroups().getGroup(rsp.getGroupInfoRsp().getGroupId());

            if (group != null) {
                group.update(rsp);
            } else {
                group = new MTGroup();
                group.update(rsp);
                model.getGroups().addOrUpdateGroup(group);
            }

            final MTGroup update = group;

            // 更新本地数据库
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getGroupService().addOrUpdate(userId, update, IGroupService.FEATURE_FOREVER);
                }
            });
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateGroupChanged(JSONObject.toJSONString(group));
            }
        }
    }

    protected void updateGroupInfo(Messages.GroupInfoRsp rsp) throws RemoteException {
        updateGroupInfo(Messages.MyGroupInfoRsp.newBuilder().setGroupInfoRsp(rsp).build());
    }

    protected void updateDiscussionInfo(Messages.DiscussionInfoRsp rsp) throws RemoteException {
        MTGroup group;
        MTModel model = getSessionManager().getCurrent();

        if (null != model) {
            final String userId = model.getCurrent().getUserId();

            group = model.getDiscussions().getGroup(rsp.getDiscussionId());

            if (group != null) {
                group.update(rsp);
            } else {
                group = new MTGroup();
                group.update(rsp);
                model.getGroups().addOrUpdateGroup(group);
            }

            final MTGroup update = group;

            // 更新本地数据库
            asyncExecute(new Runnable() {
                @Override
                public void run() {
                    getGroupService().addOrUpdate(userId, update, IGroupService.FEATURE_TEMP);
                }
            });
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateDiscussionChanged(JSONObject.toJSONString(group));
            }
        }
    }
}
