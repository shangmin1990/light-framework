package com.le.bigdata.common.service;

import com.le.bigdata.common.dao.mapper.MenuMapper;
import com.le.bigdata.common.model.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin on 16/3/25.
 */
@Service
@Transactional
public class MenuService extends BaseService<Integer, MenuModel> {

    @Autowired
    protected MenuMapper menuMapper;

    /**
     * 非叶子节点下是否有孩子
     *
     * @param id
     * @return
     */
    public boolean hasChildren(Integer id) {
        List list = menuMapper.children(id);
        if (list == null)
            return false;
        if (list.size() == 0)
            return false;
        return true;
    }

    /**
     * 不是树形结构 所有可以显示的目录
     */
    public List<MenuModel> getAllMenuItem() {
        return menuMapper.getAllMenuItem();
    }

    /**
     * 获得所有菜单，包括不显示的菜单 树形结构
     *
     * @return
     */
    public List<MenuModel> getAllMenuItemContainsDisplay() {
        List<MenuModel> allMenus = menuMapper.getAllMenuIemsContainsDisplayMenu();
        List<MenuModel> rootMenus = getRootMenu(allMenus);
        for (MenuModel menuModel : rootMenus) {
            buildMenuTree(menuModel, allMenus);
        }
        return rootMenus;
    }

    /**
     * 递归构造菜单树
     *
     * @param model
     * @param allMenus
     */
    private void buildMenuTree(MenuModel model, List<MenuModel> allMenus) {
        Integer id = model.getId();
        List<MenuModel> menuModels = getChildrenMenu(id, allMenus);
        model.setChildren(menuModels);
        for (MenuModel menuModel : menuModels) {
            buildMenuTree(menuModel, allMenus);
        }
    }

    /**
     * 获得子菜单
     *
     * @param id       父菜单标识
     * @param allMenus 所有菜单
     */
    private List<MenuModel> getChildrenMenu(Integer id, List<MenuModel> allMenus) {
        List<MenuModel> children = new ArrayList<>();
        for (MenuModel model : allMenus) {
            if (model.getParentId() != null) {
                if (model.getParentId().intValue() == id.intValue()) {
                    children.add(model);
                }
            }
        }
        return children;
    }

    /*
    * 获得根菜单
    * */
    private List<MenuModel> getRootMenu(List<MenuModel> allMenus) {
        List<MenuModel> rootMenus = new ArrayList<>();
        for (MenuModel menuModel : allMenus) {
            if (menuModel.getHeight() != null) {
                if (Integer.valueOf(menuModel.getHeight()) == 0) {
                    rootMenus.add(menuModel);
                }
            }
        }
        return rootMenus;
    }

    public List<MenuModel> getChildren(Integer parentId) {
        return menuMapper.children(parentId);
    }

    public List<MenuModel> getChildrenDisplayed(Integer parentId) {
        return menuMapper.childrenDisplay(parentId);
    }

    public MenuModel getMenuTree(Integer id) {
        MenuModel root = menuMapper.selectByPrimaryKey(id);
        buildMenuTree(root);
        return root;
    }

    /**
     * 递归构造菜单树
     *
     * @param root
     */
    protected void buildMenuTree(MenuModel root) {
        Integer id = root.getId();
        List<MenuModel> menuModels = menuMapper.children(id);
        root.setChildren(menuModels);
        for (MenuModel menuModel : menuModels) {
            if (!menuModel.getLeaf()) {
                buildMenuTree(menuModel);
            }

        }
    }

    /**
     * 递归构造菜单树 (只包含显示的菜单)
     *
     * @param root
     */
    protected void buildMenuTreeDisplayed(MenuModel root) {
        Integer id = root.getId();
        List<MenuModel> menuModels = menuMapper.childrenDisplay(id);
        root.setChildren(menuModels);
        for (MenuModel menuModel : menuModels) {
            //menuModel.setParent(root);
            if (hasChildren(menuModel.getId()) && menuModel.getDisplay()) {
                buildMenuTreeDisplayed(menuModel);
            }

        }
    }

    /**
     * 是否
     *
     * @param all 是否查询全部(包括不显示的菜单 再配置时有用) true查询全部 false 只查询display=true的菜单
     * @return
     */
    public List<MenuModel> getRootMenu(Boolean all) {
        if (all) {
            return menuMapper.getRootMenu();
        } else {
            return menuMapper.getRootMenuDisplay();
        }
    }


    public boolean updateMenuById(Integer id, String name, String url, Integer priority, Boolean display) {
        return menuMapper.updateMenuById(id, name, url, priority, display) > 0;
    }

    /**
     * 返回所有菜单(包括未显示) 树形结构
     *
     * @return
     */
    public List<MenuModel> getAllMenu() {
        List<MenuModel> rootMenus = getRootMenu(true);
        List<MenuModel> allMenus = new ArrayList<MenuModel>(rootMenus.size());
        for (MenuModel menuModel : rootMenus) {
            Integer id = menuModel.getId();
            MenuModel menuModel1 = getMenuTree(id);
            allMenus.add(menuModel1);
        }
        return allMenus;
    }

    /**
     * 返回所有菜单(只包含显示的菜单) 树形结构
     *
     * @return
     */
    public List<MenuModel> getAllMenuDisplay() {
        List<MenuModel> rootMenus = getRootMenu(false);
//        List<MenuModel> allMenus = new ArrayList<MenuModel>(rootMenus.size());
        for (MenuModel menuModel : rootMenus) {
            buildMenuTreeDisplayed(menuModel);
        }
        return rootMenus;
    }

    /**
     * 获得某一个parent下的所有可见菜单 树形结构
     *
     * @param id
     * @return
     */
    public MenuModel getMenuTreeDisplayed(Integer id) {
        MenuModel root = menuMapper.selectByPrimaryKey(id);
        if (!root.getDisplay())
            return null;
        buildMenuTreeDisplayed(root);
        return root;
    }

    public boolean updateMenuPriorityBatch(List<MenuModel> menuModels) {
        int result = 0;
        for (MenuModel menuModel : menuModels) {
            int i = menuMapper.updateMenuPriority(menuModel);
            result += i;
        }
        return result == menuModels.size();
    }

    public List<MenuModel> getMenuListLeaf() {
        return menuMapper.getMenuListLeaf();
    }

    public List<MenuModel> getMenuListByCondition(String name) {
        return menuMapper.getMenuListByCondition("%" + name + '%');
    }

    public MenuModel getRootByLeaf(MenuModel leaf) {
        if (leaf == null) {
            return null;
        }
        while (leaf.getParentId() != null && leaf.getParentId() > 0) {
            leaf = leaf.getParent();
        }
        return leaf;
    }
}
