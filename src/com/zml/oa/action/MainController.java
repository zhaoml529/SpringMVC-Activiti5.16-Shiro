package com.zml.oa.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制器
 *
 * @author zml
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping(value = "/top")
    public String index() {
        return "main/top";
    }

    @RequestMapping(value = "/welcome")
    public String welcome() {
        return "main/welcome";
    }
    
    @RequestMapping(value = "/nav")
    public String nav() {
    	return "main/nav";
    }

}
