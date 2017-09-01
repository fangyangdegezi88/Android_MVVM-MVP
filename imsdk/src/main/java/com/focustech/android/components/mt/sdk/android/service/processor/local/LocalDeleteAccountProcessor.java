package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;

import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;

/**
 * 删除本地账号
 */
public class LocalDeleteAccountProcessor extends AbstractProcessor<String, Void, Void> {
    @Override
    public Void request(final String data) {
        final String key = IDGenerator.getLocalMessageId();

        addDefaultTimeoutProcess(key, Operation.ACCOUNT_DELETE);

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getAccountService().delete(data);
                operationComplete(key);
                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateDeleteAccountSuccessful();
                    }
                } catch (RemoteException e) {
                    // TODO
                }
            }
        });

        return null;
    }
}
