package club.ouka.task.entity;


import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Description: 
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:36
 */
@Builder
public class SysJob {
    /**
    * ID
    */
    private Integer id;

    /**
    * 任务名称
    */
    @NotBlank(message = "任务名称不能为空")
    private String jobName;

    /**
    * 任务组名
    */
    @NotBlank(message = "任务组不能为空")
    private String jobGroup;

    /**
    * 时间表达式
    */
    @NotBlank(message = "表达式不能为空")
    private String jobCron;

    /**
    * 类路径,全类型
    */
    @NotBlank(message = "限定类名不能为空")
    private String jobClassPath;

    /**
    * 传递map参数
    */
    private String jobDataMap;

    /**
    * 状态:1启用 0停用 -1开发中
    */
    private Integer jobStatus;

    /**
    * 任务功能描述
    */
    @NotBlank(message = "任务描述不能为空")
    private String jobDescribe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getJobClassPath() {
        return jobClassPath;
    }

    public void setJobClassPath(String jobClassPath) {
        this.jobClassPath = jobClassPath;
    }

    public String getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(String jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobDescribe() {
        return jobDescribe;
    }

    public void setJobDescribe(String jobDescribe) {
        this.jobDescribe = jobDescribe;
    }

    @Override
    public String toString() {
        return "SysJob{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobCron='" + jobCron + '\'' +
                ", jobClassPath='" + jobClassPath + '\'' +
                ", jobDataMap='" + jobDataMap + '\'' +
                ", jobStatus=" + jobStatus +
                ", jobDescribe='" + jobDescribe + '\'' +
                '}';
    }
}