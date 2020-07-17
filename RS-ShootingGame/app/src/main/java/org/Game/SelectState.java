package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.IState;
import com.example.gameframework.R;

public class SelectState implements IState {

    private Bitmap player1, player2, player3, gamestart;
    private BackGround back;
    int flag;//비행기 선택 변수

    private static SelectState select = new SelectState();
    public SelectState(){

    }

    public static SelectState getInstance(){
        return select;
    }


    @Override
    public void Init() {
        flag = 3;

        back = new BackGround(1);
        player1 = AppManager.getInstance().getBitmap(R.drawable.air1);
        player1 = Bitmap.createScaledBitmap(player1,300, 300, true);//비행기 1의 비트맵을 받아와 사이즈까지 조절 함
        player2 = AppManager.getInstance().getBitmap(R.drawable.air2);
        player2 = Bitmap.createScaledBitmap(player2,300, 300, true);
        player3 = AppManager.getInstance().getBitmap(R.drawable.air3);
        player3 = Bitmap.createScaledBitmap(player3,300, 300, true);
        gamestart = AppManager.getInstance().getBitmap(R.drawable.gamestart);
    }

    @Override
    public void Destroy() {
        AppManager.getInstance().m_gameState = GameState.getInstance();
    }

    @Override
    public void Update() {
        long GameTime = System.currentTimeMillis();
        back.Update(GameTime);
    }

    @Override
    public void Render(Canvas canvas) {
        back.Draw(canvas);

        Paint p=new Paint();//화면 상단에 캐릭터 선택하라는 문구를 띄움
        p.setTextSize(80);
        p.setColor(Color.GREEN);
        canvas.drawText("비행기를 선택하세요!",200,200, p);

        canvas.drawBitmap(player1, 130, 500, null);
        canvas.drawBitmap(player2, 630, 500, null);
        canvas.drawBitmap(player3, 370, 900, null);
        canvas.drawBitmap(gamestart, 300, 1300, null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//비행기 선택,
        int x = (int)event.getX(0);
        int y = (int)event.getY(0);
        if (x > 160 && x < 460 && y > 500 && y < 800)//비행기 1의 좌표를 선택했을 시 선택 이미지를 불러옴
        {//비행기 1 선택 시
            player1 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air1s),300, player1.getHeight(), true);
            flag = 0;

            player2 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air2),300, player2.getHeight(), true);
            player3 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air3),300, player3.getHeight(), true);
        }

        else if (x > 630 && x < 930 && y > 500 && y < 800)//비행기 2 선택 시 위의 if문과 처리는 같음
        {//비행기 2 선택 시
            player2 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air2s),300, player2.getHeight(), true);
            flag = 1;

            player1 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air1),300, player1.getHeight(), true);
            player3 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air3),300, player3.getHeight(), true);
        }
        else if(x > 370 && x < 670 && y > 900 && y < 1200){
            player3 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air3s),300, player3.getHeight(), true);
            flag = 2;

            player2 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air2),300, player2.getHeight(), true);
            player1 = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.air1),300, player1.getHeight(), true);
        }

        //게임시작을 눌렀을 경우
        if(flag != 3 && x > 300 && x < 750 && y > 1300 && y < 1452){
            GameState.getInstance().playertype = flag;
            AppManager.getInstance().getGameView().changeGameState(GameState.getInstance());
        }
        return true;
    }
}
