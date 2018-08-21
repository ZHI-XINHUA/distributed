package zxh.baiduProtoBufSerializable;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import java.io.IOException;


public class ProtoBuf {
    public static void main(String[] args) throws IOException {
        Student student = new Student();
        student.setName("james");
        student.setAge(33);


        Codec<Student> codec = ProtobufProxy.create(Student.class,false);
        //序列化
        byte[] bytes = codec.encode(student);

        //反序列化
        Student student1 = codec.decode(bytes);
        System.out.println(student1);

    }
}

