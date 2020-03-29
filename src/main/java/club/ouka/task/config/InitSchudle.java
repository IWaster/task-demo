package club.ouka.task.config;

import club.ouka.task.Util.BaseJob;
import club.ouka.task.Util.Task2DB;
import club.ouka.task.annotation.Task;
import club.ouka.task.entity.SysJob;
import club.ouka.task.exception.TaskException;
import club.ouka.task.service.TaskService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: springboot启动时进行的操作
 * 此处查询库中的定时任务并执行
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:13
 */
@Log4j2
@Component
public class InitSchudle implements CommandLineRunner {
    private static final Task2DB INSTANCE = Task2DB.getInstance();
    @Autowired
    private TaskService taskService;
    @Autowired
    private  MyJobFactory myJobFactory;


    @Override
    public void run(String... args) throws Exception {
        //读取 项目中定时任务的类
        List<Class<? extends BaseJob>> classList = INSTANCE.getClassList("classpath:application.yml");
        //读取 定义的定时任务信息
        List<SysJob> taskMsg = INSTANCE.getTaskMsg(classList);
        //清空quartz官方数据库信息
        try {
            taskService.clearQuqrtzMsg();
        } catch (Exception e) {
            throw new TaskException("500","初始化清空仓库失败");
        }
        //插入代码定义的定时任务信息
        try {
            taskService.insertSysJobByList(taskMsg);
        } catch (Exception e) {
            throw new TaskException("500","插入定义的任务失败");
        }
        //读取定时任务信息加载定时任务
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("jobStatus", "0");
        List<SysJob> sysJobs = taskService.querySysJobList(map);
        if (null==sysJobs||sysJobs.size()==0) {
            log.info("======项目启动，没有要执行的任务======");
        }
        //获取调度器实例
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        // 如果不设置JobFactory，Service注入到Job会报空指针
        scheduler.setJobFactory(myJobFactory);
        // 启动调度器
        scheduler.start();

        for (SysJob job : sysJobs) {
            String jobClassName=job.getJobName();
            String jobGroupName=job.getJobGroup();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(job.getJobClassPath()).getClass()).withIdentity(jobClassName, jobGroupName).build();
            if (StringUtils.isNotEmpty(job.getJobDataMap())) {
                JSONObject jb = JSONObject.parseObject(job.getJobDataMap());
                Map<String, Object> dataMap = (Map<String, Object>)jb.get("data");
                for (Map.Entry<String, Object> m:dataMap.entrySet()) {
                    jobDetail.getJobDataMap().put(m.getKey(),m.getValue());
                }
            }
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobCron());
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                    .withSchedule(scheduleBuilder).startNow().build();

            // 任务不存在的时候才添加
            if( !scheduler.checkExists(jobDetail.getKey()) ){
                try {
                    scheduler.scheduleJob(jobDetail, trigger);
                } catch (SchedulerException e) {
                    log.info("\n创建定时任务失败"+e);
                    throw new Exception("创建定时任务失败");
                }
            }
        }
    }

    public static BaseJob getClass(String classname) throws Exception
    {
        Class<?>  c= Class.forName(classname);
        return (BaseJob)c.newInstance();
    }
}
