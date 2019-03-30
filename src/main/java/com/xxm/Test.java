package com.xxm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {

        final String startKiftd = "java -jar I:\\kiftd-master\\kiftd-1.0.16-RELEASE.jar -console";
        OpenKiftdWorker.open(startKiftd);


        String originPath = "I:\\Download";
        String targetPath = "I:\\null";

        AsyncWorker asyncWorker = new AsyncWorker(originPath,targetPath);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(asyncWorker);
    }
}
