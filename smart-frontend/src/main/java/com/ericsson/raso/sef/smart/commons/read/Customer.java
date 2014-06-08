package com.ericsson.raso.sef.smart.commons.read;

public class Customer {

	private CustomerRead customerRead;
	private CustomerBucketRead customerBucketRead;
	private CustomerVersionRead customerVersionRead;

	public CustomerRead getCustomerRead() {
		return customerRead;
	}

	public void setCustomerRead(CustomerRead customerRead) {
		this.customerRead = customerRead;
	}

	public CustomerBucketRead getCustomerBucketRead() {
		return customerBucketRead;
	}

	public void setCustomerBucketRead(CustomerBucketRead customerBucketRead) {
		this.customerBucketRead = customerBucketRead;
	}

	public CustomerVersionRead getCustomerVersionRead() {
		return customerVersionRead;
	}

	public void setCustomerVersionRead(CustomerVersionRead customerVersionRead) {
		this.customerVersionRead = customerVersionRead;
	}

}
