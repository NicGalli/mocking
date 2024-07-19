package com.example;

import java.util.Objects;

public class Employee {
	private String id;
	private double salary;
	private boolean paid;

	public Employee(String id, double salary) {
		this.id = id;
		this.salary = salary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, paid, salary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id) && paid == other.paid
				&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the salary
	 */
	public double getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}

	/**
	 * @return the paid
	 */
	public boolean isPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
}
