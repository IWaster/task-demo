package club.ouka.task.Util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * Description:
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:33
 */
public interface BaseJob extends Job {
	void execute(JobExecutionContext context) throws JobExecutionException;
}

