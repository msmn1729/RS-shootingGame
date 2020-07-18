package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

public class ExitState implements IState {

    private BackGround m_background;
    private Bitmap gameover;

    private static ExitState exit = new ExitState();

    public ExitState(){

    }

    public static ExitState getInstance(){
        return exit;
    }


    @Override
    public void Init() {
        SoundManager.getInstance().play(6);
        SoundManager.getInstance().pauseMusic(R.raw.bgm);
        m_background = new BackGround(2);
        gameover = AppManager.getInstance().getBitmap(R.drawable.gameover);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long gameTime = System.currentTimeMillis();
        m_background.Update(gameTime);
    }

    @Override
    public void Render(Canvas canvas) {

        m_background.Draw(canvas);
        canvas.drawBitmap(gameover, 150, 300, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AppManager.getInstance().getGameView().changeGameState(ReadyState.getInstance());
        return true;
    }
}
