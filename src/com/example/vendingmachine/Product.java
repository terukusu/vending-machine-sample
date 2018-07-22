package com.example.vendingmachine;

public class Product {
	private final int id;
	private final String name;
	private final int price;

	public Product(int id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append("(id=").append(getId())
			.append(",name=").append(getName())
			.append(",price=").append(getPrice())
			.append(")");
		
		return sb.toString();
	}
}
