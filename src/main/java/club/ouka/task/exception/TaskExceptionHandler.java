package club.ouka.task.exception;

import club.ouka.task.Util.R;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:41
 */
@RestController
@ControllerAdvice
@Log4j2
public class TaskExceptionHandler {
    @ExceptionHandler(value = TaskException.class)
    public R taskExceptionHandler(TaskException taskException){
        log.error("定时任务模块出现异常，异常信息[{}]",taskException.getMsg()+"-"+taskException.getCause());
        taskException.printStackTrace();
        return R.error(taskException.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception exception){
        log.error("定时任务模块出现异常，异常信息{}",exception.getCause());
        exception.printStackTrace();
        return R.error("请求失败");
    }
}
