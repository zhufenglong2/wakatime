package com.wf2311.wakatime.sync.entity;

import com.wf2311.wakatime.sync.util.CommonUtil;
import com.wf2311.wakatime.sync.util.StringArrayConverter;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2019-01-10 14:15.
 */
@Data
@Entity
@Table(name = "duration")
public class DurationEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 依赖
     */
    @Convert(converter = StringArrayConverter.class)
    private List<String> dependencies;

    /**
     * 持续时间（秒）
     */
    private Double duration;

    /**
     * 所属项目名称
     */
    private String project;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;


    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


    /**
     * secret api key
     */
    @Column(name = "user_api_key")
    private String apiKey;

    public void setProject(String project) {
        this.project = CommonUtil.subStringIfOverLength(project, 50);
    }

}