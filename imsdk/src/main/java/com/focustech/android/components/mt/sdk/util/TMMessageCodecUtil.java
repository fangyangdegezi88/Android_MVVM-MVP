package com.focustech.android.components.mt.sdk.util;

import com.focustech.tm.components.oneway.util.ByteUtils;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Head;
import com.google.protobuf.InvalidProtocolBufferException;

import org.apache.commons.lang.ArrayUtils;

/**
 * 消息编解码工具类
 *
 * @author zhangxu
 */
public class TMMessageCodecUtil {
    private static final int TAG_HEAD_BODY_LENGTH = 9;

    private TMMessageCodecUtil() {
    }


    public static final TMMessage decode(byte[] data) throws InvalidProtocolBufferException {
        TMMessage message = new TMMessage();

        int headLength = ByteUtils.byte2int(data, 1);
        message.setHead(Head.TMHeadMessage.parseFrom(ArrayUtils.subarray(data, TAG_HEAD_BODY_LENGTH,
                TAG_HEAD_BODY_LENGTH + headLength)));
        message.setBody(ArrayUtils.subarray(data, TAG_HEAD_BODY_LENGTH + headLength, data.length));
        return message;
    }
}
