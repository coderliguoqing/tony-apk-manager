package com.tony.admin.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tony.admin.web.common.dao.CrudDao;
import com.tony.admin.web.model.SysMenu;

import java.util.List;

/**
 * 菜单DAO接口
 *
 * @author mij
 */
@Mapper
public interface SysMenuMapper extends CrudDao<SysMenu> {

    /**
     * 根据用户查询菜单
     *
     * @param userId the user id
     * @return the list
     */
    List<SysMenu> findListByUserId(Integer userId);

    /**
     * 根据角色查询菜单
     *
     * @param roleId the role id
     * @return the list
     */
    List<SysMenu> findListByRoleId(Integer roleId);

    /**
     * 根据父ID查询菜单
     *
     * @param menu the menu
     * @return the list
     */
    List<SysMenu> findByParentIdsLike(SysMenu menu);

    /**
     * 更新父ID
     *
     * @param menu the menu
     * @return the int
     */
    int updateParentIds(SysMenu menu);

}
