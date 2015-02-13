package com.zml.oa.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zml.oa.pagination.PaginationThreadUtils;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping("/modelAction")
public class ProcessModelAction {
	private static final Logger logger = Logger.getLogger(ProcessModelAction.class);
	
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 模型列表
	 * @return
	 */
	@RequestMapping(value = "/listModel_page")
    public ModelAndView modelList() {
        ModelAndView mav = new ModelAndView("workflow/list_model");
        ModelQuery modelQuery = repositoryService.createModelQuery();
        int[] pageParams = PaginationThreadUtils.setPage(modelQuery.list().size());
        List<Model> list = modelQuery.listPage(pageParams[0], pageParams[1]);
        mav.addObject("list", list);
        return mav;
    }
	
	/**
	 * 创建模型
	 * @param name
	 * @param key
	 * @param description
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,
                       HttpServletRequest request, HttpServletResponse response) {
		logger.info("name: "+name+" key: "+key+" des: "+description);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

            response.sendRedirect(request.getContextPath() + "/service/editor?id=" + modelData.getId());
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
        }
    }
}
