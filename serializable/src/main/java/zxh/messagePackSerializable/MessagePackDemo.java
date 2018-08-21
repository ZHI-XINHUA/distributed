package zxh.messagePackSerializable;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xh.zhi on 2018-8-21.
 */
public class MessagePackDemo {
    public static void main(String[] args) throws IOException {
        // 创建被序列化对象
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");

        //创建MessagePack对象
        MessagePack messagePack  = new MessagePack();

        //序列化
        byte[] bytes = messagePack.write(src);

        //反序列化方式一：直接使用模板反序列化
        List<String> list = messagePack.read(bytes, Templates.tList(Templates.TString));
        for (String s: list) {
            System.out.println(s);
        }

        //反序列化方式二：反序列化为Value，然后转换类型
        Value dynamic  =  messagePack.read(bytes);
        List<String> list1 =  new Converter(dynamic).read(Templates.tList(Templates.TString));
        for (String s: list1) {
            System.out.println(s);
        }


        test1();
    }

    public static void test1() throws IOException {
        Student student = new Student();
        student.setName("james");
        student.setAge(33);

        //创建MessagePack对象
        MessagePack messagePack  = new MessagePack();

        //序列化
        byte[] bytes = messagePack.write(student);

        Student student1 = messagePack.read(bytes,Student.class);
        System.out.println(student1.toString());

    }


}
