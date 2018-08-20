package src.main.java.zxh.clone ;

import java.io.Serializable;

/**
 * Created by xh.zhi on 2018-8-20.
 */
public class Teacher implements Serializable {
    private static final long serialVersionUID = -5742697700401561522L;

    private String name;

    public Teacher(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

