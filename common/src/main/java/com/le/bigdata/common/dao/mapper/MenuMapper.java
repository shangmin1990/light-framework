package com.le.bigdata.common.dao.mapper;

import com.le.bigdata.common.dao.BaseMapper;
import com.le.bigdata.common.model.MenuModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by benjamin on 16/3/25.
 */
public interface MenuMapper extends BaseMapper<MenuModel> {
    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu where height = 0 " +
            "order by priority desc")
    List<MenuModel> getRootMenu();

    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where parent_id = #{id} order by priority desc")
    List<MenuModel> children(@Param("id") Integer id);

    /**
     * 获取可以显示的目录
     *
     * @return
     */
    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where height = 0 and display = true order by priority desc")
    List<MenuModel> getRootMenuDisplay();

    /**
     * 获取可以显示的子集目录
     *
     * @return
     */
    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where parent_id = #{id} and display = true order by priority desc")
    List<MenuModel> childrenDisplay(@Param("id") Integer id);

    int updateMenuById(@Param("id") Integer id,
                       @Param("name") String name,
                       @Param("url") String url,
                       @Param("priority") Integer priority,
                       @Param("display") Boolean display);

    /**
     * 可以显示的目录 所有目录 不是树形结构
     *
     * @return
     */
    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where display = 1 order by height asc, priority desc")
    List<MenuModel> getAllMenuItem();

    /**
     * 可以显示的目录 所有目录 不是树形结构
     *
     * @return
     */
    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "order by height asc, priority desc")
    List<MenuModel> getAllMenuIemsContainsDisplayMenu();

    int insertMenuBatch(@Param("list") List<MenuModel> menuModels);

    @Update("update menu set  priority = #{menu.priority} where id = #{menu.id}")
    int updateMenuPriority(@Param("menu") MenuModel menuModel);

    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where display = 1 and isLeaf = 1 order by height asc, priority desc")
    List<MenuModel> getMenuListLeaf();

    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where display = 1 and isLeaf = 1 and name like #{name} order by height asc, priority desc")
    List<MenuModel> getMenuListByCondition(@Param("name") String name);

    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu " +
            "where display = 1 and isLeaf = 1 and url like concat('%', #{url}, '%') ")
    List<MenuModel> findByUrl(@Param("url") String url);

    @Select("select id,name,priority,url,add_time as addTime,user_add as userAdd,icon,isLeaf as leaf,height," +
            "parent_id as parentId,resource_type as resourceType, display from menu where parent_id = #{id}")
    MenuModel getMenuByParentId(@Param("id") Integer id);
}
