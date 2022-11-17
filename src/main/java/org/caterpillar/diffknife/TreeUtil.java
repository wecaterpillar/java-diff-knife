package org.caterpillar.diffknife;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.caterpillar.diffknife.model.DiffItem;
import org.caterpillar.diffknife.model.DiffResult;

import java.util.*;

public class TreeUtil {

    public static DiffResult diffNodeList(List<JSONObject> workingNodeList, List<JSONObject> baseNodeList) {
        return diffNodeList(workingNodeList, baseNodeList, null);
    }

    public static DiffResult diffNodeList(List<JSONObject> workingNodeList, List<JSONObject> baseNodeList, DiffConfig config) {
        DiffResult diffResult = new DiffResult();
        // 1. prepare, config
        if (config == null) {
            config = DiffConfig.DEFAULT_CONFIG;
        }

        String treeIdKey = DiffConfig.getTreeIdKey(config);
        String treeParentIdKey = DiffConfig.getTreeParentIdKey(config);
        String treeNameKey = DiffConfig.getTreeNameKey(config);
        // 对比两颗树结构变化，先对比节点，数量找到新增和删除的部分，再对比相同节点父节点改变的部分。


        Map<String, JSONObject> mBaseNode = new HashMap<>();
        for (JSONObject json : baseNodeList) {
            mBaseNode.put(json.getStr(treeIdKey), json);
        }

        Map<String, JSONObject> mWorkingNode = new HashMap<>();
        for (JSONObject json : workingNodeList) {
            mWorkingNode.put(json.getStr(treeIdKey), json);
        }

        // 2. 对比 current vs base
        List<JSONObject> newNodes = new ArrayList<>();
        List<JSONObject> delNodes = new ArrayList<>();
        List<DiffItem> moveItems = new ArrayList<>();

        Set<String> setSameNode = new HashSet<>();
        for (JSONObject json : workingNodeList) {
            String id = json.getStr(treeIdKey);
            if (mBaseNode.containsKey(id)) {
                // 相同节点
                setSameNode.add(id);
                // 检查移动父节点，忽略排序
                String baseParentId = mBaseNode.get(id).getStr(treeParentIdKey, "");
                String currParentId = json.getStr(treeParentIdKey, "");
                DiffItem diffItem = null;
                if(!currParentId.equals(baseParentId)){
                    // change parent
                    diffItem = new DiffItem(DiffItem.TREE_NODE_MOVE);
                    diffItem.setObjType("treeNode");
                    diffItem.setObjKey(id);
                    String name = json.getStr(DiffConfig.getTreeParentIdKey(config), id);
                    diffItem.setObjLabel(name);

                    String basePath = getTreePath(mBaseNode.get(id), mBaseNode, config);
                    String currPath = getTreePath(json, mWorkingNode, config);
                    diffItem.setPath(currPath);
                    diffItem.setCurrLabel(currPath);
                    diffItem.setBaseLabel(basePath);
                    moveItems.add(diffItem);
                }
                // TODO 检查节点数据变化
            } else {
                // new
                newNodes.add(json);
            }
        }
        for (JSONObject json : baseNodeList) {
            String id = json.getStr(treeIdKey);
            if (!setSameNode.contains(id)) {
                delNodes.add(json);
            }
        }
        // 3. record diff
        // check tree for all new node and all del node
        // 3.1 new 组合新增节点为多棵树
        List<JSONObject> listTreeNew = TreeUtil.listToTree(newNodes, config);
        if (CollectionUtil.isNotEmpty(listTreeNew)) {
            for (JSONObject json : listTreeNew) {
                DiffItem diffItem = new DiffItem(DiffItem.TREE_NODE_ADD);
                diffItem.setObjType("treeNode");
                String id = json.getStr(treeIdKey);
                diffItem.setObjKey(id);
                String name = json.getStr(treeNameKey, id);
                diffItem.setObjLabel(name);
                String treePath= getTreePath(json, mWorkingNode, config);
                diffItem.setPath(treePath);
                addOrDelTreeItemWithChildren(diffItem, json, false, config);
                diffResult.addDiff(diffItem);
            }
        }
        // 3.2 move
        diffResult.addAll(moveItems);
        // 3.3 del 组合删除节点为多颗树
        List<JSONObject> listTreeDel = TreeUtil.listToTree(delNodes, config);
        if (CollectionUtil.isNotEmpty(listTreeDel)) {
            for (JSONObject json : listTreeDel) {
                DiffItem diffItem = new DiffItem(DiffItem.TREE_NODE_DEL);
                diffItem.setObjType("treeNode");
                String id = json.getStr(treeIdKey);
                diffItem.setObjKey(id);
                String name = json.getStr(treeNameKey, id);
                diffItem.setObjLabel(name);
                String treePath= getTreePath(json, mBaseNode, config);
                diffItem.setPath(treePath);
                addOrDelTreeItemWithChildren(diffItem, json, true, config);
                diffResult.addDiff(diffItem);
            }
        }
        return diffResult;
    }

    private static String getTreePath(JSONObject currNode, Map<String, JSONObject> mNodes, DiffConfig config){
        String path = "";
        if(currNode==null){
            return path;
        }
        // 假定包含节点自身
        String id = currNode.getStr(DiffConfig.getTreeIdKey(config), "");
        String name = currNode.getStr(DiffConfig.getTreeNameKey(config), id);
        if(ObjectUtil.isEmpty(name) && ObjectUtil.isEmpty(id)){
            return path;
        }
        path = DiffItem.getItemLabel(id, name);
        String parentId = currNode.getStr(DiffConfig.getTreeParentIdKey(config), "");
        if(!ObjectUtil.isEmpty(parentId) && mNodes.containsKey(parentId)){
            JSONObject parentNode = mNodes.get(parentId);
            String parentPath = getTreePath(parentNode, mNodes, config);
            if(ObjectUtil.isNotEmpty(parentPath)){
                path = parentPath + "/" + path;
            }
        }
        return path;
    }

    private static void addOrDelTreeItemWithChildren(DiffItem parentItem, JSONObject parentNode, boolean isDel, DiffConfig config){
        if(parentItem==null || parentNode==null){
            return;
        }
        // check children
        String childrenKey = DiffConfig.getTreeChildrenKey(config);
        if(!parentNode.containsKey(childrenKey)){
            return;
        }
        JSONArray children = parentNode.getJSONArray(childrenKey);
        if(children==null) {
            return;
        }

        DiffItem diffItem = null;
        for(int i=0; i<children.size(); i++){
            JSONObject childJson = null;
            Object child = children.get(i);
            if(child instanceof JSONObject){
                childJson = (JSONObject)child;
            }else{
                childJson = new JSONObject(child);
            }
            String id = childJson.getStr(DiffConfig.getTreeIdKey(config));
            if(ObjectUtil.isEmpty(id)){
                continue;
            }
            String name = childJson.getStr(DiffConfig.getTreeParentIdKey(config), id);
            if(isDel){
                diffItem = new DiffItem(DiffItem.ITEM_DEL);
            }else {
                diffItem = new DiffItem(DiffItem.ITEM_ADD);
            }
            diffItem.setObjType("node");
            diffItem.setObjKey(id);
            diffItem.setObjLabel(name);
            diffItem.setIgnore(true);
            parentItem.getChildren().add(diffItem);

            // check child's children
            addOrDelTreeItemWithChildren(diffItem, childJson, isDel, config);
        } // end for
    }

    public static List<JSONObject> listToTree(List<JSONObject> list, JSONObject config) {
        // 1. prepare
        if (CollectionUtil.isEmpty(list)) return Collections.emptyList();

        List<JSONObject> listTreeNode = new ArrayList<>();
        String treeIdKey = DiffConfig.getTreeIdKey(config);
        String treeParentIdKey = DiffConfig.getTreeParentIdKey(config);
        String treeWeightKey = DiffConfig.getTreeWeightKey(config);  //ignoreWeight=true返回空字符串
        String treeChildrenKey = DiffConfig.getTreeChildrenKey(config);

        Map<String, JSONObject> mTemp = new LinkedHashMap<>();
        for (JSONObject node : list) {
            mTemp.put(node.getStr(treeIdKey), node);
        }
        for (JSONObject node : list) {
            String id = node.getStr(treeIdKey);
            String parentId = node.getStr(treeParentIdKey);
            if (mTemp.containsKey(parentId) && !id.equals(parentId)) {
                if (mTemp.get(parentId).getJSONArray(treeChildrenKey) == null) {
                    mTemp.get(parentId).set(treeChildrenKey, new JSONArray());
                }
                if(ObjectUtil.isEmpty(treeWeightKey)){
                    // 忽略排序插入到尾部
                    mTemp.get(parentId).getJSONArray(treeChildrenKey).add(node);
                }else{
                    // TODO 检查weight更换插入位置
                    // mTemp.get(parentId).getJSONArray(treeChildrenKey).add(3, node);
                    mTemp.get(parentId).getJSONArray(treeChildrenKey).add(node);
                }
            } else {
                listTreeNode.add(node);
            }
        }
        return listTreeNode;
    }
}