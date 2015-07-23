package com.zml.oa.action;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zml.oa.entity.Datagrid;
import com.zml.oa.entity.Message;
import com.zml.oa.pagination.Page;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping("/modelAction")
public class ProcessModelAction {
	private static final Logger logger = Logger.getLogger(ProcessModelAction.class);
	
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 跳转模型列表
	 * @return
	 */
	@RequestMapping(value = "/toListModel")
	public String toListModel(){
		return "workflow/list_model";
	}
	
	
	/**
	 * 模型列表
	 * @return
	 */
	@RequestMapping(value = "/listModel")
	@ResponseBody
    public Datagrid<Model> modelList(
    		@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        Page<Model> p = new Page<Model>(page, rows);
        int[] pageParams = p.getPageParams(modelQuery.list().size());
        List<Model> list = modelQuery.listPage(pageParams[0], pageParams[1]);
        return new Datagrid<Model>(p.getTotal(), list);
    }
	
	/**
	 * 跳转创建模型页面
	 * @return
	 */
	@RequestMapping(value = "/toAddModel")
	public String toCreateModel(){
		return "workflow/add_model";
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

            response.sendRedirect(request.getContextPath() + "/modeler/service/editor?id=" + modelData.getId());
            return;
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
        }
    }
	
	/**
	 * 根据Model部署流程
	 * @param modelId
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "deploy/{modelId}")
    @ResponseBody
    public Message deploy(@PathVariable("modelId") String modelId) {
    	Message message = new Message();
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            message.setStatus(Boolean.TRUE);
            message.setMessage("部署成功，部署ID=" + deployment.getId()+" 请到【流程定义】菜单中查看！");
        } catch (Exception e) {
        	message.setStatus(Boolean.FALSE);
        	message.setMessage("根据模型部署流程失败:modelId="+modelId);
            logger.error("根据模型部署流程失败：modelId={}" + modelId, e);
        }
        return message;
    }

    /**
     * 导出model的xml文件
     * @param modelId
     * @param response
     */
    @RequestMapping(value = "export/{modelId}")
    public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={}" + modelId, e);
        }
    }

    /**
     * 删除模型
     * @param modelId
     * @return
     */
    @RequestMapping(value = "delete/{modelId}")
    @ResponseBody
    public Message delete(@PathVariable("modelId") String modelId) {
    	Message message = new Message();
    	try {
    		repositoryService.deleteModel(modelId);
    		 message.setStatus(Boolean.TRUE);
             message.setMessage("删除成功！");
		} catch (Exception e) {
			message.setStatus(Boolean.FALSE);
            message.setMessage("删除失败！");
		}
        return message;
    }
}
