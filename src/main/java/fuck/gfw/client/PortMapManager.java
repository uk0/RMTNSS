// Copyright (c) 2015 firsh.ME
package fuck.gfw.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import fuck.gfw.rudp.Route;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PortMapManager {
	
	MapClient mapClient;

	ArrayList<MapRule> mapList=new ArrayList<MapRule>();

	HashMap<Integer, MapRule> mapRuleTable=new HashMap<Integer, MapRule>();

	String configFilePath="conf/conf_client.json";

	PortMapManager(MapClient mapClient){
		this.mapClient=mapClient;
		//listenPort();
		loadMapRule();
	}

	void addMapRule(MapRule mapRule) throws Exception{
		if(getMapRule(mapRule.name)!=null){
			throw new Exception("映射 "+mapRule.name+" 已存在,请修改名称!");
		}
		ServerSocket serverSocket=null;
		try {
			serverSocket = new ServerSocket(mapRule.getListen_port());
			listen(serverSocket);
			mapList.add(mapRule);
			mapRuleTable.put(mapRule.listen_port, mapRule);
			saveMapRule();
		} catch (IOException e2) {
			//e2.printStackTrace();
			throw new Exception("端口 "+mapRule.getListen_port()+" 已经被占用!");
		}finally{
			if(serverSocket!=null){
				serverSocket.close();
			}
		}
	}

	void removeMapRule(String name){
		MapRule mapRule=getMapRule(name);
		if(mapRule!=null){
			mapList.remove(mapRule);
			mapRuleTable.remove(mapRule.listen_port);
			if(mapRule.serverSocket!=null){
				try {
					mapRule.serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				saveMapRule();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void updateMapRule(MapRule mapRule_origin,MapRule mapRule_new) throws Exception{
		if(getMapRule(mapRule_new.name)!=null&&!mapRule_origin.name.equals(mapRule_new.name)){
			throw new Exception("映射 "+mapRule_new.name+" 已存在,请修改名称!");
		}
		ServerSocket serverSocket=null;
		if(mapRule_origin.listen_port!=mapRule_new.listen_port){
			try {
				serverSocket = new ServerSocket(mapRule_new.getListen_port());
				listen(serverSocket);
				mapRule_origin.using=false;
				if(mapRule_origin.serverSocket!=null){
					mapRule_origin.serverSocket.close();
				}
				mapRule_origin.serverSocket=serverSocket;
				mapRuleTable.remove(mapRule_origin.listen_port);
				mapRuleTable.put(mapRule_new.listen_port, mapRule_new);
			} catch (IOException e2) {
				//e2.printStackTrace();
				throw new Exception("端口 "+mapRule_new.getListen_port()+" 已经被占用!");
			}finally{
//				if(serverSocket!=null){
//					serverSocket.close();
//				}
			}
		}
		mapRule_origin.name=mapRule_new.name;
		mapRule_origin.listen_port=mapRule_new.listen_port;
		mapRule_origin.dst_port=mapRule_new.dst_port;
		mapRule_origin.dst_addr=mapRule_new.dst_addr;
		saveMapRule();

	}

	void saveMapRule() throws Exception{
		JSONObject json=new JSONObject();
		JSONArray json_map_list=new JSONArray();
		json.put("map_list", json_map_list);
		if(mapList.size()==0){

		}
		for(MapRule r:mapList){
			JSONObject json_rule=new JSONObject();
			json_rule.put("name", r.name);
			json_rule.put("listen_port", r.listen_port);
			json_rule.put("dst_port", r.dst_port);
			json_rule.put("dst_address",r.dst_addr);
			json_map_list.add(json_rule);
		}
		try {
			saveFile(json.toJSONString().getBytes("utf-8"), configFilePath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("保存失败!");
		}
	}

	void loadMapRule(){
		String content;
		JSONObject json=null;
		JSONArray dst_conf=null;
		MapRule mapRule = null;
		try {
			content = readFileUtf8(configFilePath);
			json=JSONObject.parseObject(content);
		    dst_conf = json.getJSONArray("dst");
			System.out.println(dst_conf.toJSONString());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		//TODO 添加端口解析
		for(Object e:dst_conf){
			JSONObject conf = (JSONObject)e;
			mapRule=new MapRule();
			mapRule.name=conf.getString("name");
			mapRule.listen_port=conf.getIntValue("listen_port");
			mapRule.dst_port=conf.getIntValue("dst_port");
			mapRule.dst_addr =conf.getString("dst_address");
			mapList.add(mapRule);

				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(mapRule.getListen_port());
					listen(serverSocket);
					mapRule.serverSocket=serverSocket;
				} catch (IOException e1) {
					mapRule.using=true;
					e1.printStackTrace();
				}
				mapRuleTable.put(mapRule.listen_port, mapRule);
		} //for循环
	}

	MapRule getMapRule(String name){
		MapRule rule=null;
		for(MapRule r:mapList){
			if(r.getName().equals(name)){
				rule=r;
				break;
			}
		}
		return rule;
	}

	public ArrayList<MapRule> getMapList() {
		return mapList;
	}

	public void setMapList(ArrayList<MapRule> mapList) {
		this.mapList = mapList;
	}

	void listen(final ServerSocket serverSocket){
		Route.es.execute(new Runnable() {

			@Override
			public void run() {
				while(true){
					try {
						final Socket socket=serverSocket.accept();
						Route.es.execute(new Runnable() {

							@Override
							public void run() {
								int listenPort=serverSocket.getLocalPort();
								MapRule mapRule=mapRuleTable.get(listenPort);
								if(mapRule!=null){
									Route route=null;
									if(mapClient.isUseTcp()){
										route=mapClient.route_tcp;
									}else {
										route=mapClient.route_udp;
									}
									PortMapProcess process=new PortMapProcess(mapClient,route, socket,mapClient.serverAddress,mapClient.serverPort,null,
											mapRule.dst_addr,mapRule.dst_port);// 增加
								}
							}
							
						});

					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		});
	}
	
	void saveFile(byte[] data,String path) throws Exception{
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(path);
			fos.write(data);
		} catch (Exception e) {
			throw e;
		} finally {
			if(fos!=null){
				fos.close();
			}
		}
	}
	
	public static String readFileUtf8(String path) throws Exception{
		String str=null;
		FileInputStream fis=null;
		DataInputStream dis=null;
		try {
			File file=new File(path);

			int length=(int) file.length();
			byte[] data=new byte[length];

			fis=new FileInputStream(file);
			dis=new DataInputStream(fis);
			dis.readFully(data);
			str=new String(data,"utf-8");

		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dis!=null){
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return str;
	}
}
