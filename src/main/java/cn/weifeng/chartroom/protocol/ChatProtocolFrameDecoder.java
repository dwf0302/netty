package cn.weifeng.chartroom.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ChatProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ChatProtocolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ChatProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
