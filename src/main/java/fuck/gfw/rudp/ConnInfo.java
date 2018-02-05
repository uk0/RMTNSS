// Copyright (c) 2015 firsh.ME
package fuck.gfw.rudp;

public class ConnInfo {

	
	String requestHost=null;
	
	String requestPath=null;
	
	boolean http=false;
	
	StreamPipe.HttpHost host=null;

	public String getRequestHost() {
		return requestHost;
	}

	public void setRequestHost(String requestHost) {
		this.requestHost = requestHost;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public boolean isHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public StreamPipe.HttpHost getHost() {
		return host;
	}

	public void setHost(StreamPipe.HttpHost host) {
		this.host = host;
	}
	
}
