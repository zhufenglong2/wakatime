package com.wf2311.wakatime.sync.repository;


import com.wf2311.wakatime.sync.config.WakatimeProperties;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 14:26.
 */
public interface DaySummaryQueryHandler<T> {
    <S extends T> List<S> saveAll(Iterable<S> entities);

    List<T> findByDayBetweenAndApiKey(LocalDate startDay, LocalDate endDay, String apiKey);

    long countByDayBetweenAndApiKey(LocalDate startDay, LocalDate endDay, String apiKey);

    @Transactional
    void deleteByDayBetweenAndApiKey(LocalDate startDay, LocalDate endDay, String apiKey);

    default List<T> queryByDayAndApiKey(LocalDate day) {
        return findByDayBetweenAndApiKey(day, day, WakatimeProperties.SECRET_API_KEY);
    }

    default long countByDayAndApiKey(LocalDate day) {
        return countByDayBetweenAndApiKey(day, day, WakatimeProperties.SECRET_API_KEY);
    }

    default void deleteByDayAndApiKey(LocalDate day) {
        deleteByDayBetweenAndApiKey(day, day, WakatimeProperties.SECRET_API_KEY);
    }

    default List<T> queryByDayAndApiKey(LocalDate startDay, LocalDate endDay) {
        return findByDayBetweenAndApiKey(startDay, endDay, WakatimeProperties.SECRET_API_KEY);
    }
}
