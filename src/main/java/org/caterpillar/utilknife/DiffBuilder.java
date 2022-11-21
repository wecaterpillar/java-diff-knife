package org.caterpillar.utilknife;

import org.caterpillar.utilknife.model.DiffResult;

import java.io.Serializable;

public class DiffBuilder<E> implements Serializable {

    protected Config config;
    protected DiffResult diffResult;

    public DiffBuilder(){
        this.config = Config.DEFAULT_CONFIG;
    }

    public DiffBuilder(Config config){
        this.config = config;
        if(this.config==null){
            this.config = Config.DEFAULT_CONFIG;
        }
    }

    public DiffBuilder<E> config(Config config){
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
