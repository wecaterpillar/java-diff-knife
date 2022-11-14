package org.caterpillar.diffknife;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.caterpillar.diffknife.model.DiffItem;
import org.caterpillar.diffknife.model.DiffResult;

import java.util.*;

public class TreeUtil {

    public static DiffResult diffNodeList(List<JSONObject> working, List<JSONObject> base) {
        return diffNodeList(working, base, null);
    }

    public static DiffResult diffNodeList(List<JSONObject> working, List<JSONObject> base, DiffConfig config) {
        DiffResult diffResult = new DiffResult();
        // 1. prepare, config
        if (config == null) {
            config = DiffConfig.DEFAULT_CONFIG;
        }
        String treeIdKey = config.getStr("tree.idKey", "id");
        String treeParentIdKey = config.getStr("tree.parentIdKey", "parentId");
        // 对比两颗树结构变化，先对比节点，数量找到新增和删除的部分，再对比相同节点父节点改变的部分。

        // 2. 对比 current vs base
        List<JSONObject> newNodes = new ArrayList<>();
        List<JSONObject> delNodes = new ArrayList<>();
        List<DiffItem> moveItems = new ArrayList<>();

        Set<String> setSameNode = new HashSet<>();
        Map<String, JSONObject> mBaseNode = new HashMap<>();
        for (JSONObject json : base) {
            mBaseNode.put(json.getStr(treeIdKey), json);
        }
        for (JSONObject json : working) {
            String id = json.getStr(treeIdKey);
            if (mBaseNode.containsKey(id)) {
                // 相同节点
                setSameNode.add(id);
                // 检查移动父节点，忽略排序
                String baseParentId = mBaseNode.get(id).getStr(treeParentIdKey);
                String currParentId = json.getStr(treeParentIdKey);
                DiffItem diffItem = null;
                if (ObjectUtil.isEmpty(currParentId) && !ObjectUtil.isEmpty(baseParentId)) {
                    // change parent
                    diffItem = new DiffItem(DiffItem.TREE_NODE_MOVE);
                } else if (!ObjectUtil.isEmpty(currParentId) && !currParentId.equals(baseParentId)) {
                    // change parent
                    diffItem = new DiffItem(DiffItem.TREE_NODE_MOVE);
                }
                if (diffItem != null) {
                    if (!ObjectUtil.isEmpty(currParentId)) {
                        diffItem.setCurrVal(currParentId);
                    }
                    if (!ObjectUtil.isEmpty(baseParentId)) {
                        diffItem.setBaseVal(baseParentId);
                    }
                    moveItems.add(diffItem);
                }
                // TODO 检查数据变化
            } else {
                // new
                newNodes.add(json);
            }
        }
        for (JSONObject json : base) {
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
                DiffItem diffItem = new DiffItem(DiffItem.ITEM_ADD);
                diffItem.setCurrVal(json.getStr(treeIdKey));
                // detail with children
                diffResult.addDiff(diffItem);
            }
        }
        // 3.2 move
        diffResult.addAll(moveItems);
        // 3.3 del 组合删除节点为多颗树
        List<JSONObject> listTreeDel = TreeUtil.listToTree(delNodes, config);
        if (CollectionUtil.isNotEmpty(listTreeDel)) {
            for (JSONObject json : listTreeDel) {
                DiffItem diffItem = new DiffItem(DiffItem.ITEM_DEL);
                diffItem.setBaseVal(json.getStr(treeIdKey));
                // detail with children
                diffResult.addDiff(diffItem);
            }
        }
        return diffResult;
    }

    public static List<JSONObject> listToTree(List<JSONObject> list, JSONObject config) {
        // 1. prepare
        if (CollectionUtil.isEmpty(list)) return Collections.emptyList();

        List<JSONObject> listTreeNode = new ArrayList<>();
        String treeIdKey = config.getStr("tree.idKey", "id");
        String treeParentIdKey = config.getStr("tree.parentIdKey", "parentId");
        String treeWeightKey = config.getStr("tree.weightKey", "weight");
        String treeChildrenKey = config.getStr("tree.childrenKey", "children");

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
                mTemp.get(parentId).getJSONArray(treeChildrenKey).add(node);
            } else {
                listTreeNode.add(node);
            }
        }
        return listTreeNode;
    }
}
