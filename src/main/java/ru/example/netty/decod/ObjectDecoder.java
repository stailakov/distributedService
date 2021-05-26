package ru.example.netty.decod;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author TaylakovSA
 */
public class ObjectDecoder extends ByteToMessageDecoder {

    private Kryo kryo;
    public ObjectDecoder(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 2)
            return;

        in.markReaderIndex();

        int len = in.readUnsignedShort();

        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }

        byte[] buf = new byte[len];
        in.readBytes(buf);

        Input input = new Input(buf);
        Object object = kryo.readClassAndObject(input);
        out.add(object);
    }
}