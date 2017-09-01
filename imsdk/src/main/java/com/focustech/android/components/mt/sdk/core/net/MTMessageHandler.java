package com.focustech.android.components.mt.sdk.core.net;

import com.focustech.android.commonlibs.capability.log.L;
import com.focustech.tm.components.oneway.HandlerAdapter;
import com.focustech.tm.open.sdk.messages.TMMessage;

/**
 * MT 协议消息达到回调
 *
 * @author zhangxu
 */
public abstract class MTMessageHandler extends HandlerAdapter<TMMessage> {
    protected L l = new L(this.getClass().getSimpleName());

}
