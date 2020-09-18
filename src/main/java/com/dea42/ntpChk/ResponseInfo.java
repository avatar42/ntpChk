/**
 * 
 */
package com.dea42.ntpChk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author avata
 *
 */
public class ResponseInfo {
	private long response_originate_time;
	private long response_receive_time;
	private long response_transmit_time;
	private long response_response_time;
	private long response_root_delay;
	private long response_dispersion;
	private long response_stratum;
	private long response_response_ticks;
	private long response_size;
	// errors found with response
	private List<String> errors = new ArrayList<String>();
	// hostname or IP
	private String host;
	// human label
	private String name;

	/**
	 * 
	 */
	public ResponseInfo() {
	}

	/**
	 * @param host
	 * @param name TODO
	 */
	public ResponseInfo(String host, String name) {
		this.host = host;
		this.name = name;
	}

	/**
	 * @return the response_originate_time
	 */
	public long getResponse_originate_time() {
		return response_originate_time;
	}

	/**
	 * @param response_originate_time the response_originate_time to set
	 */
	public void setResponse_originate_time(long response_originate_time) {
		this.response_originate_time = response_originate_time;
	}

	/**
	 * @return the response_receive_time
	 */
	public long getResponse_receive_time() {
		return response_receive_time;
	}

	/**
	 * @param response_receive_time the response_receive_time to set
	 */
	public void setResponse_receive_time(long response_receive_time) {
		this.response_receive_time = response_receive_time;
	}

	/**
	 * @return the response_transmit_time
	 */
	public long getResponse_transmit_time() {
		return response_transmit_time;
	}

	/**
	 * @param response_transmit_time the response_transmit_time to set
	 */
	public void setResponse_transmit_time(long response_transmit_time) {
		this.response_transmit_time = response_transmit_time;
	}

	/**
	 * @return the response_response_time
	 */
	public long getResponse_response_time() {
		return response_response_time;
	}

	/**
	 * @param response_response_time the response_response_time to set
	 */
	public void setResponse_response_time(long response_response_time) {
		this.response_response_time = response_response_time;
	}

	/**
	 * @return the response_root_delay
	 */
	public long getResponse_root_delay() {
		return response_root_delay;
	}

	/**
	 * @param response_root_delay the response_root_delay to set
	 */
	public void setResponse_root_delay(long response_root_delay) {
		this.response_root_delay = response_root_delay;
	}

	/**
	 * @return the response_dispersion
	 */
	public long getResponse_dispersion() {
		return response_dispersion;
	}

	/**
	 * @param response_dispersion the response_dispersion to set
	 */
	public void setResponse_dispersion(long response_dispersion) {
		this.response_dispersion = response_dispersion;
	}

	/**
	 * @return the response_stratum
	 */
	public long getResponse_stratum() {
		return response_stratum;
	}

	/**
	 * @param response_stratum the response_stratum to set
	 */
	public void setResponse_stratum(long response_stratum) {
		this.response_stratum = response_stratum;
	}

	/**
	 * @return the response_response_ticks
	 */
	public long getResponse_response_ticks() {
		return response_response_ticks;
	}

	/**
	 * @param response_response_ticks the response_response_ticks to set
	 */
	public void setResponse_response_ticks(long response_response_ticks) {
		this.response_response_ticks = response_response_ticks;
	}

	/**
	 * @return the response_size
	 */
	public long getResponse_size() {
		return response_size;
	}

	/**
	 * @param response_size the response_size to set
	 */
	public void setResponse_size(long response_size) {
		this.response_size = response_size;
	}

	/**
	 * @return the good
	 */
	public boolean isGood() {
		return errors.isEmpty();
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param error add to the errors list
	 */
	public void addError(String error) {
		this.errors.add(error);
	}

	public long getOffset() {
		return ((response_receive_time - response_originate_time) + (response_transmit_time - response_response_time))
				/ 2;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseInfo [response_originate_time=").append(response_originate_time)
				.append(", response_receive_time=").append(response_receive_time).append(", response_transmit_time=")
				.append(response_transmit_time).append(", response_response_time=").append(response_response_time)
				.append(", response_root_delay=").append(response_root_delay).append(", response_dispersion=")
				.append(response_dispersion).append(", response_stratum=").append(response_stratum)
				.append(", response_response_ticks=").append(response_response_ticks).append(", response_size=")
				.append(response_size).append(", errors=").append(errors).append(", host=").append(host)
				.append(", name=").append(name).append("]");
		return builder.toString();
	}

}
