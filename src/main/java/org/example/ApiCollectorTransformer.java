package org.example;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ApiCollectorTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if(className==null){
            return classfileBuffer;
        }
        // 处理动态生成的代理类
        if (className.contains("$$EnhancerBySpringCGLIB$$")) {
            className = className.substring(0, className.indexOf("$$EnhancerBySpringCGLIB$$"));
        }
        if(className.contains("$$FastClassBySpringCGLIB$$")){
            className=className.substring(0,className.indexOf("$$FastClassBySpringCGLIB$$"));
        }
        className = className.replace(".", "/");
        // 只处理目标包下的类
        if (className.startsWith("org/javaweb/vuln/controller")) {
            try {
                ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass=classPool.get(className);
                // 遍历类中的所有方法
                for (CtMethod method : ctClass.getDeclaredMethods()) {
                    ApiInfo apiInfo = new ApiInfo();
                    AnnotationsAttribute attr = (AnnotationsAttribute)
                            method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
                    if (attr != null) {
                        for (Annotation annotation : attr.getAnnotations()) {
                            String annotationName = annotation.getTypeName();
                            if (annotationName.contains("RequestMapping") ||
                                    annotationName.contains("GetMapping") ||
                                    annotationName.contains("PostMapping")) {
                                // 提取注解信息
                                String url = annotation.getMemberValue("value").toString();
                                String methodType = annotationName.contains("GetMapping") ? "GET" :
                                        annotationName.contains("PostMapping") ? "POST" : "UNKNOWN";
                                apiInfo.setUrl(url);
                                apiInfo.setMethod(methodType);
                            }
                        }
                    }
                    // 获取方法参数类型
                    CtClass[] parameterTypes = method.getParameterTypes();
                    // 遍历方法参数
                    if (parameterTypes != null) {
                        for (int i = 0; i < parameterTypes.length; i++) {
                            apiInfo.addParameter("param"+i, parameterTypes[i].getName(), true);
                        }
                    }
                    // 将 ApiInfo 对象存储到 ApiCollector 中
                    ApiCollector.addApiInfo(apiInfo);
                }
                return ctClass.toBytecode();
            } catch (NotFoundException | IOException | CannotCompileException e) {
                throw new RuntimeException(e);
            }
        }
        return classfileBuffer; // 未修改的字节码
    }
}
