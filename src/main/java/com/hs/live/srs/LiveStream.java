package com.hs.live.srs;

import com.alibaba.fastjson.JSONObject;

public class LiveStream {
	public String ip;//服务器ip
	public int id;
	public String app;
	public String name;
	public String url;  //http流地址		http://ip:8585
	public boolean active;
	public int clients ;
	//{"code":0,"server":49308,"stream":{"id":49310,"name":"dk","vhost":49309,"app":"live","live_ms":1611216178205,"clients":1,
	//"frames":83842,"send_bytes":47426233452939428,"recv_bytes":47426234264940626,"kbps":{"recv_30s":2499,"send_30s":0},"publish":{"active":true,"cid":538},"video":
	//{"codec":"H264","profile":"High","level":"3.1","width":1280,"height":720},"audio":{"codec":"AAC","sample_rate":44100,"channel":2,"profile":"LC"}}}
	public static LiveStream from(JSONObject jo) {
		LiveStream s = new LiveStream();
		if(jo.containsKey("id")) s.id = jo.getIntValue("id");
		if(jo.containsKey("app")) s.app = jo.getString("app");
		if(jo.containsKey("name")) s.name = jo.getString("name");
		if(jo.containsKey("clients")) s.clients= jo.getIntValue("clients");
		if(jo.containsKey("publish")) {
			JSONObject p = jo.getJSONObject("publish");
			s.active = p.getBooleanValue("active");
		}
		return s;
	}
}
