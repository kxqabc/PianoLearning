package com.example.kxqabc.midi;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxqabc on 2016/10/26.
 */
public class DataAnalyze {
    private byte[] midi_data;
    private int length;
    List<DataItem> dataItemList = new ArrayList<>();
    List<MidiItem> midiItemList = new ArrayList<>();
    List<MusicSpeed> musicSpeedList = new ArrayList<>();

    public DataAnalyze(byte[] midi_data) {
            this.midi_data = midi_data;
            this.length = midi_data.length;
    }

    public void getData(){
        int i=0;
        int time=0;
        int start_time=0;
        int music_scale=0;
        int music_note=0;
        int speed_start_time=0;
        List<String> typeList = new ArrayList<>();

        Log.i("华丽丽分割线","------------------------------------------------------");
        while (i<length){
            /**
             * 判断时间(这里的i时钟指向时间的最后一个时间位置)
             */
            if((midi_data[i]&0xFF) > 127){
                i=i+1;
                if((midi_data[i]&0xFF)>127){
                    i=i+1;
                    if((midi_data[i]&0xFF)>127){
                        i=i+1;
                        if((midi_data[i]&0xFF)>127){
                            i=i+1;}else {time = (midi_data[i]&0xFF)*1+(midi_data[i-1]&0x7F)*128+(midi_data[i-2]&0x7F)*128*128+(midi_data[i-3]&0x7F)*128*128*128;}
                    }else{time = (midi_data[i]&0xFF)*1+(midi_data[i-1]&0x7F)*128+(midi_data[i-2]&0x7F)*128*128;}
                }else{time = (midi_data[i]&0xFF)*1+(midi_data[i-1]&0x7F)*128;}
            }else{time = (midi_data[i]&0xFF)*1;}
            /**
             * 判断音符
             */

            /**
             * 判断事件类型（可能出现前后两事件公用的情况）
             */
        //Log.e("ff:",HexString(midi_data[i+1]));
        if((midi_data[i+1]&0xFF)>127||midi_data[i+1]==-1){   //如果此位置符合事件种类的要求（>=0x80）(-1指的是ff)
            /**判断音符(这里的i最后指向下一个事件的时间的第一位)**/
            String action = HexString(midi_data[i+1]);
            String[] a = action.split("");
            switch (a[1]){                     //a[1]是动作的代号，a[0]=0,a[2]=通道号
                case "8":typeList.add("松开");music_note = (midi_data[i+2]&0xFF) % 12;music_scale = ((midi_data[i+2]&0xFF)/12)-1;i=i+4;break;
                case "9":typeList.add("按下");music_note = (midi_data[i+2]&0xFF) % 12;music_scale = ((midi_data[i+2]&0xFF)/12)-1;i=i+4;break;
                case "a":typeList.add("Key after Touch");music_note = 0;music_scale = 0;i=i+4;break;
                case "b":typeList.add("控制器");music_note = 0;music_scale = 0;i=i+4;break;
                case "c":typeList.add("Program changes");music_note = 0;music_scale = 0;i=i+3;break;
                case "d":typeList.add("Aftertouch");i=i+4;music_note = 0;music_scale = 0;break;
                case "e":typeList.add("滑音");i=i+4;music_note = 0;music_scale = 0;break;
                case "f":{typeList.add("系统设置");Log.e("51",""+midi_data[i+2]);
                    if(midi_data[i+2]==81){
                        speed_start_time=speed_start_time+time;
                        int speed=0;
                        switch (midi_data[i+3]){
                            case 1:speed=midi_data[i+4];break;
                            case 2:speed=midi_data[i+4]*256+midi_data[i+5];break;
                            case 3:speed=(midi_data[i+4]&0xFF)*256*256+(midi_data[i+5]&0xFF)*256+(midi_data[i+6]&0xFF);break;
                            case 4:speed=midi_data[i+4]*256*256*256+midi_data[i+5]*256*256+midi_data[i+6]*256+midi_data[i+7];break;
                            case 5:speed=midi_data[i+4]*256*256*256*256+midi_data[i+5]*256*256*256+midi_data[i+6]*256*256+midi_data[i+7]*256+midi_data[i+8];break;
                            default:speed=600000;break;
                        }
                        double scale = (double) speed/(ReadMidi.fen_qu*1000);
//                        Log.e("DataAnalyze,fen_qu=",""+ReadMidi.fen_qu);
//                        Log.e("DataAnalyze,scale=",""+scale);
                        MusicSpeed musicSpeed = new MusicSpeed(speed,speed_start_time,scale);
                        musicSpeedList.add(musicSpeed);
                    }else if (midi_data[i+2]==88){
                        speed_start_time=speed_start_time+time;
                        Log.e("拍子：","");
                    }
                    i = i+ midi_data[i+3] + 4;
                    music_note = 0;
                    music_scale = 0;
                    break;} //fo实际是ff
                default:Log.i("action","出错了！"+"action:"+action);i=i+4;music_note = 0;music_scale = 0;break;
            }
            //i=i+4;
        }
//            if((midi_data[i+1]&0xFF)>127){   //如果此位置符合事件种类的要求（>=0x80）
//                /**判断音符**/
//                music_note = (midi_data[i+2]&0xFF) % 12;
//                music_scale = ((midi_data[i+2]&0xFF)/12)-1;
//                /**判断音符**/
//                String action = HexString(midi_data[i+1]);
//                //Log.i("action",action);
//                switch (action){
//                    case "80":typeList.add("松开");break;
//                    case "90":typeList.add("按下");break;
//                    case "a0":typeList.add("Key after Touch");break;
//                    case "b0":typeList.add("控制器");break;
//                    case "c0":typeList.add("Program changes");break;
//                    case "d0":typeList.add("Aftertouch");break;
//                    case "e0":typeList.add("滑音");break;
//                    case "f0":typeList.add("系统码");break;
//                    default:Log.i("action","出错了！"+"action:"+action);break;
//                }
//                //Log.i("list length",""+typeList.size());
//                i=i+4;
//            }

            else{
            /**判断音符**/
            music_note = (midi_data[i+1]&0xFF) % 12;
            music_scale = ((midi_data[i+1]&0xFF)/12)-1;
            /**判断音符**/
            int last_index = typeList.size()-1;
            typeList.add(typeList.get(last_index));
            i=i+3;
        }
            start_time = start_time + time;
//            int start_time=0;
//            for(int i=0;i<dataItemList.size()-1;i++){
//                int duration=0;
//                if(dataItemList.get(i).type.equals("按下")){
//                    int j=i+1;
//                    while (1>0){
//                        if((dataItemList.get(j).type.equals("松开"))&&(dataItemList.get(j).music_note==dataItemList.get(i).music_note)){
//                            duration=duration+dataItemList.get(j).start_time;
//                            break;
//                        }
//                        else{
//                            duration=duration+dataItemList.get(j).start_time;
//                            j=j+1;
//                        }
//                    }
//                    MidiItem midiItem = new MidiItem(duration,start_time,dataItemList.get(i).music_note,dataItemList.get(i).music_scale);
//                    midiItemList.add(midiItem);
//                }
//                start_time=start_time + dataItemList.get(i+1).start_time;
//            }
            DataItem dataItem = new DataItem(music_note,music_scale,start_time,typeList.get(typeList.size()-1),time);
            dataItemList.add(dataItem);

        }
    }

//    public int computeSpeed(){
//
//    }
    public void printDataList(int i){
        Log.i("printDataList","------------------------------------------------------------");
        Log.i("音符：",""+dataItemList.get(i).music_note);
        Log.i("音阶：",""+dataItemList.get(i).music_scale);
        Log.i("类型：",""+dataItemList.get(i).type);
        Log.i("时间：",""+dataItemList.get(i).start_time);
        Log.i("列表长度：",""+dataItemList.size());
        Log.i("音乐长度：",""+length);
    }

    public List<MidiItem> analyze(){
        int start_time=0;
        for(int i=0;i<dataItemList.size()-1;i++){
            int duration=0;
            if(dataItemList.get(i).type.equals("按下")){
                int j=i+1;
                while (1>0 && j<dataItemList.size()){
                    if((dataItemList.get(j).type.equals("松开"))&&(dataItemList.get(j).music_note==dataItemList.get(i).music_note)){
                        duration=duration+dataItemList.get(j).music_duration;
                        break;
                    }
                    else{
                        duration=duration+dataItemList.get(j).music_duration;
                        j=j+1;
                    }
                }
                MidiItem midiItem = new MidiItem(duration,dataItemList.get(i).start_time,dataItemList.get(i).music_note,dataItemList.get(i).music_scale);
                midiItemList.add(midiItem);
//                Log.i("start_time:",""+midiItem.mstart_time);
            }
        }
        return midiItemList;
    }
    public void printMidiList(int i){
        Log.i("printMidiList","------------------------------------------------------------");
        Log.i("时长",""+midiItemList.get(i).mduration);
        Log.i("开始时刻",""+midiItemList.get(i).mstart_time);
        Log.i("音符",""+midiItemList.get(i).music_note);
        Log.i("音阶",""+midiItemList.get(i).music_scale);
    }

    /**
     *将byte转换为16进制字符串
     */
    public static String HexString(byte b){
        String hex = Integer.toHexString(b & 0xF0);   //注意这里只取高4位
        return hex;
    }

    public class DataItem{
        private int music_note;
        private int music_scale;
        private int start_time;
        private int music_duration;
        private String type;

        public DataItem(int music_note, int music_scale, int start_time, String type,int duration) {
            this.music_note = music_note;
            this.music_scale = music_scale;
            this.start_time = start_time;
            this.type = type;
            this.music_duration = duration;
        }
    }

    /**
     * 每个音符的属性
     */
    public class MidiItem{
        public int music_note;
        public int music_scale;
        public int mduration;
        public int mstart_time;

        public MidiItem(int mduration, int mstart_time, int music_note, int music_scale) {
            this.mduration = mduration;
            this.mstart_time = mstart_time;
            this.music_note = music_note;
            this.music_scale = music_scale;
        }
    }

    /**
     *
     */
    public class MusicSpeed{
        public int start_time;
        public int speed;
        public double scale;

        public MusicSpeed(int speed, int start_time,double scale) {
            this.speed = speed;
            this.start_time = start_time;
            this.scale = scale;

        }
    }
}
