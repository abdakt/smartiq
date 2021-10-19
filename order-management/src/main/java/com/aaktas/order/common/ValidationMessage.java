package com.aaktas.order.common;

public class ValidationMessage {

	String message;
	String description;
	boolean isValid;

	public ValidationMessage() {
	}

	public ValidationMessage(String message, String description, boolean isValid) {

		this.message = message;
		this.description = description;
		this.isValid = isValid;
	}

	public ValidationMessage(boolean isValid) {
		this.isValid = isValid;
	}

	public String getMessage() {
		return message + " " + getDescription();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "ValidationMessage [message=" + message + ", description=" + description + ", isValid=" + isValid + "]";
	}
	

}
