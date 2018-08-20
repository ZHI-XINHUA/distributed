package zxh.fastJsonSerializable;

import com.alibaba.fastjson.JSON;

public class FastJson {

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("james");
        student.setAge(33);

        //对象转字符串（序列化）
        String jsonSerializ = JSON.toJSONString(student);
        System.out.println(jsonSerializ);

        //json字符串转对象(反序列化)
        Student student1 = JSON.parseObject(jsonSerializ,Student.class);
        System.out.println(student1);
    }
}

class Student {
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
    public String toString(){
        return "Student [ name: "+name+", age: "+ age+ " ]";
    }
}
