package club.ouka.task.service.impl;

import club.ouka.task.Util.SchedulerUtil;
import club.ouka.task.entity.SysJob;
import club.ouka.task.exception.TaskException;
import club.ouka.task.mapper.SysJobMapper;
import club.ouka.task.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:27
 */
@Log4j2
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private SysJobMapper sysJobMapper;

    /**
     * 定时任务列表
     *
     * @param map
     * @return
     */
    @Override
    public List<SysJob> querySysJobList(HashMap<String, String> map) {
        return sysJobMapper.querySysJobList(map);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSysJobByList(List<SysJob> sysJobs) {
        return sysJobMapper.insertSysJobByList(sysJobs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearQuqrtzMsg() {
        return sysJobMapper.clearQuqrtzMsg();
    }

    @Override
    public SysJob selectJobById(Integer id) {
        return sysJobMapper.selectJobById(id);
    }

    /**
     * 修改任务状态
      * @param id
     * @param i
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeState(Integer id, int i) {
        SysJob sysJob = sysJobMapper.selectJobById(id);
        if (sysJob.getJobStatus()==-1) {
            throw new TaskException("500","开发中的任务禁止修改状态,任务id"+id);
        }
        if (i==1||i==0) {
            SysJob updateBean = SysJob.builder().id(id).jobStatus(i).build();
            int i1 = sysJobMapper.updateByPrimaryKeySelective(updateBean);
        }else {
            int i1 = sysJobMapper.deleteByPrimaryKey(id);
        }
        switch (i){
            case 0: //启动
                try {
                    SchedulerUtil.jobresume(sysJob.getJobName(),sysJob.getJobGroup());
                    return 1;
                } catch (Exception e) {
                    log.error("修改状态失败[{}]","任务id:",id,"-操作类型:",i,"-",e);
                    throw new TaskException("500","更新任务状态失败[启动],任务id"+id);
                }
            case 1: //暂停
                try {
                    SchedulerUtil.jobPause(sysJob.getJobName(),sysJob.getJobGroup());
                    return 1;
                } catch (Exception e) {
                    log.error("修改状态失败[{}]","任务id:",id,"-操作类型:",i,"-",e);
                    throw new TaskException("500","更新任务状态失败[暂停],任务id"+id);
                }
            case 2: //删除
                try {
                    SchedulerUtil.jobdelete(sysJob.getJobName(),sysJob.getJobGroup());
                    return 1;
                } catch (Exception e) {
                    log.error("修改状态失败[{}]","任务id:",id,"-操作类型:",i,"-",e);
                    throw new TaskException("500","更新任务状态失败[删除],任务id"+id);
                }
                default:
                    log.error("修改状态失败,未知操作[{}]","任务id:",id,"-操作类型:",i,"-");
                    throw new TaskException("500","更新任务状态失败[未知操作],任务id"+id);
        }

    }

    /**
     * 更新任务cron
     * @param id
     * @param cron
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(Integer id, String cron) {
        SysJob sysJob = sysJobMapper.selectJobById(id);
        if (null==sysJob) {
            throw new TaskException("500","没有id为"+id+"的定时任务");
        }
        SysJob updateBean = SysJob.builder().id(id).jobCron(cron).build();
        int i = sysJobMapper.updateByPrimaryKeySelective(updateBean);
        return i;
    }

    /**
     * 增加定时任务
     * @param sysJob
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addJob(SysJob sysJob) {
        SysJob job = sysJobMapper.selectJob(sysJob);
        if (null==sysJob) {
            throw new TaskException("500","定时任务信息错误请核对");
        }
        if (job.getJobStatus()==-1) {
            throw new TaskException("500","开发中的任务禁止添加");
        }
        if (job.getJobStatus()==0) {
            throw new TaskException("500","任务已经执行,请勿重复添加");
        }
        sysJob.setId(0);
        int insert = sysJobMapper.insert(sysJob);
        try {
            SchedulerUtil.addJob(sysJob.getJobClassPath(),sysJob.getJobName(),sysJob.getJobGroup(),sysJob.getJobCron(),sysJob.getJobDataMap());
        } catch (Exception e) {
            throw new TaskException("500","任务添加失败");
        }
        return insert;
    }
}
