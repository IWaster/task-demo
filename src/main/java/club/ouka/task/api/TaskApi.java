package club.ouka.task.api;

import club.ouka.task.Util.IpUtil;
import club.ouka.task.Util.R;
import club.ouka.task.annotation.CheckIpAddr;
import club.ouka.task.entity.SysJob;
import club.ouka.task.service.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:38
 */
@Controller
public class TaskApi {

    @Autowired
    private TaskService taskService;

    @CheckIpAddr
    @GetMapping("task")
    public String industry(HttpServletRequest request) {
        return "JobManager";
    }

    @ResponseBody
    @RequestMapping("ip")
    public String test(HttpServletRequest request){return IpUtil.getIpAddr(request);}

    /**
     * 定时任务列表
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @CheckIpAddr
    @ResponseBody
    @RequestMapping("/task/queryTask")
    public R queryTask(HttpServletRequest request, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<SysJob> list = taskService.querySysJobList(null);
        PageInfo<SysJob> sysJobPageInfo = new PageInfo<>(list);
        return R.ok().put("list",list).put("number",sysJobPageInfo.getTotal());
    }

    /**
     * 任务状态修改
     * @param request
     * @param id 任务id
     * @param statusCode 0启动 1暂停 2删除
     * @return
     */
    @CheckIpAddr
    @ResponseBody
    @RequestMapping("job/changeStatus")
    public R resumeJob(HttpServletRequest request,@RequestParam("id")String id,
                       @RequestParam("statusCode")String statusCode){
        Assert.hasLength(id,"id不能为空");
        Assert.notNull(statusCode,"状态码不能为空");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("id",Integer.parseInt(id));
        int i = taskService.changeState(Integer.parseInt(id), Integer.parseInt(statusCode));
        return i==0? R.ok("修改失败"):R.error("修改成功");

    }

    /**
     * 更新任务cron
     * @param request
     * @param id
     * @param cron
     * @return
     */
    @CheckIpAddr
    @ResponseBody
    @RequestMapping("job/updateJob")
    public R updateJob(HttpServletRequest request,@RequestParam("id")String id,
                       @RequestParam("cron")String cron){
        Assert.hasLength(id,"id不能为空");
        Assert.isNull(cron,"cron表达式不能为空");
        int i = taskService.updateTask(Integer.parseInt(id), cron);
        return i==0? R.ok("更新失败"):R.error("更新成功");
    }

    /**
     * 添加任务
     * @param request
     * @param sysJob
     * @return
     */
    @CheckIpAddr
    @ResponseBody
    @PostMapping(value = "job/addJob",produces = "application/json;charset=UTF-8")
    public R addJob(HttpServletRequest request,@RequestBody SysJob sysJob){
        int i = taskService.addJob(sysJob);
        return i==0? R.ok("更新失败"):R.error("更新成功");
    }
}
