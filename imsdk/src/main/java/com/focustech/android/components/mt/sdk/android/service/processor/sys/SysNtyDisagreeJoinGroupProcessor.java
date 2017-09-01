package com.focustech.android.components.mt.sdk.android.service.processor.sys;

import com.alibaba.fastjson.JSONObject;
import com.focustech.android.commonlibs.capability.log.LogFormat;
import com.focustech.android.components.mt.sdk.android.db.gen.SystemNotify;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.DisagreeJoinGroupNotify;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractGroupProcessor;
import com.focustech.android.components.mt.sdk.util.ReflectionUtil;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;
/**
 * 管理员同意加入。system->申请人
 *
 * @author zhangxu
 */
public class SysNtyDisagreeJoinGroupProcessor extends AbstractGroupProcessor<Void, Void, byte[]> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        final Messages.DisagreeJoinGroupSysNty nty = Messages.DisagreeJoinGroupSysNty.parseFrom(message.getBody());

        if (logger.isDebugEnabled()) {
            logger.debug(LogFormat.format(LogFormat.LogModule.SERVICE, LogFormat.Operation.PROCESS, "{}"), nty);
        }

        // 增加系统消息
        SystemNotify sys = addSystemNotify(CMD.SYS_NTY_JOIN_GROUP_DISAGREE, Messages.RecentContactType.GROUP
                , nty.getGroupId(), message.getHead().getSvrSeqId(), message.getBody());

        doBiz(nty);

        getSystemNotifyService().processed(sys);

    }

    @Override
    public void internalCycle(byte[] data) throws Throwable {
        doBiz(Messages.DisagreeJoinGroupSysNty.parseFrom(data));
    }

    private void doBiz(Messages.DisagreeJoinGroupSysNty nty) throws Throwable {
        DisagreeJoinGroupNotify notify = new DisagreeJoinGroupNotify();
        ReflectionUtil.copyProperties(nty, notify);
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateJoinGroupDisagreed(JSONObject.toJSONString(notify));
        }
    }
}
