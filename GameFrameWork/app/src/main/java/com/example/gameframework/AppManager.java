package com.example.gameframework;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class AppManager {
    //싱글톤 객체 생성, 리턴
    private static AppManager s_instance;

    private GameView m_gameView;
    private Resources m_resources;
    public IState m_gameState;

    public static AppManager getInstance(){
        if(s_instance == null) s_instance = new AppManager();
        return s_instance;
    }

    void setGameView (GameView _gameView) {
        m_gameView = _gameView;
    }

    void setResources (Resources _resources) {
        m_resources = _resources;
    }

    public GameView getGameView() {
        return m_gameView;
    }

    public Resources getResource( ) {
        return m_resources;
    }

    //매니저 클래스를 이용한 비트맵 가져오기
    public Bitmap getBitmap(int r){
        return BitmapFactory.decodeResource(m_resources, r);
    }

    //디바이스의 width, height구해서 좌표 반환
    public Point getDeviceSize(){
        Point p = new Point();
        DisplayMetrics metrics = AppManager.getInstance().getResource().getDisplayMetrics();
        p.x = metrics.widthPixels;
        p.y = metrics.heightPixels;
        return p;
    }
}
