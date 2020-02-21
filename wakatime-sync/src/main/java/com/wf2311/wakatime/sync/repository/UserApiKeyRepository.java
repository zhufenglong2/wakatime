package com.wf2311.wakatime.sync.repository;

import com.wf2311.wakatime.sync.entity.UserApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZFL
 */
public interface UserApiKeyRepository extends JpaRepository<UserApiKey,String> {
}
