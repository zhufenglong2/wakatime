package com.wf2311.wakatime.sync.config;

import com.wf2311.wakatime.sync.entity.UserApiKey;
import com.wf2311.wakatime.sync.repository.UserApiKeyRepository;
import javassist.ClassPath;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 13:38.
 */
@Configuration
@ConfigurationProperties(prefix = "wakatime")
public class WakatimeProperties {

    public static List<String> SECRET_API_KEYS;
    /**
     * 由于对于多个用户进行数据同步，每个方法都会需要传递用户SECRET_API_KEY，避免混淆，
     * 创建一个常量表示当前查询的用户SECRET_API_KEY
     */
    public static String SECRET_API_KEY;
    public static String PROXY_URL = null;

    private String secretApiKey;
    private String ftqqKey;
    private String dingdingKey;
    private Boolean fillNoDataDay;
    private String proxyUrl;

    private LocalDate startDay = LocalDate.now();

    public void setStartDay(String startDay) {
        if (startDay != null) {
            try {
                this.startDay = LocalDate.parse(startDay);
            } catch (Exception e) {
            }
        }
    }

    public LocalDate getStartDate() {
        return startDay;
    }

    public String getSecretApiKey() {
        return secretApiKey;
    }

    public void setSecretApiKey(String secretApiKey) {
        this.secretApiKey = secretApiKey;
        SECRET_API_KEY = secretApiKey;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
        PROXY_URL = proxyUrl;
    }

    public String getFtqqKey() {
        return ftqqKey;
    }

    public void setFtqqKey(String ftqqKey) {
        this.ftqqKey = ftqqKey;
    }

    public String getDingdingKey() {
        return dingdingKey;
    }

    public void setDingdingKey(String dingdingKey) {
        this.dingdingKey = dingdingKey;
    }

    public Boolean getFillNoDataDay() {
        return fillNoDataDay;
    }

    public void setFillNoDataDay(Boolean fillNoDataDay) {
        this.fillNoDataDay = fillNoDataDay;
    }
}
