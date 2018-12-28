package com.demoapp.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticConfig {

	@Value("${elasticsearch.host}")
	private String esHost;

	@Value("${elasticsearch.port}")
	private int esPort;

	@Value("${elasticsearch.cluster.name}")
	private String clusterName;

	@Bean
	public Client client() throws UnknownHostException {
		Settings esSettings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", false)
				.build();

		// https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
		TransportClient client = new PreBuiltTransportClient(esSettings);
		return client.addTransportAddress(new TransportAddress(InetAddress.getByName(esHost), esPort));
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() throws Exception {
		return new ElasticsearchTemplate(client());
	}

}