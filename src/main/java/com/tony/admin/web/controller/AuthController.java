package com.tony.admin.web.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tony.admin.web.common.dao.Paging;
import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.common.response.ResponseBean;
import com.tony.admin.web.common.security.JwtTokenUtil;
import com.tony.admin.web.common.security.WebUtils;
import com.tony.admin.web.common.security.model.AuthUser;
import com.tony.admin.web.common.utils.StringHelper;
import com.tony.admin.web.common.utils.VerifyCodeUtils;
import com.tony.admin.web.model.SysMenu;
import com.tony.admin.web.model.SysRole;
import com.tony.admin.web.model.SysUser;
import com.tony.admin.web.service.ISystemService;
import com.tony.admin.web.vo.RightTreeVo;

/**
 * 
 * @Description  authentication controller
 *
 * @author Guoqing
 * @Date 2018年1月16日
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/admin/auth")
public class AuthController{
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private ISystemService systemService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;

	@PostMapping(value = "/getData")
	public ResponseBean getData() {
		ResponseBean responseBean = new ResponseBean(true, 0, "请求成功", System.currentTimeMillis());
		return responseBean;
	}

	/**
	 * create token
	 * @param jsonObject
	 * @param session
	 * @return
	 */
	@PostMapping("/token")
	public ResponseBean createToken(@RequestBody JSONObject jsonObject){
		String userName = jsonObject.getString("username").trim();
		String password = jsonObject.getString("password").trim();
		String uniqueCode = jsonObject.getString("uniqueCode").trim();
		String verifyCode = jsonObject.getString("verifyCode").trim();
		
		if( !redisRepository.exists(VerifyCodeUtils.VERIFY_CODE + uniqueCode) ){
			return new ResponseBean(false, 1001, "验证码已失效，请重新登录", null);
		}
		
		//验证验证码是否正确
		String code = redisRepository.get(VerifyCodeUtils.VERIFY_CODE + uniqueCode);
		if( !code.equals(verifyCode) ){
			return new ResponseBean(false, 1002, "验证码错误，请重新登录", null);
		}else{
			//验证成功即删除验证码
			redisRepository.del(VerifyCodeUtils.VERIFY_CODE + uniqueCode);
		}
		
		//做用户登录次数限制，1分钟内不能超过5次
		Map<String, Object> map = systemService.userLoginLimit(userName);
		if( (Boolean)map.get("isSuccess") == false ){
			return new ResponseBean(false, 1003, map.get("responseMsg").toString(), null);
		}
		
		try {
			
			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userName, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			final String token = jwtTokenUtil.generateToken(userDetails, jwtTokenUtil.getRandomKey());
			
			return new ResponseBean(true, 0, "登录成功", JwtTokenUtil.TOKEN_TYPE_BEARER + " " + token);
		} catch (UsernameNotFoundException e) {
			logger.info("用户认证失败：" + "userName wasn't in the system");
			return new ResponseBean(false, 1004, "用户名或密码错误", null);
		} catch (LockedException lae) {
			logger.info("用户认证失败：" + "account for that username is locked, can't login");
			return new ResponseBean(false, 1005, "账号已被锁定，不能登录", null);
		} catch (AuthenticationException ace) {
			logger.info("用户认证失败：" + ace);
			ace.printStackTrace();
			return new ResponseBean(false, 1006, "用户信息错误，请重试", null);
		}
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@DeleteMapping("/token")
	public ResponseBean deleteToken(HttpServletRequest request){
		String tokenHeader = request.getHeader("Authorization");
		String token = tokenHeader.split(" ")[1];
		jwtTokenUtil.deleteToken(token);
		return new ResponseBean(true, 0, "请求成功", null);
	}
	
	 /**
     * List page info.
     *
     * @param role the role
     * @param page the page
     * @return the page info
     */
    @PreAuthorize("hasAuthority('sys:role:view')")
    @PostMapping(value = "/role/list")
    public PageInfo<SysRole> list( @RequestBody JSONObject jsonObject) {
    	SysRole role = JSONObject.parseObject(jsonObject.getJSONObject("role").toJSONString(), SysRole.class);
		Paging page = (Paging) JSONObject.parseObject(jsonObject.getJSONObject("page").toJSONString(), Paging.class);
        return systemService.findRolePage(page, role);
    }

    /**
     * All list.
     *
     * @return the list
     */
    @PreAuthorize("hasAuthority('sys:role:view')")
    @GetMapping(value = "/role/all")
    public List<SysRole> all() {
        return systemService.findAllRoleList();
    }

    /**
     * Gets role.
     *
     * @param roleId the role id
     * @return the role
     */
    @PreAuthorize("hasAuthority('sys:role:view')")
    @GetMapping(value = "/role/{roleId}")
    public SysRole getRole(@PathVariable("roleId") Integer roleId) {
        return systemService.getRoleById(roleId);
    }

    /**
     * Save role sys role.
     *
     * @param role the role
     * @return the sys role
     */
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PostMapping(value = "/role/edit")
    public SysRole saveRole(@Valid @RequestBody JSONObject jsonObject) {
    	SysRole role = JSONObject.parseObject(jsonObject.getJSONObject("role").toJSONString(), SysRole.class);
        return systemService.saveRole(role);
    }

    /**
     * Delete response entity.
     *
     * @param roleId the role id
     * @return the response entity
     */
    @SuppressWarnings("rawtypes")
	@PreAuthorize("hasAuthority('sys:role:edit')")
    @DeleteMapping(value = "/role/{roleId}")
    public ResponseEntity delete(@PathVariable("roleId") Integer roleId) {
        systemService.deleteRoleById(roleId);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    /**
     * Gets current user info.
     *
     * @return the current user info
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/user/info")
    public Object getCurrentUserInfo() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Save current user info response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @SuppressWarnings("rawtypes")
	@PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/user/info")
    public ResponseEntity saveCurrentUserInfo(@Valid @RequestBody SysUser user) {
        AuthUser authUser = WebUtils.getCurrentUser();
        //只能更新当前用户信息
        if (authUser.getId() == user.getId()) {
            // 保存用户信息
            systemService.updateUserInfo(user);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    
    /**
     * Reset password response entity.
     *
     * @param dto the dto
     * @return the response entity
     * @throws BusinessException the business exception
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/user/password")
    public ResponseBean updatePassword(@RequestBody JSONObject jsonObject) {
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");

        try {
			AuthUser user = WebUtils.getCurrentUser();

			// 重置密码
			if (StringHelper.isNotBlank(oldPassword) && StringHelper.isNotBlank(newPassword)) {

			    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			        return new ResponseBean(false, 1001, "旧密码错误", null);
			    }

			    systemService.updateUserPasswordById(user.getId(), passwordEncoder.encode(newPassword));
			}
			return new ResponseBean(true, 0, "请求成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseBean(false, 1001, "网络繁忙", null);
		}
    }
    
    /**
     * List page info.
     *
     * @param user the user
     * @param page the page
     * @return the page info
     */
    @PreAuthorize("hasAuthority('sys:user:view')")
    @PostMapping(value = "/user/list")
    public PageInfo<SysUser> userList(@RequestBody JSONObject jsonObject) {
    	SysUser user = JSONObject.parseObject(jsonObject.getJSONObject("user").toJSONString(), SysUser.class);
		Paging page = (Paging) JSONObject.parseObject(jsonObject.getJSONObject("page").toJSONString(), Paging.class);
        return systemService.findUserPage(page, user);
    }

    /**
     * Gets user.
     *
     * @param userId the user id
     * @return the user
     */
    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/user/{userId}")
    public SysUser getUser(@PathVariable("userId") Integer userId) {
        return systemService.getUserById(userId);
    }

    /**
     * Save user sys user.
     *
     * @param user the user
     * @return the sys user
     */
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PostMapping(value = "/user/edit")
    public ResponseBean saveUser( @RequestBody JSONObject jsonObject) {
    	try {
			SysUser user = JSONObject.parseObject(jsonObject.getJSONObject("user").toJSONString(), SysUser.class);
			String password = user.getPassword();
			//如果是新增用户
			if( user.getId() == null ){
				//判断登录名是否已经存在
				if( systemService.getUserByLoginName(user.getLoginName()) != null) {
					return new ResponseBean(false, 1101, "登录名已经存在，请勿重复添加", null);
				}
				//用户密码不能为空
				if (StringHelper.isNotBlank(password)) {
					user.setPassword(passwordEncoder.encode(password));
				}        	
			}
			// 保存用户信息
			systemService.saveUser(user);
	        return new ResponseBean(true, 0, "请求成功", null);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseBean(false, 1001, "网络繁忙，请稍后再试", null);
		}
    }
    
    /**
     * 用户信息更新密码字段
     * <p>Title: editPassword</p>  
     * <p>Description: </p>  
     * @param jsonObject
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PostMapping(value = "/user/editPassword")
    public ResponseBean editPassword( @RequestBody JSONObject jsonObject) {
    	try {
			SysUser user = new SysUser();
			int userId = jsonObject.getInteger("id");
			String newPassword = jsonObject.getString("newPassword");
			user.setId(userId);
			user.setPassword(passwordEncoder.encode(newPassword));
			// 保存用户信息
			systemService.saveUser(user);
	        return new ResponseBean(true, 0, "请求成功", null);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseBean(false, 1001, "网络繁忙，请稍后再试", null);
		}
    }

    /**
     * Delete response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @DeleteMapping(value = "/user/{userId}")
    public ResponseBean userDelete(@PathVariable("userId") Integer userId) {
        systemService.deleteUserById(userId);
        return new ResponseBean(true, 0, "请求成功", null);
    }
    
    /**
     * 将用户选择的皮肤信息存入缓存
     * @param skins
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = "/user/changeSkins")
    public ResponseBean changeSkins(@RequestBody String skins ){
    	AuthUser authUser = WebUtils.getCurrentUser();
    	Integer userId = authUser.getId();
    	if( StringHelper.isEmpty(skins) ){
    		return new ResponseBean(false, 1002 ,"请选择皮肤风格", null);
    	}else{
    		String redisKey = "style_skins_" + userId;
    		redisRepository.set(redisKey, skins);
    	}
    	return new ResponseBean(true, 0 ,"请求成功", null);
    }
    
    @PostMapping(value = "/user/getSkins")
    public Map<String, Object> getSkins() {
    	Map<String, Object> resultMap = new HashMap<>(1);
    	AuthUser authUser = WebUtils.getCurrentUser();
    	Integer userId = authUser.getId();
		String redisKey = "style_skins_" + userId;
		String skins = redisRepository.get(redisKey);
		resultMap.put("skins", skins);
    	return resultMap;
    }
    
    /**
     * 获取用户已授权列表，未授权列表
     * @param jsonObject
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:auth')")
    @PostMapping(value = "/user/authList")
    public ResponseBean authList( @RequestBody JSONObject jsonObject) {
    	Map<String, Object> result = new HashMap<String, Object>(2);
    	try {
			int userId = jsonObject.getInteger("userId");
			List<SysRole> authList = systemService.getAuthList(userId);
			List<SysRole> unAuthList = systemService.getUnAuthList(userId);
			result.put("authList", authList);
			result.put("unAuthList", unAuthList);
	        return new ResponseBean(true, 0, "请求成功", result);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseBean(false, 1001, "网络繁忙，请稍后再试", null);
		}
    }
    
    /**
     * 获取用户未授权列表
     * @param jsonObject
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:auth')")
    @PostMapping(value = "/user/editAuthList")
    public ResponseBean editAuthList( @RequestBody JSONObject jsonObject) {
    	try {
			int userId = jsonObject.getInteger("userId");
			String type = jsonObject.getString("type");
			List<SysRole> list = JSONArray.parseArray(jsonObject.getJSONArray("list").toJSONString(), SysRole.class);
			//授权
			if( "auth".equals(type) ){
				systemService.addAuthList(userId, list);
			}else{
				//取消授权
				systemService.cancelAuthList(userId, list);
			}
	        return new ResponseBean(true, 0, "请求成功", null);
		} catch (Exception e) {
			e.printStackTrace();
	        return new ResponseBean(false, 1001, "网络繁忙，请稍后再试", null);
		}
    }
    
    /**
     * 获取用户登录之后的导航菜单
     * 2018-11-13 新调整，增加按钮级别的菜单权限，获取菜单的接口做了调整，除了返回菜单树以外，还返回了用户的permissions
     * @return
     */
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/menu/nav")
    public String getMenuNav() {
    	JSONObject result = new JSONObject();
        AuthUser user = WebUtils.getCurrentUser();
        
        //获取菜单原始数据
        List<SysMenu> oldList = systemService.getMenuListByUserId(user.getId());
        //构建菜单树
        List<SysMenu> menuList = systemService.makeTree(oldList, true);
        //获取菜单中的权限标识
        Set<String> perSet = new HashSet<String>();
        for(SysMenu menu : oldList) {
        	if( StringUtils.isNotBlank(menu.getPermission()) ) {
        		perSet.add(menu.getPermission());
        	}
        }
        result.put("menus", menuList);
        result.put("permission", perSet);
        return result.toJSONString();
    }

    /**
     * Gets menu tree.
     *
     * @return the menu tree
     */
    @PreAuthorize("hasAuthority('sys:menu:view')")
    @GetMapping(value = "/menu/tree")
    public List<SysMenu> getMenuTree() {

        AuthUser user = WebUtils.getCurrentUser();

        return systemService.getMenuTree(user.getId());
    }

    /**
     * Gets menu list.
     *
     * @return the menu list
     */
    @PreAuthorize("hasAuthority('sys:menu:view')")
    @GetMapping(value = "/menu/list")
    public List<SysMenu> getMenuList() {

        AuthUser user = WebUtils.getCurrentUser();

        return systemService.getMenuList(user.getId());
    }
    
    @PostMapping(value = "/user/getUserAllRight")
    public ResponseBean getUserAllRight(@RequestBody JSONObject jsonObject) {
    	Integer userId = jsonObject.getInteger("userId");
    	//获取菜单原始数据
        List<SysMenu> oldList = systemService.getMenuListByUserId(userId);
        //构建菜单树
        List<SysMenu> menuList = systemService.makeTree(oldList, false);
        //将菜单数据转换成echarts要展示的数据
        List<RightTreeVo> treeList = systemService.translateTree(menuList);
        RightTreeVo rootTree = new RightTreeVo();
		rootTree.setId(1);
		rootTree.setIsShow(true);
		rootTree.setName("菜单树");
		rootTree.setChildren(treeList);
    	return new ResponseBean(true, 0, "请求成功", rootTree);
    }

    /**
     * Delete menu response entity.
     *
     * @param menuId the menu id
     * @return the response entity
     */
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @DeleteMapping(value = "/menu/delete")
    public ResponseBean deleteMenu(@RequestBody JSONObject jsonObject) {
    	Integer menuId = jsonObject.getInteger("menuId");
        systemService.deleteMenuById(menuId);

        return new ResponseBean(true, 0, "请求成功", null);
    }

    /**
     * Gets menu.
     *
     * @param menuId the menu id
     * @return the menu
     */
    @PreAuthorize("hasAuthority('sys:menu:view')")
    @GetMapping(value = "/menu/getMenuInfo")
    public SysMenu getMenu(@RequestBody JSONObject jsonObject) {
    	Integer menuId = jsonObject.getInteger("menuId");
        return systemService.getMenuById(menuId);

    }

    /**
     * Save menu sys menu.
     *
     * @param menu the menu
     * @return the sys menu
     */
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    @PostMapping(value = "/menu/edit")
    public SysMenu saveMenu(@RequestBody JSONObject jsonObject) {
    	SysMenu menu = JSONObject.parseObject(jsonObject.getJSONObject("sysMenu").toJSONString(), SysMenu.class);
        return systemService.saveMenu(menu);
    }
    
    /**
     * <p>Title: getUserByLoginName</p>  
     * <p>Description: </p>  
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/user/getUserByLoginName")
    public String getUserByLoginName(@RequestBody JSONObject jsonObject) {
    	SysUser user = systemService.getUserByLoginName(jsonObject.getString("loginName"));
    	JSONObject result = new JSONObject();
    	result.put("user", user);
    	return result.toJSONString();
    }
    
}
