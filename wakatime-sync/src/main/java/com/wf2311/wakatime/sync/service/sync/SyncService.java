package com.wf2311.wakatime.sync.service.sync;

import com.wf2311.wakatime.sync.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 16:21.
 */
@Service
@Slf4j
public class SyncService {

    @Resource
    private DurationService durationService;
    @Resource
    private HeartBeatService heartBeatService;
    @Resource
    private DaySummaryService daySummaryService;

    //    @Transactional(rollbackFor = Exception.class)
    public void sync(LocalDate start, LocalDate end) {
        LocalDate day = start;
        // 判断开始时间是否晚于结束时间，不晚于则进入循环
        while (!day.isAfter(end)) {
            try {
                syncDay(day);
            } catch (Exception e) {
                CommonUtil.syncLogFail().error(e.getMessage(), e);
            }
            day = day.plusDays(1);
        }
    }

    public void syncDay(LocalDate day) {
        heartBeatService.sync(day);
        durationService.sync(day);
        daySummaryService.sync(day);
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncLastDay() {
        LocalDate start = LocalDate.now().minusDays(1);
        sync(start, start);
    }

    public void sync(int day) {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = LocalDate.now().minusDays(day);
        sync(start, end);
    }
}
