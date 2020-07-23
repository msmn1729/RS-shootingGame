package com.example.gameframework;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.util.HashMap;

public class SoundManager {
    private static SoundManager s_instance;

    private SoundPool m_SoundPool;//안드로이드에서 지원하는 사운드 풀
    private HashMap m_SoundPoolMap;//불러온 사운드의 아이디 값을 지정할 해시맵
    private AudioManager m_AudioManager;//사운드 관리를 위한 오디오 매니저
    private Context m_Activity;//애플리케이션의 컨텍스트 값
    private MediaPlayer m_BGM;

    int m_SoundPoolCount=0;
    int poolflag = 1;
    int bgmflag = 1;

    //싱글톤 객체 생성 및 리턴
    public static SoundManager getInstance(){
        if(s_instance == null) s_instance = new SoundManager();
        return s_instance;
    }

    public void Init(Context _context){

        //버전에 따른 SoundPool 객체 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            m_SoundPool = new SoundPool.Builder().build();
        }
        else
            m_SoundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);

        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);
        m_Activity = _context;
        m_BGM = new MediaPlayer();

    }

    public void addMusic( int _MusicID){
        m_BGM = MediaPlayer.create(m_Activity,_MusicID);
        m_BGM.start();
        m_BGM.setLooping(true);
    }
    public void pauseMusic(int _MusicID){
        m_BGM.pause();
    }
    public void stopMusic(int _MusicID){
        m_BGM.stop();
        bgmflag=0;
    }

    public void startMusic(int _MusicID){
        if(bgmflag==0){
            bgmflag=1;
            addMusic(_MusicID);
        }
    }
    public void playMusic(int _MusicID){
        if(bgmflag==1)
            m_BGM.start();
    }


    public void addSound(int _index, int _soundID){
        //효과음를 로드 시키고
        int id = m_SoundPool.load(m_Activity, _soundID, 1);
        m_SoundPoolMap.put(_index, id);//해시맵에 아이디 값을 받아온 인덱스 저장
        m_SoundPoolCount++;
        poolflag = 1;
    }

    public void offsound() {
        //효과음 정지
        for(int i = 0;i<m_SoundPoolCount+1;i++)
            m_SoundPool.stop(i);
        poolflag = 0;
    }
    public void onsound() {
        poolflag=1;
    }

    public void play(int _index){
        //재생
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if(poolflag==1)
            m_SoundPool.play((Integer)m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1 ,0, 1f);
    }


}