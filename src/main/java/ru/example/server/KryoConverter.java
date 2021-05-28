package ru.example.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;

/**
 * @author TaylakovSA
 */
public class KryoConverter {

    private Kryo kryo;

    public KryoConverter() {
        this.kryo = new Kryo();
    }

    public Object toObject(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(bytes);
        Input input = new Input(bytes);
        return kryo.readClassAndObject(input);
    }

    public ByteBuf toByteBuf(Object object) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Output output = new Output(outStream, 4096);

        kryo.writeClassAndObject(output, object);
        output.flush();

        byte[] outArray = outStream.toByteArray();
        return Unpooled.wrappedBuffer(outArray);
    }
}
