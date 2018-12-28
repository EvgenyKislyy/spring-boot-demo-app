package com.demoapp.dto;

import java.time.LocalDate;

public class ReportDto {

	private LocalDate date;
	private Double amount;

	public ReportDto() {
		super();
	}

	public ReportDto(LocalDate date, Double amount) {
		super();
		this.date = date;
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
