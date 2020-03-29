package club.ouka.task.task;

import club.ouka.task.Util.BaseJob;
import club.ouka.task.annotation.Task;
import club.ouka.task.entity.SysJob;
import club.ouka.task.mapper.SysJobMapper;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-29
 * Time: 8:33
 */
@Log4j2
@Task(jobName = "test3",
        jobGroup = "test3",
        jobCorn = "0/4 * * * * ?",
        jobClassPath = "club.ouka.task.task.Test",
        jobDescribe = "test3",
        jobStatus = -1)
public class Test implements BaseJob {
    HashMap<String,String> map = new HashMap<String,String>();
    public Test(){
        map.put("jobStatus", "0");
    }
    @Autowired
    SysJobMapper sysJobMapper;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<SysJob> list = sysJobMapper.querySysJobList(map);
        System.err.println(list);
    }
}
