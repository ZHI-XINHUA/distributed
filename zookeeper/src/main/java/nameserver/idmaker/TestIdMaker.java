package nameserver.idmaker;

import java.util.concurrent.Executors;

/**
 * Created by xh.zhi on 2018-11-12.
 */
public class TestIdMaker {
    public static void main(String[] args) throws Exception{
        IDMaker idMaker = new IDMaker("192.168.3.31:2181",
                "/NameService/IdGen", "ID");

        idMaker.start();

        try {
            for (int i = 0; i < 12; i++) {
                String id = idMaker.generateId(IDMaker.RemoveMethod.IMMEDIATELY);
                System.out.println(id);

            }
        } finally {
            idMaker.stop();

        }
    }
}
