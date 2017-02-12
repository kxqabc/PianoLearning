package com.example.kxqabc.midi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
=======
import android.widget.RadioButton;
import android.widget.RadioGroup;
>>>>>>> 加入手动改变键面的功能
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxqabc on 2016/11/17.
 */
public class PlayMidiActivity extends Activity{
    private String fileName;
    private TextView mTimeScale;
    private ImageButton mPauseMusic;
    private ImageButton mRewind;
    private ImageButton mForward;
    private SeekBar mSeekBar;
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
=======
    private RadioGroup mRadioGroup;
    private RadioButton mPageOne;
    private RadioButton mPageTwo;
    private RadioButton mPageThree;
>>>>>>> 加入手动改变键面的功能

    private final int milliseconds = 10; //每隔10毫秒更新一次
    private int[] a;
    private boolean isFirstTimePlay=true;
    private boolean stopThread = false;
    private boolean paused=false;
    private Handler mHandler;
    private RelativeLayout rootGroup;
    private RelativeLayout group;
    private List<ObjectAnimator> animatorList = new ArrayList<>();
    private MediaPlayer player;
    private Object[] objects;
    private List<PlayMusic> playMusicList = new ArrayList<>();
    private static int flash_duration=5000;
    private static int time_sclase=0;
    private long begin_mills=0;
    private float flash_translation = 780f;
    private long rightnow_mills=0;
    private float weight = 0;
    private float height = 0;
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
=======
    private int pageNum;
>>>>>>> 加入手动改变键面的功能

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_midi);
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
        mTimeScale = (TextView) findViewById(R.id.timeScale);
        mPauseMusic = (ImageButton) findViewById(R.id.pause);
        mPauseMusic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mPauseMusic.setBackgroundResource(R.drawable.play);
        mRewind = (ImageButton) findViewById(R.id.rewind);
        mForward = (ImageButton) findViewById(R.id.fastForward);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        group = (RelativeLayout) findViewById(R.id.playMidiLayout);
        rootGroup = (RelativeLayout) findViewById(R.id.PlayMidiLayout);

        Intent intent = getIntent();
        fileName = intent.getStringExtra("name");
        Log.i("fileName",fileName);

        player = new MediaPlayer();

        AnalysisMain analysisMain = new AnalysisMain(Environment.getExternalStorageDirectory()+"/MidiPlayer",fileName);

        try {
            objects = analysisMain.readMidi.readDateFIle(analysisMain.midiFile);
            if(objects!=null){
            Log.e("objects长度:",""+objects.length);
=======
        initView();
        init();
    }

    private void init(){
        pageNum = 2;
        Intent intent = getIntent();
        fileName = intent.getStringExtra("name");
        Log.i("fileName",fileName);
        player = new MediaPlayer();
        AnalysisMain analysisMain = new AnalysisMain(Environment.getExternalStorageDirectory()+"/MidiPlayer",fileName);
        try {
            objects = analysisMain.readMidi.readDateFIle(analysisMain.midiFile);
            if(objects!=null){
                Log.e("objects长度:",""+objects.length);
>>>>>>> 加入手动改变键面的功能
            }else
            { Log.e("objects长度:","0000");}
            for(int i=0;i<objects.length;i++){
                PlayMusic playMusic = new PlayMusic((List<DataAnalyze.MidiItem>) objects[i]);
                printMidi(playMusic.midiItemList);
                playMusicList.add(playMusic);
            }
            Statistics(); //统计音符分布情况
            a = new int[playMusicList.size()];
            for(int b:a){
                b=0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787

=======
    }
    private void initView(){
        mTimeScale = (TextView) findViewById(R.id.timeScale);
        mPauseMusic = (ImageButton) findViewById(R.id.pause);
        mPauseMusic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mPauseMusic.setBackgroundResource(R.drawable.play);
        mRewind = (ImageButton) findViewById(R.id.rewind);
        mForward = (ImageButton) findViewById(R.id.fastForward);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        group = (RelativeLayout) findViewById(R.id.playMidiLayout);
        rootGroup = (RelativeLayout) findViewById(R.id.PlayMidiLayout);
        mRadioGroup = (RadioGroup) findViewById(R.id.pianoPage);
        mPageOne = (RadioButton) findViewById(R.id.pageOne);
        mPageTwo = (RadioButton) findViewById(R.id.pageTwo);
        mPageThree = (RadioButton) findViewById(R.id.pageThree);
>>>>>>> 加入手动改变键面的功能
        mPauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paused=!paused;
                mPauseMusic.setBackgroundResource(R.drawable.pause);
                if(isFirstTimePlay){
                    getLayoutSize(rootGroup);  //获取根Layout的长宽
                    isFirstTimePlay=false;
                    paused=false;
                    begin_mills=System.currentTimeMillis();
                    final String midPath = Environment.getExternalStorageDirectory()+"/MidiPlayer/"+fileName;
                    Log.e("filePath",midPath);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                player.setDataSource(midPath);
                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mediaPlayer.start();
                                    }
                                });
                                player.prepareAsync();
                            }catch (IOException e){
                                e.printStackTrace();
                                Log.e("出错了","Error");
                            }
                        }
                    },flash_duration);

                    new Thread(){
                        @Override
                        public void run(){
                            while(!stopThread){
                                try{
                                    sleep(milliseconds);
                                    mHandler.sendEmptyMessage(0);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                    Log.e("正在播放","starting");
                }else{
                    if(paused) {
                        mPauseMusic.setBackgroundResource(R.drawable.play);
                        player.pause();
                        for (ObjectAnimator anim : animatorList) {
                            anim.pause();
                        }
                    }else{
                        player.start();
                        mPauseMusic.setBackgroundResource(R.drawable.pause);
                        for (ObjectAnimator anim : animatorList) {
                            anim.resume();
                        }
                    }
                }

            }
        });
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787

=======
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = player.getDuration();
                int max = seekBar.getMax();
                player.seekTo(time*dest/max);
                Log.e("dest",""+dest);
                Log.e("time",""+time);
                Log.e("max",""+max);
                Log.e("seekTo",""+time*dest/max);
            }
        });
>>>>>>> 加入手动改变键面的功能
        mRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = player.getCurrentPosition();
                int time = player.getDuration();
                int max = mSeekBar.getMax();
                int new_position = 0;
                if(position==0){

                }else if(position<=flash_duration){
                    player.pause();
                    group.removeAllViews();
                    new_position = 0;
                    player.seekTo(new_position);
                    int numberOfPlaymusic = 0;
                    for(PlayMusic playMusic:playMusicList) {
                        if (playMusic.midiItemList.size() > 0) {
                            int i = 0;
                            int index = 0;
                            for (DataAnalyze.MidiItem midiItem : playMusic.midiItemList) {
                                i++;
                                if (midiItem.mstart_time >= (new_position) && midiItem.mstart_time <= (new_position+flash_duration)) {
                                    float translationX = (float) (new_position+flash_duration-midiItem.mstart_time) * (flash_translation / flash_duration);
                                    int duration = (int)(flash_duration*(1f - (translationX/flash_translation)));
                                    if(midiItem.music_scale>=2){
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                                        translateImp(midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX, flash_translation,duration);
=======
                                        translateImp(pageNum,midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX, flash_translation,duration);
>>>>>>> 加入手动改变键面的功能
                                    }
                                }else if (midiItem.mstart_time>(new_position+flash_duration)){
                                    index = i;
                                    break;
                                }
                            }
                            a[numberOfPlaymusic] = index;
                        }
                        numberOfPlaymusic++;
                    }
                    mSeekBar.setProgress(position*max/time);
                    player.start();
                }else{
                    player.pause();
                    group.removeAllViews();
                    new_position = position - flash_duration;
                    player.seekTo(new_position);
                    int numberOfPlaymusic = 0;
                    for(PlayMusic playMusic:playMusicList) {
                        if (playMusic.midiItemList.size() > 0) {
                            int i = 0;
                            int index = 0;
                            for (DataAnalyze.MidiItem midiItem : playMusic.midiItemList) {
                                i++;
                                if (midiItem.mstart_time >= (new_position) && midiItem.mstart_time <= (new_position+flash_duration)) {
                                    float translationX = (float) (new_position+flash_duration-midiItem.mstart_time) * (flash_translation / flash_duration);
                                    int duration = (int)(flash_duration*(1f - (translationX/flash_translation)));
                                    if(midiItem.music_scale>=2){
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                                        translateImp(midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX, flash_translation,duration);
=======
                                        translateImp(pageNum,midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX, flash_translation,duration);
>>>>>>> 加入手动改变键面的功能
                                    }
                                }else if (midiItem.mstart_time>(new_position+flash_duration)){
                                    index = i;
                                    break;
                                }
                            }
                            a[numberOfPlaymusic] = index;
                        }
                        numberOfPlaymusic++;
                    }
                    mSeekBar.setProgress(position*max/time);
                    player.start();
                    paused=false;
                    mPauseMusic.setBackgroundResource(R.drawable.pause);
                }

            }
        });

        mForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = player.getCurrentPosition();
                int time = player.getDuration();
                int max = mSeekBar.getMax();
                int new_position = 0;
                if(position>=(time-flash_duration)){//在音乐开始之前和将结束时禁用快进键

                }else if(position==0){

                }else{
                    player.pause();
                    group.removeAllViews();
                    new_position = position + flash_duration;
                    player.seekTo(new_position);
                    int numberOfPlaymusic = 0;
                    for(PlayMusic playMusic:playMusicList) {
                        if (playMusic.midiItemList.size() > 0) {
                            int i = 0;
                            int index = 0;
                            for (DataAnalyze.MidiItem midiItem : playMusic.midiItemList) {
                                i++;
                                if (midiItem.mstart_time >= (new_position) && midiItem.mstart_time <= (new_position+flash_duration)) {
                                    float translationX = (float) (new_position+flash_duration-midiItem.mstart_time) * (flash_translation / flash_duration);
                                    int duration = (int)(flash_duration*(1f - (translationX/flash_translation)));
                                    if(midiItem.music_scale>=2){
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                                    translateImp(midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX,flash_translation, duration);
=======
                                        translateImp(pageNum,midiItem.mduration, midiItem.music_note, midiItem.music_scale, 0, translationX,flash_translation, duration);
>>>>>>> 加入手动改变键面的功能
                                    }
                                }else if (midiItem.mstart_time>(new_position+flash_duration)){
                                    index = i;
                                    break;
                                }
                            }
                            a[numberOfPlaymusic] = index;
                        }
                        numberOfPlaymusic++;
                    }
                    mSeekBar.setProgress(position*max/time);
                    player.start();
                    paused=false;
                    mPauseMusic.setBackgroundResource(R.drawable.pause);
                }

            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(!stopThread){              //onDestory();中释放player的资源时防止再发送message，否则会报错！
                    switch (msg.what){
                        case 0:
                            rightnow_mills=(System.currentTimeMillis()-begin_mills);
                            int position;
                            int time;
                            int max;
                            /**防止音乐启动前不断尝试获取time=0，使得setProgress中的time=0作为被除数违法**/
                            if(player.isPlaying()||paused==true){
                                position = player.getCurrentPosition();//Log.i("当前位置",""+position);
                                time = player.getDuration();//Log.i("音乐时长",""+time);
                                max = mSeekBar.getMax();//Log.i("seekBar长度",""+max);
                                mSeekBar.setProgress(position*max/time);
                            }else{
                                position = 0;
                                mSeekBar.setProgress(position);
                            }
                            /********/
                            int i = 0;
                            for(PlayMusic playMusic:playMusicList) {
                                if (player.isPlaying()||paused==true) {  //音乐开始播放后使用音乐的进度时间，而不使用系统时间差了
                                    mPauseMusic.setClickable(true);      //恢复暂停键
                                    if(playMusic.midiItemList.size()!=0) {
                                        if ((playMusic.midiItemList.get(a[i]).mstart_time <= (position+flash_duration) )&& (a[i]<playMusic.midiItemList.size()-1)&&(!paused) ) { //如果时间轴到了第i个音轨的第a[i]个音符,!pause放在这里面是为了防止在暂停时还有动画产生！
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                                            if(playMusic.midiItemList.get(a[i]).music_scale>=2){
                                                translateImp(playMusic.midiItemList.get(a[i]).mduration, playMusic.midiItemList.get(a[i]).music_note, playMusic.midiItemList.get(a[i]).music_scale,i,0,flash_translation,flash_duration);
                                            }
=======
                                            createFlash(pageNum,playMusic.midiItemList.get(a[i]),0,flash_translation,flash_duration);
>>>>>>> 加入手动改变键面的功能
                                            a[i]++;
                                        }
                                    }
                                    i++;
                                } else {
                                    mPauseMusic.setClickable(false); //在动画到底前，即音乐开始前不允许点击暂停键
                                    if (playMusic.midiItemList.size() != 0) {
                                        if ((playMusic.midiItemList.get(a[i]).mstart_time <= rightnow_mills) && (a[i] < playMusic.midiItemList.size() - 1)) { //如果时间轴到了第i个音轨的第a[i]个音符
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                                            if(playMusic.midiItemList.get(a[i]).music_scale>=2){
                                                translateImp(playMusic.midiItemList.get(a[i]).mduration, playMusic.midiItemList.get(a[i]).music_note, playMusic.midiItemList.get(a[i]).music_scale, i,0,flash_translation,flash_duration);
                                            }
=======
                                            createFlash(pageNum,playMusic.midiItemList.get(a[i]),0,flash_translation,flash_duration);
>>>>>>> 加入手动改变键面的功能
                                            a[i]++;
                                        }
                                    }
                                    i++;
                                }
                            }
                            break;
                        default:
                            break;
                    }

                }else{Log.e("stopThread=true","211 line");}
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
                }

        };
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = player.getDuration();
                int max = seekBar.getMax();
                player.seekTo(time*dest/max);
                Log.e("dest",""+dest);
                Log.e("time",""+time);
                Log.e("max",""+max);
                Log.e("seekTo",""+time*dest/max);
            }
        });

=======
            }

        };
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                if(checkedId==mPageOne.getId()){
                    rootGroup.setBackgroundResource(R.drawable.background1);
                    pageNum = 1;
                }else if(checkedId==mPageTwo.getId()){
                    rootGroup.setBackgroundResource(R.drawable.background2);
                    pageNum = 2;
                }else if(checkedId==mPageThree.getId()){
                    rootGroup.setBackgroundResource(R.drawable.background3);
                    pageNum = 3;
                }
            }
        });
    }
    public void createFlash(int page_num, DataAnalyze.MidiItem midiItem ,float inital_position,float final_position,int  duration){
        int min_music_num = 0;
        int max_music_num = 0;
        switch (page_num){
            case 1:min_music_num = 21;max_music_num = 49;break;
            case 2:min_music_num = 49;max_music_num = 79;break;
            case 3:min_music_num = 79;max_music_num = 108;break;
        }
        if(((midiItem.music_scale+1)*12+midiItem.music_note)>=min_music_num && (midiItem.music_scale*12+midiItem.music_note)<max_music_num){
            translateImp(page_num,midiItem.mduration,midiItem.music_note,midiItem.music_scale,0,inital_position,final_position,duration);
        }
>>>>>>> 加入手动改变键面的功能
    }
    public void getLayoutSize(RelativeLayout group){
         weight = group.getMeasuredWidth();
         height = group.getMeasuredHeight();
        mTimeScale.setText(""+weight+"/"+height);
    }

    public class AnalysisMain{
        private File midiFile;
        private ReadMidi readMidi;
        public AnalysisMain(String filePath,String fileName){
            this.readMidi=new ReadMidi();
            this.midiFile=new File(filePath, fileName);
        }
    }
    public void printMidi(List<DataAnalyze.MidiItem> midiItemList){
        if(midiItemList.size()>0){
            for (int i=0;i<16;i++){
                Log.i("printmidi","第"+i+"个音符："+midiItemList.get(i).music_note+"/"+midiItemList.get(i).music_scale);
                Log.i("printmidi","第"+i+"个音符的开始时间："+midiItemList.get(i).mstart_time);
                Log.i("printmidi","第"+i+"个音符持续时间："+midiItemList.get(i).mduration);
                Log.i("********","********");
            }
        }else {
            Log.i("PlayMidiActivity","null");
        }
    }
    public void Statistics() {
        int minNote = 127;
        int maxNote = 0;
        int Note = 0;
        int n2_3 = 0;
        int n3_4 = 0;
        int n4_5 = 0;
        int n5_6 = 0;
        int n6_7 = 0;
        int n7_8 = 0;
        int n8_9 = 0;
        int n9_10 = 0;
        int n10_11 = 0;
        for (PlayMusic playMusic : playMusicList) {
            for (DataAnalyze.MidiItem item : playMusic.midiItemList) {
                Note = item.music_note + (item.music_scale + 1) * 12;
                //Log.e("音符",""+Note);
                if (Note <= minNote) {
                    minNote = Note;
                }
                if (Note > maxNote) {
                    maxNote = Note;
                }
                if (Note >= 20 && Note < 30) {
                    n2_3++;
                } else if (Note >= 30 && Note < 40) {
                    n3_4++;
                } else if (Note >= 40 && Note < 50) {
                    n4_5++;
                } else if (Note >= 50 && Note < 60) {
                    n5_6++;
                } else if (Note >= 60 && Note < 70) {
                    n6_7++;
                } else if (Note >= 70 && Note < 80) {
                    n7_8++;
                } else if (Note >= 80 && Note < 90) {
                    n8_9++;
                } else if (Note >= 90 && Note < 100) {
                    n9_10++;
                } else {
                    n10_11++;
                }

            }
            Log.e("统计", "二" + n2_3 + "/" + n3_4 + "/" + n4_5 + "/" + n5_6 + "/" + n6_7 + "/" + n7_8 + "/" + n8_9 + "/" + n9_10 + "/" + n10_11);
            Log.e("最低音符：", "" + minNote);
            Log.e("最高音符：", "" + maxNote);
        }
    }
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
    public void translateImp(int music_note_length,int music_note,int music_scale,int orbitColor,float initial_position,float final_position,int duration){
=======
    public void translateImp(int page_num,int music_note_length,int music_note,int music_scale,int orbitColor,float initial_position,float final_position,int duration){
>>>>>>> 加入手动改变键面的功能
        /***
         * 每当有一个琴键按下时，动态新建一个imageView，并将imageView加入到layout中
         **/
        int Duration=duration;
        float Y = 0;
<<<<<<< dd36a519de460df8b8c8b33b798e54ff3bc6f787
        if(music_note<=4){
            Y = (music_scale-2)*(weight/3)+(music_note+1)*(weight/(3*14));
        }else if(music_note>4){
            Y = (music_scale-2)*(weight/3)+(music_note+2)*(weight/(3*14));
        }
=======
        switch (page_num){
            case 1:{
                if(music_note<=4){
                    Y = (14*music_scale+music_note-9)*(weight/34);
                }else if(music_note>4){
                    Y = (14*music_scale+music_note-8)*(weight/34);
                }
            }break;
            case 2:{
                if(music_note<=4){
                   Y = (14*music_scale+music_note-43)*(weight/35);
                }else if(music_note>4){
                Y = (14*music_scale+music_note-42)*(weight/35);
                }
            }break;
            case 3:{
                if(music_note<=4){
                    Y = (14*music_scale+music_note-78)*(weight/35);
                }else if(music_note>4){
                    Y = (14*music_scale+music_note-77)*(weight/35);
                }
            }break;
        }

>>>>>>> 加入手动改变键面的功能
        final ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams params;
        int music_width;
        int music_length ;
        if(music_note_length>0 && music_note_length<ReadMidi.fen_qu/3){ //16
            music_width = 20;
            music_length = 30;
            params = new RelativeLayout.LayoutParams(music_width,music_length);
            switch(music_note){
                case 0:imageView.setBackgroundResource(R.drawable.c16);break;
                case 1:imageView.setBackgroundResource(R.drawable.cc16);break;
                case 2:imageView.setBackgroundResource(R.drawable.d16);break;
                case 3:imageView.setBackgroundResource(R.drawable.dd16);break;
                case 4:imageView.setBackgroundResource(R.drawable.e16);break;
                case 5:imageView.setBackgroundResource(R.drawable.f16);break;
                case 6:imageView.setBackgroundResource(R.drawable.ff16);break;
                case 7:imageView.setBackgroundResource(R.drawable.g16);break;
                case 8:imageView.setBackgroundResource(R.drawable.gg16);break;
                case 9:imageView.setBackgroundResource(R.drawable.a16);break;
                case 10:imageView.setBackgroundResource(R.drawable.aa16);break;
                case 11:imageView.setBackgroundResource(R.drawable.b16);break;
                default:break;
            }
        }else if(music_note_length>=ReadMidi.fen_qu/3 && music_note_length<ReadMidi.fen_qu*2/3){//8
            music_width = 20;
            music_length = 60;
            params = new RelativeLayout.LayoutParams(music_width,music_length);
            switch(music_note){
                case 0:imageView.setBackgroundResource(R.drawable.c8);break;
                case 1:imageView.setBackgroundResource(R.drawable.cc8);break;
                case 2:imageView.setBackgroundResource(R.drawable.d8);break;
                case 3:imageView.setBackgroundResource(R.drawable.dd8);break;
                case 4:imageView.setBackgroundResource(R.drawable.e8);break;
                case 5:imageView.setBackgroundResource(R.drawable.f8);break;
                case 6:imageView.setBackgroundResource(R.drawable.ff8);break;
                case 7:imageView.setBackgroundResource(R.drawable.g8);break;
                case 8:imageView.setBackgroundResource(R.drawable.gg8);break;
                case 9:imageView.setBackgroundResource(R.drawable.a8);break;
                case 10:imageView.setBackgroundResource(R.drawable.aa8);break;
                case 11:imageView.setBackgroundResource(R.drawable.b8);break;
                default:break;
            }
        }else if(music_note_length>=ReadMidi.fen_qu*2/3 && music_note_length>ReadMidi.fen_qu*3/2){//4
            music_width = 20;
            music_length = 90;
            params = new RelativeLayout.LayoutParams(music_width,music_length);
            switch(music_note){
                case 0:imageView.setImageResource(R.drawable.c4);break;
                case 1:imageView.setImageResource(R.drawable.cc4);break;
                case 2:imageView.setImageResource(R.drawable.d4);break;
                case 3:imageView.setImageResource(R.drawable.dd4);break;
                case 4:imageView.setImageResource(R.drawable.e4);break;
                case 5:imageView.setImageResource(R.drawable.f4);break;
                case 6:imageView.setImageResource(R.drawable.ff4);break;
                case 7:imageView.setImageResource(R.drawable.g4);break;
                case 8:imageView.setImageResource(R.drawable.gg4);break;
                case 9:imageView.setImageResource(R.drawable.a4);break;
                case 10:imageView.setImageResource(R.drawable.aa4);break;
                case 11:imageView.setImageResource(R.drawable.b4);break;
                default:break;
            }

        }else{//2
            music_width = 20;
            music_length = 120;
            params = new RelativeLayout.LayoutParams(music_width,music_length);
            switch(music_note){
                case 0:imageView.setImageResource(R.drawable.c2);break;
                case 1:imageView.setImageResource(R.drawable.cc2);break;
                case 2:imageView.setImageResource(R.drawable.d2);break;
                case 3:imageView.setImageResource(R.drawable.dd2);break;
                case 4:imageView.setImageResource(R.drawable.e2);break;
                case 5:imageView.setImageResource(R.drawable.f2);break;
                case 6:imageView.setImageResource(R.drawable.ff2);break;
                case 7:imageView.setImageResource(R.drawable.g2);break;
                case 8:imageView.setImageResource(R.drawable.gg2);break;
                case 9:imageView.setImageResource(R.drawable.a2);break;
                case 10:imageView.setImageResource(R.drawable.aa2);break;
                case 11:imageView.setImageResource(R.drawable.b2);break;
                default:break;
            }

        }
        params.addRule(RelativeLayout.ALIGN_START);
        params.leftMargin=(int)(Y-(music_width/2)); //距离顶部的距离
        //imageView.setBackgroundColor(android.graphics.Color.parseColor(color));
        imageView.setLayoutParams(params);
        imageView.setVisibility(View.VISIBLE);   //使补间动画的源图可见
        group.addView(imageView);
        //Animation animation= AnimationUtils.loadAnimation(this,R.anim.translation_demo_0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"translationY",(initial_position-music_length),(final_position-music_length));   //???????????
        animatorList.add(animator);
        animator.setDuration(Duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.e("动画结束", "onAnimationEnd");
                super.onAnimationEnd(animation);
                ViewGroup parent = (ViewGroup) imageView.getParent();
                if (parent != null)
                    parent.removeView(imageView);
            }
        });
    }
    public class PlayMusic{
        public List<DataAnalyze.MidiItem> midiItemList =new ArrayList<>();
        public PlayMusic(List<DataAnalyze.MidiItem> midiItemList) {
            this.midiItemList = midiItemList;
            Log.e("midiItemList长度：",""+midiItemList.size());
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopThread = true;
        time_sclase=0;
        player.stop();
        player.release();
    }
}
