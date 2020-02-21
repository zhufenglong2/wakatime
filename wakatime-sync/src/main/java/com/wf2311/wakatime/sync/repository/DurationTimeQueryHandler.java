package com.wf2311.wakatime.sync.repository;

import com.wf2311.wakatime.sync.config.WakatimeProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 14:26.
 */
public interface DurationTimeQueryHandler<T> {

    <S extends T> List<S> saveAll(Iterable<S> entities);

    List<T> findByStartTimeBetweenAndApiKey(LocalDateTime startTime, LocalDateTime endTime, String apiKey);

    long countByStartTimeBetweenAndApiKey(LocalDateTime startTime, LocalDateTime endTime, String apiKey);

    long deleteByStartTimeBetweenAndApiKey(LocalDateTime startTime, LocalDateTime endTime, String apiKey);

    /**
     * @param day
     * 要注意如果之后的界面数据查询就不能用这个方法，因为apiKey要换为对应的职工apiKey
     */
    default List<T> queryByDayAndApiKey(LocalDate day) {
        return findByStartTimeBetweenAndApiKey(day.atStartOfDay(), day.plusDays(1).atStartOfDay().minusSeconds(1), WakatimeProperties.SECRET_API_KEY);
    }

    default long countByDayAndApiKey(LocalDate day) {
        return countByStartTimeBetweenAndApiKey(day.atStartOfDay(), day.plusDays(1).atStartOfDay().minusSeconds(1), WakatimeProperties.SECRET_API_KEY);
    }

    default long deleteByDayAndApiKey(LocalDate day) {
        return deleteByStartTimeBetweenAndApiKey(day.atStartOfDay(), day.plusDays(1).atStartOfDay().minusSeconds(1), WakatimeProperties.SECRET_API_KEY);
    }
}
