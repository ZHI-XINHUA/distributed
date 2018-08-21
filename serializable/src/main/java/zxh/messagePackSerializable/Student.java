package zxh.messagePackSerializable;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import org.msgpack.annotation.Message;

/**
 * Created by xh.zhi on 2018-8-21.
 */
@Message
public class Student {

    private String name;

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
    @Override
    public String toString(){
        return "Student [ name: "+name+", age: "+ age+ " ]";
    }
}
