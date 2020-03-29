package club.ouka.task.annotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 定时任务定义
 * User: Ouka
 * Date: 2020-03-28
 * Time: 20:03
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
    /**
     * 任务名
     * @return
     */
    String jobName();

    /**
     * 任务状态
     * @return
     */
    int jobStatus();

    /**
     * 任务组
     * @return
     */
    String jobGroup();

    /**
     * 任务corn表达式
     * @return
     */
    String jobCorn();

    /**
     * 任务类全限定类名
     * @return
     */
    String jobClassPath();

    /**
     * 需要用到的数据（json）
     * @return
     */
    String jobData() default "";

    /**
     * 任务描述
     * @return
     */
    String jobDescribe();
}
