package com.example.kxqabc.midi.Chunks;

/**
 * Created by kxqabc on 2016/10/24.
 */
public class TrackChunk {
    private byte[] midiId;
    private int length;
    public byte[] midiData;

    public TrackChunk() {
        this.midiId = new byte[4];
    }
    public byte[] getMidiId() {
        return midiId;
    }

    public void setMidiId(byte[] midiId) {
        this.midiId = midiId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setData(byte[] data) {
        this.midiData = data;
    }
    public byte[] getMidiData(){
        return this.midiData;
    }
    /**
     * 获取每个音轨的数组数据(过滤开头的系统信息)
     * @return
     */
    public byte[] getData(byte[] data) {
        int position=0; //最后一个midi系统信息(FF)的位置
        int position_new_start=0; //真正的音乐数据起始位置
        int k=1; //FF所在的位置地方
        byte[] new_data;
        for(int j=0;j<data.length&&data[k]==(byte)0xFF;j++){
            k=k+data[k+2]+3;
        }
        if(data.length>100){
            int i = 0;
        while(i<100){
            if(data[i]==(byte)0xFF){
                if(i>position){
                position=i;
                i=i+(data[position+2]&0xFF)+3;
                    position_new_start=i;
                }
            }
            i++;
        }
        new_data = new byte[data.length-position_new_start];
        System.arraycopy(data,position_new_start,new_data,0,data.length-position_new_start);
        }else
            new_data=data;
        return new_data;
    }

    /**
     *将byte转换为16进制字符串
     */
    public static String HexString(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        return hex;
    }
}
