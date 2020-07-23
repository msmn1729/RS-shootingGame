package org.Game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.GameView;
import com.example.gameframework.GameViewThread;
import com.example.gameframework.IState;
import com.example.gameframework.InsertView;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

import static com.example.gameframework.GameView.sfh;
import static com.example.gameframework.MainActivity.mcontext;

public class ExitState implements IState {
    GameView gv;
    private BackGround m_background;
    private Bitmap gameover;
    private GameViewThread m_thread;
    private static ExitState exit = new ExitState();

    public ExitState(){
        m_thread = new GameViewThread(sfh, gv);
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
        Intent intent = new Intent(mcontext, InsertView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(intent);
       // SoundManager.getInstance().play(4);
        return true;
    }
}
