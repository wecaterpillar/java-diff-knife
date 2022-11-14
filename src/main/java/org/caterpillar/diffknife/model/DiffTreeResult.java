package org.caterpillar.diffknife.model;

public class DiffTreeResult extends DiffResult {

    public static DiffResult toDiffResult(DiffTreeResult diffTreeResult) {
        if(diffTreeResult==null){
            return null;
        }
        return diffTreeResult.toDiffResult();
    }

    public DiffResult toDiffResult(){
        // TODO 转换以及消噪
        return this;
    }
}
