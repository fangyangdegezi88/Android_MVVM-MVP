package com.focustech.android.components.mt.sdk.util;

import java.util.UUID;

/**
 * 异步Key
 *
 * @author zhangxu
 */
public class IDGenerator {
    private IDGenerator() {
    }

    public static String getKeyUseCliSeqId(int cliSeqId) {
        return String.valueOf(cliSeqId);
    }

    public static String getKeyUseTaskId(long taskId) {
        return "task-" + taskId;
    }

    public static String getLocalMessageId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
