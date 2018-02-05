// Copyright (c) 2015 firsh.ME
package fuck.gfw.rudp.message;

import fuck.gfw.utils.ByteShortConvert;
import fuck.gfw.utils.ByteIntConvert;

import java.net.DatagramPacket;


public class PingMessage extends Message {

	public short sType= MessageType.sType_PingMessage;
	
	byte[] dpData=new byte[20];
	
	int pingId;
	int downloadSpeed,uploadSpeed;
	
	public PingMessage(int connectId,int clientId,int pingId,int downloadSpeed,int uploadSpeed){
		ByteShortConvert.toByteArray(ver, dpData, 0);  //add: ver
		ByteShortConvert.toByteArray(sType, dpData, 2);  //add: service type
		ByteIntConvert.toByteArray(connectId, dpData, 4); //add: sequence
		ByteIntConvert.toByteArray(clientId, dpData, 8); //add: sequence
		ByteIntConvert.toByteArray(pingId, dpData, 12); //add: sequence
		ByteShortConvert.toByteArray((short) (downloadSpeed/1024), dpData, 16);
		ByteShortConvert.toByteArray((short) (uploadSpeed/1024), dpData, 18);
		dp=new DatagramPacket(dpData,dpData.length);
	}
	
	public PingMessage(DatagramPacket dp){
		this.dp=dp;
		dpData=dp.getData();
		ver=ByteShortConvert.toShort(dpData, 0);
		sType=ByteShortConvert.toShort(dpData, 2);
		connectId=ByteIntConvert.toInt(dpData, 4);
		clientId=ByteIntConvert.toInt(dpData, 8);
		pingId=ByteIntConvert.toInt(dpData, 12);
		downloadSpeed=ByteShortConvert.toShort(dpData, 16);
		uploadSpeed=ByteShortConvert.toShort(dpData, 18);
	}

	public int getPingId() {
		return pingId;
	}

	public void setPingId(int pingId) {
		this.pingId = pingId;
	}

	public int getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setDownloadSpeed(int downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	public int getUploadSpeed() {
		return uploadSpeed;
	}

	public void setUploadSpeed(int uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}

}
