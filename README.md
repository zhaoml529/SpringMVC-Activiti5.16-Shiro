SpringOA简介
========

基于SpringMVC+Shiro+Activiti 5.16 的简单OA，可以快速入门Activiti学习用。

框架简介
--------
由于最近在学习Activit本项目参考Workflow讲义上的实战项目，并且加入了自己的东西，并不是照搬过来。<br>
框架以Spring Framework为核心、Spring MVC作为模型视图控制器、Shiro作为权限框架、Hibernate作为数据库操作层。<br>
本项目也可以为学习SpringMVC的同学提供帮助。<br>
本项目以查询待办任务、查待受理任务、查看运行中的流程以及流程控制中的一些问题为基础入门Activiti。<br>
功能有请假流程实例、报销流程实例、薪资调整流程实例等功能。<br>
继承Activiti自带的modeler,实现在线编辑流程文件。<br>
Shiro实现登录认证和权限控制，结合Ehcache缓存权限列表，毕竟权限表并不是总在变化。
用户在线列表，可以强制踢出。Shiro的密码的加密解密，并发登陆、会话管理等功能。

框架版本
--------
<ul>
<li>Activiti 5.16</li>
<li>Spring-4.0.0.RELEASE</li>
<li>Shiro-all-1.2.3</li>
<li>Hibernate-4.2.2.Final</li>
<li>Hibernate-validator-5.1.3.Final</li>
<li>validation-api-1.1.0.GA</li>
</ul>

数据库
-------
<ul>
<li>目前只支持MySql,建议MySql 5.5及以上</li>
</ul>


后续功能
--------
1.加入安全框架Shiro. ---已实现<br>
2.加入缓存 ehcache. --已实现<br>
3.前端页面jquery EasyUI<br>

首页
--------
![](https://github.com/zhaoml529/SpringOA/tree/EasyUI/WebContent/images/git_main.jpg)  
    
