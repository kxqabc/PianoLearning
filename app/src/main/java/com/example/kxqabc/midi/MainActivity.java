package com.example.kxqabc.midi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ListView mlistView;
    private ArrayList name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlistView = (ListView)findViewById(R.id.listView);
      /**创建自己的文件夹*/
        String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
        Log.i("path",externalStorageDirectory);
        File dieFirstFilder = new File(externalStorageDirectory+"/MidiPlayer");
        if(!dieFirstFilder.exists()){
            dieFirstFilder.mkdirs();
        }
      /**创建自己的文件夹*/
        name = new ArrayList();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory(); // 获得SD卡路径
            File[] files = path.listFiles();// 读取path下所有文件及文件夹
            getFileName(files);
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, name,
                    R.layout.list_item, new String[] { "Name" },
                    new int[] { R.id.fileName });
            mlistView.setAdapter(adapter);
            mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HashMap map = (HashMap) name.get(i);
                    String fileName = ""+map.get("Name");
                    Intent intent = new Intent(MainActivity.this,PlayMidiActivity.class);
                    intent.putExtra("name",fileName);
                    startActivity(intent);
                }
            });
        }
    }
    /***
     * 获取SD卡中mid文件的名称
     */
    private void getFileName(File[] files) {
        if (files != null)// 先判断目录是否为空，否则会报空指针
        {
            for (File file : files) {
                if (file.isDirectory()) {          //file是否是一个路径
                    getFileName(file.listFiles());  //如果file是一个路径，那么将该路径下所有的文件和路径作为实参送给getFileName();
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".MID")||fileName.endsWith(".mid")) {
                        HashMap map = new HashMap();
                        map.put("Name", fileName);  //点“.”前的文件名
                        name.add(map);
                    }
                }
            }
        }
    }
}
