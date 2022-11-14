package org.caterpillar.diffknife;


import org.caterpillar.diffknife.model.DiffResult;
import org.caterpillar.diffknife.model.DiffTreeResult;
import org.caterpillar.diffknife.model.TreeObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiffUtil {
    public static DiffResult diff(Object obj1, Object obj2, DiffConfig config){
        DiffResult diffResult = null;
        // 1. check null
        if(obj1==null || obj2==null){
            // TODO null as simple bean
            // 新增或删除
            return  diffResult;
        }
        // 2. check tree object
        if(obj1 instanceof TreeObj && obj2 instanceof TreeObj){
            diffResult = TreeUtil.diff((TreeObj)obj1, (TreeObj)obj2, config).toDiffResult();
            return  diffResult;
        }
        // 3. other
        // TODO 检查类型，处理特别类型
        // 3.1 check object class
        // 3.2 check object data

        // 4. default
        diffResult = new DiffBuilder<Object>().config(config).diff(obj1, obj2).getDiffResult();
        return diffResult;
    }
}
