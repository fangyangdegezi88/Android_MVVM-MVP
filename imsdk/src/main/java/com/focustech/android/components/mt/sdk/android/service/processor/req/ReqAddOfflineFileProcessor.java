package com.focustech.android.components.mt.sdk.android.service.processor.req;

import android.os.RemoteException;

import com.focustech.android.commonlibs.capability.request.file.upload.TaskCallback;
import com.focustech.android.components.mt.sdk.FileType;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.async.AbstractAsyncContext;
import com.focustech.android.components.mt.sdk.android.service.pojo.FileData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractHttpOperationSupportProcessor;
import com.focustech.android.components.mt.sdk.util.AsyncContent;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.io.IOException;

import io.netty.util.Timeout;

/**
 * 更新用户状态
 *
 * @author zhangxu
 */
public class ReqAddOfflineFileProcessor extends AbstractHttpOperationSupportProcessor<FileData, Long, Void> implements TaskCallback {
    @Override
    public void timeout(String asyncKey, Object asyncContext, Timeout timeout) {
        doReconnect();

        AddOfflineFileAsyncContent content = AsyncContent.getContent(asyncKey);

        if (null != content) {
            try {
                if (getBizInvokeCallback() != null) {
                    getBizInvokeCallback().privateTaskFailure(content.getTaskId());
                }
            } catch (RemoteException e) {
                // TODO
            }
        }
    }

    @Override
    public boolean onReceipt(TMMessage message, AbstractAsyncContext context) {
        AddOfflineFileAsyncContent content = (AddOfflineFileAsyncContent) context;

        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateTaskComplete(content.getTaskId());
            }
        } catch (RemoteException e) {
            // TODO
        }

        return true;
    }

    @Override
    public Long request(FileData data) {
        // 首先上传
        long taskId = 0;
        try {
            taskId = addUploadTask(data.getFileName(), FileType.FILE_TYPE_HEAD, this);
            addDefaultTimeoutProcess(IDGenerator.getKeyUseTaskId(taskId), new AddOfflineFileAsyncContent(data.getContactId(), taskId), Long.MAX_VALUE);
        } catch (IOException e) {
            // TODO
        }
        return taskId;
    }

    @Override
    public void onSuccessful(long taskId, String fileId) {
        String asyncKey = IDGenerator.getKeyUseTaskId(taskId);

        AddOfflineFileAsyncContent content = AsyncContent.getContent(IDGenerator.getKeyUseTaskId(taskId));
        operationComplete(asyncKey);

        byte[] send = Messages.AddOfflineFileReq.newBuilder()
                .setTimestamp(0L)
                .setFileId(fileId)
                .setToUserId(content.getUserId())
                .build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_ADD_OFFLINE_FILE, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), content);
    }

    @Override
    public void onFailure(long taskId, Throwable cause) {
        operationComplete(IDGenerator.getKeyUseTaskId(taskId));

        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateTaskFailure(taskId);
            }
        } catch (RemoteException e) {
            // TODO
        }
    }

    @Override
    public void onProcess(long taskId, long total, long complete) {
        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateTaskProcessing(taskId, total, complete);
            }
        } catch (RemoteException e) {
            // TODO
        }
    }

    @Override
    public void onCanceled(long taskId) {
        operationComplete(IDGenerator.getKeyUseTaskId(taskId));
    }

    private static class AddOfflineFileAsyncContent extends AbstractAsyncContext {
        private long taskId;
        private String userId;

        private AddOfflineFileAsyncContent(String userId, long taskId) {
            this.userId = userId;
            this.taskId = taskId;
        }

        @Override
        public Operation getOperation() {
            return Operation.SEND_OFFLINE_FILE;
        }

        @Override
        public String getOperationData() {
            return userId;
        }

        public String getUserId() {
            return userId;
        }

        public long getTaskId() {
            return taskId;
        }
    }
}