package com.focustech.android.components.mt.sdk.android.service.processor.req;

import android.os.RemoteException;

import com.focustech.android.commonlibs.capability.request.file.upload.TaskCallback;
import com.focustech.android.components.mt.sdk.FileType;
import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.pojo.UserHeadData;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractHttpOperationSupportProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

import java.io.IOException;

/**
 * 请求更新用户头像
 *
 * @author zhangxu
 */
public class ReqUpdateUserHeadProcessor extends AbstractHttpOperationSupportProcessor<UserHeadData, Long, Void> implements TaskCallback {
    @Override
    public Long request(UserHeadData data) {
        long value = 0;

        // 系统头像
        if (data.getHeadType() == Messages.HeadType.SYSTEM) {
            doUpdate(data.getHeadType(), data.getUserHeadId());
        } else {
            try {
                value = processCustomHead(data);
            } catch (IOException e) {
                // TODO
            }
        }

        return value;
    }

    @Override
    public void onSuccessful(long taskId, String fileId) {
        operationComplete(IDGenerator.getKeyUseTaskId(taskId));
        // 继续处理
        doUpdate(Messages.HeadType.CUSTOM, fileId);
    }

    @Override
    public void onFailure(long taskId, Throwable cause) {
        // TODO 日志
        try {
            operationComplete(IDGenerator.getKeyUseTaskId(taskId));
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateTaskFailure(taskId);
            }
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onProcess(long taskId, long total, long complete) {
        try {
            if (getBizInvokeCallback() != null) {
                getBizInvokeCallback().privateTaskProcessing(taskId, total, complete);
            }
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onCanceled(long taskId) {
        operationComplete(IDGenerator.getKeyUseTaskId(taskId));
    }

    private long processCustomHead(UserHeadData data) throws IOException {
        // 首先上传
        return addUploadTask(data.getFileName(), FileType.FILE_TYPE_HEAD, this);
    }

    private void doUpdate(Messages.HeadType headType, String fileId) {
        byte[] send = Messages.UserHeadReq.newBuilder().setUserHeadType(headType).setUserHeadId(fileId).build().toByteArray();
        int cliSeqId = getMessageService().sendMessage(CMD.REQ_UPDATE_USER_HEAD, send);
        addDefaultTimeoutProcess(IDGenerator.getKeyUseCliSeqId(cliSeqId), Operation.UPDATE_USER_HEAD);
    }
}
