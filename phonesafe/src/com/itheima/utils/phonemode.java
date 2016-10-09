package com.itheima.utils;

public class phonemode {
	private String phone;
	private String mode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "phonemode [phone=" + phone + ", mode=" + mode + "]";
	}

}
