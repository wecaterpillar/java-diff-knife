package org.caterpillar.utilknife;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.caterpillar.utilknife.model.DiffResult;

import java.util.*;

/**
 * Tree Util
 * - diff
 * - convert (tree <=> list)
 */
public class TreeUtil {

    public static DiffResult diffNodeList(List<JSONObject> workingNodeList, List<JSONObject> baseNodeList) {
        return diffNodeList(workingNodeList, baseNodeList, null);
    }

    public static DiffResult diffNodeList(List<JSONObject> workingNodeList, List<JSONObject> baseNodeList, Config config) {
        return new TreeDiffBuilder(config).diffNodeList(workingNodeList, baseNodeList).getDiffResult();
    }
    public static DiffResult diffTree(JSONObject working, JSONObject base){
        return diffTree(working, base, null);
    }

    public static DiffResult diffTree(JSONObject working, JSONObject base, Config config){
        return new TreeDiffBuilder(config).diff(working, base).getDiffResult();
    }

    public static List<JSONObject> listToTree(List<JSONObject> list, JSONObject config) {
        // 1. prepare
        if (CollectionUtil.isEmpty(list)) {return Collections.emptyList();}

        List<JSONObject> listTreeNode = new ArrayList<>();
        String treeIdKey = Config.getIdKey(config);
        String treeParentIdKey = Config.getParentIdKey(config);
        String treeWeightKey = Config.getWeightKey(config);  //ignoreWeight=true返回空字符串
        String treeChildrenKey = Config.getChildrenKey(config);

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

    public static List<JSONObject> treeToList(JSONObject tree, JSONObject config){
        if(tree==null){return  Collections.emptyList();}
        // 参数准备
        List<JSONObject> list = new ArrayList<>();
        JSONObject top = new JSONObject();
        String treeChildrenKey = Config.getChildrenKey(config);
        BeanUtil.copyProperties(tree, top, treeChildrenKey);
        list.add(top);
        // check children
        String id = top.getStr(Config.getIdKey(config));
        checkTreeChildrenToList(list, id, tree, config);
        return  list;
    }
    private static void checkTreeChildrenToList(List<JSONObject> list, String parentId, JSONObject parentNode, JSONObject config){
        if(list==null || parentNode==null) {
            return;
        }
        String treeChildrenKey = Config.getChildrenKey(config);
        // 无叶子节点
        if(!parentNode.containsKey(treeChildrenKey)){
            list.add(parentNode);
            return;
        }
        JSONArray children = parentNode.getJSONArray(treeChildrenKey);
        if(children==null){
            return;
        }
        for(int i=0; i<children.size(); i++){
            JSONObject childJson = null;
            Object child = children.get(i);
            if(child instanceof JSONObject){
                childJson = (JSONObject)child;
            }else{
                childJson = new JSONObject(child);
            }
            JSONObject childNode = new JSONObject();
            BeanUtil.copyProperties(childJson, childNode, treeChildrenKey);
            childNode.set(Config.getParentIdKey(config), parentId);
            list.add(childNode);

            // check child's children
            String id = childJson.getStr(Config.getIdKey(config));
            checkTreeChildrenToList(list, id, childJson, config);
        }


    }
}