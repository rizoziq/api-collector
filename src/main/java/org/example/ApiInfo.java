package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// ApiInfo 类
@Data // 自动生成 Getter、Setter、toString、equals 和 hashCode 方法
@AllArgsConstructor // 自动生成全参数构造函数
@NoArgsConstructor // 自动生成无参构造函数
public class ApiInfo {

    private String url;           // API 的 URL
    private String method;        // 请求方法 (如 "GET", "POST", "PUT", "DELETE" 等)
    private List<Parameter> parameters=new ArrayList<>(); // 参数列表

    // 嵌套的 Parameter 类
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter {
        private String name;       // 参数名称
        private String type;       // 参数类型 (如 "String", "Integer", "Boolean" 等)
        private boolean required=true;  // 是否必填
    }

    public void addParameter(String name, String type, boolean required) {
        parameters.add(new Parameter(name, type, required));
    }
}