package com.itheima.utils;

public class smsInfos {
	private String date;
	private String address;
	private String type;
	private String body;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "smsInfos [date=" + date + ", address=" + address + ", type="
				+ type + ", body=" + body + "]";
	}

}
