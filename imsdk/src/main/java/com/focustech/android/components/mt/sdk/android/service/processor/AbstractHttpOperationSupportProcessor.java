package com.focustech.android.components.mt.sdk.android.service.processor;

import com.focustech.android.commonlibs.capability.request.file.upload.KeyValue;
import com.focustech.android.commonlibs.capability.request.file.upload.RangeUploadTask;
import com.focustech.android.commonlibs.capability.request.file.upload.TaskCallback;
import com.focustech.android.commonlibs.util.TaskUtil;
import com.focustech.android.components.mt.sdk.MTRuntime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象实现
 *
 * @author zhangxu
 */
public abstract class AbstractHttpOperationSupportProcessor<PARAM, RETURN, INNER> extends AbstractProcessor<PARAM, RETURN, INNER> {
    private static final String FIELD_FILE = "file";
    private static final String FIELD_FILE_TYPE = "type";

    protected long addUploadTask(String fileName, String fileType, TaskCallback callback) throws IOException {
        long taskId = TaskUtil.getId();
        List<KeyValue> formData = new ArrayList<>();
        formData.add(new KeyValue(FIELD_FILE_TYPE, fileType));
        asyncExecute(new RangeUploadTask(taskId, MTRuntime.getFileUploadUrl(), fileName, FIELD_FILE, formData, callback));
        return taskId;
    }
}
