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

    @Test
    public void testDiffNodeList() {
        List<JSONObject> baseList = new ArrayList<JSONObject>();
        baseList.add(createTreeNode("1", "0"));
        baseList.add(createTreeNode("21", "1"));
        baseList.add(createTreeNode("22", "1"));
        baseList.add(createTreeNode("31", "21"));
        baseList.add(createTreeNode("32", "21"));
        baseList.add(createTreeNode("33", "22"));
        baseList.add(createTreeNode("34", "33"));

        List<JSONObject> workingList = new ArrayList<JSONObject>();
        workingList.add(createTreeNode("1", "0"));
        workingList.add(createTreeNode("21", "1"));
        workingList.add(createTreeNode("22", "1"));
        workingList.add(createTreeNode("31", "22"));
        workingList.add(createTreeNode("35", "22"));
        workingList.add(createTreeNode("41", "35"));
        workingList.add(createTreeNode("42", "35"));

        DiffResult result = TreeUtil.diffNodeList(workingList, baseList);
        System.out.println(result);
//        if (result != null) {
//            Iterator<DiffItem> it = result.iterator();
//            while (it.hasNext()) {
//                DiffItem diffItem = it.next();
//                System.out.println(diffItem);
//            }
//        }
        Assert.assertEquals(result.size(), 4);
    }

    private static JSONObject createTreeNode(String id, String parentId){
        JSONObject node = new JSONObject();
        //tree.idKey = "id";
        //tree.parentIdKey = "parentId";
        //tree.weightKey = "weight";
        //tree.nameKey = "name";
        //tree.childrenKey = "children";
        node.set("id", id);
        node.set("parentId", parentId);
        node.set("name", "node"+id);
        node.set("weight", 0);
        return node;
    }

}