package zxh.baiduProtoBufSerializable;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by xh.zhi on 2018-8-21.
 */
public class Student {
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
    @Override
    public String toString(){
        return "Student [ name: "+name+", age: "+ age+ " ]";
    }
}
