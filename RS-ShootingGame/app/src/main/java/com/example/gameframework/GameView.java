package com.example.gameframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.Game.ReadyState;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private IState m_state;
    private GameViewThread m_thread;
//    private GraphicObject m_Image;
//    private SpriteAnimation m_spr;

    public GameView(Context context) {
        super(context);
        //키 입력 처리를 받기 위해
        setFocusable(true);

        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());

        SoundManager.getInstance().Init(context);//초기화
        //효과음 HashMap에 저장
        SoundManager.getInstance().addSound(1,R.raw.eff_missile1);
        SoundManager.getInstance().addSound(2,R.raw.eff_missile2);
        SoundManager.getInstance().addSound(3,R.raw.eff_missile3);
        SoundManager.getInstance().addSound(4,R.raw.eff_click);
        SoundManager.getInstance().addSound(5,R.raw.eff_kill);
        SoundManager.getInstance().addSound(6,R.raw.eff_over);
        SoundManager.getInstance().addSound(7,R.raw.eff_hurt);
        SoundManager.getInstance().addSound(8,R.raw.eff_rano);//랜덤 버프
        SoundManager.getInstance().addSound(9,R.raw.eff_ranx);//랜덤 디버프
        //SoundManager.getInstance().addSound(10,R.raw.eff_rank);
        //rank에서 효과음을 넣고 싶었는데 프로그램이 자꾸 종료가 되어서
        //재생이 되는지 안되는지 알수가 없습니다....따라서 일단 뺄게요
        SoundManager.getInstance().addMusic(R.raw.bgm);

        changeGameState(new ReadyState());
        getHolder().addCallback(this);

        m_thread = new GameViewThread(getHolder(), this);
//        m_Image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.background2));
//        m_spr = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.walk));//이미지 어디?
//        m_spr.initSpriteData(180, 300, 5 ,4);

    }

    public void onDraw(Canvas canvas){

//        Bitmap _scratch = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
        canvas.drawColor(Color.BLACK);
//        canvas.drawBitmap(_scratch, 10, 10, null);
        m_state.Render(canvas);
//        m_Image.Draw(canvas);
//        m_spr.Draw(canvas);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //실행 상태
        m_thread.setRunning(true);
        //스레드 실행
        m_thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while(retry){
            try{
                //스레드 중지
                m_thread.join();
                retry = false;
            }catch (InterruptedException e){
                //스레드가 종료되도록 계속 시도
            }
        }
    }

    //지속적으로 실행하여, 갱신이 수행되므로 run에서 Update를 수행
    public void Update(){
        long gameTime = System.currentTimeMillis();
//        m_spr.Update(gameTime);//프레임에 대한 변화
        m_state.Update();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        m_state.onKeyDown(keyCode, event);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        m_state.onTouchEvent(event);
        return true;
    }

    public void changeGameState(IState _state){
        if(m_state != null)
            m_state.Destroy();
        _state.Init();
        m_state = _state;
    }

}
