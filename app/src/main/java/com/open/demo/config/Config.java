package com.open.demo.config;

import com.open.demo.utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2016/10/18.
 */

public class Config {
    private Config() {
    }

//    public static final String YOUTUBE_API_KEY = "AIzaSyAP1H0PtjMyfu1FZZs10-TEklKgesvEpQw";
    public static final String YOUTUBE_API_KEY = "AIzaSyCbl1b5Ga6q3u3vv41zt4egLAmnatkp5mU";
    public static  final String YOUTUBE_CHANNEL_ID = "UCJnvK2B5QvnT70ZK_an3eMg";

    public static Map<String, List<String>> allData = new HashMap<String,List<String>>();
    public static List<String> UTD_IDS = new ArrayList<String>();
    public static List<String> UTD_IMGS = new ArrayList<String>();
    public static List<String> UTD_TITLES = new ArrayList<String>();
    public static List<String> PL_IDS = new ArrayList<String>();
    public static List<String> PL_TITLES = new ArrayList<String>();
    public static List<String> PL_IMGS = new ArrayList<String>();

    public static void initPlayListDatas() {

//        AsyncTaskDataLoad atdl = new AsyncTaskDataLoad(allData);
//        atdl.execute();
        HttpUtils hu = new HttpUtils();
        allData = hu.getAll();
//        System.out.println(allData);
        List<String> updateToday = allData.get("UPDATE_TODAY");
        List<String> playLists = allData.get("PLAY_LISTS");
        for(String tds : updateToday){
            String[] td = tds.split("&&");
            UTD_IDS.add(td[0]);
            UTD_TITLES.add(td[1]);
            UTD_IMGS.add(td[2]);
        }
        for(String pls : playLists){
            String[] pl = pls.split("&&");
            PL_IDS.add(pl[0]);
            PL_TITLES.add(pl[1]);
            PL_IMGS.add(pl[2]);
        }
    }
}
