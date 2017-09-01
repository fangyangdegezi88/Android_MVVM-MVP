package com.focustech.android.components.mt.sdk.android.service.processor.local;

import com.focustech.android.components.mt.sdk.android.db.gen.FriendExt;
import com.focustech.android.components.mt.sdk.android.service.pojo.user.UserBase;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 更新本地用户扩展信息
 */
public class LocalUpdateUserExtProcessor extends AbstractMessageProcessor<UserBase, Void, Void> {
    @Override
    public Void request(final UserBase data) {
        if (null == data.getExt()) {
            return null;
        }

        asyncExecute(new Runnable() {
            @Override
            public void run() {
                String userId = data.getUserId();
                List<FriendExt> exts = new ArrayList<>();

                for (Map.Entry<String, String> entry : data.getExt().getEntry().entrySet()) {
                    FriendExt ext = new FriendExt();
                    ext.setFriendUserId(userId);
                    ext.setName(entry.getKey());
                    ext.setValue(entry.getValue());

                    exts.add(ext);
                }

                getFriendExtService().addOrUpdate(exts);
            }
        });

        return null;
    }
}
