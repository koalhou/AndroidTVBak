package com.open.demo.utils;

import com.open.demo.config.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/*
 * 测试类
 * 测试Https接口 post
 * 接收下属客户端上传样本,保存样本文件
 */
public class HttpUtils {

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * post方式请求服务器(https协议)
     *
     * @param u
     *            请求地址
     * @param charset
     *            编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws NoSuchProviderException
     */
    public static String get(String u, String charset)
            throws NoSuchAlgorithmException, KeyManagementException, IOException, NoSuchProviderException {
        try {
            URL url = new URL(u);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            System.out.println(conn.getResponseCode());
            System.out.println(conn.getResponseMessage());
            InputStream is = conn.getInputStream();
            if (is != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                return new String(outStream.toByteArray(),"UTF-8");
            }
            return null;//conn.getResponseMessage();



//            TrustManager[] tm = { new TrustAnyTrustManager() };
//            // SSLContext sc = SSLContext.getInstance("SSL");
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, tm, new java.security.SecureRandom());
//            URL console = new URL(u);
//
//            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
//            conn.setSSLSocketFactory(sc.getSocketFactory());
//            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("GET");
//            conn.connect();
////			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
////			out.write(content.getBytes(charset));
//            // 刷新、关闭
////			out.flush();
////			out.close();
//            InputStream is = conn.getInputStream();
//            if (is != null) {
//                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = is.read(buffer)) != -1) {
//                    outStream.write(buffer, 0, len);
//                }
//                is.close();
//                return new String(outStream.toByteArray(),"UTF-8");
//            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
//
//    public static void main(String[] args) throws ClientProtocolException, IOException {
//        try {
//            post("https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=50&channelId=UCJnvK2B5QvnT70ZK_an3eMg&key=AIzaSyCbl1b5Ga6q3u3vv41zt4egLAmnatkp5mU", "aaaaaaaaaaaaaaa", "UTF-8");
//        } catch (KeyManagementException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }


    public Map<String,List<String>> getAll(){
        HttpUtils hu = new HttpUtils();
        String query = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&maxResults=50&channelId=" + Config.YOUTUBE_CHANNEL_ID + "&key=" + Config.YOUTUBE_API_KEY;
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
    public List<String[]> getPlayListById(String plid) {
        HttpUtils hu = new HttpUtils();
        String query = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+plid+"&maxResults=50&key=" + Config.YOUTUBE_API_KEY;
        List<String[]> thisplvs = new ArrayList<String[]>();
        try {

//            Gson gson = new Gson();
            String vedioStr = hu.get(query, "UTF-8");
            Map<String, Object> videosOri = (Map<String,Object>)JacksonUtils.jsonToMapRuntimeException(vedioStr);
//            Map m = gson.fromJson(vedioStr, Map.class);
//            List l = JacksonUtils.jsonToList(vedioStr);
//            System.out.println(m);
//            List<Object> videos = (List<Object>) m.get("items");
            List<Object> videos = (List<Object>) videosOri.get("items");
            for(Object o : videos){
                Map<String, Object> vedioDetails = (Map<String,Object>)o;
                Map<String, Object> snippet = (Map<String,Object>)vedioDetails.get("snippet");
                Map<String,String> vvid = (Map<String,String>) snippet.get("resourceId");
                String vid = vvid.get("videoId");
                String title = (String)snippet.get("title");
                Map<String,Object> thumbnails = (Map<String,Object>)snippet.get("thumbnails");
                Map<String,String> medium = (Map<String,String>)thumbnails.get("medium");
                String vimg = (String)medium.get("url");
                String[] utd = {vid,title,vimg};
//                String[] utd = {vid};
                thisplvs.add(utd);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return thisplvs;
    }

}