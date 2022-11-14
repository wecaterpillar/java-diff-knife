package org.caterpillar.diffknife;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

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

        // 2 树相关参数配置
        initTreeNodeConfig(config);
    }

    public static void initTreeNodeConfig(DiffConfig config){
        // TODO 需要初始化为tree.xx 格式
        config.set("tree", TreeNodeConfig.DEFAULT_CONFIG);
        // 属性名配置字段
        //tree.idKey = "id";
        //tree.parentIdKey = "parentId";
        //tree.weightKey = "weight";
        //tree.nameKey = "name";
        //tree.childrenKey = "children";
        // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        //tree.deep = ;
    }

}
