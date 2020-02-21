package com.wf2311.wakatime.sync.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ZFL
 */
@Data
@Entity
@Table(name = "user_api_key")
public class UserApiKey {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="username")
    private String userName;
    @Column(name="post")
    private String post;
    @Column(name = "secret_api_key")
    private String apiKey;
}
