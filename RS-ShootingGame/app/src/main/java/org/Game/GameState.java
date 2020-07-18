package org.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {
    public Player m_player;
    private BackGround m_background;
    private GraphicObject m_keypad;
    int playertype;
    int killcnt=0;
    int missileSpeed = 30;


    long PlayerMissile = System.currentTimeMillis();
    long LastRegenBox = System.currentTimeMillis();

    Random randEnem = new Random();
    Random randomBox = new Random();//랜덤 함수

    int width = AppManager.getInstance().getGameView().getDisplay().getWidth();//너비측정

    ArrayList<Enemy> m_enemlist = new ArrayList<Enemy>();//적
    public ArrayList<Missile_Player> m_pmslist = new ArrayList<Missile_Player>();//미사일 위치
    ArrayList<Missile> m_enemmslist = new ArrayList<Missile>();
    ArrayList<RandomBox> m_randomboxList = new ArrayList<RandomBox>();//랜덤 박스

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance(){
        return game;
    }

    public GameState(){
        playertype = 3;
    }


    public void makeEnemy(){

        if(System.currentTimeMillis() - LastRegenEnemy >= 1000){
            LastRegenEnemy = System.currentTimeMillis();

            int enemytype = randEnem.nextInt(3);
            Enemy enem = null;

            if(enemytype == 0)
                enem = new Enemy_1();
            else if(enemytype == 1)
                enem = new Enemy_2();
            else if(enemytype == 2)
                enem = new Enemy_3();

            enem.setPosition(randEnem.nextInt(1000), -60);
            enem.movetype = randEnem.nextInt(3);

            m_enemlist.add(enem);
        }

    }

    @Override
    public void Init() {
        //게임시작시 모든 적을 제거, 죽인 수 초기화 후 진행
        allclear();
        killcnt = 0;

        if(playertype == 0) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air1));
        }
        else if(playertype == 1){
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air2));
        }
        else if(playertype == 2)
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air3));

        m_keypad = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.keypad));
        m_background = new BackGround(2);
        m_keypad.setPosition(0, 1200);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long GameTime = System.currentTimeMillis();
        m_player.Update(GameTime);
        m_background.Update(GameTime);

        for(int i = m_pmslist.size() - 1; i>=0; i--) {
            Missile_Player pms = m_pmslist.get(i);
            pms.Update();
            if(pms.state == Missile.STATE_OUT)m_pmslist.remove(i);
        }
        for(int i =m_enemlist.size() - 1; i>=0; i--){
            Enemy enem = m_enemlist.get(i);
            enem.Update(GameTime);
            if(enem.state == Missile.STATE_OUT)m_enemlist.remove(i);
        }
        for(int i =m_enemmslist.size() - 1; i>=0; i--){
            Missile enemms = m_enemmslist.get(i);
            enemms.Update();
            if(enemms.state == Missile.STATE_OUT)
                m_enemmslist.remove(i);
        }
        //랜덤 박스 업데이트 및 상태 확인
        for (int k = m_randomboxList.size() - 1; k >= 0; k--) {
            m_randomboxList.get(k).Update(GameTime);
            if (m_randomboxList.get(k).state == RandomBox.STATE_OUT)
                m_randomboxList.remove(k);
        }

        MakeRandomBox();
        makeEnemy();
        MakePlayerMissile();
        CheckCollision();
    }

    @Override
    public void Render(Canvas canvas) {

        m_background.Draw(canvas);
        for(Missile_Player pms : m_pmslist)
            pms.Draw(canvas);
        for(Missile enemms: m_enemmslist)
            enemms.Draw(canvas);
        for(Enemy enem : m_enemlist){
            enem.Draw(canvas);
        }
        for (RandomBox randomBox : m_randomboxList)
            randomBox.Draw(canvas);

        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        Paint p = new Paint();
        p.setTextSize(40);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setColor(Color.BLACK);
        canvas.drawText("남은 목숨 : " +String.valueOf(m_player.getLife()),100, 100, p);
        canvas.drawText("쓰러뜨린 적 : "+ String.valueOf(killcnt),700, 100,p);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int x = m_player.getX();
        int y = m_player.getY();

        //방향 움직일 때 위치 변경
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            m_player.setPosition(x - 20, y);
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            m_player.setPosition(x + 20, y);
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
            m_player.setPosition(x, y - 20);
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            m_player.setPosition(x, y + 20);
//        if(keyCode == KeyEvent.KEYCODE_SPACE)
//            m_pmslist.add(new Missile_Player(x + 10, y));

        return true;
    }

    //모든적, 랜덤박스 제거
    public void allclear(){
        for(int i = m_pmslist.size() - 1; i>= 0; i--){
            m_pmslist.remove(i);
        }

        for(int i = m_enemlist.size()-1; i>=0;i--){
            m_enemlist.remove(i);
        }

        for(int i = m_enemmslist.size()-1;i>=0;i--){
            m_enemmslist.remove(i);
        }
        for(int i = m_randomboxList.size()-1;i>=0;i--){
            m_randomboxList.remove(i);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        Rect rt = new Rect();

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            rt.set(0,1230,30,1260);
            if (rt.contains(x, y)) {
                m_player.setPosition(x - 20, y);
            }

            rt.set(30, 1200, 60, 1230);
            if (rt.contains(x, y)) {
                m_player.setPosition(x, y - 20);
            }

            rt.set(60, 1230, 90, 1260);
            if (rt.contains(x, y)) {
                m_player.setPosition(x + 20, y);
            }

            rt.set(30, 1260, 60, 1290);
            if (rt.contains(x, y)) {
                m_player.setPosition(x, y + 20);
            }
        }
        return true;
    }

    public void MakePlayerMissile()// 미사일을 생성
    {
        //미사일이 1초마다 나감
        if(System.currentTimeMillis()-PlayerMissile>=1000) {
            PlayerMissile = System.currentTimeMillis();
            ;// 여기서 플레이어와 같은 위치에서 미사일이 나감
            Missile_Player pms = new Missile_Player(m_player.getX()-40, m_player.getY()-150, missileSpeed);// 미사일 위치,위치,속도

            // 플레이어 미사일 list에 추가
            m_pmslist.add(pms);
            SoundManager.getInstance().play(1);
        }

    }

    public void MakeRandomBox()// 랜덤 박스 생성
    {
            if (System.currentTimeMillis() - LastRegenBox >= 2500) {
                LastRegenBox = System.currentTimeMillis();

                RandomBox m_randomBox = null;
                int boxType = randomBox.nextInt(8);
                boxType = 4;

                if (boxType == 4)
                    m_randomBox = new RandomBox_plusLife(); // 플레이어 목숨 수

//                else if (boxType == 1)
//                    m_randomBox = new RandomBox_MissileRegenSpeed();// 미사일 리젠 속도
//
//                else if (boxType == 2)
//                    m_randomBox = new RandomBox_MIssileSpeed();// 플레이어 미사일 속도

                m_randomBox.setPosition(160 + (int)randomBox.nextInt(width - 320), -60); // 랜덤 박스 위치
                m_randomboxList.add(m_randomBox);

            }
    }

    public void CheckCollision(){
        for(int i = m_pmslist.size() - 1; i>= 0; i--){
            for(int j = m_enemlist.size() - 1; j>=0; j--){
                if(CollisionManager.CheckBoxToBox(m_pmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                    m_pmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;

                    return;
                }
            }
        }

        for(int i = m_enemlist.size()-1; i>=0;i--){
            if(CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)){
                m_enemlist.remove(i);
                m_player.destroyPlayer();
                if(m_player.getLife() <= 0)
                    AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
            }
        }

        for(int i = m_enemmslist.size()-1;i>=0;i--){
            if(CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)){
                m_enemmslist.remove(i);
                m_player.destroyPlayer();
                if(m_player.getLife() <= 0) AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
            }
        }

        //랜덤박스충돌
        for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_randomboxList.get(i).m_BoundBox)) {
                if(m_randomboxList.get(i).boxtype == 4) {//생명증가
                    m_randomboxList.remove(i);
                    m_player.addLife();
                }
//                if (m_randomboxList.get(i).BoxType == 1)// hp회복
//                {
//                    if(subcnt<3) {
//                        subcnt++; // 서브 미사일 횟수 증가
//                    }
//                    m_randomboxList.remove(i);
//                    m_player.addLife();
//                    AppManeger.getInstance().getGameView().viberator();
//                } else if (m_randomboxList.get(i).BoxType == 2)// 플레이어 미사일 속도
//                {
//                    if(subcnt<3) {
//                        subcnt++; // 서브 미사일 횟수 증가
//                    }
//
//                    player_missile_speed += 15;
//                    m_randomboxList.remove(i);
//                    AppManeger.getInstance().getGameView().viberator();
//                } else if (m_randomboxList.get(i).BoxType == 3)// 미사일 리젠 속도
//                {
//                    if(subcnt<3) {
//                        subcnt++; // 서브 미사일 횟수 증가
//                    }
//
//                    player_missile_regen_speed -= 10;// 0.01초 감소
//                    m_randomboxList.remove(i);
//                    AppManeger.getInstance().getGameView().viberator();
//                }
                return;
                
            }
        }
    }
}

