package com.focustech.android.commonlibs.capability.request.file.upload;

/**
 * task回调
 *
 * @author zhangxu
 */
public interface TaskCallback {
    /**
     * 任务执行成功
     *
     * @param taskId
     * @param data   任务数据
     */
    void onSuccessful(long taskId, String data);

    /**
     * 任务实行失败
     *
     * @param taskId
     * @param cause
     */
    void onFailure(long taskId, Throwable cause);

    /**
     * 任务执行中
     *
     * @param taskId
     * @param total    总数
     * @param complete 已完成
     */
    void onProcess(long taskId, long total, long complete);

    /**
     * 任务被取消了
     *
     * @param taskId
     */
    void onCanceled(long taskId);
}
