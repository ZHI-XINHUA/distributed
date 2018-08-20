package src.main.java.zxh.javaSerializable;

import java.io.Serializable;

/**
 * Created by xh.zhi on 2018-8-20.
 *  序列化：
 *   1、实现Serializable接口
 *   2、指定serialVersionUID 版本，不指定容易出现不同版本导致反序列化失败
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 6680172788206952052L;

    private String name;

    private int age;

    //transient修饰：不进行序列化操作
    private transient int sex;

    public Person(String name, int age, int sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
