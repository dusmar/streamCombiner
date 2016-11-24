package org.dm.streamcombiner.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents data XML fragment.
 * 
 * @author Dusan Maruscak
 */

@XmlRootElement
public class Data implements Serializable {

	/**
	 * Timestamp represented as Long because this field is used to determine
	 * order in output stream.
	 */
	private Long timestamp;

	/**
	 * Amount represented as String. Not to many "merged" operations are
	 * expected. May be changed to BigDecimal in future
	 */
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

}
