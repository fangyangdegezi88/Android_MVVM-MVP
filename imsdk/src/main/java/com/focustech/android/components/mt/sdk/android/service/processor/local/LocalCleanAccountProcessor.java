package com.focustech.android.components.mt.sdk.android.service.processor.local;

import android.os.RemoteException;

import com.focustech.android.components.mt.sdk.android.service.Operation;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractProcessor;
import com.focustech.android.components.mt.sdk.util.IDGenerator;

/**
 * 清除本地账号
 */
public class LocalCleanAccountProcessor extends AbstractProcessor<Void, Void, Void> {
    @Override
    public Void request(Void data) {
        final String key = IDGenerator.getLocalMessageId();

        addDefaultTimeoutProcess(key, Operation.ACCOUNT_CLEAN);

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                getAccountService().clean();
                operationComplete(key);
                try {
                    if (getBizInvokeCallback() != null) {
                        getBizInvokeCallback().privateCleanAccountsSuccessful();
                    }
                } catch (RemoteException e) {
                    // TODO
                }
            }
        });

        return null;
    }
}
