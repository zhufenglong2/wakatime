package com.wf2311.wakatime.sync.repository;

import com.wf2311.wakatime.sync.entity.ProjectDurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 14:22.
 */
public interface ProjectDurationRepository extends JpaRepository<ProjectDurationEntity, Long>, DurationTimeQueryHandler<ProjectDurationEntity> {

    /**
     * 用于测试查询部分字段
     */
    @Query("select  p.id as id , p.branch as branch , count(p.duration) as total from  ProjectDurationEntity p group by p.endTime")
//    @Query("select p from  ProjectDurationEntity p group by p.endTime")
    List<ProjectDurationSummary> findGrounpByEndTime();
}
