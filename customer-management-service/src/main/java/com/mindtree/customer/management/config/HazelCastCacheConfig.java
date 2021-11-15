package com.mindtree.customer.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class HazelCastCacheConfig {
	@Bean
	public Config hazelCastCache() {
		return new Config().setInstanceName("email").setInstanceName("phoneNumber").addMapConfig(new MapConfig().setName("customermanagement")
				.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE)).setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(100));

	}
}
