package netty.mytomcat.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xh.zhi on 2018-9-25.
 * 加载自定义配置
 */
public class CustomConfig {

    private static Map<String,String> ctx;

    public CustomConfig(String ... props){
        for(String path : props){
            Properties p = new Properties();

            try {
                p.load(CustomConfig.class.getClassLoader().getResourceAsStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载properties文件
     * @param props
     */
    public static void load(String ... props){
        new CustomConfig(props);
    }
}
