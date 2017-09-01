package com.focustech.android.components.mt.sdk.android.service.processor.msg;


import com.focustech.android.components.mt.sdk.android.service.CMD;
import com.focustech.android.components.mt.sdk.android.service.processor.AbstractMessageProcessor;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Messages;

/**
 * 键盘输入
 *
 * @author zhangxu
 */
public class KeyboardInputMessageProcessor extends AbstractMessageProcessor<String, Void, Void> {
    @Override
    public void onMessage(TMMessage message) throws Throwable {
        if (getBizInvokeCallback() != null) {
            getBizInvokeCallback().privateKeyboardInputMessage(Messages.KeyboardInputMessage.parseFrom(message.getBody()).getUserId());
        }
    }

    @Override
    public Void request(String userId) {
        getMessageService().sendMessage(CMD.KEYBOARD_INPUT_MESSAGE
                , Messages.KeyboardInputMessage.newBuilder().setUserId(userId).build().toByteArray());

        return null;
    }
}
