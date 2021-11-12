package com.mindtree.order.management.ordermanagementservice.hazelcastconfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class HazelCastCacheConfig {
	
	@Bean
	public Config hazelCastConfig() {
		return new Config().setInstanceName("order").setInstanceName("customer")
				.setInstanceName("restaurant").setInstanceName("custRest")
				.addMapConfig(new MapConfig().setName("ordermanagement")
						.setMaxSizeConfig(
								new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
						.setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(100));
	}

}
