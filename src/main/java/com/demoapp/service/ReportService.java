package com.demoapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demoapp.dao.OrderRepository;
import com.demoapp.dto.ReportDto;

@Service
public class ReportService {

	@Autowired
	public OrderRepository orderRepository;

	public List<ReportDto> getReportsData() {
		return orderRepository.getReports();
	}

}
