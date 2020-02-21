package com.wf2311.wakatime.sync.service.sync;

import com.wf2311.wakatime.sync.config.WakatimeProperties;
import com.wf2311.wakatime.sync.entity.UserApiKey;
import com.wf2311.wakatime.sync.repository.UserApiKeyRepository;
import com.wf2311.wakatime.sync.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.stream.Collectors;

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

    /**
     * @param start 开始同步数据的时间
     * @param end 结束同步数据的时间
     * 同步start开始的数据到end结束，包括end那天的数据
     */
    //    @Transactional(rollbackFor = Exception.class)
    public void sync(LocalDate start, LocalDate end) {
        LocalDate day = start;
        // 判断开始时间是否晚于结束时间，不晚于则进入循环，不使用before是因为end那天也要同步
        while (!day.isAfter(end)) {
            try {
                syncDay(day);
            } catch (Exception e) {
                CommonUtil.syncLogFail().error(e.getMessage(), e);
            }
            day = day.plusDays(1);
        }
    }
    @Resource
    UserApiKeyRepository userApiKeyRepository;
    public void syncDay(LocalDate day) {
        WakatimeProperties.SECRET_API_KEYS = userApiKeyRepository.findAll().stream().map(UserApiKey::getApiKey).collect(Collectors.toList());
        for (String apiKey : WakatimeProperties.SECRET_API_KEYS){
//            设置当前的用户SECRET_API_KEY
            WakatimeProperties.SECRET_API_KEY = apiKey;
            //        心跳同步
            heartBeatService.sync(day);
            //        持续时间同步
            durationService.sync(day);
//            //        每日总览数据同步
//            daySummaryService.sync(day);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncLastDay() {
        LocalDate start = LocalDate.now().minusDays(1);
        sync(start, start);
    }

    /**
     * 查询过去day天到昨天的数据
     * @param day 要同步多少天的数据
     */
    public void sync(int day) {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = LocalDate.now().minusDays(day);
        sync(start, end);
    }
}
