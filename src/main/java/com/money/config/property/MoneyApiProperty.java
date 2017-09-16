package com.money.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("money")
public class MoneyApiProperty {

	private String originPermitida = "http://localhost:8000";

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}
}
