package org.caterpillar.diffknife;

import org.caterpillar.diffknife.model.DiffResult;
import org.caterpillar.diffknife.model.DiffTreeResult;
import org.caterpillar.diffknife.model.TreeObj;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TreeUtil {

    /**
     * 树对比
     * map包含树必要字段：id和parentId
     * @param list1 列表形式的树数据
     * @param list2 列表形式的树数据
     * @return
     */
    public static DiffResult diff(List<Map> list1, List<Map> list2){
        return diff(list1, list2, null);
    }

    public static DiffResult diff(List<Map> list1, List<Map> list2, DiffConfig config){
        if(config==null){
            config = DiffConfig.DEFAULT_CONFIG;
        }
        TreeObj tree1 = TreeUtil.listToTree(list1, config);
        TreeObj tree2 = TreeUtil.listToTree(list2, config);
        DiffTreeResult diffTreeResult = diff(tree1, tree2, config);
        return DiffTreeResult.toDiffResult(diffTreeResult);
    }

    public static DiffTreeResult diff(TreeObj tree1, TreeObj tree2, DiffConfig config){
        //return new DiffBuilder<TreeObj>().config(config).diff(tree1, tree2).getDiffResult();
        return null;
    }

    public static TreeObj listToTree(List<Map> list, Properties config) {

        return null;
    }
}
