package club.ouka.task.Util;

import club.ouka.task.annotation.Task;
import club.ouka.task.entity.SysJob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Description: 定时任务信息存放入库
 * User: Ouka
 * Date: 2020-03-28
 * Time: 20:34
 */
public class Task2DB {
    private static volatile Task2DB lazySingleton;
    /**
     * 扫描关键词
     */
    private static  final String SCAN_PACKAGE="task.msg.scanpackage";
    private Properties config = new Properties();
    private List<Class<?extends BaseJob>> classList = new ArrayList<>();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private Task2DB() {
        if (lazySingleton != null) {
            throw new RuntimeException("禁止多实例");
        }
    }

    public static Task2DB getInstance() {
        if (lazySingleton == null) {
            synchronized (Task2DB.class){
                lazySingleton = new Task2DB();}
        }
        return lazySingleton;
    }


    /**
     * 获取定时任务类
     * @param lcoations
     * @return
     * @throws ClassNotFoundException
     */
    public List<Class<?extends BaseJob>> getClassList(String... lcoations) throws ClassNotFoundException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(lcoations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * 扫描包下类
     * @param scanPackage
     * @return
     * @throws ClassNotFoundException
     */
    private List<Class<?extends BaseJob>> doScanner(String scanPackage) throws ClassNotFoundException {
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                Class<?> aClass = Class.forName(className);
                Task annotation = aClass.getAnnotation(Task.class);
                if (null!=annotation) {

                    classList.add((Class<? extends BaseJob>) aClass);
                }
            }
        }
        System.out.println(classList);
        return classList;
    }

    /**
     * 获取扫描到的定时任务的信息
     * @param clazzs
     * @return
     */
    public static List<SysJob> getTaskMsg(List<Class<? extends BaseJob>> clazzs){
        List<SysJob> list = new ArrayList<>();
        int i =0;
        for (Class<? extends BaseJob> clazz : clazzs) {
            Task annotation = clazz.getAnnotation(Task.class);
            SysJob build = SysJob.builder().id(++i)
                    .jobStatus(annotation.jobStatus())
                    .jobClassPath(annotation.jobClassPath())
                    .jobCron(annotation.jobCorn())
                    .jobDataMap(annotation.jobData())
                    .jobDescribe(annotation.jobDescribe())
                    .jobName(annotation.jobName())
                    .jobGroup(annotation.jobGroup())
                    .build();
            list.add(build);
        }

        return list;
    }

    /**
     * 测试
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Task2DB instance = Task2DB.getInstance();
        List<Class<? extends BaseJob>> classList = instance.getClassList("classpath:application.yml");
            instance.getTaskMsg(classList);
    }
}
