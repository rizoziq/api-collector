package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ApiCollector {
    private static Map<String, ApiInfo> apiMap = new HashMap<>();

    public static void addApiInfo(String url, String method, List<ApiInfo.Parameter> parameters) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setUrl(url);
        apiInfo.setMethod(method);
        apiInfo.setParameters(parameters);
        apiMap.put(url, apiInfo);
    }
    public static void addApiInfo(ApiInfo apiInfo) {
        apiMap.put(apiInfo.getUrl(), apiInfo);
    }

    public static void writeToFile(String filePath) {
        System.out.println("Writing API info to file: " + filePath);
        Map<String, ApiInfo> filteredMap = new HashMap<>();
        for (Map.Entry<String, ApiInfo> entry : apiMap.entrySet()) {
            if (entry.getKey() != null) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.println("API info: " + filteredMap);
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);
        try {
            objectMapper.writeValue(jsonFile, filteredMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}