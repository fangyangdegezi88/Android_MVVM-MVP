package com.focustech.android.components.mt.sdk.android.service.processor.msg;


import android.os.RemoteException;

import com.focustech.android.commonlibs.capability.request.file.upload.TaskCallback;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.async.AsyncMessageContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.AbstractMessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageData;
import com.focustech.android.components.mt.sdk.android.service.pojo.MessageMeta;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.android.components.mt.sdk.util.NTPTime;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 聊天消息处理器
 *
 * @author zhangxu
 */
public abstract class AbstractChatMessageProcessor extends AbstractMessageProcessor<MessageData, Void, Void> {
    public Void request(final MessageData data) {
        if (!getMessageService().isConnected()) {
            data.setSendStatus(AbstractMessageData.Status.SEND_FAIL);
            asyncSaveMessage(getSessionManager().getUserId(), data, false);

            try {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateMessageSendFail(data.getContactId(), (int) data.getContactType(), data.getLocalMessageId());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }

        data.setTimestamp(NTPTime.now());
        data.setFromUserId(getSessionManager().getUserId());

        if (data.isMedia()) {
            final int total = data.getMsgMeta().getCustomMeta().getMultiMedias().size();
            final AtomicInteger complete = new AtomicInteger(0);

            // 首先上传
            for (MessageMeta.MultiMediaDescriptor md : data.getMsgMeta().getCustomMeta().getMultiMedias()) {
                try {
                    md.setUploadTaskId(addUploadTask(md.getFile(), md.getUploadFileType(), new TaskCallback() {
                        @Override
                        public void onSuccessful(long taskId, String fileId) {
                            operationComplete(IDGenerator.getKeyUseTaskId(taskId));
                            data.uploadComplete(taskId, fileId);

                            if (complete.incrementAndGet() == total) {
                                doSend(data);
                            }
                        }

                        @Override
                        public void onFailure(long taskId, Throwable cause) {
                            try {
                                operationComplete(IDGenerator.getKeyUseTaskId(taskId));
                                if (getBizInvokeCallback() != null) {
                                    getBizInvokeCallback().privateTaskFailure(taskId);
                                    getBizInvokeCallback().privateMessageSendFail(data.getContactId(), (int) data.getContactType(), data.getLocalMessageId());
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onProcess(long taskId, long total, long complete) {
                            try {
                                if (getBizInvokeCallback() != null) {
                                    getBizInvokeCallback().privateTaskProcessing(taskId, total, complete);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCanceled(long taskId) {
                            operationComplete(IDGenerator.getKeyUseTaskId(taskId));
                        }
                    }));

                    // 增加绑定关系
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateMessageBindingUploadTask(data.getLocalMessageId(), md.getUploadTaskId());
                    }
                } catch (Exception e) {
                    // TODO
                }
            }
        } else {
            doSend(data);
        }

        return null;
    }


    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        try {
            final MessageData data = ((AsyncMessageContext) context).getData();
            data.setSvrMsgId(message.getHead().getSvrSeqId());
            data.setSendStatus(AbstractMessageData.Status.SEND_SUCCESSFUL);

            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateMessageSendSuccessful(data.getContactId(), (int) data.getContactType(), data.getLocalMessageId());
            }
            createConversation(Messages.RecentContactType.valueOf((int) data.getContactType()), data, true);

            asyncSaveMessage(getSessionManager().getUserId(), data, true);
        } catch (Throwable e) {
            // TODO
        }

        return true;
    }

    protected abstract void doSend(MessageData data);
}
