// Copyright (c) 2015 firsh.ME
package fuck.gfw.utils;

import java.net.DatagramPacket;


public class MessageCheck{
	public static int checkVer(DatagramPacket dp){
		int ver= ByteShortConvert.toShort(dp.getData(), 0);
		return ver;
	}
	public static int checkSType(DatagramPacket dp){
		int sType= ByteShortConvert.toShort(dp.getData(), 2);
		return sType;
	}
}
