package com.zml.oa.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zml.oa.entity.GroupAndResource;
import com.zml.oa.entity.Resource;
import com.zml.oa.entity.User;
import com.zml.oa.service.IGroupAndResourceService;
import com.zml.oa.service.IResourceService;
import com.zml.oa.service.IUserService;


/**
 * 首页控制器
 *
 * @author zml
 */
@Controller
public class MainController {

	@Autowired
	private IUserService userService;
	
    @Autowired
    private IGroupAndResourceService grService;
    
    @Autowired
    private IResourceService resourceService;
	
    @RequestMapping(value = "/north")
    public String index() {
        return "layout/north";
    }

    @RequestMapping(value = "/main")
    public String main() {
        return "layout/main";
    }
    
    @RequestMapping(value = "/center")
    public String center() {
    	return "layout/center";
    }
    
    @RequestMapping(value = "/south")
    public String nav() throws Exception {
    	return "layout/south";
    }
    
    @RequestMapping("/")
    public String index(Model model) throws Exception {
    	String username = (String) SecurityUtils.getSubject().getPrincipal();
    	User user = this.userService.getUserByName(username);
        List<GroupAndResource> grList = this.grService.getResource(user.getGroup().getId());
        List<Resource> menus = this.resourceService.getMenus(grList);
        model.addAttribute("menuList", menus);
        return "index";
    }
}
