package org.caterpillar.diffknife;

import org.caterpillar.diffknife.model.DiffResult;
import org.caterpillar.diffknife.model.DiffTreeResult;
import org.caterpillar.diffknife.model.TreeObj;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeUtilTest {

    @Test
    public void testDiff() {
        List<Map> list1 = new ArrayList<Map>();
        List<Map> list2 = new ArrayList<Map>();

        DiffResult result = TreeUtil.diff(list1, list2);
        Assert.assertEquals(result, new DiffResult());
    }


    @Test
    public void testDiff3() {
        DiffTreeResult result = TreeUtil.diff(new TreeObj(), new TreeObj(), new DiffConfig());
        Assert.assertEquals(result, new DiffTreeResult());
    }

    @Test
    public void testListToTree() {
        //TreeObj result = TreeUtil.listToTree(List.of(Map.of("String", "String")), null);
        //Assert.assertEquals(result, new TreeObj());
    }
}