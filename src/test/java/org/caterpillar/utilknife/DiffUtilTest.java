package org.caterpillar.utilknife;

import cn.hutool.json.JSONObject;
import org.caterpillar.utilknife.model.DiffItem;
import org.caterpillar.utilknife.model.DiffResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public class DiffUtilTest {
    @Test
    public void testDiff1() {
        // simple bean

    }

    @Test
    public void testDiff2() {
        // date time
    }

    @Test
    public void testDiff3() {
        // list
    }

    @Test
    public void testDiff4() {
        // map
    }

    @Test
    public void testDiff5() {
        // set
    }

    @Test
    public void testDiff6() {
        // bean
    }

    @Test
    public void testDiff7() {
        // json 一级属性
        JSONObject baseJson = new JSONObject();
        baseJson.set("field1","11");
        baseJson.set("field2","22");
        baseJson.set("field3","33");
        baseJson.set("field4","44");
        baseJson.set("field5","55");

        JSONObject currJson = new JSONObject();
        currJson.putAll(baseJson);
        currJson.set("filed6","new66");
        currJson.set("filed7","new77");
        baseJson.set("field2","change22");
        baseJson.remove("field4");

        DiffResult result = DiffUtil.diff(currJson, baseJson);
        if (result != null) {
            Iterator<DiffItem> it = result.iterator();
            while (it.hasNext()) {
                DiffItem diffItem = it.next();
                System.out.println(diffItem);
            }
        }
        Assert.assertEquals(result.size(), 4);
    }

    @Test
    public void testDiff8() {
        // json 多级属性
    }
}
