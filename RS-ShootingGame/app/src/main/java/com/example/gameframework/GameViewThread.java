package com.example.gameframework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread {
    //접근을 위한 멤버 변수
    private SurfaceHolder m_surfaceHolder;
    private GameView m_gameView;

    //스레드 실행 상태 멤버 변수
    private boolean m_run = false;

    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameView){
        m_surfaceHolder = surfaceHolder;
        m_gameView = gameView;
    }
    public void setRunning(boolean run){
        m_run = run;
    }

    @Override
    public void run() {
        Canvas _canvas;
        while(m_run){
            _canvas = null;
            try{//SurFaceHolder를 통해 Surface에 접근해서 가져옴
                m_gameView.Update();
                synchronized (m_surfaceHolder){
                    _canvas = m_surfaceHolder.lockCanvas(null);
                    if (_canvas == null) break;
                    m_gameView.onDraw(_canvas);
                }
            }finally {
                if(_canvas != null)
                    m_surfaceHolder.unlockCanvasAndPost(_canvas);
            }
        }
    }
}
