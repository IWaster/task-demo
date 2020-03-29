package club.ouka.task.aspect;

import club.ouka.task.Util.IpUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-29
 * Time: 0:46
 */
@Component
@Aspect
@Log4j2
public class CheckIpAddrAdepter {

    @Value("${admin.backstage.ip}")
    private String ip;

    @Around("@annotation(club.ouka.task.annotation.CheckIpAddr)")
    public Object checkCompanyStatus(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest arg = (HttpServletRequest) joinPoint.getArgs()[0];
        String ipAddr = IpUtil.getIpAddr(arg);
        log.info("client ip:[{}]",ipAddr);
        for (String s : ip.split(",")) {
            if (s.equals(ipAddr)) {
                return  (Object) joinPoint.proceed();
            }
        }
        return "404";
    }
}
