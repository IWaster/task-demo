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
 * Date: 2020-03-29
 * Time: 0:06
 */
@Log4j2
@Task(jobName = "test2",
        jobGroup = "test2",
        jobCorn = "0/3 * * * * ?",
        jobClassPath = "club.ouka.task.task.ChunmingNiubi",
        jobDescribe = "test2",
        jobStatus = 1)
public class ChunmingNiubi implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.error("chunmingniubi,执行时间{}",sdf.format(date));
    }
}
