package org.caterpillar.diffknife;

import cn.hutool.json.JSONObject;
import java.io.Serializable;

/**
 * 对比配置参数
 */
public class DiffConfig extends JSONObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static DiffConfig DEFAULT_CONFIG = new DiffConfig();

    public DiffConfig(){
        initDiffConfig(this);
    }

    public static void initDiffConfig(DiffConfig config){
        // 1 对比参数配置
        // 数据对比深度, 默认对比到基础数据类型
        config.set("diffDeep", 0);
        // 对比结果展示深度，默认第二层（树默认为叶子节点）
        config.set("showDeep", 0);

        // 2 树相关参数配置
        initTreeNodeConfig(config);
    }

    public static void initTreeNodeConfig(DiffConfig config){
        // 1 属性名配置字段
        config.set("tree.idKey", "id");
        config.set("tree.parentIdKey", "parentId");
        config.set("tree.weightKey", "weight");  //排序字段
        config.set("tree.nameKey", "name");
        config.set("tree.childrenKey", "children");
        // 2 对比参数配置
        // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        //config.set("tree.deep", null);
        // 是否忽略排序, 默认忽略
        config.set("tree.ignoreWeight", true);
    }

    //////////////////////////////////////////////////////////

    static String getTreeIdKey(JSONObject config){
        return config.getStr("tree.idKey", "id");
    }

    static String getTreeParentIdKey(JSONObject config){
        return config.getStr("tree.parentIdKey", "parentId");
    }

    static String getTreeNameKey(JSONObject config){
        return config.getStr("tree.nameKey", "name");
    }

    static String getTreeWeightKey(JSONObject config){
        boolean ignoreWeight = config.getBool("tree.ignoreWeight", true);
        if(ignoreWeight){
            return "";
        }
        return config.getStr("tree.weightKey", "weight");
    }

    static String getTreeChildrenKey(JSONObject config){
        return config.getStr("tree.childrenKey", "children");
    }

}