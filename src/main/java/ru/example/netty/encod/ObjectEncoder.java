package ru.example.netty.encod;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.example.server.JsonUtil;

import java.io.ByteArrayOutputStream;

/**
 * @author TaylakovSA
 */
public class ObjectEncoder extends MessageToByteEncoder<Object> {

    private final Kryo kryo;

    public ObjectEncoder(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Output output = new Output(outStream, 4096);

        kryo.writeClassAndObject(output, in);
        output.flush();

        byte[] outArray = outStream.toByteArray();
        out.writeShort(outArray.length);
        out.writeBytes(outArray);
//
//        byte[] bytes = JsonUtil.toByteArray(in);
//        out.writeShort(bytes.length);
//        out.writeBytes(bytes);
    }

}