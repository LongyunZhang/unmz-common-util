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
package net.unmz.java.util.json.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", "Lily");

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        JsonArray jsonArray = new JsonArray();

        if (jsonArray.size() == 0) {
            jsonArray.add("a");
            jsonArray.add("b");
            jsonObject.add("ids", jsonArray);
        }

        System.out.println(jsonObject.toString());
    }
}
