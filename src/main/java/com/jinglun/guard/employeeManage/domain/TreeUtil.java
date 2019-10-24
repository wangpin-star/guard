package com.jinglun.guard.employeeManage.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TreeUtil {
    /**
     * 将无序的结点集合，创建成一棵树。
     * 创建过程中使用了树的广度优先遍历，并且在考察无序集合的元素时，
     * 将其逐个插入到广度优先遍历结果集中，最后得到的结果集即是广度优先
     * 遍历的结果，也是从根元素(结果集中第一个元素)串联好的树形结构。
     * @param root 根元素
     * @param list 无序的、不含根元素的集合
     * @return 包含子类的树形结构的根元素
     */
    public static TreeDomain getTree(TreeDomain root, ArrayList<TreeDomain> list) {
        // 模拟树的广度遍历结果的集合
        LinkedList<TreeDomain> traversalList = new LinkedList<>();
        traversalList.push(root);
        // 原始集合不为空，则继续迭代，将其中的元素加入到树的广度遍历结果集合中
        while(list.size() != 0) {
            // 迭代原始集合中的元素
            Iterator<TreeDomain> iterAll = list.iterator();
            while(iterAll.hasNext()) {
                TreeDomain ndInAll = iterAll.next();
                // 迭代树的广度遍历结果集合中的元素
                Iterator<TreeDomain> iterTrav = traversalList.iterator();
                int indInTrav = 0;// 记录迭代当前元素的位置
                boolean mate = false;// 标识是否找到父子类匹配关系
                while(iterTrav.hasNext()) {
                    TreeDomain ndInTrav = iterTrav.next();
                    // 如果存在父子类关系，则在在树的广度遍历结果集合中添加该元素，并父类中加入子元素
                    if(!mate) {
                        if(ndInAll.isChildFrom(ndInTrav)) {
                            // 如果存在父子类关系，则在父类中加入子元素，并设置标识
                            // 将子类层级+1,以实现最大组织结构的功能
                            ndInAll.setLevel(ndInTrav.getLevel() + 1);
                            ndInTrav.addChildNode(ndInAll);
                            mate = true;
                        }
                    } else {
                        // 在找到iterInAll元素的父类之后，继续迭代，找到它的兄弟结点的位置
                        if(ndInAll.isBrother(ndInTrav)) {
                            break;
                        }
                    }
                    indInTrav++; // 执行++之后为迭代当前元素的位置
                }
                if(mate) {
                    // 如果找到iterInAll元素的父类，则在它的兄弟结点之前插入该元素
                    traversalList.add(indInTrav, ndInAll);
                    // 移除已经匹配的元素
                    iterAll.remove();
                }
            }
        }
        // 最后将所有元素已经放到了树的广度遍历结果集合中，并且元素之间建立好了子父关系，即只取根就可得到所有元素
        TreeDomain root2 = traversalList.getFirst();
        return root2;
    }
}
