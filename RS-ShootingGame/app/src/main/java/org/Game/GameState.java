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
    int specialSkill=3;
    int missileSpeed = 30;


    long PlayerMissile = System.currentTimeMillis();

    Random randEnem = new Random();

    ArrayList<Enemy> m_enemlist = new ArrayList<Enemy>();//적
    public ArrayList<Missile_Player> m_pmslist = new ArrayList<Missile_Player>();//미사일 위치
    ArrayList<Missile> m_enemmslist = new ArrayList<Missile>();
    ArrayList<RandomBox> random = new ArrayList<RandomBox>();//랜덤 박스

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance(){
        return game;
    }

    public GameState(){
        playertype = 3;
    }

    public void makeRandomBox(){
        if(System.currentTimeMillis() - LastRegenEnemy >= 1000) {
            LastRegenEnemy = System.currentTimeMillis();

            RandomBox ran = new RandomBox(AppManager.getInstance().getBitmap(R.drawable.randombox));
            ran.setPosition(randEnem.nextInt(1000), -60);
            random.add(ran);
        }
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
//        for(int i = random.size() - 1; i>=0; i--){
//            RandomBox rann = random.get(i);
//            rann.Update(GameTime);
//            if(rann.state == Missile.STATE_OUT)
//                random.remove(i);
//        }
//        makeRandomBox();
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
//        for(RandomBox randomBoxes : random)
//             randomBoxes.Draw(canvas);
        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        Paint p = new Paint();
        p.setTextSize(40);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setColor(Color.BLACK);
        canvas.drawText("생명  " +String.valueOf(m_player.getLife()),100, 100, p);
        canvas.drawText("처치한 적  "+ String.valueOf(killcnt),700, 100,p);
        canvas.drawText("필살기  " + String.valueOf(specialSkill), 700, 1600, p);
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

    //모든적을 제거
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
    }
}

