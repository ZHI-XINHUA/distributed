package src.main.java.zxh.clone;

import java.io.*;

/**
 * Created by xh.zhi on 2018-8-20.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 6680172788206952051L;

    private String no;

    private String name;

    private Teacher teacher;

    /**
     * 深度复制
     * @return
     * @throws Exception
     */
    public Object deepClone() throws Exception{
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(baos);
        outputStream.writeObject(this);

        //反序列化
        ByteArrayInputStream bain = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream inputStream = new ObjectInputStream(bain);
        return  inputStream.readObject();
    }

    public Student(String no,String name,Teacher teacher){
        this.name = name;
        this.no = no;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Student{" +
                "no='" + no + '\'' +
                ", name='" + name + '\'' +
                ", teacher=" + teacher +
                '}';
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
