package com.focustech.android.components.mt.sdk.android;

import android.content.Context;

import com.focustech.android.components.mt.sdk.android.service.MessageService;

/**
 * Android Context Holder
 *
 * @author zhangxu
 */
public class ContextHolder {
    private static Context context;
    private static MessageService messageService;

    private ContextHolder() {

    }

    public static MessageService getMessageService() {
        return messageService;
    }

    public static void setMessageService(MessageService messageService) {
        ContextHolder.messageService = messageService;
    }

    public static void setAndroidContext(Context context) {
        ContextHolder.context = context;
    }

    public static Context getAndroidContext() {
        return ContextHolder.context;
    }

    public static void clear() {
        ContextHolder.context = null;
        ContextHolder.messageService = null;
    }
}
