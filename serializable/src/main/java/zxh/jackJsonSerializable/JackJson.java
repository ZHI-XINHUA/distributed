package src.main.java.zxh.jackJsonSerializable;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JackJson {

    public static void main(String[] args) throws IOException {
        Student student = new Student();
        student.setName("james");
        student.setAge(33);


        ObjectMapper mapper = new ObjectMapper();
        //序列化对象
        byte[] bytes = mapper.writeValueAsBytes(student);
        //反序列化对象
        Student student1 = mapper.readValue(bytes,Student.class);
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
