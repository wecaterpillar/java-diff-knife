package org.caterpillar.diffknife.model;

import java.util.ArrayList;

/**
 * 对比差异结果
 *
 * 差异记录方案：
 * 方案一： 完整层次记录
 * 方案二； 确认一次有效层级（可能是某种类型或者某个层次）
 * 如何确认有效的记录层次，确认好记录颗粒度后，在其上层次打平到该层，其下层次计入详细信息。
 *
 *
 */
public class DiffResult extends ArrayList<DiffItem> {
    public void addDiff(DiffItem diffItem){
        if(diffItem!=null){
            this.add(diffItem);
        }
    }
}
