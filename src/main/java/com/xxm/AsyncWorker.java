package com.xxm;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class AsyncWorker implements Runnable {

    private final String arg1 = "sys_originpath";

    private final String arg2 = "sys_configpath";

    private final String arg3 = "sys_filepath";

    private final String arg4 = "sys_complier";

    private String sys_originpath;

    private String sys_configpath;

    private String sys_filepath;

    private String sys_complier;

    private String configPath;

    private CopyHelper helper = CopyInstance.getHelper();

    public AsyncWorker(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void run() {
        try {
            readConfig();
            listenTargetConfig();
        } catch (Exception e) {
            e.printStackTrace();
            //send mail
        }
    }

    public void readConfig() {
        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configPath));
            properties.load(bufferedReader);
            this.sys_originpath = properties.getProperty(arg1);
            this.sys_configpath = properties.getProperty(arg2);
            this.sys_filepath = properties.getProperty(arg3);
            this.sys_complier = properties.getProperty(arg4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一直对配置文件进行监听，如果配置文件改动，说明有新版本要编译，这时启动while循环对目标文件夹进行监听
     */
    public void listenTargetConfig() {
        try {
            Path path = new File(sys_configpath).toPath();
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = ws.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> watchEvent : watchEvents) {
                    System.out.println("配置文件被修改：" + watchEvent.context().toString());
                    //监听到配置文件变化，重新读取
                    readConfig();
                    //开始对origin文件夹监听
                    listenOriginDir();
                }
                watchKey.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenOriginDir() {
        try {
            int flag = 0;
            Set<String> files = new HashSet<>();
            Path path = new File(sys_originpath).toPath();
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
            while (flag<Integer.parseInt(sys_complier)) {
                WatchKey watchKey = ws.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                Thread.sleep(1000);
                for (WatchEvent<?> watchEvent : watchEvents) {
                    files.add(watchEvent.context().toString());
                }
                watchKey.reset();
                //假如监听300秒，因为编译是持续过程，成果物出现有先后顺序，所以等300秒后，假设文件都编译完成，之后开始同步
                flag++;
            }
            helper.syncFile(files,sys_originpath,sys_filepath);
            files.clear();
            //文件同步完毕

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
