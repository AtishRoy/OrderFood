package com.mindtree.restaurant.service.restaurantsearchservice.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;

@Configuration
@EnableElasticsearchRepositories
//@PropertySource("classpath:application.properties")
public class ElasticSearchConfiguration {

    Logger logger = LoggerFactory.getLogger(ElasticSearchConfiguration.class);

    /*@Value("${elastic.seach.home}")
    private String elasticsearchHome;*/

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;
    
    @Value("${elastic.search.port}")
    private String serverPort;
    
    @Value("${elastic.search.address}")
    private String serverIPAddress;
    
    private TransportClient client;

    @Bean
    public Client client() throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder().put("client.transport.sniff", true)
            .put("cluster.name", clusterName).build();
        client = new PreBuiltTransportClient(elasticsearchSettings);
        //String ipAddress = env.getProperty("elastic.search.address");
        //String portNumber = env.getProperty("elastic.search.port");
        client.addTransportAddress(
            new InetSocketTransportAddress(InetAddress.getByName(serverIPAddress), Integer.parseInt(serverPort)));
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(client());
        elasticsearchTemplate.createIndex(Restaurant.class);
        return elasticsearchTemplate;
    }
    
    @PreDestroy
    public void destroy() {
        client.close();
    }

}