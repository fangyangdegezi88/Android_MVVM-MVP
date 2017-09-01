package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.alibaba.fastjson.JSONObject;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.group.GroupUserQueryData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 请求拉取好友
 *
 * @author zhangxu
 */
public class ReqGroupUserInfoProcessor extends AbstractProcessor<GroupUserQueryData, Void, Void> {
    @Override
    public Void request(GroupUserQueryData data) {
        byte[] send = Messages.GroupSingleUserInfoReq.newBuilder()
                .setGroupId(data.getGroupId()).setUserId(data.getGroupUserId()).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_GROUP_USER_INFO, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.GROUP_USER_INFO, JSONObject.toJSONString(data));

        return null;
    }
}
