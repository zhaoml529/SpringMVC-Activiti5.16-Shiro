package com.zml.oa.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.SuperviseReceive;
import com.zml.oa.pagination.Page;

/**
 * 督察接收
 * @author zhao
 *
 */
@Controller
@RequestMapping("/superviserReveive")
public class SuperviseReceiveController {

	/**
	 * 跳转到督察接收
	 * @return
	 */
	@RequestMapping(value = "/toSuperviseReceive")
	public ModelAndView toSuperviseReceive() {
		ModelAndView mv = new ModelAndView("/superviseReceive/list_receive");
		return mv;
	}
	
	/**
	 * 加载组信息-easyui
	 * @return
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public Datagrid<SuperviseReceive> getList(@RequestParam(value = "page", required = false) Integer page, 
								@RequestParam(value = "rows", required = false) Integer rows) throws Exception{
		Page<SuperviseReceive> p = new Page<SuperviseReceive>(page, rows);
		//this.groupService.getGroupListPage(p);
		Datagrid<SuperviseReceive> dataGrid = new Datagrid<SuperviseReceive>(p.getTotal(), p.getResult());
		return dataGrid;
	}
}
