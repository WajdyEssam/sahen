package com.malaz.model;

public final class History {
	private final int id;
	private final Operation operation;
	private final String time;
	private final int amount;
	private final String detials;
	
	public static History getInstance(int id, Operation operation, String time, int amount, String detials) {
		return new History(id, operation, time, amount, detials);
	}
	
	public int getId() {
		return id;
	}

	public String getTime() {
		return time;
	}

	public Operation getOperation() {
		return operation;
	}

	public int getAmount() {
		return amount;
	}
	
	public String getDetials() {
		return detials;
	}
	
	private History(int id, Operation operation, String time, int amount, String detials) {
		this.id = id;
		this.operation = operation;
		this.time = time;
		this.amount = amount;
		this.detials = detials;
	}
}

