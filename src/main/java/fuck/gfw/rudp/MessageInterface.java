// Copyright (c) 2015 firsh.ME
package fuck.gfw.rudp;

import java.net.DatagramPacket;

public interface MessageInterface {
	public int getVer();
	public int getSType();
	public DatagramPacket getDatagramPacket();
}
  
