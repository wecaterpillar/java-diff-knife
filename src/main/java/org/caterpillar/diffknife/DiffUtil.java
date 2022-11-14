package org.caterpillar.diffknife;


import org.caterpillar.diffknife.model.DiffResult;


public class DiffUtil {
    public static DiffResult diff(Object working, Object base, DiffConfig config){
        DiffResult diffResult = null;
        // 1. check null
        if(working==null || base==null){
            // TODO null as simple bean
            // 新增或删除
            diffResult = new DiffBuilder<Object>().config(config).diff(working, base).getDiffResult();
            return  diffResult;
        }
        // 2. check tree object

        // 3. other
        // TODO 检查类型，处理特别类型
        // 3.1 check object class
        // 3.2 check object data

        // 4. default
        diffResult = new DiffBuilder<Object>().config(config).diff(working, base).getDiffResult();
        return diffResult;
    }
}
