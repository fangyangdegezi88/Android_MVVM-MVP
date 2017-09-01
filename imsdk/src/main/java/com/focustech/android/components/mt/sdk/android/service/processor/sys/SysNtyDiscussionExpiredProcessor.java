package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.IGroupService;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 讨论组过期的系统通知
 *
 * @author zhangxu
 */
public class SysNtyDiscussionExpiredProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        Messages.DiscussionExpiredSysNty nty = Messages.DiscussionExpiredSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }


        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_DISCUSSION_EXPIRED, Messages.RecentContactType.DISCUSSION, nty.getDiscussionId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);
    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.DiscussionExpiredSysNty.parseFrom(data));
    }

    private void doBiz(Messages.DiscussionExpiredSysNty nty) throws Throwable {
        final String userId = getSessionManager().getUserId();
        final String discussionId = nty.getDiscussionId();

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getGroupService().delete(userId, discussionId, IGroupService.FEATURE_TEMP);
                // 是自己，删除群
                updateLastTimestamp(userId, LastTimestampType.DATA_DISCUSSION_LIST);
            }
        });
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateDiscussionExpired(discussionId);
        }
    }
}
