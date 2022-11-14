package org.caterpillar.diffknife;

import cn.hutool.json.JSONObject;
import org.caterpillar.diffknife.model.DiffItem;
import org.caterpillar.diffknife.model.DiffResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeUtilTest {

    private static JSONObject createNode(String id, String parentId, String name, int index){
        JSONObject node = new JSONObject();
        //tree.idKey = "id";
        //tree.parentIdKey = "parentId";
        //tree.weightKey = "weight";
        //tree.nameKey = "name";
        //tree.childrenKey = "children";
        node.set("id", id);
        node.set("parentId", parentId);
        node.set("name", name);
        node.set("weight", index);
        return node;
    }

    @Test
    public void testDiff() {
        List<JSONObject> working = new ArrayList<JSONObject>();
        List<JSONObject> base = new ArrayList<JSONObject>();

        JSONObject item1 = createNode("1","0","1", 0);
        JSONObject item21 = createNode("21","1","21", 0);
        JSONObject item22 = createNode("22","1","22", 0);
        JSONObject item31 = createNode("31","21","31", 0);
        JSONObject item32 = createNode("32","21","32", 0);
        JSONObject item33 = createNode("33","22","33", 0);
        JSONObject item34 = createNode("34","22","34", 0);
        working.add(item1);
        working.add(item21);
        working.add(item31);
        base.add(item1);
        base.add(item21);
        base.add(item22);
        DiffResult result = TreeUtil.diff(working, base);
        if (result != null) {
            Iterator<DiffItem> it = result.iterator();
            while (it.hasNext()) {
                DiffItem diffItem = it.next();
                System.out.println(diffItem);
            }
        }
        Assert.assertEquals(result.size(), 2);
    }

}