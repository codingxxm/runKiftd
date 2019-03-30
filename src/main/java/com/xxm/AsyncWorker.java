package com.xxm;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AsyncWorker implements Runnable {

    private String originPath;

    private String targetPath;

    private Set<String> files = new HashSet<>();

    private Set<String> doneFiles = new HashSet<>();

    private OpenKiftdWorker worker = new OpenKiftdWorker();

    public AsyncWorker(String originPath, String targetPath) {
        worker.execCommand("-files");
        this.originPath = originPath;
        this.targetPath = targetPath;
    }

    @Override
    public void run() {
        try {
            Path path = new File(originPath).toPath();
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey watchKey = ws.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                Thread.sleep(500);
                for (WatchEvent<?> watchEvent : watchEvents) {
                    files.add(watchEvent.context().toString());
                }
                SyncFiles();
                watchKey.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //send mail
        }
    }

    public void SyncFiles() {
        System.out.println("开始复制文件");
        Iterator<String> iterator = files.iterator();
        while (iterator.hasNext()) {
            try {
                String file = iterator.next();
                FileUtils.copyFile(new File(originPath + File.separator + file), new File(targetPath + File.separator + file));
                worker.execCommand("import " + originPath + File.separator + file);
                doneFiles.add(file);
                System.out.println("已经将" + originPath + File.separator + file + "复制到" + targetPath + File.separator + file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        files.clear();
        System.out.println("同步完成");
    }

}
