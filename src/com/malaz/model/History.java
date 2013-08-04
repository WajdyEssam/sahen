package com.malaz.model;

public final class History {
	private final String id;
	private final Operation operation;
	private final String time;
	private final int amount;
	
	public static History getInstance(String id, Operation operation, String time, int amount) {
		return new History(id, operation, time, amount);
	}
	
	public String getId() {
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
	
	private History(String id, Operation operation, String time, int amount) {
		this.id = id;
		this.operation = operation;
		this.time = time;
		this.amount = amount;
	}
}

