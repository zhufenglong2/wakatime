package com.wf2311.wakatime.sync;

import com.wf2311.wakatime.sync.config.WakatimeProperties;
import com.wf2311.wakatime.sync.domain.Duration;
import com.wf2311.wakatime.sync.entity.HeartBeatEntity;
import com.wf2311.wakatime.sync.entity.Time;
import com.wf2311.wakatime.sync.entity.UserApiKey;
import com.wf2311.wakatime.sync.message.MessageFactory;
import com.wf2311.wakatime.sync.repository.*;
import com.wf2311.wakatime.sync.service.sync.SyncService;
import com.wf2311.wakatime.sync.spider.WakaTimeDataSpider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 该类为测试类
 * 添加AbstractTransactionalJUnit4SpringContextTests是为了回滚数据库
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests extends AbstractTransactionalJUnit4SpringContextTests {

    @Test
    public void contextLoads() {
    }

    @Resource
    private TimeRepository timeRepository;
    @Resource
    private HeartBeatRepository heartBeatRepository;
    @Test
    public void test2() {
        Time time = new Time();
        time.setDay(LocalDate.now());
        time.setTime(LocalDateTime.now());
        System.out.println(time.getTime());
        timeRepository.save(time);
    }

    /**
     * 测试jpa获取数据库中数据
     */
    @Test
    public void test(){
        long id = 1;
        Optional<HeartBeatEntity> heartBeatEntity = heartBeatRepository.findById(id);
        heartBeatEntity.ifPresent(beatEntity -> System.out.println(beatEntity.toString()));
    }
    @Resource
    ProjectDurationRepository projectDurationRepository;
    /**
    * 测试获取每个项目的持续时间
    */
    @Test
    public void testProjectDurationTotal(){
        LocalDate start = LocalDate.now().minusDays(3);
        LocalDate end = LocalDate.parse("2020-02-18", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = start.atStartOfDay().atZone(zone).toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
//        System.out.println(localDateTime);
//        System.out.println(start+"\n"+end);
        List<ProjectDurationSummary> projectDurationEntities = projectDurationRepository.findGrounpByEndTime();
        for(ProjectDurationSummary projectDurationEntity :projectDurationEntities){
            System.out.println(projectDurationEntity.getId()+"  "+projectDurationEntity.getTotal());
        }
    }
    /**
     * 测试用户apikey表的信息查询
     */
    @Resource
    UserApiKeyRepository userApiKeyRepository;
    @Test
    public void testUserRepository(){
        String id = "10001";
        Optional<UserApiKey> userApiKey = userApiKeyRepository.findById(id);
        List<String> apiKeys = userApiKeyRepository.findAll().stream().map(UserApiKey::getApiKey).collect(Collectors.toList());
        if(userApiKey.isPresent()){
            System.out.println(userApiKey.toString());
        }
        for(String apiKey:apiKeys){
            System.out.println(apiKey);
        }
    }

    /**
     * 测试是否将用户apikey表中的字段传入常量类
     */
    @Test
    public void testProperties(){
        WakatimeProperties.SECRET_API_KEYS = userApiKeyRepository.findAll().stream().map(UserApiKey::getApiKey).collect(Collectors.toList());
        for(String apiKey: WakatimeProperties.SECRET_API_KEYS){
            System.out.println(apiKey);
        }
    }

    /**
     * 测试基于用户apiKey的各种查询
     */
    @Test
    public void testHeartBeatQuery(){
        long local = heartBeatRepository.countByTimeBetweenAndApiKey(LocalDate.parse("2020-02-20",DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
                LocalDate.parse("2020-02-20",DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).atStartOfDay().minusSeconds(1)
                , "78c6cd06-bd3e-4cc5-ae88-79b8274f1877");
        System.out.println(local);
    }

    @Resource
    private SyncService syncService;

    private void syncAllYear(Integer year) {
        CountDownLatch latch = new CountDownLatch(2);
        //fixme 经测试，使用12个线程并发执行，会被wakatime api拦截
        new Thread(() -> syncYearA(year,latch)).start();
//        new Thread(() -> syncYearB(year,latch)).start();
//        new Thread(() -> syncYearC(year,latch)).start();
        new Thread(() -> syncYearD(year,latch)).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void syncYearA(Integer year, CountDownLatch latch) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 5, 31);
        syncService.sync(start, end);
        latch.countDown();
    }

//    private void syncYearB(Integer year, CountDownLatch latch) {
//        LocalDate start = LocalDate.of(year, 4, 1);
//        LocalDate end = LocalDate.of(year, 6, 30);
//        syncService.sync(start, end);
//        latch.countDown();
//    }
//
//    private void syncYearC(Integer year, CountDownLatch latch) {
//        LocalDate start = LocalDate.of(year, 7, 1);
//        LocalDate end = LocalDate.of(year, 9, 30);
//        syncService.sync(start, end);
//        latch.countDown();
//    }

    private void syncYearD(Integer year, CountDownLatch latch) {
        LocalDate start = LocalDate.of(year, 6, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        syncService.sync(start, end);
        latch.countDown();
    }

    @Test
    public void sync2016() {
        syncAllYear(2016);
    }

    @Test
    public void sync2017() {
        syncAllYear(2017);
    }

    @Test
    public void sync2018() {
        syncAllYear(2018);
    }

    @Test
    public void sync2019A() {
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.now().minusDays(1);
        syncService.sync(start, end);
    }
    @Test
    public void syncCustom() {
        LocalDate start = LocalDate.of(2019, 2, 3);
        LocalDate end = LocalDate.now().minusDays(1);
        syncService.sync(start, end);
    }

    @Resource
    private MessageFactory messageFactory;

    @Test
    public void testProxy() {
        List<Duration> duration = WakaTimeDataSpider.duration(LocalDate.now().minusDays(1));
        System.out.println(duration.size());
    }


    @Test
    public void testSendMessage() {
        LocalDate day = LocalDate.of(2019, 1, 19);
        messageFactory.sendDayWakatimeInfo(day);
    }

}

