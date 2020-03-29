package club.ouka.task.task;

import club.ouka.task.Util.BaseJob;
import club.ouka.task.annotation.Task;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 20:15
 */
@Log4j2
@Task(jobName = "test",
        jobGroup = "test",
        jobCorn = "0/2 * * * * ?",
        jobClassPath = "club.ouka.task.task.InsureMsgTask",
        jobDescribe = "test",
        jobStatus = 0)
public class InsureMsgTask implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("定时任务测试,执行时间{}",sdf.format(date));
    }
}
