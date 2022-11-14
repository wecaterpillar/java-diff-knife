package org.caterpillar.diffknife.model;

import lombok.Data;

/**
 * 对比结果明细
 *
 * 记录对比结果，结果方便业务理解
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


    public DiffItem(){

    }

    public DiffItem(int opType){
        this.opType = opType;
    }

    private long id;
    private int opType;  //操作类型

    // 变更对象或属性
    private String objType;  //对象类型，可选
    private String objVal;   //对象值
    private String objLabel; //对象标签，可选

    // 父级或路径
    private long parentId; //多层对比结果明细项关联到父级
    private String path;  //路径

    // 当前值
    private String currVal;
    private String currLabel;
    // 历史值
    private String baseVal;
    private String baseLabel;
    // 是否忽略
    private boolean isIgnore = false;


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

    public String getObjVal() {
        return objVal;
    }

    public void setObjVal(String objVal) {
        this.objVal = objVal;
    }

    public String getObjLabel() {
        return objLabel;
    }

    public void setObjLabel(String objLabel) {
        this.objLabel = objLabel;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
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

    @Override
    public String toString() {
        return "DiffItem{" +
                "id=" + id +
                ", opType=" + opType +
                ", objType='" + objType + '\'' +
                ", objVal='" + objVal + '\'' +
                ", objLabel='" + objLabel + '\'' +
                ", parentId=" + parentId +
                ", path='" + path + '\'' +
                ", currVal='" + currVal + '\'' +
                ", currLabel='" + currLabel + '\'' +
                ", baseVal='" + baseVal + '\'' +
                ", baseLabel='" + baseLabel + '\'' +
                ", isIgnore=" + isIgnore +
                '}';
    }
}
