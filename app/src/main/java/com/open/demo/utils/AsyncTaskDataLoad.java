package com.open.demo.utils;

import android.os.AsyncTask;

import com.open.demo.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2016/11/6.
 */

public class AsyncTaskDataLoad extends AsyncTask <String, Integer,Map<String, List<String>>>{

    private  Map<String, List<String>> resultStr = null;
    public AsyncTaskDataLoad(Map<String, List<String>> initStr){
        this.resultStr = initStr;
    }

    @Override
    public Map<String, List<String>> doInBackground(String... params) {

        HttpUtils hu = new HttpUtils();
        String query = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=5&channelId=" + Config.YOUTUBE_CHANNEL_ID + "&key=" + Config.YOUTUBE_API_KEY;
        String updateTodayQuery = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&order=date&channelId=" + Config.YOUTUBE_CHANNEL_ID + "&key=" + Config.YOUTUBE_API_KEY;
//String query = "https://www.baidu.com";
        Map<String, List<String>> datas = new HashMap<String, List<String>>();
        try {
            String playListStr = hu.get(query, "UTF-8");
            Map<String,Object> oriPlayLists = (Map<String, Object>) JacksonUtils.jsonToMap(playListStr);
//            System.out.println(oriPlayLists);
            List<Object> items = (List<Object>) oriPlayLists.get("items");
            List<String> playList = new ArrayList<String>();
            for(Object object : items){
                Map<String, Object> list = (Map<String, Object>)object;
                String plid = (String)list.get("id");
                Map<String,Object> snippet = (Map<String,Object>)list.get("snippet");
                String title = (String)snippet.get("title");
                Map<String,Object> thumbnails = (Map<String,Object>)snippet.get("thumbnails");
                Map<String,Object> medium = (Map<String,Object>)thumbnails.get("medium");
                String plImg = (String)medium.get("url");
                String pl = plid + "&&" + title + "&&" + plImg;
                playList.add(pl);
            }
            String updateToday = hu.get(updateTodayQuery,"UTF-8");
            Map<String, Object> updateTodayOri = (Map<String,Object>)JacksonUtils.jsonToMap(updateToday);
            List<Object> updateTodayitems = (List<Object>) updateTodayOri.get("items");
            List<String> utds = new ArrayList<String>();
            for(Object o : updateTodayitems){
                Map<String, Object> vedioDetails = (Map<String,Object>)o;
                Map<String, String> vvid = (Map<String,String>)vedioDetails.get("id");
                String vid = vvid.get("videoId");
                Map<String, Object> snippet = (Map<String,Object>)vedioDetails.get("snippet");
                String title = (String)snippet.get("title");
                Map<String,Object> thumbnails = (Map<String,Object>)snippet.get("thumbnails");
                Map<String,String> medium = (Map<String,String>)thumbnails.get("medium");
                String vimg = (String)medium.get("url");
                String utd = vid + "&&" + title + "&&" + vimg;
                utds.add(utd);
            }
            datas.put("PLAY_LISTS", playList);
            datas.put("UPDATE_TODAY", utds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }
//
//    private List<String> buildPL(String plid) {
//
//    }

    @Override
    protected void onPostExecute(Map<String, List<String>> result)
    {
        if(resultStr!=null && result!=null) {
            resultStr = result;
        }
        super.onPostExecute(result);
    }
}
