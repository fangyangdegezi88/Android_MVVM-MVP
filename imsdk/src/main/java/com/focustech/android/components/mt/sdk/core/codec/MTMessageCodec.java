package com.focustech.android.components.mt.sdk.core.codec;

import com.focustech.android.components.mt.sdk.MTRuntime;
import com.focustech.tm.components.oneway.CustomCodec;
import com.focustech.tm.components.oneway.net.codec.HeadAndBodyBasedMessage;
import com.focustech.tm.open.sdk.messages.TMMessage;
import com.focustech.tm.open.sdk.messages.protobuf.Head;
import com.focustech.tm.open.sdk.util.BitEncrypt;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * 麦通消息编解码器
 *
 * @author zhangxu
 */
public class MTMessageCodec implements CustomCodec<HeadAndBodyBasedMessage, TMMessage> {
    private static final MTMessageCodec INSTANCE = new MTMessageCodec();

    private MTMessageCodec() {

    }

    public static MTMessageCodec getInstance() {
        return INSTANCE;
    }

    @Override
    public List<byte[]> encode(TMMessage message) {
        List<byte[]> value = new ArrayList<>();
        value.add(message.getHead().toByteArray());
        if (MTRuntime.optionsEncodeEnable()) {
            value.add(BitEncrypt.encode(message.getBody(), MTRuntime.optionsEncodeKey()));
        } else {
            value.add(message.getBody());
        }

        return value;
    }

    @Override
    public TMMessage decode(HeadAndBodyBasedMessage object) {
        TMMessage message = new TMMessage();
        try {
            message.setHead(Head.TMHeadMessage.parseFrom(object.getHead()));
            if (MTRuntime.optionsEncodeEnable()) {
                message.setBody(BitEncrypt.decode(object.getBody(), MTRuntime.optionsEncodeKey()));
            } else {
                message.setBody(object.getBody());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return message;
    }
}
