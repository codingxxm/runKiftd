package com.xxm;


import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWorker {

    final static String startKiftd = "java -jar /data/kiftd-master/kiftd-1.0.16-RELEASE.jar -console";

    public static void main(String[] args) {
        if(args==null){
            throw new NullPointerException("arguments error , can not be null");
        }
        OpenKiftdWorker.open(startKiftd);
        ExecutorService executor = Executors.newCachedThreadPool();
        for(String config:args){
            AsyncWorker asyncWorker = new AsyncWorker(config);
            executor.execute(asyncWorker);
        }
    }

}
