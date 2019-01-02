package com.demoapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demoapp.dto.ReportDto;
import com.demoapp.entity.elastic.Order;
import com.demoapp.service.ElasticService;
import com.demoapp.service.ReportService;

@RestController
@RequestMapping("/api")
public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private ReportService reportService;

	@Autowired
	private ElasticService elasticService;

	@GetMapping("report")
	public Map<String, Double> getReport() {
		Map<String, Double> map = new HashMap<>();
		for (ReportDto report : reportService.getReportsData()) {
			map.put(report.getDate().toString(), report.getAmount());
		}
		logger.info("Getting reports, size {}", map.size());
		return map;
	}

	@GetMapping("search")
	public List<Order> search(@RequestParam String name) {
		logger.info("Search for {}", name);
		return elasticService.search(name);
	}

	@PostMapping("refresh")
	public void refresh() {
		elasticService.refresh();
	}

}
