package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.neu.test.R;
import com.neu.test.layout.SwipeFlingAdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ShowImageActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, View.OnClickListener{

    Random ran = new Random();
    ArrayList<String> path;

    private int cardWidth;
    private int cardHeight;

    private SwipeFlingAdapterView swipeView;
    private InnerAdapter adapter;
    ArrayList<Bitmap> listBitmap2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        initView();
        path = new ArrayList<>();
        path = (ArrayList<String>) getIntent().getSerializableExtra("photo");
        loadData();
        swipeView.setAdapter(adapter);
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels);
        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        if (swipeView != null) {
            swipeView.setIsNeedSwipe(true);
            swipeView.setFlingListener(this);
            swipeView.setOnItemClickListener(this);
            adapter = new InnerAdapter();
            swipeView.setAdapter(adapter);
        }
    }



    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
    }

    @Override
    public void onRightCardExit(Object dataObject) {
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 3) {
            loadData2();
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

    @Override
    public void onClick(View v) {
    }

    private void loadData() {

//        path.add("http://39.97.108.172:8080/pic/yuhang/image_2020329202716.jpg");
//        path.add("http://39.97.108.172:8080/pic/yuhang/image_2020329201846.jpg");
//        path.add("http://39.97.108.172:8080/pic/yuhang/image_2020326101729.jpg");

        new AsyncTask<Void, Void, List<Bitmap>>() {
            @Override
            protected List<Bitmap> doInBackground(Void... params) {
                ArrayList<Bitmap> list = new ArrayList<>(path.size());
                Bitmap bitmap;
                for(int i=0;i<path.size();i++) {
                    bitmap = getHttpBitmap(path.get(i));
                    listBitmap2.add(bitmap);
                    list.add(bitmap);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Bitmap> list) {
                super.onPostExecute(list);
                adapter.addAll(list);
            }
        }.execute();
    }

    public void loadData2(){
        adapter.addAll(listBitmap2);
    }


    private class InnerAdapter extends BaseAdapter {

        ArrayList<Bitmap> objs;

        public InnerAdapter() {
            objs = new ArrayList<>();
        }

        public void addAll(Collection<Bitmap> collection) {
            Log.e("tttttt",isEmpty()+"");
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
                // swipeView.notify();
                //swipeView.setAdapter(adapter);
            } else {
                objs.addAll(collection);
            }
        }

        public void clear() {
            objs.clear();
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                objs.remove(index);
                Log.e("eeeeeeeeeee",index+"");
                if(objs.size()==1){
                    objs.addAll(listBitmap2);
                }
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public Bitmap getItem(int position) {
            if(objs==null ||objs.size()==0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Bitmap bitmap = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(ShowImageActivity.this).inflate(R.layout.card_new_item, parent, false);
                holder  = new ViewHolder();
                convertView.setTag(holder);
                //convertView.getLayoutParams().width = cardWidth;
                holder.portraitView = (ImageView) convertView.findViewById(R.id.portrait);
                //holder.portraitView.getLayoutParams().width = cardWidth;
                //holder.portraitView.getLayoutParams().height = cardHeight;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            Log.e("ttttttt1",position+"");
            Log.e("ttttttt3",listBitmap2.size()+"");

//      holder.portraitView.setImageResource(headerIcons[position]);
            holder.portraitView.setImageBitmap(bitmap);
            //holder.jobView.setText(talent.jobName);

            final CharSequence no = "暂无";

            return convertView;
        }

    }

    private static class ViewHolder {
        ImageView portraitView;
        CheckedTextView collectView;

    }
    public Bitmap getHttpBitmap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try{
            myFileUrl = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(1000);
            conn.setDoInput(true);
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
