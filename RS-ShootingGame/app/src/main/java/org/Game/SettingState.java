package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

public class SettingState implements IState {

    int backflag = 1;
    int effectflag = 1;

    private static SettingState setting = new SettingState();

    Point point = new Point();
    Bitmap bg_sound1, bg_sound2;
    Bitmap ef_sound1, ef_sound2;
    Bitmap btn;
    BackGround back;

    public SettingState(){

    }

    public static SettingState getInstance(){
        return setting;
    }

    @Override
    public void Init() {
        point.x = AppManager.getInstance().getDeviceSize().x;
        point.y = AppManager.getInstance().getDeviceSize().y;

        back = new BackGround(2);

        if(backflag==1) {
            bg_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            bg_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
        }else if(backflag==0){
            bg_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            bg_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnon);
        }

        if(effectflag==1) {
            ef_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            ef_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
        }else if(effectflag==0){
            ef_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            ef_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnon);
        }

        btn = AppManager.getInstance().getBitmap(R.drawable.complete);
    }

    @Override
    public void Destroy() {
        AppManager.getInstance().m_gameState = ReadyState.getInstance();
    }

    @Override
    public void Update() {
        long gameTime = System.currentTimeMillis();
        back.Update(gameTime);
    }
    public void Update(long gameTime){

    }
    @Override
    public void Render(Canvas canvas) {
        back.Draw(canvas);
        Paint p=new Paint();//화면 상단에 캐릭터 선택하라는 문구를 띄움
        p.setTextSize(80);
        p.setColor(Color.WHITE);

        canvas.drawText("배경음",200,400, p);
        canvas.drawText("효과음",200,800, p);
        p.setTextSize(70);
        canvas.drawText("on", 420, 700, p);
        canvas.drawText("off", 810, 700, p);

        canvas.drawText("on", 420, 1100, p);
        canvas.drawText("off", 810, 1100, p);

        if(backflag == 1) {
            canvas.drawBitmap(bg_sound1, 400, 500, null);
            canvas.drawBitmap(bg_sound2, 800, 510, null);
        }
        else{
            canvas.drawBitmap(bg_sound1, 410, 510, null);
            canvas.drawBitmap(bg_sound2, 790, 500, null);
        }
        if(effectflag == 1) {
            canvas.drawBitmap(ef_sound1, 400, 900, null);
            canvas.drawBitmap(ef_sound2, 800, 910, null);
        }
        else{
            canvas.drawBitmap(ef_sound1, 410, 910, null);
            canvas.drawBitmap(ef_sound2, 790, 900, null);
        }
        canvas.drawBitmap(btn, 330, 1200, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        SoundManager.getInstance().play(4);

        //배경음 켜짐
        if(x > 400 && x < 550 && y > 500 && y < 650){
            SoundManager.getInstance().play(4);
            backflag = 1;
            bg_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            bg_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            SoundManager.getInstance().startMusic(R.raw.bgm);
        }
        //배경음 꺼짐
        if(x > 800 && x < 950 && y > 510 && y < 660){
            SoundManager.getInstance().play(4);
            backflag = 0;
            bg_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            bg_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            SoundManager.getInstance().stopMusic(R.raw.bgm);
        }
        //효과음 켜짐
        if(x > 400 && x < 550 && y > 900 && y < 1050){
            SoundManager.getInstance().play(4);
            effectflag = 1;

            ef_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            ef_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            SoundManager.getInstance().onsound();
        }
        //효과음 꺼짐
        if(x > 800 && x < 950 && y > 900 && y < 1050){
            SoundManager.getInstance().play(4);
            effectflag = 0;

            ef_sound1 = AppManager.getInstance().getBitmap(R.drawable.btnoff1);
            ef_sound2 = AppManager.getInstance().getBitmap(R.drawable.btnon);
            SoundManager.getInstance().offsound();
        }

        if(x > 330 && x < 700 && y > 1200 && y < 1350){
            SoundManager.getInstance().play(4);
            AppManager.getInstance().getGameView().changeGameState(ReadyState.getInstance());
        }

        return true;
    }
}