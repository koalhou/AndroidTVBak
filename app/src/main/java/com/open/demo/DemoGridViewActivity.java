/*
Copyright 2016 The Open Source Project

Author: hailongqiu <356752238@qq.com>
Maintainer: hailongqiu <356752238@qq.com>
					  pengjunkun <junkun@mgtv.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.GridViewTV;
import com.open.androidtvwidget.view.MainUpView;
import com.open.demo.utils.AsyncTaskImageLoad;
import com.open.demo.utils.HttpUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * GridView Demo测试.
 */
public class DemoGridViewActivity extends Activity {

    private List<String> data;
    private MainUpView mainUpView1;
    private View mOldView;
    private GridViewTV gridView;
    private GridViewAdapter mAdapter;
    private int mSavePos = -1;
//    private int mCount = 50;

    public static final String PlayList_ID = "AIzaSyAP1H0PtjMyfu1FZZs10-TEklKgesvEpQw";


    private static List<String[]> playList= new ArrayList<String[]>();
//    private static List<String[]> playList1= new ArrayList<String[]>();



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_grid_view);

        initPlaylist();
        gridView = (GridViewTV) findViewById(R.id.gridView);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        // 建议使用 NoDraw.
        mainUpView1.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
        bridget.setTranDurAnimTime(200);
        // 设置移动边框的图片.
        mainUpView1.setUpRectResource(R.drawable.white_light_10);
        // 移动方框缩小的距离.
        mainUpView1.setDrawUpRectPadding(new Rect(10, 10, 10, -55));
        // 加载数据.
        getData(playList.size());
        //
        updateGridViewAdapter();
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //
        gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 这里注意要加判断是否为NULL.
                 * 因为在重新加载数据以后会出问题.
                 */
                if (view != null) {
                    mainUpView1.setFocusView(view, mOldView, 1.2f);
                }
                mOldView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mFindhandler.removeCallbacksAndMessages(null);
//                mSavePos = position; // 保存原来的位置(不要按照我的抄，只是DEMO)
//                initGridViewData(new Random().nextInt(3));
//                mFindhandler.sendMessageDelayed(mFindhandler.obtainMessage(), 111);
//                Toast.makeText(getApplicationContext(), "GridView Item " + position + " pos:" + mSavePos, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), YouTubePlayerActivity.class);
                i.putExtra("key",playList.get(position)[0]);
                startActivity(i);
            }
        });
//        initGridViewData(new Random().nextInt(3));

        mFirstHandler.sendMessageDelayed(mFirstHandler.obtainMessage(), 188);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initPlaylist() {
        Intent i = getIntent();
        String plid = (String)i.getExtras().get("plid");
        HttpUtils hu = new HttpUtils();
        playList = hu.getPlayListById(plid);
//        playList1 = hu.getPlayListById1(plid);
    }

    // 延时请求初始位置的item.
    Handler mFirstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gridView.setDefualtSelect(2);
        }
    };

    // 更新数据后还原焦点框.
    Handler mFindhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mSavePos != -1) {
                gridView.requestFocusFromTouch();
                gridView.setSelection(mSavePos);
            }
        }
    };

    public List<String> getData(int count) {
        data = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            String text = playList.get(i)[1];
            data.add(text);
        }
        return data;
    }

    private void updateGridViewAdapter() {
        mAdapter = new GridViewAdapter(this, data);
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DemoGridView Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    ///// Adapter 类 start start //////////

    class GridViewAdapter extends BaseAdapter {

        private List<String> mDatas;
        private final LayoutInflater mInflater;

        public GridViewAdapter(Context context, List<String> data) {
            mDatas = data;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
                convertView.setTag(new ViewHolder(convertView));
                ImageView iv = (ImageView)convertView.findViewById(R.id.imgView);
//                iv.setImageBitmap(PicUtil.getbitmap("http://img.youtube.com/vi/"+playList.get(position)+"/1.jpg"));
//                LoadImage(iv,"http://img.youtube.com/vi/"+playList.get(position)[0]+"/0.jpg");
                    LoadImage(iv,playList.get(position)[2]);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            bindViewData(position, viewHolder);
            return convertView;
        }

        private void LoadImage(ImageView img, String path)
        {
            //异步加载图片资源
            AsyncTaskImageLoad async=new AsyncTaskImageLoad(img);
            //执行异步加载，并把图片的路径传送过去
            async.execute(path);

        }

        private void bindViewData(int position, ViewHolder viewHolder) {
            String title = mDatas.get(position);
            viewHolder.titleTv.setText(title);
        }

        class ViewHolder {
            View itemView;
            TextView titleTv;

            public ViewHolder(View view) {
                this.itemView = view;
                this.titleTv = (TextView) view.findViewById(R.id.textView);
            }
        }
    }
    class DownImage extends Thread {

        private ImageView imageView;
        private  String url;

        public DownImage(ImageView imageView, String url) {
            this.imageView = imageView;
            this.url = url;
        }
        @Override
        public void run(){
            Bitmap bitmap = null;
            try {
                //加载一个网络图片
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
//                bitmap = getWebPicture(url);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    ///// Adapter 类 end end //////////

}