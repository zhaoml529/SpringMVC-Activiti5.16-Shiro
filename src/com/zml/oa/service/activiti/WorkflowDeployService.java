package com.zml.oa.service.activiti;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
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
     * 第一种：部署classpath下面的流程定义
     * @param processKey 流程定义KEY
     * @throws Exception
     */
    public void deployFromClasspath(String exportDir, String... processKey) throws Exception {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
//      String[] processKeys = {"salary", "vacation", "expense", "CountSalary"};
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
     * 第二种：部署classpath/bpmn下的流程定义
     * @param exportDir
     * @param processKey
     * @throws Exception 
     */
    public void deployFromClassPathBpmn(String exportDir, String... processKey) throws Exception{
    	ResourceLoader resourceLoader = new DefaultResourceLoader();
//      String[] processKeys = {"salary.bpmn", "vacation.bpmn", "expense.bpmn", "CountSalary.bpmn"};
        List<String> processKeys = loadBpmnFile(resourceLoader);
        for (String loopProcessKey : processKeys) {
            if (ArrayUtils.isNotEmpty(processKey)) {
            	//部署单个流程
                if (ArrayUtils.contains(processKey, loopProcessKey)) {
                    logger.debug("hit module of {}", (Object[]) processKey);
                    deploySingleProcessByBpmn(resourceLoader, loopProcessKey, exportDir);
                } else {
                    logger.debug("module: {} not equals process key: {}, ignore and continue find next.", loopProcessKey, processKey);
                }
            } else {
            	//部署所有流程
            	deploySingleProcessByBpmn(resourceLoader, loopProcessKey, exportDir);
            }
        }
    }

    /**
     * 第一种：部署单个流程定义
     * 此处演示部署以.zip 或者.bar结尾的文件，也可以直接部署.bpmn文件。
     *
     * @param resourceLoader {@link ResourceLoader}
     * @param processKey     模块名称
     * @throws IOException 找不到zip或bar文件时
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
	            DeploymentBuilder builder = repositoryService.createDeployment();
	            builder.addZipInputStream(zis);
	            //该方法仅仅将DeploymentBuilder 的 isDuplicateFilterEnabled 属性设置为true
	            //目的是过滤重复部署（如果资源没有发生变化则不会重复部署）
	          //过滤关键是为流程部署起一个名字，没有名字不会过滤,甚至在oracle环境下会报错。
	            builder.name(processKey.substring(0, processKey.indexOf('.')));
	            builder.enableDuplicateFiltering();
	            Deployment deployment = builder.deploy();
	            
//	            Deployment deployment = repositoryService.createDeployment().addZipInputStream(zis).deploy(); //此方法会重复部署，不管资源变没变化
	
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
     * 第二种：部署.bpmn和.png文件
     * @param resourceLoader
     * @param processKey
     * @param exportDir
     * @throws IOException
     */
    private void deploySingleProcessByBpmn(ResourceLoader resourceLoader, String processKey, String exportDir) throws IOException {
    	System.out.println("processKey: "+processKey);
    	String classpathResourceUrl = "classpath:/bpmn/" + processKey;
    	Resource resource = resourceLoader.getResource(classpathResourceUrl);
    	if(resource.exists()){
    		InputStream inputStream = resource.getInputStream();
	        if (inputStream == null) {
	            logger.warn("ignore deploy workflow module: {}", classpathResourceUrl);
	        } else {
	        	logger.debug("finded workflow module: {}, deploy it!", classpathResourceUrl);
	        	System.out.println(resource.getFilename());
	        	String resourceName = resource.getFilename();
	        	DeploymentBuilder builder = repositoryService.createDeployment();
//	        	builder.addClasspathResource("bpmn/"+resourceName); 使用addClasspathResource方法将流程描述文件和流程图部署到数据库中
	        	builder.addInputStream(resourceName, inputStream);
	        	builder.name(resourceName);
	        	builder.enableDuplicateFiltering();
	        	builder.deploy();
	        }
    	}
    }
    
    /**
     * 第一种：读取资源目录下的流程文件.zip或者.bar
     * @param resourceLoader
     * @return
     * @throws Exception
     */
    public List<String> loadDeployFile(ResourceLoader resourceLoader) throws Exception {
    	List<String> processKeys = new ArrayList<>();
    	String classpathResourceUrl = "classpath:/deploy/";
        Resource resource = resourceLoader.getResource(classpathResourceUrl);
        File[] listFiles = resource.getFile().listFiles();
        if(listFiles.length != 0){
        	for(File f : listFiles) {
        		if(f.getName().endsWith(".zip") || f.getName().endsWith(".bar")){
        			processKeys.add(f.getName());
        		}
        	}
        }else{
        	logger.debug("The classpath:/deploy/ is empty!");
        	throw new Exception("The classpath:/deploy/ is empty!");
        }
        if(processKeys.size() == 0){
        	logger.debug("can not find .zip or .bar in classpath:/deploy/ !");
        	throw new Exception("can not find .zip or .bar in classpath:/deploy/ !");
        }
        return processKeys;
	}
    
    /**
     * 第二种：读取resource/bpmn目录下的流程描述文件
     * @param resourceLoader
     * @return
     * @throws Exception 
     */
    public List<String> loadBpmnFile(ResourceLoader resourceLoader) throws Exception{
    	List<String> processKeys = new ArrayList<String>();
    	String classpathResourceUrl = "classpath:/bpmn/";
    	Resource resource = resourceLoader.getResource(classpathResourceUrl);
    	File[] listFiles = resource.getFile().listFiles();
    	if(listFiles.length != 0){
	    	for(File f : listFiles) {
	    		String fileName = f.getName();
				if(fileName.endsWith(".bpmn") || fileName.endsWith(".png")){
					processKeys.add(fileName);
				}
			}
    	}else{
    		logger.debug("The classpath:/deploy/ is empty!");
    	}
    	if(processKeys.size() == 0){
        	logger.debug("can not find .bpmn or .png in classpath:/bpmn/ !");
        	throw new Exception("can not find .zip or .bar in classpath:/bpmn/ !");
        }
    	return processKeys;
    }
    
    /**
     * 方法一：重新部署单个流程定义
     *
     * @param processKey 流程定义KEY
     * @throws Exception
     * @see #deployFromClasspath
     */
    public void redeploySingleFrom(String exportDir, String... processKey) throws Exception {
        this.deployFromClasspath(exportDir, processKey);
    }

    /**
     * 方法一：重新部署所有流程定义，调用：{@link #deployFromClasspath()}完成功能
     *
     * @throws Exception
     * @see #deployFromClasspath
     */
    public void deployAllFromClasspath(String exportDir) throws Exception {
        this.deployFromClasspath(exportDir);
    }

    /**
     * 方法二：根据.bpmn文件部署
     * @param exportDir
     * @param processKey
     * @throws Exception
     */
    public void redeployBpmn(String exportDir, String... processKey) throws Exception {
    	this.deployFromClassPathBpmn(exportDir, processKey);
    }
    
    /**
     * 方法二：部署全部bpmn流程描述文件
     * @param exportDir
     * @throws Exception
     */
    public void redeployAllBpmn(String exportDir) throws Exception {
    	this.deployFromClassPathBpmn(exportDir);
    }
}
