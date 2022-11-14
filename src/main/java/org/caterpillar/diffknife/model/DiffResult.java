package org.caterpillar.diffknife.model;

import java.util.ArrayList;

/**
 * 对比差异结果
 */
public class DiffResult extends ArrayList<DiffItem> {
    public void addDiff(DiffItem diffItem){
        if(diffItem!=null){
            this.add(diffItem);
        }
    }
}
