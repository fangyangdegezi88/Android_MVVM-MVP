package com.focustech.android.components.mt.sdk.android.service.processor.req;


import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.LastTimestampType;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.FetchData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 拉取消息
 *
 * @author zhangxu
 */
public class ReqFetchMessageProcessor extends AbstractMessageProcessor<FetchData, Void, Void> {
    private static int reqId = 0;

    @Override
    public Void request(FetchData data) {
        if (null == data) {
            data = new FetchData();
            data.setType(Messages.RecentContactType.PERSON);
            data.setFromTimestamp(getFromTimestamp(LastTimestampType.MESSAGE_FRIEND));
            data.setToTimestamp(getToTimestamp(LastTimestampType.MESSAGE_FRIEND));
        }

        byte[] send = Messages.FetchMessageReq.newBuilder().setType(data.getType())
                .setAfterTimestamp(data.getFromTimestamp())
                .setBeforeTimestamp(data.getToTimestamp())
                .setCount(MTRuntime.getMTFetchCount())
                .setOrder(Messages.Order.DESC)
                .setGroupId(null == data.getContactId() ? "" : data.getContactId())
                .setReqId(reqId++)
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_FETCH_MESSAGE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.FETCH_MESSAGE);

        return null;
    }
}
