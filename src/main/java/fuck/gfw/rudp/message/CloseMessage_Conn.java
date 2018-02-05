// Copyright (c) 2015 firsh.ME
package fuck.gfw.rudp.message;


import fuck.gfw.utils.ByteShortConvert;
import fuck.gfw.utils.ByteIntConvert;

import java.net.DatagramPacket;

public class CloseMessage_Conn extends Message {

	public short sType= MessageType.sType_CloseMessage_Conn;
	
	byte [] data;
	byte [] dpData;
		
	public CloseMessage_Conn(int connectId,int clientId){
		byte[] dpData=new byte[12];
		this.clientId=clientId;
		this.connectId=connectId;
		ByteShortConvert.toByteArray(ver, dpData, 0);
		ByteShortConvert.toByteArray(sType, dpData, 2);
		ByteIntConvert.toByteArray(connectId, dpData, 4);
		ByteIntConvert.toByteArray(clientId, dpData, 8);
		dp=new DatagramPacket(dpData,dpData.length);
	}
	
	public CloseMessage_Conn(DatagramPacket dp){
		this.dp=dp;
		dpData=dp.getData();
		ver=ByteShortConvert.toShort(dpData, 0);
		sType=ByteShortConvert.toShort(dpData, 2);
		connectId=ByteIntConvert.toInt(dpData, 4);
		clientId=ByteIntConvert.toInt(dpData, 8);
	}
	
}
