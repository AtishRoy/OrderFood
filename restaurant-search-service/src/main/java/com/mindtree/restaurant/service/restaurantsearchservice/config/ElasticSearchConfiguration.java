package com.mindtree.restaurant.service.restaurantsearchservice.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
//@PropertySource("classpath:application.properties")
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

	Logger logger = LoggerFactory.getLogger(ElasticSearchConfiguration.class);


	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Value("${spring.elasticsearch.rest.uris}")
	private String serverHostAndPort;

	@Value("${spring.elasticsearch.rest.connection-timeout}")
	private int connectionTimeout;

	@Value("${spring.elasticsearch.rest.read-timeout}")
	private int readTimeout;

	@Override
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(serverHostAndPort)
				.withConnectTimeout(connectionTimeout).withSocketTimeout(readTimeout).build();

		return RestClients.create(clientConfiguration).rest();
	}
	
	@Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

}