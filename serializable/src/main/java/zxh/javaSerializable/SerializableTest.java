package src.main.java.zxh.javaSerializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xh.zhi on 2018-8-20.
 */
public class SerializableTest {
    public static void main(String[] args) throws Exception{
        //序列化
        serialize();

        //反deserialize
        deserialize();
    }

    /**
     * 序列化
     */
    public static void serialize() throws Exception{
        Person p = new Person("james",30,0);
        //序列化对象输出到本地文件中
        FileOutputStream fileOutputStream = new FileOutputStream("person.txt");
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        //写到流中
        outputStream.writeObject(p);
    }

    /**
     * 反序列化
     */
    public static void deserialize() throws Exception{
        //获取已序列化的文件
        FileInputStream fileInputStream = new FileInputStream("person.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        //读取流
        Person person = (Person) objectInputStream.readObject();
        System.out.println(person.toString());
    }
}
