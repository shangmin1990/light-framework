package net.shmin.core.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 下午6:04
 * @Description:
 */
public class SerializationUtil {

    private SerializationUtil(){

    }

//    private static Kryo kryo = new Kryo();

    public static byte[] serializeByKryo(Object o){
        Assert.notNull(o);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = new Kryo();
        kryo.writeObject(output, o);
        byte[] result = output.getBuffer();
        output.close();
        return result;
    }


    public static <T> T deserializeByKryo(byte[] bytes, Class<T> clazz){
        Assert.notNull(bytes);
        Assert.isTrue(bytes.length > 0);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = new Kryo();
        T t = kryo.readObject(input, clazz);
        input.close();
        return t;
    }

    public static void main(String[] args) {
        Integer i = new Integer(3);

        byte[] bytes = serializeByKryo(i);
        System.out.println(bytes);

        Integer a = deserializeByKryo(bytes, Integer.class);

        System.out.println(a == 3);
    }
}
