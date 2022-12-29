package org.caterpillar.utilknife;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.caterpillar.utilknife.model.DiffItem;
import org.caterpillar.utilknife.model.DiffResult;

import java.io.Serializable;

@Slf4j
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
        return this;
    }



    public String getId(E item){
        Object idValue =BeanUtil.getFieldValue(item, Config.getIdKey(config));
        if(idValue!=null){
            return  String.valueOf(idValue);
        }
        return null;
    }

    public String getName(E item){
        Object nameValue =BeanUtil.getFieldValue(item, Config.getNameKey(config));
        if(nameValue!=null){
            return  String.valueOf(nameValue);
        }
        return null;
    }

    String getStrField(E item, String filedName, String defaultValue){
        String fieldValue = defaultValue;
        try{
            Object value =BeanUtil.getFieldValue(item, filedName);
            if(value!=null){
                fieldValue =  String.valueOf(value);
            }
        }catch (Exception e){
            fieldValue = defaultValue;
        }
        return fieldValue;
    }

    public DiffResult getDiffResult(){
        if(this.diffResult==null){
            this.diffResult = new DiffResult();
        }
        return this.diffResult;
    }
    protected void addDiff(DiffItem diffItem){
        if(this.diffResult==null){
            this.diffResult = new DiffResult();
        }
        this.diffResult.add(diffItem);
    }
}
