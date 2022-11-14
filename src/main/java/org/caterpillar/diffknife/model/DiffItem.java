package org.caterpillar.diffknife.model;

/**
 * 对比结果明细
 *
 * 记录对比结果，结果方便业务理解
 *
 */
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
    private String curVal;
    private String curLabel;
    // 历史值
    private String preVal;
    private String preLabel;
    // 是否忽略
    private boolean isIgnore = false;

}
