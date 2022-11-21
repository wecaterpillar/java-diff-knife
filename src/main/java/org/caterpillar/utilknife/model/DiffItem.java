package org.caterpillar.utilknife.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 对比结果明细
 *
 * 记录对比结果，结果方便业务理解
 *
 * 明细内容（忽略展示部分为树结构）
 *
 */
@Data
public class DiffItem  {

    // normal / simple object
    // collection - list/map/set
    // tree
    // bean/json

    public static int ITEM_DEFAULT = 0;
    public static int ITEM_ADD = 1;   //增加数据
    public static int ITEM_DEL = 2;   //删除数据
    public static int VALUE_CHANGE = 3;  //变更数据, 可能包含子层修改
    public static int INDEX_CHANGE = 4;  //更改位置/排序，同层
    public static int TREE_NODE_MOVE = 5;  //树节点移动（非同级，同级移动按更改位置处理）
    public static int TREE_NODE_ADD = 6;   //树节点增加，可能包含子树
    public static int TREE_NODE_DEL = 7;   //树节点删除，可能包含子树

    public DiffItem(){

    }

    public DiffItem(int opType){
        this.opType = opType;
    }

    private long id;
    private int opType;  //操作类型

    // 变更对象或属性
    private String objType;  //对象类型，可选  treeNode, object, property

    private String objKey;   //对象值 节点ID/对象ID/属性key
    private String objLabel; //对象标签，可选

    // 路径
    //private long parentId; //多层对比结果明细项关联到父级
    private String path;  //路径

    // 当前值
    private String currVal;
    private String currLabel;

    // 历史值
    private String baseVal;
    private String baseLabel;

    // 显示时是否忽略
    private boolean isIgnore = false;

    // 明细
    private List<DiffItem> children = new ArrayList<>();
    // 树 新增/删除的明细内容（树包含子节点）
    // 对象 多个属性变化
    // 通常为忽略显示的子层变更
    private String changeDetail;

    public List<DiffItem> getChildren(){
        return this.children;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }


    public String getObjKey() {
        return objKey;
    }

    public void setObjKey(String objKey) {
        this.objKey = objKey;
    }

    public String getObjLabel() {
        return objLabel;
    }

    public void setObjLabel(String objLabel) {
        this.objLabel = objLabel;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCurrVal() {
        return currVal;
    }

    public void setCurrVal(String currVal) {
        this.currVal = currVal;
    }

    public String getCurrLabel() {
        return currLabel;
    }

    public void setCurrLabel(String currLabel) {
        this.currLabel = currLabel;
    }

    public String getBaseVal() {
        return baseVal;
    }

    public void setBaseVal(String baseVal) {
        this.baseVal = baseVal;
    }

    public String getBaseLabel() {
        return baseLabel;
    }

    public void setBaseLabel(String baseLabel) {
        this.baseLabel = baseLabel;
    }

    public boolean isIgnore() {
        return isIgnore;
    }

    public void setIgnore(boolean ignore) {
        isIgnore = ignore;
    }

    public String getChangeDetail() {
        return changeDetail;
    }

    public void setChangeDetail(String changeDetail) {
        this.changeDetail = changeDetail;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DiffItem{");
        sb.append("opType="+opType);
        if(id>0) sb.append(", id=").append(id);
        if(!ObjectUtil.isEmpty(path)) sb.append(", path=").append(path);
        if(!ObjectUtil.isEmpty(objType)) sb.append(", objType=").append(objType);
        //obj label
        String object = getItemLabel(objKey, objLabel);
        if(!ObjectUtil.isEmpty(object)) sb.append(", obj=").append(object);
        //base
        String base = getItemLabel(baseVal, baseLabel);
        if(ObjectUtil.isNotEmpty(base)) sb.append(", base=").append(base);
        //curr
        String curr = getItemLabel(currVal, currLabel);
        if(ObjectUtil.isNotEmpty(curr)) sb.append(", curr=").append(curr);
        // children
        if(CollectionUtil.isNotEmpty(children)){
            sb.append("children[");
            for(DiffItem item: children){
                sb.append(item.toString());
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String getItemLabel(String key, String label){
        StringBuffer sb = new StringBuffer();
        if(!ObjectUtil.isEmpty(label)){
            sb.append(label);
            if(!ObjectUtil.isEmpty(key) && !key.equals(label)){
                sb.append("(").append(key).append(")");
            }
        }else{
            sb.append(key);
        }
        return sb.toString();
    }
}