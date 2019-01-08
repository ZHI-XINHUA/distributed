package zxh.forjava;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xh.zhi on 2018-12-29.
 */
public class MongoClientTestTest {
    MongoClientTest client;
    @Before
    public void setUp() throws Exception {
        client = new MongoClientTest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        client.test();
    }

}