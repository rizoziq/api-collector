package org.example;

import java.lang.instrument.Instrumentation;

public class ApiCollectorAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Starting org.example.ApiCollectorAgent...");
        inst.addTransformer(new ApiCollectorTransformer());
        // 添加 Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down ApiCollectorAgent, writing API info to file...");
            ApiCollector.writeToFile("api_info.json");
        }));
    }


    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Attaching org.example.ApiCollectorAgent...");
        inst.addTransformer(new ApiCollectorTransformer(), true);
        // 添加 Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down ApiCollectorAgent, writing API info to file...");
            ApiCollector.writeToFile("api_info.json");
        }));
    }
}