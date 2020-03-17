package com.like.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Sprider4Tencet {

	public static void main(String[] args) throws IOException, InterruptedException {
		File file = new File("D:/pingpang.txt");
		
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter =new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
		for(int j=1;j< 590;j++){
			String url = "http://c.v.qq.com/vchannelinfo?otype=json&uin=b50d59193206b3bab87401b4530cff9a&qm=1&pagenum="+j+"&num=24&sorttype=0&orderflag=0&callback=data&low_login=1";
			String reString = HttpClientUtil.httpPostJson(url, null);
			Thread.sleep(10);
			JSONObject jsonObject = JSONObject.fromObject(reString.replace("data(", "").replace(")", ""));
			JSONArray videolst = null;
			try{
				videolst= jsonObject.getJSONArray("videolst");
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			if(videolst != null){
				for(int i=0;i<videolst.size();i++){
					JSONObject video = videolst.getJSONObject(i);
					String content = "主题:"+video.getString("title")+" 上传时间:"+video.getString("uploadtime")+"点击量:"+video.getString("play_count")+"视频时长:"+video.getString("duration")+"\n";
					fileWriter.write(content);
				}
			}
		}
		fileWriter.flush();
        fileWriter.close();
	}
}
