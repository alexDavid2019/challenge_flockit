package com.flockit.enums;

public enum User_Role {
	ROLE_USER("ROLE_USER"), 
	ROLE_ADMIN("ROLE_ADMIN");

	private final String value;

	User_Role(final String newValue) {
		value = newValue;
	}

	public String getValue() {
		return value;
	}
}
