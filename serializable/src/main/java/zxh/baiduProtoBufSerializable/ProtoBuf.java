package zxh.baiduProtoBufSerializable;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;
import java.io.Serializable;


public class ProtoBuf {
    public static void main(String[] args) throws IOException {
        Student student = new Student();
        student.setName("james");
        student.setAge(33);

        Codec<Student> codec = ProtobufProxy.create(Student.class,false);
        byte[] bytes = codec.encode(student);

        Student student1 = codec.decode(bytes);
        System.out.println(student1);
    }
}

class Student  implements Serializable {
    private static final long serialVersionUID = -2572627257192440745L;

    @Protobuf(fieldType = FieldType.STRING,order = 1)
    private String name;

    @Protobuf(fieldType = FieldType.INT32,order = 2)
    private int age;

    public Student(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String toString(){
        return "Student [ name: "+name+", age: "+ age+ " ]";
    }
}
