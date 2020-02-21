package com.wf2311.wakatime.sync.spider;

import com.wf2311.wakatime.sync.domain.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-09 16:11.
 */
public class WakaTimeDataSpider {
    public static DaySummary summary(LocalDate date) {
        Map<String, String> params = MapBuilder.newBuilder()
                .putStart(date).putEnd(date).build();
        String s = JoddHttpClient.get(ApiUrl.SUMMARY, params);
//        将请求到的数据进行解析，反序列化为DaySummary对象
        DaySummary summary = JsonParser.parseList(s, DaySummary.class).get(0);
//        summary设置所请求的数据时间 用于之后插入
        summary.setDate(date);
        return summary;
    }

    public static List<DaySummary> summary(LocalDate start, LocalDate end) {
        Map<String, String> params = MapBuilder.newBuilder()
                .putStart(start).putEnd(end).build();
        String s = JoddHttpClient.get(ApiUrl.SUMMARY, params);
        return JsonParser.parseList(s, DaySummary.class);
    }

    public static List<HeartBeat> heartbeat(LocalDate date) {
//        调用MapBuilder的ofDate方法，将传入的date作为Map的value部分，对应的key是"date"
        Map<String, String> params = MapBuilder.ofDate(date).build();
        String s = JoddHttpClient.get(ApiUrl.HEART_BEATS, params);
        return JsonParser.parseList(s, HeartBeat.class);
    }

    public static List<Project> project() {
        String s = JoddHttpClient.get(ApiUrl.PROJECTS, Collections.emptyMap());
        return JsonParser.parseList(s, Project.class);
    }

    public static List<Duration> duration(LocalDate date) {
        Map<String, String> params = MapBuilder.ofDate(date).build();
        String s = JoddHttpClient.get(ApiUrl.DURATION, params);
        return JsonParser.parseList(s, Duration.class);
    }

    public static List<ProjectDuration> projectDuration(LocalDate date, String project) {
        Map<String, String> params = MapBuilder.ofDate(date).put("project", project).build();
        String s = JoddHttpClient.get(ApiUrl.DURATION, params);
        return JsonParser.parseList(s, ProjectDuration.class);
    }

    public static Project project(String name) throws UnsupportedEncodingException {
        String s = JoddHttpClient.get(ApiUrl.PROJECT_DETAIL + URLEncoder.encode(name, Charset.defaultCharset().displayName()).replace("+", "%20"), Collections.emptyMap());
        return JsonParser.parse(s, Project.class);
    }
}