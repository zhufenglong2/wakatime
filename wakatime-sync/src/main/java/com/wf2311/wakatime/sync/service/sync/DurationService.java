package com.wf2311.wakatime.sync.service.sync;

import com.wf2311.wakatime.sync.convert.DurationConverter;
import com.wf2311.wakatime.sync.convert.ProjectDurationConverter;
import com.wf2311.wakatime.sync.domain.Duration;
import com.wf2311.wakatime.sync.entity.DurationEntity;
import com.wf2311.wakatime.sync.entity.ProjectDurationEntity;
import com.wf2311.wakatime.sync.repository.DurationRepository;
import com.wf2311.wakatime.sync.repository.ProjectDurationRepository;
import com.wf2311.wakatime.sync.spider.WakaTimeDataSpider;
import com.wf2311.wakatime.sync.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 14:34.
 */
@Service
public class DurationService {
    @Resource
    private DurationRepository durationRepository;
    @Resource
    private ProjectDurationRepository projectDurationRepository;

    /**
     * 同步某天的数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int sync(LocalDate day) {
        long local = durationRepository.countByDayAndApiKey(day);
        List<Duration> vos = WakaTimeDataSpider.duration(day);
        int remote = vos != null ? vos.size() : 0;
        if (remote <= local) {
            CommonUtil.syncLog().info(String.format("%s的持续时间数据已是最新，无需同步。", day));
            return 0;
        }
        List<DurationEntity> durations = DurationConverter.of(vos).getDurations();
        if (!CollectionUtils.isEmpty(durations)) {
//            先删除本地数据，然后保存新查询到的数据
            deleteDataIfNotNull(day);
            durationRepository.saveAll(durations);
//          保存项目持续时间
            saveProjectDuration(day, getProjectDuration(day, durations));
        }
        int num = (int) (remote - local);
        CommonUtil.syncLog().info(String.format("%s的持续时间数据同步完毕，新增%d条记录。", day, num));
        return num;
    }

    /**
     * @param day 查询的某天日期
     * @param durations 查询到的数据转化为元素为DruationEntity类型的集合
     * @return 将持续时间通过list的流操作转换为元素为ProjictDurationEntity类型的集合
     */
    private List<ProjectDurationEntity> getProjectDuration(LocalDate day, List<DurationEntity> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            return Collections.emptyList();
        }
//      类型转换
        List<String> projects = durations.stream().map(DurationEntity::getProject).distinct().collect(toList());
        List<ProjectDurationEntity> result = new ArrayList<>();
        for (String p : projects) {
            result.addAll(getProjectDuration(day, p));
        }
        return result;
    }

    private void deleteDataIfNotNull(LocalDate day) {
        long existCount = durationRepository.countByDayAndApiKey(day);
        if (existCount > 0) {
            durationRepository.deleteByDayAndApiKey(day);
        }
    }

    private void saveProjectDuration(LocalDate day, List<ProjectDurationEntity> durations) {
        if (CollectionUtils.isEmpty(durations)) {
            return;
        }
        long existCount = projectDurationRepository.countByDayAndApiKey(day);
        if (existCount > 0) {
            projectDurationRepository.deleteByDayAndApiKey(day);
        }
        projectDurationRepository.saveAll(durations);
    }

    /**
     * 格式转换
     */
    private List<ProjectDurationEntity> getProjectDuration(LocalDate day, String project) {
        return ProjectDurationConverter.of(WakaTimeDataSpider.projectDuration(day, project)).getDurations();
    }
}
