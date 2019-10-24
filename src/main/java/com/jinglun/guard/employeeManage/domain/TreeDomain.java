package com.jinglun.guard.employeeManage.domain;

import java.util.ArrayList;
import java.util.List;

public class TreeDomain {
    private Object data;
    private Integer depart_id;//id改为depart_id，修改对应的set，get方法，并修改调用这些方法的类
    private String name;
    private String label;
    private Integer Level;
    private Boolean spread=true;
    private Integer id=1;//代表层级，layui树形组件的id属性，被解析为 <div class="layui-tree-set">的data_id属性，根据data_id 设置图标


    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
        id=level;
        spread=false;//只有第一层默认展开
    }//设置level的同时设置id

    public Boolean getSpread() {
        return spread;
    }

    public Integer getId() {
		return id;
	}

	public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private Integer parentId;
    private List<TreeDomain> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<TreeDomain> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDomain> children) {
        this.children = children;
    }

    public Integer getDepart_id() {
        return depart_id;
    }

    public void setDepart_id(Integer id) {
        this.depart_id = id;
    }

    public TreeDomain(Integer id, String name, String label, Integer level, Integer parentId) {
        this.depart_id = id;
        this.name = name;
        this.label = label;
        Level = level;
        this.parentId = parentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public TreeDomain(Integer id, String name, Integer parentId) {
        this.depart_id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public TreeDomain(Integer id, String name,String label, Integer parentId) {
        this.depart_id = id;
        this.name = name;
        this.label = label;
        this.parentId = parentId;
    }



    public void addChildNode(TreeDomain node) {
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(node);
    }

    public boolean isBrother(TreeDomain node) {
        return this.parentId.equals(((TreeDomain)node).getParentId());
    }

    public boolean isChildFrom(TreeDomain node) {
        return this.parentId.equals(node.getDepart_id());
    }

    public TreeDomain() {
    }
}
