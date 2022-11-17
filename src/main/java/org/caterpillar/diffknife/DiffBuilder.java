package org.caterpillar.diffknife;

import org.caterpillar.diffknife.model.DiffResult;

import java.io.Serializable;

public class DiffBuilder<E> implements Serializable {

    protected DiffConfig config;
    protected DiffResult diffResult;

    public DiffBuilder(){
        this.config = DiffConfig.DEFAULT_CONFIG;
    }

    public DiffBuilder(DiffConfig config){
        this.config = config;
        if(this.config==null){
            this.config = DiffConfig.DEFAULT_CONFIG;
        }
    }

    public DiffBuilder<E> config(DiffConfig config){
        if(config!=null){
            this.config = config;
        }
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
