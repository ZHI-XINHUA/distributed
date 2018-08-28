package rest_spring_cxf;

import java.util.Arrays;
import java.util.List;

public class Storage {

    //模拟数据库存储
    public static List<User> users = Arrays.asList(
            new User(1,"james"),
            new User(2,"kobe"),
            new User(3,"jorden")
    );
}
