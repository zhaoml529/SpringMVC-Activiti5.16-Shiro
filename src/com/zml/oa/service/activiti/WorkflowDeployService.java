package com.zml.oa.service.activiti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.zml.oa.util.WorkflowUtils;

/**
 * 流程部署控制器
 *
 * @author zml
 */
@Service
public class WorkflowDeployService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RepositoryService repositoryService;

    /**
     * 部署classpath下面的流程定义
     * <p>
     * 从属性配置文件中获取属性<b>workflow.modules</b>扫描**deployments**
     * </p>
     * <p>
     * 然后从每个**deployments/${module}**查找在属性配置文件中的属性**workflow.module.keys.${
     * submodule}**
     * <p>
     * 配置实例：
     * <p/>
     * <pre>
     * #workflow for deploy
     * workflow.modules=budget,erp,oa
     * workflow.module.keys.budget=budget
     * workflow.module.keys.erp=acceptInsurance,billing,effectInsurance,endorsement,payment
     * workflow.module.keys.oa=caruse,leave,officalstamp,officesupply,out,overtime
     * </pre>
     * <p/>
     * </p>
     *
     * @param processKey 流程定义KEY
     * @throws Exception
     */
    public void deployFromClasspath(String exportDir, String... processKey) throws Exception {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
//        String[] processKeys = {"salary", "vacation", "expense", "CountSalary"};
        List<String> processKeys = loadDeployFile(resourceLoader);
        for (String loopProcessKey : processKeys) {
            if (ArrayUtils.isNotEmpty(processKey)) {
            	//部署单个流程
                if (ArrayUtils.contains(processKey, loopProcessKey)) {
                    logger.debug("hit module of {}", (Object[]) processKey);
                    deploySingleProcess(resourceLoader, loopProcessKey, exportDir);
                } else {
                    logger.debug("module: {} not equals process key: {}, ignore and continue find next.", loopProcessKey, processKey);
                }
            } else {
            	//部署所有流程
                deploySingleProcess(resourceLoader, loopProcessKey, exportDir);
            }
        }
    }

    /**
     * 部署单个流程定义
     *
     * @param resourceLoader {@link ResourceLoader}
     * @param processKey     模块名称
     * @throws IOException 找不到zip文件时
     */
    private void deploySingleProcess(ResourceLoader resourceLoader, String processKey, String exportDir) throws IOException {
        String classpathResourceUrl = "classpath:/deploy/" + processKey;
        logger.debug("read workflow from: {}", classpathResourceUrl);
        Resource resource = resourceLoader.getResource(classpathResourceUrl);
        logger.info(" resource: "+ resource.exists());
        if(resource.exists()){
        	InputStream inputStream = resource.getInputStream();
	        if (inputStream == null) {
	            logger.warn("ignore deploy workflow module: {}", classpathResourceUrl);
	        } else {
	            logger.debug("finded workflow module: {}, deploy it!", classpathResourceUrl);
	            ZipInputStream zis = new ZipInputStream(inputStream);
	            Deployment deployment = repositoryService.createDeployment().addZipInputStream(zis).deploy();
	
	            // export diagram
	            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
	            for (ProcessDefinition processDefinition : list) {
	                WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
	            }
	        }
        }else{
        	logger.error("the file:[ {} ]is not exists!", classpathResourceUrl);
        }
    }

    /**
     * 读取资源目录下的流程文件
     * @param resourceLoader
     * @return
     * @throws Exception
     */
    public List<String> loadDeployFile(ResourceLoader resourceLoader) throws Exception {
    	List<String> processKeys = new ArrayList<>();
    	String classpathResourceUrl = "classpath:/deploy/";
        Resource resource = resourceLoader.getResource(classpathResourceUrl);
        File[] listFiles = resource.getFile().listFiles();
        for(File f : listFiles) {
			if(f.getName().endsWith(".zip")){
				processKeys.add(f.getName());
			}
		}
        return processKeys;
	}
    
    /**
     * 重新部署单个流程定义
     *
     * @param processKey 流程定义KEY
     * @throws Exception
     * @see #deployFromClasspath
     */
    public void redeploySingleFrom(String exportDir, String... processKey) throws Exception {
        this.deployFromClasspath(exportDir, processKey);
    }

    /**
     * 重新部署所有流程定义，调用：{@link #deployFromClasspath()}完成功能
     *
     * @throws Exception
     * @see #deployFromClasspath
     */
    public void deployAllFromClasspath(String exportDir) throws Exception {
        this.deployFromClasspath(exportDir);
    }

}
