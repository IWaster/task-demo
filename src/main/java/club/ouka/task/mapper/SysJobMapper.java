package club.ouka.task.mapper;

import club.ouka.task.entity.SysJob;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Description: 
 * User: Ouka
 * Date: 2020-03-28
 * Time: 19:36
 */
@Mapper
public interface SysJobMapper {

    /**
     * 定时任务列表
     * @param map
     * @return
     */
    List<SysJob> querySysJobList(HashMap<String, String> map);

    /**
     * 根据主键删除定时任务
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 创建新的定时任务
     * @param record
     * @return
     */
    int insert(SysJob record);

    /**
     * 更新定时任务
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(SysJob record);

    int updateByPrimaryKey(SysJob record);

    int clearQuqrtzMsg();

    int insertSysJobByList(List<SysJob> sysJobs);

    SysJob selectJobById(Integer id);

    SysJob selectJob(SysJob sysJob);
}