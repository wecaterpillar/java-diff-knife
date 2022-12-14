package org.caterpillar.utilknife;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.caterpillar.utilknife.model.DiffItem;
import org.caterpillar.utilknife.model.DiffResult;

import java.util.*;

@lombok.extern.slf4j.Slf4j
public class TreeDiffBuilder extends JsonDiffBuilder {

    public TreeDiffBuilder(){
        super();
    }

    public TreeDiffBuilder(Config config){
        super(config);
    }

    public TreeDiffBuilder diffTree(JSONObject working, JSONObject base){
        if(config==null){
            config = Config.DEFAULT_CONFIG;
        }
        List<JSONObject> workingNodeList = TreeUtil.treeToList(working, config);
        List<JSONObject> baseNodeList = TreeUtil.treeToList(base, config);
        diffNodeList(workingNodeList, baseNodeList);
        return this;
    }

    public TreeDiffBuilder diffNodeList(List<JSONObject> workingNodeList, List<JSONObject> baseNodeList) {
        DiffResult diffResult = new DiffResult();
        if(config==null){
            config = Config.DEFAULT_CONFIG;
        }
        String treeIdKey = Config.getIdKey(config);
        String treeParentIdKey = Config.getParentIdKey(config);
        String treeNameKey = Config.getNameKey(config);
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
        List<DiffItem> changeItems = new ArrayList<>();

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
                    String name = json.getStr(Config.getParentIdKey(config), id);
                    diffItem.setObjLabel(name);

                    String basePath = getTreePath(mBaseNode.get(id), mBaseNode);
                    String currPath = getTreePath(json, mWorkingNode);
                    diffItem.setPath(currPath);
                    diffItem.setCurrLabel(currPath);
                    diffItem.setBaseLabel(basePath);
                    moveItems.add(diffItem);
                }
                // TODO 检查节点数据变化, 待验证
                try{
                    DiffResult nodeDiffResult = new JsonDiffBuilder().config(this.config).diff(json, mBaseNode.get(id)).getDiffResult();
                    if(nodeDiffResult!=null && nodeDiffResult.size()>0){
                        if(diffItem!=null){
                            // 如果同时存在移动父节点和更改，则自动合并
                            diffItem.setChildren(nodeDiffResult);
                        }else{
                            diffItem = new DiffItem(DiffItem.VALUE_CHANGE);
                            diffItem.setObjType("treeNode");
                            diffItem.setObjKey(id);
                            String name = json.getStr(Config.getParentIdKey(config), id);
                            diffItem.setObjLabel(name);
                            String currPath = getTreePath(json, mWorkingNode);
                            diffItem.setPath(currPath);

                            // detail
                            diffItem.setChildren(nodeDiffResult);

                            changeItems.add(diffItem);
                        }
                    }
                }catch (Throwable e){
                    //log.warn(e.getMessage(), e);
                }
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
                String treePath= getTreePath(json, mWorkingNode);
                diffItem.setPath(treePath);
                addOrDelTreeItemWithChildren(diffItem, json, false);
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
                String treePath= getTreePath(json, mBaseNode);
                diffItem.setPath(treePath);
                addOrDelTreeItemWithChildren(diffItem, json, true);
                diffResult.addDiff(diffItem);
            }
        }
        // 3.4 change value
        diffResult.addAll(changeItems);

        // set diff result
        this.diffResult = diffResult;
        return this;
    }

    private  String getTreePath(JSONObject currNode, Map<String, JSONObject> mNodes){
        String path = "";
        if(currNode==null){
            return path;
        }
        // 假定包含节点自身
        String id = currNode.getStr(Config.getIdKey(config), "");
        String name = currNode.getStr(Config.getNameKey(config), id);
        if(ObjectUtil.isEmpty(name) && ObjectUtil.isEmpty(id)){
            return path;
        }
        path = DiffItem.getItemLabel(id, name);
        String parentId = currNode.getStr(Config.getParentIdKey(config), "");
        if(!ObjectUtil.isEmpty(parentId) && mNodes.containsKey(parentId)){
            JSONObject parentNode = mNodes.get(parentId);
            String parentPath = getTreePath(parentNode, mNodes);
            if(ObjectUtil.isNotEmpty(parentPath)){
                path = parentPath + "/" + path;
            }
        }
        return path;
    }

    private  void addOrDelTreeItemWithChildren(DiffItem parentItem, JSONObject parentNode, boolean isDel){
        if(parentItem==null || parentNode==null){
            return;
        }
        // check children
        String childrenKey = Config.getChildrenKey(config);
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
            String id = childJson.getStr(Config.getIdKey(config));
            if(ObjectUtil.isEmpty(id)){
                continue;
            }
            String name = childJson.getStr(Config.getParentIdKey(config), id);
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
            addOrDelTreeItemWithChildren(diffItem, childJson, isDel);
        } // end for
    }
}
