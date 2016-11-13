package org.dm.streamcombiner.model;


/**
 * 
 * @author Dusan Maruscak
 *
 */
public class Data  implements Cloneable {

	private Long timestamp;
	private String amount;

	public Data(Long timestamp, String amount) {
		this();
		this.timestamp = timestamp;
		this.amount = amount;
	}

	public Data() {
		super();
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Amount [timestamp=" + timestamp + ", amount=" + amount + "]";
	}

	public String toJSONString() {
		return String.format("{ \"data\": { \"timestamp\":%d, \"amount\":\"%s\" }}%n", getTimestamp(), getAmount());
	}

	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
