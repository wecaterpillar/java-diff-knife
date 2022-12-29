package org.caterpillar.utilknife;


import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.caterpillar.utilknife.model.DiffResult;

import java.util.List;


/**
 * Diff Util
 * - diff
 */
public class DiffUtil {
    public static DiffResult diff(Object working, Object base){
        return diff(working, base, null);
    }
    public static DiffResult diff(Object working, Object base, Config config){
        DiffResult diffResult = null;
        if(config==null){
            config = Config.DEFAULT_CONFIG;
        }
        // 1. check null
        if(working==null || base==null){
            //
        }
        // 2. check tree object
        // 顶层tree采用TreeUtil，不考虑非顶层的tree

        // 3. 统一转为json处理
        // 假定JSONConfig为默认，待通过config生成新的JSONConfig
        JSONConfig jsonConfig= JSONConfig.create();
        JSONObject workingJson = JSONUtil.parseObj(JSONUtil.toJsonStr(working));
        JSONObject baseJson = JSONUtil.parseObj(JSONUtil.toJsonStr(base));
        diffResult = new JsonDiffBuilder().config(config).diff(workingJson, baseJson).getDiffResult();
        return diffResult;
    }
}
