package org.caterpillar.diffknife.model;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;

public class TreeObj extends Tree<Object> {

    public Tree<Object> getNode(Object id) {
        return TreeUtil.getNode(this, id);
    }
}
