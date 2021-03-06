package com.tony.admin.web.sys.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.tony.admin.web.sys.model.SysRole;
import java.util.List;

/**
 * 角色DAO接口
 *
 * @author Guoqing
 */
@Mapper
public interface SysRoleMapper extends tk.mybatis.mapper.common.Mapper<SysRole> {

    /**
     * 查询用户角色列表
     *
     * @param userId the user id
     * @return the list
     */
    List<SysRole> findListByUserId(Integer userId);

    /**
     * 删除角色菜单
     *
     * @param role the role
     * @return the int
     */
    int deleteRoleMenu(SysRole role);

    /**
     * 插入角色菜单
     *
     * @param role the role
     * @return the int
     */
    int insertRoleMenu(SysRole role);
}
