package net.test;

import net.unmz.java.util.http.HttpUtils;
import org.junit.Test;

/**
 * Created by longyun on 2019/5/30.
 */
public class HttpTest {

    @Test
    public void test() throws Exception {
        String url = "http://www.baidu.com";
        String res = HttpUtils.doGet(url);
        System.out.println(res);
    }
}
