# api-collector

## 简介
使用Java Agent技术实现的API采集器，支持对JVM中的API进行采集
目前可以采集POST和GET请求
将获取的API信息写入到api_info.json文件中

## 源代码介绍

### ApiCollectorAgent.java
Agent的入口类，实现了Agent的premain方法和agentmain方法，用于在JVM启动时和运行中加载Agent

### ApiCollectorTransformer.java
实现了ClassFileTransformer接口，用于对字节码进行转换，实现对API的采集

### ApiInfo.java
API信息的实体类

### ApiInfoCollector.java
API信息的收集器，用于收集API信息