package org.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

import java.util.ArrayList;
import java.util.Random;


import static com.example.gameframework.MainActivity.mcontext;

public class GameState implements IState {
    public Player m_player;
    private BackGround m_background;
    private GraphicObject m_keypad;
    public static int playertype; //비행기 타입
    //public Enemy m_enemy;
    private M_Circle m_circle;

    public static int killcnt = 0; //적 처치수

    int missileSpeed;//미사일 발사 속도
    float nowMissileSpeed;//현재 발사 속도 나타내주는 변수
    int specialSkill;//필살기 개수

    long Skilltime = 0;

    int startskill3;
    int missileState;
    int score;

    long GameTime;
    int fflag, startflag;

    long PlayerMissile = System.currentTimeMillis();
    long LastRegenBox = System.currentTimeMillis();

    Random randEnem = new Random();
    Random randomBox = new Random();//랜덤 함수

    //int width = AppManager.getInstance().getGameView().getDisplay().getWidth();//디바이스 너비
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;

    ArrayList<Enemy> m_enemlist = new ArrayList<Enemy>();//적리스트
    ArrayList<Missile_Player> m_pmslist = new ArrayList<Missile_Player>();//미사일 리스트
    ArrayList<Missile_Player> m_lpmslist = new ArrayList<Missile_Player>();//왼쪽 미사일 리스트
    ArrayList<Missile_Player> m_rpmslist = new ArrayList<Missile_Player>();//오른쪽 미사일 리스트
    ArrayList<Missile> m_enemmslist = new ArrayList<Missile>();//적 미사일 리스트
    ArrayList<RandomBox> m_randomboxList = new ArrayList<RandomBox>();//랜덤 박스
    //필살기1, 2 어레이리스트
    ArrayList<Skill1_SuperMissile> m_skill1_list = new ArrayList<Skill1_SuperMissile>();//미사일 위치
    ArrayList<Skill2_Enemy_Explosion> m_skill2_list = new ArrayList<Skill2_Enemy_Explosion>(); //폭발할 적 리스트

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance() {
        return game;
    }

    public GameState() {
        playertype = 3;
    }

    //적을 생성하는 메소드
    public void makeEnemy() {
        if (System.currentTimeMillis() - LastRegenEnemy >= 7000) { //적생성 주기(디폴트 값은 1000)
            LastRegenEnemy = System.currentTimeMillis();

            int enemytype = randEnem.nextInt(3);
            Enemy enem = null;

            if (enemytype == 0)
                enem = new Enemy_1();
            else if (enemytype == 1)
                enem = new Enemy_2();
            else if (enemytype == 2)
                enem = new Enemy_3();

            enem.setPosition(randEnem.nextInt(1000), -60); //적 생성위치 랜덤
            enem.movetype = randEnem.nextInt(3); //적 움직임 패턴 랜덤

            m_enemlist.add(enem);
        }

    }

    @Override
    public void Init() {
        //게임시작시 모든 적을 제거, 죽인 수 초기화 후 진행
        allclear();
        m_circle = new M_Circle(AppManager.getInstance().getBitmap(R.drawable.circle_4));

        killcnt = 0;
        missileSpeed = 1000;
        nowMissileSpeed = 1.0f;
        missileState = 1;
        score = 0;
        specialSkill = 3; //초기 필살기 개수

        if (playertype == 0) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player1));
        } else if (playertype == 1) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player2));
        } else if (playertype == 2)
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player3));

        m_keypad = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.keypad));
        m_circle = new M_Circle(AppManager.getInstance().getBitmap(R.drawable.circle_5));
        m_background = new BackGround(2);
        //키패드 절대
//        m_keypad.setPosition(0, (int) (AppManager.getInstance().getDeviceSize().y * 1200));
        //키패드 상대
        m_keypad.setPosition(0, (int) (AppManager.getInstance().getDeviceSize().y * 0.7));
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        GameTime = System.currentTimeMillis();
        m_player.Update(GameTime);
        m_background.Update(GameTime);
        m_circle.Update(GameTime);

        //필살기 최대, 최소 조절
        if (specialSkill < 0) {
            specialSkill = 0;
        }

        if (specialSkill > 3)
            specialSkill = 3; //필살기 최대 3개 소유가능

        //미사일 발사 속도 조절
        if (missileSpeed <= 0) {
            missileSpeed = 100;
            nowMissileSpeed = 0.1f;
        } else if (missileSpeed > 1500) {
            missileSpeed = 1500;
            nowMissileSpeed = 1.5f;
        }

        //미사일 상태 조절
        if (missileState >= 3)
            missileState = 3;

        if (missileState <= 1)
            missileState = 1;

        //플레이어 생명 최대 조절
        if (m_player.getLife() > 5)
            m_player.m_Life = 5;

        //2단계일때
        if (missileState >= 2) {
            for (int i = m_lpmslist.size() - 1; i >= 0; i--) {
                Missile_Player lpms = m_lpmslist.get(i);
                lpms.Update();
                if (lpms.state == Missile.STATE_OUT) m_lpmslist.remove(i);
            }
        }

        //3단계일떄
        if (missileState == 3) {
            for (int i = m_rpmslist.size() - 1; i >= 0; i--) {
                Missile_Player rpms = m_rpmslist.get(i);
                rpms.Update();
                if (rpms.state == Missile.STATE_OUT) m_rpmslist.remove(i);
            }
        }

        //라이프가 0이될때 죽어야하므로 주기적인 체크
        if (m_player.getLife() <= 0)
            AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());

        //필살기1 포문 한 번만 진행
        for (int i = m_skill1_list.size() - 1; i >= 0; i--) {
            Skill1_SuperMissile skill1 = m_skill1_list.get(i);
            skill1.Update();
            if (skill1.state == Missile.STATE_OUT) m_skill1_list.remove(i);
        }
        //필살기2
        for (int i = m_skill2_list.size() - 1; i >= 0; i--) {
            Skill2_Enemy_Explosion skill2 = m_skill2_list.get(i);
            skill2.Update(GameTime); //시간
            if (skill2.getAnimationEnd()) m_skill2_list.remove(i); //폭발애니메이션 끝나면 리스트삭제
        }

        for (int i = m_pmslist.size() - 1; i >= 0; i--) {
            Missile_Player pms = m_pmslist.get(i);
            pms.Update();
            if (pms.state == Missile.STATE_OUT) m_pmslist.remove(i);
        }
        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            Enemy enem = m_enemlist.get(i);
            enem.Update(GameTime);
            if (enem.state == Missile.STATE_OUT) m_enemlist.remove(i);
        }
        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
            Missile enemms = m_enemmslist.get(i);
            enemms.Update();
            if (enemms.state == Missile.STATE_OUT)
                m_enemmslist.remove(i);
        }
        //랜덤 박스 업데이트 및 상태 확인
        for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
            m_randomboxList.get(i).Update(GameTime);
            if (m_randomboxList.get(i).state == RandomBox.STATE_OUT)
                m_randomboxList.remove(i);
        }

        MakeRandomBox();
        makeEnemy();
        MakePlayerMissile();
        CheckCollision();
        Skill3();
    }

    //캔버스에 그려주는 메소드
    @Override
    public void Render(Canvas canvas) {

        m_background.Draw(canvas);
        if (missileState >= 2) { //좌측 보조 미사일
            for (Missile_Player lpms : m_lpmslist)
                lpms.Draw(canvas);
        }
        if (missileState == 3) { //우측 보조 미사일
            for (Missile_Player rpms : m_rpmslist)
                rpms.Draw(canvas);
        }
        try {
            for (Missile_Player pms : m_pmslist)
                pms.Draw(canvas);
            for (Missile enemms : m_enemmslist)
                enemms.Draw(canvas);
            for (Enemy enem : m_enemlist)
                enem.Draw(canvas);
            for (RandomBox randomBox : m_randomboxList)
                randomBox.Draw(canvas);
            for (Skill1_SuperMissile skill1 : m_skill1_list)
                skill1.Draw(canvas);
            for (Skill2_Enemy_Explosion skill2 : m_skill2_list)
                skill2.Draw(canvas);
        } catch (Exception e) {
            System.out.println("오류");
            e.printStackTrace();
        }

        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        //필살기3을 그려줌
        if (startskill3 == 3) m_circle.Draw(canvas);
        Paint p = new Paint();
        p.setTextSize(70);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        //텍스트(절대좌표)
//        if (m_player.getLife() == 1) {
//            p.setColor(Color.RED);
//            canvas.drawText("LIFE : " + String.valueOf(m_player.getLife()), 100, 100, p);
//            p.setColor(Color.WHITE);
//        } else {
//            p.setColor(Color.WHITE);
//            canvas.drawText("LIFE : " + String.valueOf(m_player.getLife()), 100, 100, p);
//        }
//        p.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("KILL : " + String.valueOf(killcnt), 600, 200, p);
//        p.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("SCORE : " + String.valueOf(50 * killcnt), 600, 100, p);
//        p.setTextSize((float) (width*0.05));
//        canvas.drawText("발사 속도 " + String.format("%.1f", nowMissileSpeed), 100, 1700, p);
//        canvas.drawText("필살기 " + String.valueOf(specialSkill), (float) (width*0.8), 1700, p);


        //텍스트(상대좌표)
        if (m_player.getLife() == 1) {
            p.setColor(Color.RED);
            canvas.drawText("LIFE : " + String.valueOf(m_player.getLife()), (float) (width*0.05), (float) (height*0.05), p);
            p.setColor(Color.WHITE);
        } else {
            p.setColor(Color.WHITE);
            canvas.drawText("LIFE : " + String.valueOf(m_player.getLife()), (float) (width*0.05), (float) (height*0.05), p);
        }
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("KILL : " + String.valueOf(killcnt), (float) (width*0.7), (float) (height*0.1), p);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("SCORE : " + String.valueOf(50 * killcnt),  (float) (width*0.7), (float) (height*0.05), p);
        p.setTextSize((float) (width*0.05));
        canvas.drawText("발사 속도 " + String.format("%.1f", nowMissileSpeed), (float) (width*0.05), (float) (height*0.95), p);
        canvas.drawText("필살기 " + String.valueOf(specialSkill), (float) (width*0.75), (float) (height*0.95), p);


        int cooldownTime = (int) ((System.currentTimeMillis() - Skilltime) / 1000);
        if (cooldownTime >= 5 && specialSkill > 0) {
            canvas.drawText("준비완료!", (float) (width*0.75), (float) (height*0.999), p);
        } else {
            if (cooldownTime >= 5) cooldownTime = 5;
            p.setColor(Color.RED);
            canvas.drawText("대기중 " + cooldownTime + "/5 s", (float) (width*0.75), (float) (height*0.999), p);
        }


    }

    //특정 변수
    int ax, ay, cir = 200;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int x = m_player.getX();
        final int y = m_player.getY();

        //방향 움직일 때 위치 변경
        if (startskill3 == 3) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                m_player.setPosition(x - 20, y);
                m_circle.setPosition(x - 20 - cir, y - cir);
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                m_player.setPosition(x + 20, y);
                m_circle.setPosition(x + 20 - cir, y - cir);
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                m_player.setPosition(x, y - 20);
                m_circle.setPosition(x - cir, y - 20 - cir);
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                m_player.setPosition(x, y + 20);
                m_circle.setPosition(x - cir, y + 20 - cir);
            }
        } else {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                m_player.setPosition(x - 20, y);
                ax = x - 20;
                ay = y;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                m_player.setPosition(x + 20, y);
                ax = x + 20;
                ay = y;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                m_player.setPosition(x, y - 20);
                ax = x;
                ay = y - 20;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                m_player.setPosition(x, y + 20);
                ax = x;
                ay = y + 20;
            }
        }


        if (keyCode == KeyEvent.KEYCODE_SPACE) //필살기 키
        {
            if (System.currentTimeMillis() - Skilltime < 5000) //딜레이 5초
            {
//                Toast myToast = Toast.makeText(mcontext, "쿨타임", Toast.LENGTH_SHORT);
//                myToast.show();
                return false;
            }
            Skilltime = System.currentTimeMillis(); //딜레이 측정하기 위해 이전값 기억
            if (specialSkill > 0) {
                specialSkill--;
                if (playertype == 0) {

                    MakeSkill1_SuperMissile();
                } else if (playertype == 1) {


//                    final Handler handler = new Handler();
//
//                    for (int i = 0; i < 3; i++)
//                    {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                for (int j = 0; j < 3; j++) {
//                                    Random r = new Random();
//                                    //이 부분이 폭발처리
//                                    m_skill2_list.add(new Skill2_Enemy_Explosion(r.nextInt(800), r.nextInt(1500)));
//
//                                }
//                                MakeSkill2_Explosion();
//                                //handler.postDelayed(this, 3000);
//                            }
//                        }, 2000);
//
//                    }
                    MakeSkill2_Explosion();
                } else if (playertype == 2) {

                    startskill3 = 3;
                    fflag = 1;
                    if (startflag == 1) {
                        m_circle.setPosition(m_player.getX(), m_player.getY());
                        startflag = 0;
                    }
                }
            }

        }

        return true;
    }

    //모든적, 랜덤박스, 필살기1 제거
    public void allclear() {
        //필살기1 제거
        for (int i = m_skill1_list.size() - 1; i >= 0; i--) {
            m_skill1_list.remove(i);
        }
        for (int i = m_pmslist.size() - 1; i >= 0; i--) {
            m_pmslist.remove(i);
        }

        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            m_enemlist.remove(i);
        }

        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
            m_enemmslist.remove(i);
        }
        for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
            m_randomboxList.remove(i);
        }

        //왼쪽 미사일
        for (int i = m_lpmslist.size() - 1; i >= 0; i--) {
            m_lpmslist.remove(i);
        }
        //오른쪽 미사일
        for (int i = m_rpmslist.size() - 1; i >= 0; i--) {
            m_rpmslist.remove(i);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        System.out.println("터치좌표 x : ," + x +" y : " + y);
        int px = m_player.getX();
        int py = m_player.getY();

        //플레이어 터치 후 움직임
        int difX = Math.abs(x - 90 - m_player.getX()); //비행기의 중점으로 비교하기 위해
        int difY = Math.abs(y - 90 - m_player.getY()); //
//        if (difX < 250 && difY < 250) m_player.setPosition(x - 90, y - 90); //터치

        //버튼 범위설정(절대)
//        Rect r_left = new Rect(0, 1300, 100, 1400);
//        Rect r_right = new Rect(200, 1300, 300, 1400);
//        Rect r_down = new Rect(100, 1200, 200, 1300);
//        Rect r_up = new Rect(100, 1400, 200, 1500);
        //버튼 범위설정(상대)
        int top = (int) (height*0.7+100);
        Rect r_left = new Rect(0, top, 100, top+100);
        Rect r_right = new Rect(200, top, 300, top+100);
        Rect r_down = new Rect(100, top-100, 200, top);
        Rect r_up = new Rect(100, top+100, 200, top+200);

        //터치패드 구현
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (r_left.contains(x, y)) {
                SoundManager.getInstance().play(4);
                m_player.setPosition(px - 20, py);
                ax = px - 20;
                ay = py;
                if (startskill3 == 3) {
                    m_circle.setPosition(px - 20 - cir, py - cir);
                }
            } else if (r_right.contains(x, y)) {
                SoundManager.getInstance().play(4);
                m_player.setPosition(px + 20, py);
                ax = px + 20;
                ay = py;
                if (startskill3 == 3) {
                    m_circle.setPosition(px + 20 - cir, py - cir);
                }
            } else if (r_up.contains(x, y)) {
                SoundManager.getInstance().play(4);
                m_player.setPosition(px, py + 20);
                ax = px;
                ay = py + 20;
                if (startskill3 == 3) {
                    m_circle.setPosition(px - cir, py + 20 - cir);
                }
            } else if (r_down.contains(x, y)) {
                SoundManager.getInstance().play(4);
                m_player.setPosition(px, py - 20);
                ax = px;
                ay = py - 20;
                if (startskill3 == 3) {
                    m_circle.setPosition(px - cir, py - cir - 20);
                }
            }
        }

        //플레이어3 필살기 자기장 같이 이동
        if (startskill3 == 3 && r_left.contains(x, y) == false
                && r_right.contains(x, y) == false
                && r_down.contains(x, y) == false
                && r_up.contains(x, y) == false) {
            m_circle.setPosition(x - 280, y - 280);
            m_player.setPosition(x - 90, y - 90);
        } else if (difX < 250 && difY < 250) {
            //m_circle.setPosition(a - 280, b - 280);
            m_player.setPosition(x - 90, y - 90);
            //a=x;b=y;
        }
        return true;
    }

    public void Skill3() {
        //무적상태
        long time = GameTime - Skilltime;
        if (GameTime <= Skilltime + 5000 && GameTime >= Skilltime) {
            //음악추가
        } else {
            startskill3 = 0;
        }
    }

    public void MakePlayerMissile()// 미사일을 생성
    {
        //미사일이 1초마다 나감
        if (System.currentTimeMillis() - PlayerMissile >= missileSpeed) {
            PlayerMissile = System.currentTimeMillis();
            // 여기서 플레이어와 같은 위치에서 미사일이 나감
            Missile_Player pms = new Missile_Player(m_player.getX() + 30, m_player.getY() - 150, missileSpeed);// 미사일 위치,위치,속도

            // 플레이어 미사일 list에 추가
            m_pmslist.add(pms);

            if (missileState >= 2) {//2단계일 경우 왼쪽
                Missile_Player pms1 = new Missile_Player(m_player.getX() - 60, m_player.getY() - 150, missileSpeed);
                m_lpmslist.add(pms1);
            }

            if (missileState == 3) {
                Missile_Player pms2 = new Missile_Player(m_player.getX() + 120, m_player.getY() - 150, missileSpeed);
                m_lpmslist.add(pms2);
            }
            SoundManager.getInstance().play(1);
        }

    }

    public void MakeRandomBox()// 랜덤박스 생성
    {
        if (System.currentTimeMillis() - LastRegenBox >= 2500) {
            LastRegenBox = System.currentTimeMillis();

            RandomBox m_randomBox = null;
            int boxType = randomBox.nextInt(8);
            //boxType = 1;

            if (boxType == 0)//필살기 증가
                m_randomBox = new RandomBox_plusEffect();
            else if (boxType == 1)//더 빠르게 발사
                m_randomBox = new RandomBox_missileSpeedUp();
            else if (boxType == 2)//미사일 업그레이드
                m_randomBox = new RandomBox_attackUp();
            else if (boxType == 3)//목숨 증가
                m_randomBox = new RandomBox_plusLife();
            else if (boxType == 4)//목숨 감소
                m_randomBox = new RandomBox_subLife();
            else if (boxType == 5)//더 느리게 발사
                m_randomBox = new RandomBox_missileSpeedDown();
            else if (boxType == 6)//필살기 감소
                m_randomBox = new RandomBox_subEffect();
            else if (boxType == 7)//미사일 다운그레이드
                m_randomBox = new RandomBox_attackDown();

            m_randomBox.setPosition(120 + (int) randomBox.nextInt(width - 320), -60); // 랜덤 박스 위치
            m_randomboxList.add(m_randomBox);

        }
    }

    //필살기 스킬1
    public void MakeSkill1_SuperMissile() {
        //화면 중간 아래에서부터 미사일 시작
        Skill1_SuperMissile skill = new Skill1_SuperMissile(0, 1800, 3000);// 미사일 위치,위치,속도
        SoundManager.getInstance().play(3);
        // 플레이어 미사일 list에 추가
        m_skill1_list.add(skill);
        SoundManager.getInstance().play(1);
    }

    //필살기 스킬2
    public void MakeSkill2_Explosion() {
        SoundManager.getInstance().play(3);

//        for (int i = 0; i < 10; i++) {
//            Random r = new Random();
//            //이 부분이 폭발처리
//            m_skill2_list.add(new Skill2_Enemy_Explosion(r.nextInt(800), r.nextInt(1500)));
//
//        }

        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            //이 부분이 폭발처리
            m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
            m_enemlist.remove(i);
            killcnt++;
        }
//
////        //미사일까지 삭제하면 에러
        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
//            m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY())); //이부분 항상 튕김
            m_enemmslist.remove(i);
        }
        //마지막 정리(필요성 테스트필요) 랜덤박스까지 삭제해버림
        //allclear();
    }

    public void CheckCollision() {

        //필살기1 충돌
        if (playertype == 0) {
            for (int i = m_skill1_list.size() - 1; i >= 0; i--) {
                for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                    if (CollisionManager.CheckBoxToBox(m_skill1_list.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                        //m_skill1_list.remove(i);
                        //이 부분이 폭발처리
                        m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(j).getX(), m_enemlist.get(j).getY()));
                        m_enemlist.remove(j);

                        //System.out.println(j + " 적기 삭제");
                        //SoundManager.getInstance().play(5);
                        killcnt++;

                        return;
                    }
                }
            }
            //스킬1 미사일처리
            for (int i = m_skill1_list.size() - 1; i >= 0; i--) {
                for (int j = m_enemmslist.size() - 1; j >= 0; j--) {
                    if (CollisionManager.CheckBoxToBox(m_skill1_list.get(i).m_BoundBox, m_enemmslist.get(j).m_BoundBox)) {
                        //m_skill1_list.remove(i);
                        //이 부분이 폭발처리
                        m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemmslist.get(j).getX(), m_enemmslist.get(j).getY()));
                        m_enemmslist.remove(j);

                        // System.out.println(j + " 적 미사일 삭제");
                        //SoundManager.getInstance().play(5);
                        //killcnt++;

                        return;
                    }
                }
            }
        }

        //내 미사일과 적기 충돌시
        for (int i = m_pmslist.size() - 1; i >= 0; i--) {
            for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                if (CollisionManager.CheckBoxToBox(m_pmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                    //이 부분이 폭발처리
                    System.out.println("내 미사일 적기랑 충돌");
                    m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(j).getX(), m_enemlist.get(j).getY()));
                    m_pmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;
                    return;
                }
            }
        }

        //왼쪽 미사일 충돌 구현
        for (int i = m_lpmslist.size() - 1; i >= 0; i--) {
            for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                if (CollisionManager.CheckBoxToBox(m_lpmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                    //이 부분이 폭발처리
                    System.out.println("왼쪽미사일 적기랑 충돌");
                    //m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
                    m_lpmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;
                    return;
                }
            }
        }

        //오른쪽 미사일 충돌 구현
        for (int i = m_rpmslist.size() - 1; i >= 0; i--) {
            for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                if (CollisionManager.CheckBoxToBox(m_rpmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                    //이 부분이 폭발처리
                    System.out.println("오른쪽 적기랑 충돌");
                    m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(j).getX(), m_enemlist.get(j).getY()));
                    m_rpmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;
                    return;
                }
            }
        }

        //필살기 스킬3은 일단 폭발처리 x
        if (startskill3 == 3) {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_circle.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    //이 부분이 폭발처리
                    // m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
                    m_enemlist.remove(i);
                    killcnt++;
                }
            }

            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_circle.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    //이 부분이 폭발처리
                    // m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
                    m_enemmslist.remove(i);
                }
            }
        } else //필살기 스킬3이 아닌 충돌
        {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    System.out.println("나랑 적기랑 충돌");
                    m_enemlist.remove(i);

                    //이 부분이 플레이어를 폭발처리 (에러발생가능성)
                    m_skill2_list.add(new Skill2_Enemy_Explosion(m_player.getX(), m_player.getY()));
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    System.out.println("나랑 적미사일 충돌");
                    m_enemmslist.remove(i);

                    //이 부분이 플레이어를 폭발처리
                    m_skill2_list.add(new Skill2_Enemy_Explosion(m_player.getX(), m_player.getY()));
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
        }

        //바로위랑 아래 똑같은 뭉치??
//        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
//            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
//                m_enemlist.remove(i);
//                //이 부분이 폭발처리
////                m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
//                m_player.destroyPlayer();
//                if (m_player.getLife() <= 0)
//                    AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
//            }
//        }
//
//        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
//            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
//                m_enemmslist.remove(i);
//                //이 부분이 폭발처리
////                m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
//                m_player.destroyPlayer();
//                if (m_player.getLife() <= 0)
//                    AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
//            }
//        }


        //랜덤박스충돌
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.postDelayed(new Runnable() {

            @Override

            public void run() {

                // 내용

                String randName = null;
                for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
                    if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_randomboxList.get(i).m_BoundBox)) {
                        if (m_randomboxList.get(i).boxtype == 0) {//필살기 +1
                            SoundManager.getInstance().play(8);
                            specialSkill++;
                            randName = "Skill +1";
                        } else if (m_randomboxList.get(i).boxtype == 1) {//발사속도 +200
                            SoundManager.getInstance().play(8);
                            missileSpeed -= 200;
                            nowMissileSpeed += 0.2f;
                            randName = "Rate of fire +20%";
                        } else if (m_randomboxList.get(i).boxtype == 2) {//미사일 업그레이드
                            SoundManager.getInstance().play(8);
                            missileState++;
                            randName = "Missile upgrade";
                        } else if (m_randomboxList.get(i).boxtype == 3) {//생명증가
                            m_player.addLife();
                            randName = "Life +1";
                        } else if (m_randomboxList.get(i).boxtype == 4) {//생명감소
                            m_player.destroyPlayer();
                            randName = "Life -1";
                        } else if (m_randomboxList.get(i).boxtype == 5) {//발사속도 - 100
                            SoundManager.getInstance().play(9);
                            missileSpeed += 100;
                            nowMissileSpeed -= 0.1f;
                            randName = "Rate of fire -10%";
                        } else if (m_randomboxList.get(i).boxtype == 6) {//필살기 -1
                            SoundManager.getInstance().play(9);
                            specialSkill--;
                            randName = "Skill -1";
                        } else if (m_randomboxList.get(i).boxtype == 7) {//미사일 다운그레이드
                            SoundManager.getInstance().play(9);
                            missileState--;
                            randName = "Missile Downgrade";
                        }
                        m_randomboxList.remove(i);

                        Toast myToast = Toast.makeText(mcontext, randName, Toast.LENGTH_SHORT);
                        myToast.show();

                        return;
                    }
                }


            }

        }, 500);
    }
}

