package src.main.java.zxh.clone;

/**
 * Created by xh.zhi on 2018-8-20.
 */
public class CloenTest {


    public static void main(String[] args) throws  Exception{
        Student student = new Student("no1","lbj",new Teacher("jordon"));
        System.out.println(student.toString());
        Student student1 = (Student) student.deepClone();
        student1.getTeacher().setName("jaber");
        System.out.println(student1.toString());
    }
}
