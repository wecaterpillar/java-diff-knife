package org.caterpillar.utilknife;

import cn.hutool.json.JSONObject;
import java.io.Serializable;

/**
 * 配置参数
 */
public class Config extends JSONObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static Config DEFAULT_CONFIG = new Config();

    public Config(){
        initDiffConfig(this);
    }

    public static void initDiffConfig(Config config){
        // 1 对比参数配置
        // 1.1 常用对比参数
        // 数据对比深度, 默认对比到基础数据类型
        config.set("diffDeep", 0);
        // 对比结果展示深度，默认第二层（树默认为叶子节点）
        config.set("showDeep", 0);

        //treeMaxDeep
        //jsonMaxDeep

        // 1.2 树对比参数配置
        // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        //config.set("tree.deep", null);
        // 是否忽略排序, 默认忽略
        config.set("ignoreWeight", true);

        // 2 字段参数配置
        // 2.1. 常用属性名配置
        config.set("idKey", "id");
        config.set("nameKey", "name");
        config.set("weightKey", "weight");  //排序字段

        // 2.2 树属性配置
        config.set("parentIdKey", "parentId");
        config.set("childrenKey", "children");
    }

    //////////////////////////////////////////////////////////

    static String getIdKey(JSONObject config){
        return config.getStr("idKey", "id");
    }

    static String getParentIdKey(JSONObject config){
        return config.getStr("parentIdKey", "parentId");
    }

    static String getNameKey(JSONObject config){
        return config.getStr("nameKey", "name");
    }

    static String getWeightKey(JSONObject config){
        boolean ignoreWeight = config.getBool("ignoreWeight", true);
        if(ignoreWeight){
            return "";
        }
        return config.getStr("weightKey", "weight");
    }

    static String getChildrenKey(JSONObject config){
        return config.getStr("childrenKey", "children");
    }

}