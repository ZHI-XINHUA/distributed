package zxh.hessianSerializable;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Hessian {
    public static void main(String[] args) throws IOException {

        Student student = new Student();
        student.setName("james");
        student.setAge(33);

        //保存序列化对象后的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //创建hessian输出流
        HessianOutput ho = new HessianOutput(bos);
        //序列化对象
        ho.writeObject(student);


        //创建hessian输入流，并读取保存序列化对象的字节数组
        HessianInput io = new HessianInput(new ByteArrayInputStream(bos.toByteArray()));
        //反序列化对象
        Student student1 = (Student) io.readObject();
        System.out.println(student1);
    }
}

/**
 * Hessian序列化的对象要实现Serializable接口
 */
class Student implements Serializable {
    private static final long serialVersionUID = 6680172788206952054L;
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
