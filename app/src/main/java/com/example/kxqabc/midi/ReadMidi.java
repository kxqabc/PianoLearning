package com.example.kxqabc.midi;

import android.util.Log;

import com.example.kxqabc.midi.Chunks.HeaderChunks;
import com.example.kxqabc.midi.Chunks.TrackChunk;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxqabc on 2016/10/24.
 */
public class ReadMidi {
    public static int fen_qu;
    public static int format;
    List<DataAnalyze.MusicSpeed> musicSpeedList= new ArrayList<>();
    public Object[] readDateFIle(File midiFile) throws Exception {
        FileInputStream stream = new FileInputStream(midiFile);
        byte[] data = new byte[stream.available()];
        stream.read(data);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data); //将data转换为ByteBuffer
        // 调用获取MTHD信息方法得到头部信息
        HeaderChunks headerChunks = this.readHeaderChunks(byteBuffer);
        List<TrackChunk> trackChunklist = this.readTreackChunks(byteBuffer,
                headerChunks);
        Object[] objects ;
        objects = this.outInforMidi(headerChunks, trackChunklist);
        for(Object object:objects){
            object=refresh((List<DataAnalyze.MidiItem>) object,musicSpeedList);
        }
        return objects;
    }

    /**
     * 将头块和音轨块分离开，并且分别提取头块中的信息和音轨块中的音序信息，返回全部音轨的音序信息
     * @param headerChunks
     * @param trackChunklist
     * @return
     */
    public Object[] outInforMidi(HeaderChunks headerChunks,List<TrackChunk> trackChunklist) {
        System.out.println("-------------MThd-----------------");
        System.out.println("头块类型:" + new String(headerChunks.getMidiId()));
        System.out.println("头块长度:" + headerChunks.getLength());
        System.out.println("格式:" + headerChunks.getFormat());
        System.out.println("音轨数:" + headerChunks.getTrackNumber());
        System.out.println("分区:" + headerChunks.getMidiTimeSet());
        fen_qu=headerChunks.getMidiTimeSet();
        format=headerChunks.getFormat();
        Object[] objects = new Object[trackChunklist.size()];
        for (int i = 0; i < trackChunklist.size(); i++) {
            System.out.println("-------------MTrk-----------------");
            System.out.println("音轨块" + i + ":"
                    + new String(trackChunklist.get(i).getMidiId()));
            System.out.println("音轨数据长度:" + trackChunklist.get(i).getLength());
            byte[] midiData = trackChunklist.get(i).getMidiData();
            DataAnalyze dataAnalyze = new DataAnalyze(midiData);
            dataAnalyze.getData();
            Log.e("MusicSpeedListLength:",i+"/"+dataAnalyze.musicSpeedList.size());
            if(dataAnalyze.musicSpeedList.size()>0) {
                musicSpeedList = dataAnalyze.musicSpeedList;
            }else {Log.e("ReadMidi:MSlist","=null");
            }
            objects[i] = dataAnalyze.analyze();
        }
        return objects;
    }

    /**
     * 获取头部信息MThd
     *
     * @param buffer
     * @return
     */
    public HeaderChunks readHeaderChunks(ByteBuffer buffer) {
        HeaderChunks headerChunks = new HeaderChunks();
        for (int i = 0; i < headerChunks.getMidiId().length; i++) { //headerChunks.getMidiId().length :指头块标识MTHd的长度=4
            headerChunks.getMidiId()[i] = (byte) (buffer.get());   //位置会自动由相应的 get() 和 put() 函数更新
        }
        headerChunks.setLength(buffer.getInt());
        headerChunks.setFormat(buffer.getShort());
        headerChunks.setTrackNumber(buffer.getShort());
        headerChunks.setMidiTimeSet(buffer.getShort());
        return headerChunks;
    }

    /**
     * 获取MTrk信息块
     *
     * @param buffer
     * @param headerChunks
     * @return
     */
    public List<TrackChunk> readTreackChunks(ByteBuffer buffer,
                                             HeaderChunks headerChunks) {
        ArrayList<TrackChunk> trackChunklist = new ArrayList<TrackChunk>();
        TrackChunk trackChunk;
        for (int i = 0; i < headerChunks.getTrackNumber(); i++) {
            trackChunk = new TrackChunk();
            for (int j = 0; j < trackChunk.getMidiId().length; j++) {  //midiId:byte[4]的数组
                trackChunk.getMidiId()[j] = (byte) (buffer.get());
            }
            trackChunk.setLength(buffer.getInt());      //得到音轨i的数据长度
            byte[] data = new byte[trackChunk.getLength()]; //新建一个音轨i数据长度的byte数组
            buffer.get(data);
            trackChunk.setData(data);
            trackChunklist.add(trackChunk);
        }
        return trackChunklist;
    }
    public List<DataAnalyze.MidiItem> refresh(List<DataAnalyze.MidiItem> midiItemList,List<DataAnalyze.MusicSpeed> musicSpeedList){
        Log.e("format=",""+format);
        switch (format) {
            case 0:{
                Log.e("format0:size=",""+musicSpeedList.size());
                Log.e("format0:speed=",""+musicSpeedList.get(0).speed);
                Log.e("format0:scale=",""+musicSpeedList.get(0).scale);
                for(DataAnalyze.MidiItem midiItem:midiItemList){
                    midiItem.mstart_time = (int)(musicSpeedList.get(0).scale*midiItem.mstart_time);
                }
            }break;
            case 1:{
                for (int i = 0; i < midiItemList.size(); i++) {
                    int new_time = 0;
                    for (int j = 1; j <= musicSpeedList.size(); j++) {
                        if (j < musicSpeedList.size()) {
                            if (midiItemList.get(i).mstart_time > musicSpeedList.get(j).start_time) {
                                //double scale =(double) musicSpeedList.get(j-1).speed/(fen_qu*1000);
                                //Log.e("speed=", "" + musicSpeedList.get(j - 1).speed);
                                new_time = new_time + (int) ((musicSpeedList.get(j).start_time - musicSpeedList.get(j - 1).start_time) * musicSpeedList.get(j - 1).scale);
                            } else {
                                //double scale =(double) musicSpeedList.get(j-1).speed/(fen_qu*1000);
                                int a = (int) ((midiItemList.get(i).mstart_time - musicSpeedList.get(j - 1).start_time) * musicSpeedList.get(j - 1).scale);
                                new_time = new_time + a;
                                midiItemList.get(i).mstart_time = new_time;
                                break;
                            }
                        } else {
                            Log.e("speed not change", "");
                            //double scale =(double) musicSpeedList.get(j-1).speed/(fen_qu*1000);
                            new_time = new_time + (int) ((midiItemList.get(i).mstart_time - musicSpeedList.get(j - 1).start_time) * musicSpeedList.get(j - 1).scale);
                            midiItemList.get(i).mstart_time = new_time;
                            break;
                        }
                    }
//            Log.i("ReadMidi:step","第"+i+"个音符"+midiItemList.get(i).mstart_time);
//            //midiItemList.get(i).mstart_time = new_time;
//            Log.e("ReadMidi:step","第"+i+"个音符"+midiItemList.get(i).mstart_time);
                }
            }break;
            case 2:{Log.e("ReadMidi:","音乐为格式2！不同步音轨！");}break;

        }
        return midiItemList;
    }
    public void MergeTrack(Object[] objects){
        List<DataAnalyze.MidiItem> midiItemList = new ArrayList<>();
        int a=0;
        for(Object object:objects){


        }
    }
    /**
     *将byte转换为16进制字符串
     */
    public static String HexString(byte b){
        String hex = Integer.toHexString(b & 0xF0);   //注意这里只取高4位
        return hex;
    }
}
