/*******************************************************
 * Copyright (C) 2019 iQIYI.COM - All Rights Reserved
 *
 * This file is part of {cupid_3}.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * Date: 2019/6/28
 * Author(s): zhanglongyun<zhanglongyun@qiyi.com>
 *
 *******************************************************/
package net.unmz.java.util.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JacksonUtils {
    public static void main(String[] args) {


        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root1 = mapper.createObjectNode();
        //key-value节点
        root1.put("key1", 1);
        root1.put("key2", 2);

        //key-array[]数组
        ArrayNode outerArray = mapper.createArrayNode();
        outerArray.add("a");
        outerArray.add("b");
        root1.put("name", outerArray);


        System.out.println(root1.toString());

        String str = "{\"tvId\":\"37867478409\",\"feedId\":\"129523015648\",\"coverUrl\":\"http://pic2.iqiyipic.com/common/20190425/e978c0b058444f248d4fdf956438035d.jpg\",\"duration\":\"30\"}";


    }
}
