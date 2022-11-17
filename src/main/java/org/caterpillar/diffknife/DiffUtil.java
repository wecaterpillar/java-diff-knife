package org.caterpillar.diffknife;


import org.caterpillar.diffknife.model.DiffResult;


public class DiffUtil {
    public static DiffResult diff(Object working, Object base){
        return diff(working, base, null);
    }
    public static DiffResult diff(Object working, Object base, DiffConfig config){
        DiffResult diffResult = null;
        if(config==null){
            config = DiffConfig.DEFAULT_CONFIG;
        }
        // 1. check null
        if(working==null || base==null){
            // TODO null as simple bean
            // 新增或删除
            diffResult = new DiffBuilder<Object>(config).diff(working, base).getDiffResult();
            return  diffResult;
        }
        // 2. check tree object
        // 如何判断是树对比？

        // 3. other
        // TODO 检查类型，处理特别类型
        // 3.1 check object class
        // 3.2 check object data

        // 4. default
        diffResult = new DiffBuilder<Object>(config).diff(working, base).getDiffResult();
        return diffResult;
    }
}
