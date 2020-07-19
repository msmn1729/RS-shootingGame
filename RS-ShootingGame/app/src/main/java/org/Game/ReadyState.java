package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

public class ReadyState implements IState {
    Point point = new Point();
    public BackGround bg_ready;
    Bitmap btn_start;
    Bitmap btn_rank;
    Bitmap btn_set;
    Bitmap btn_exit;
    Bitmap shoot;


    private static ReadyState ready = new ReadyState();
    public ReadyState(){

    }

    public static ReadyState getInstance(){
        return ready;
    }

    @Override
    public void Init() {
        point.x = AppManager.getInstance().getDeviceSize().x;
        point.y = AppManager.getInstance().getDeviceSize().y;

        shoot = AppManager.getInstance().getBitmap(R.drawable.shootinggame);
//        shoot = Bitmap.createScaledBitmap(shoot, point.x, point.y, true);

//        bg_ready = AppManager.getInstance().getBitmap(R.drawable.back1);
//        bg_ready = Bitmap.createScaledBitmap(bg_ready, point.x, point.y, true);

        bg_ready = new BackGround(0);

        btn_start = AppManager.getInstance().getBitmap(R.drawable.startbtn2);
        btn_start = Bitmap.createScaledBitmap(btn_start, point.x/2, point.y/8, true);

        btn_rank = AppManager.getInstance().getBitmap(R.drawable.rankbtn);
        btn_rank = Bitmap.createScaledBitmap(btn_rank, point.x/2, point.y/8, true);

        btn_set = AppManager.getInstance().getBitmap(R.drawable.setbtn);
        btn_set = Bitmap.createScaledBitmap(btn_set, point.x/2, point.y/8, true);

        btn_exit = AppManager.getInstance().getBitmap(R.drawable.exitbtn);
        btn_exit = Bitmap.createScaledBitmap(btn_exit, point.x/2, point.y/8, true);

    }

    @Override
    public void Destroy() {
        AppManager.getInstance().m_gameState = SelectState.getInstance();
    }

    @Override
    public void Update() {
        long gameTime = System.currentTimeMillis();
        bg_ready.Update(gameTime);
    }

    @Override
    public void Render(Canvas canvas) {
        bg_ready.Draw(canvas);

        canvas.drawBitmap(shoot, 140, 200, null);
        canvas.drawBitmap(btn_start,point.x/4, (point.y/8)*4-20, null);
        canvas.drawBitmap(btn_rank, point.x/4, (point.y/8) * 5 -30, null);
        canvas.drawBitmap(btn_set,point.x/4,(point.y/8) * 6 -40 , null);
        canvas.drawBitmap(btn_exit, point.x/4, (point.y/8) * 7 - 50, null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        //버튼 범위설정
        Rect r_start = new Rect(point.x/4,(point.y/8) * 4 - 20,(point.x/4)+point.x/2,(point.y/8)*4 - 20 + (point.y/8));
        Rect r_rank = new Rect(point.x/4,(point.y/8) * 5 - 30,(point.x/4)+point.x/2,(point.y/8)*5 - 30 + (point.y/8));
        Rect r_set = new Rect(point.x/4,(point.y/8) * 6 - 45,(point.x/4)+point.x/2,(point.y/8)*6 - 45 + (point.y/8));
        Rect r_exit = new Rect(point.x/4,(point.y/8)*7 - 50,(point.x/4) + (point.x/2),(point.y/8)*7 - 50 + (point.y/8));


        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(r_start.contains(x,y)){
                SoundManager.getInstance().play(4);
                AppManager.getInstance().getGameView().changeGameState(SelectState.getInstance());
            }
            else if(r_rank.contains(x,y)) {
                SoundManager.getInstance().play(4);
                AppManager.getInstance().getGameView().changeGameState(new RankState());
            }

            else if(r_set.contains(x,y))
            {
                SoundManager.getInstance().play(4);
                AppManager.getInstance().getGameView().changeGameState(SettingState.getInstance());
            }
            else if (r_exit.contains(x, y))
            {
                SoundManager.getInstance().play(4);
                System.exit(0);
            }
        }
        return true;
    }

}