package org.caterpillar.diffknife;

import org.caterpillar.diffknife.model.DiffResult;

import java.io.Serializable;

public class DiffBuilder<E> implements Serializable {

    private DiffConfig config;
    private DiffResult diffResult;

    public DiffBuilder<E> config(DiffConfig config){
        this.config = config;
        return this;
    }

    public DiffBuilder<E> diff(E working, E base){
        // TODO diff 逻辑处理
        return this;
    }

    public DiffResult getDiffResult(){
        return this.diffResult;
    }
}
