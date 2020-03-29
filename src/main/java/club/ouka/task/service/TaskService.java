package club.ouka.task.service;

import club.ouka.task.entity.SysJob;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:26
 */
public interface TaskService {
    /**
     * 定时任务列表
     * @param map
     * @return
     */
    List<SysJob> querySysJobList(HashMap<String, String> map);


    int insertSysJobByList(List<SysJob> sysJobs);

    int clearQuqrtzMsg();

    SysJob selectJobById(Integer id);

    int changeState(Integer id, int i);

    int updateTask(Integer id, String cron);

    int addJob(SysJob sysJob);
}
