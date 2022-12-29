package org.caterpillar.utilknife;

import cn.hutool.json.JSONObject;
import org.caterpillar.utilknife.model.DiffItem;
import org.caterpillar.utilknife.model.DiffResult;

import java.util.Set;

public class JsonDiffBuilder extends DiffBuilder<JSONObject> {

    public JsonDiffBuilder() {
        super();
    }

    public JsonDiffBuilder(Config config) {
        super(config);
    }

    public DiffBuilder diff(JSONObject workingJson, JSONObject baseJson) {
        // 默认只比较一层
        DiffResult diffResult = new DiffResult();

        Set<String> baseKeySet = baseJson.keySet();
        Set<String> workingKeySet = workingJson.keySet();
        DiffItem diffItem = null;
        for (String key : baseKeySet) {
            if (workingJson.containsKey(key)) {
                if (!baseJson.getStr(key, "").equals(workingJson.getStr(key, ""))) {
                    //修改属性
                    diffItem = new DiffItem(DiffItem.VALUE_CHANGE);
                    diffItem.setObjType("property");
                    diffItem.setObjKey(key);
                    diffItem.setCurrVal(workingJson.getStr(key));
                    diffItem.setBaseVal(baseJson.getStr(key));
                    diffResult.addDiff(diffItem);
                }
            } else {
                //被删除了
                diffItem = new DiffItem(DiffItem.ITEM_DEL);
                diffItem.setObjType("property");
                diffItem.setObjKey(key);
                diffItem.setBaseVal(baseJson.getStr(key));
                diffResult.addDiff(diffItem);
            }
        }
        for (String key : workingKeySet) {
            if (!baseJson.containsKey(key)) {
                //新增的
                diffItem = new DiffItem(DiffItem.ITEM_ADD);
                diffItem.setObjType("property");
                diffItem.setObjKey(key);
                diffItem.setCurrVal(baseJson.getStr(key));
                diffResult.addDiff(diffItem);
            }
        }
        this.diffResult = diffResult;
        return this;
    }
}