package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.LogRecord;

public class GameState implements IState {
    public Player m_player;
    private BackGround m_background;
    private GraphicObject m_keypad;
    public static int playertype;
    public Enemy m_enemy;
    private M_Circle m_circle;

    public static int killcnt = 0;

    int missileSpeed;//미사일 발사 속도
    float nowMissileSpeed;//현재 발사 속도 나타내주는 변수
    int specialSkill;//필살기 개수

    long Skilltime = 0;
    int startskill3;
    int missileState;
    int score;

    long GameTime;
    int fflag, a, b, startflag;

    long PlayerMissile = System.currentTimeMillis();
    long LastRegenBox = System.currentTimeMillis();

    Random randEnem = new Random();
    Random randomBox = new Random();//랜덤 함수

    int width = AppManager.getInstance().getGameView().getDisplay().getWidth();//너비측정

    ArrayList<Enemy> m_enemlist = new ArrayList<Enemy>();//적리스트
    public ArrayList<Missile_Player> m_pmslist = new ArrayList<Missile_Player>();//미사일 리스트
    ArrayList<Missile_Player> m_lpmslist = new ArrayList<Missile_Player>();//왼쪽 미사일 리스트
    ArrayList<Missile_Player> m_rpmslist = new ArrayList<Missile_Player>();//오른쪽 미사일 리스트
    ArrayList<Missile> m_enemmslist = new ArrayList<Missile>();//적 미사일 리스트
    ArrayList<RandomBox> m_randomboxList = new ArrayList<RandomBox>();//랜덤 박스


    //필살기2로 루프 중에 적기 삭제시 에러발생-해결
    Iterator iter = m_enemlist.iterator();
    public ArrayList<Skill1_SuperMissile> m_skilllist = new ArrayList<Skill1_SuperMissile>();//미사일 위치
    ArrayList<Skill2_Enemy_Explosion> m_skill2_list = new ArrayList<Skill2_Enemy_Explosion>(); //폭발할 적 리스트

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance() {
        return game;
    }

    public GameState() {
        playertype = 3;
    }


    public void makeEnemy() {

        if (System.currentTimeMillis() - LastRegenEnemy >= 1000) { //적생성 주기
            LastRegenEnemy = System.currentTimeMillis();

            int enemytype = randEnem.nextInt(3);
            Enemy enem = null;

            if (enemytype == 0)
                enem = new Enemy_1();
            else if (enemytype == 1)
                enem = new Enemy_2();
            else if (enemytype == 2)
                enem = new Enemy_3();

            enem.setPosition(randEnem.nextInt(1000), -60);
            enem.movetype = randEnem.nextInt(3);

            m_enemlist.add(enem);
        }

    }

    @Override
    public void Init() {
        m_circle = new M_Circle(AppManager.getInstance().getBitmap(R.drawable.circle_4));
        //게임시작시 모든 적을 제거, 죽인 수 초기화 후 진행
        allclear();
        killcnt = 0;
        missileSpeed = 1000;
        nowMissileSpeed = 1.0f;
        missileState = 1;
        score = 0;
        specialSkill = 30; //초기 필살기 개수

        if (playertype == 0) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player1));
        } else if (playertype == 1) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player2));
        } else if (playertype == 2)
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.player3));

        m_keypad = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.keypad));
        m_circle = new M_Circle(AppManager.getInstance().getBitmap(R.drawable.circle_5));
        m_background = new BackGround(2);
        m_keypad.setPosition(0, 1200);
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
            specialSkill = 10; //임시

        //최대 미사일 발사 속도 조절
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
        for (int i = m_skilllist.size() - 1; i >= 0; i--) {
            Skill1_SuperMissile skill1 = m_skilllist.get(i);
            skill1.Update();
            if (skill1.state == Missile.STATE_OUT) m_skilllist.remove(i);
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

    @Override
    public void Render(Canvas canvas) {

        m_background.Draw(canvas);
        if (missileState >= 2) {
            for (Missile_Player lpms : m_lpmslist)
                lpms.Draw(canvas);
        }
        if (missileState == 3) {
            for (Missile_Player rpms : m_rpmslist)
                rpms.Draw(canvas);
        }
        for (Missile_Player pms : m_pmslist)
            pms.Draw(canvas);
        for (Missile enemms : m_enemmslist)
            enemms.Draw(canvas);
        for (Enemy enem : m_enemlist)
            enem.Draw(canvas);
        for (RandomBox randomBox : m_randomboxList)
            randomBox.Draw(canvas);
        for (Skill1_SuperMissile skill1 : m_skilllist)
            skill1.Draw(canvas);
        for (Skill2_Enemy_Explosion skill2 : m_skill2_list)
            skill2.Draw(canvas);

        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        if (startskill3 == 3) m_circle.Draw(canvas);
        Paint p = new Paint();
        p.setTextSize(70);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setColor(Color.WHITE);
        canvas.drawText("LIFE : " + String.valueOf(m_player.getLife()), 100, 100, p);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("KILL : " + String.valueOf(killcnt), 600, 200, p);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("SCORE : " + String.valueOf(50 * killcnt), 600, 100, p);
        p.setTextSize(40);
        canvas.drawText("발사 속도 : " + String.format("%.1f", nowMissileSpeed), 100, 1600, p);
        canvas.drawText("필살기 " + String.valueOf(specialSkill), 750, 1600, p);
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
            if (specialSkill > 0) {
                specialSkill--;
                if (playertype == 0) {

                    MakeSkill1_SuperMissile();
                } else if (playertype == 1) {

//                    new Handler().postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Random rand = new Random();
//                            m_skill2_list.add(new Skill2_Enemy_Explosion(rand.nextInt(800),
//                                    rand.nextInt(1300)));
//                        }
//                    }, 1000);
                    final int[] i = {0};
                    final int[] tmp_y = {1000};
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i[0] < 3) {
                                Random rand = new Random();
                                for (int j = 0; j < 5; j++) {
                                    m_skill2_list.add(new Skill2_Enemy_Explosion(rand.nextInt(800),
                                            rand.nextInt(1300)));
//                                    m_skill2_list.add(new Skill2_Enemy_Explosion(rand.nextInt(80),
//                                            tmp_y[0]));
//                                    tmp_y[0] = tmp_y[0] -100;

                                }
                                allclear();
                                handler.postDelayed(this, 2000);
                                i[0]++;
                            }
                        }
                    }, 2000);

//                    MakeSkill2_Explosion();

                } else if (playertype == 2) {
                    Skilltime = System.currentTimeMillis();
                    startskill3 = 3;
                    fflag = 1;
                    if (startflag == 1) {
                        m_circle.setPosition(246, 1100);
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
        for (int i = m_skilllist.size() - 1; i >= 0; i--) {
            m_skilllist.remove(i);
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

        int px = m_player.getX();
        int py = m_player.getY();

        //플레이어 터치 후 움직임
        int difX = Math.abs(x - m_player.getX());
        int difY = Math.abs(y - m_player.getY());
//        if (difX < 250 && difY < 250) m_player.setPosition(x - 90, y - 90); //터치

        //버튼 범위설정
        Rect r_left = new Rect(0, 1300, 100, 1400);
        Rect r_right = new Rect(200, 1300, 300, 1400);
        Rect r_down = new Rect(100, 1200, 200, 1300);
        Rect r_up = new Rect(100, 1400, 200, 1500);

        //터치패드 구현
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

    public void MakeRandomBox()// 랜덤 박스 생성
    {
        if (System.currentTimeMillis() - LastRegenBox >= 2500) {
            LastRegenBox = System.currentTimeMillis();

            RandomBox m_randomBox = null;
            int boxType = randomBox.nextInt(8);

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
        Skill1_SuperMissile skill = new Skill1_SuperMissile(0, 1800, 500);// 미사일 위치,위치,속도
        SoundManager.getInstance().play(3);
        // 플레이어 미사일 list에 추가
        m_skilllist.add(skill);
        SoundManager.getInstance().play(1);
    }

    //필살기 스킬2
    public void MakeSkill2_Explosion() {
        SoundManager.getInstance().play(3);
        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            m_enemlist.remove(i);
            //이 부분이 폭발처리

            final int finalI = i;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(finalI).getX(),
                            m_enemlist.get(finalI).getY()));
                }
            }, 1000);


        }

//        //미사일까지 삭제하면 에러
//        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
//            m_enemmslist.remove(i);
//        }
    }

    public void CheckCollision() {

        //필살기1 충돌
        if (playertype == 0) {
            for (int i = m_skilllist.size() - 1; i >= 0; i--) {
                for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                    if (CollisionManager.CheckBoxToBox(m_skilllist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                        //m_skilllist.remove(i);
                        //이 부분이 폭발처리
//                        m_skill2_list.add(new Skill2_Enemy_Explosion(m_enemlist.get(i).getX(),
//                                m_enemlist.get(i).getY()));
                        m_enemlist.remove(j);

                        System.out.println(j + " 적기 삭제");
                        //SoundManager.getInstance().play(5);
                        killcnt++;

                        return;
                    }
                }
            }
            for (int i = m_skilllist.size() - 1; i >= 0; i--) {
                for (int j = m_enemmslist.size() - 1; j >= 0; j--) {
                    if (CollisionManager.CheckBoxToBox(m_skilllist.get(i).m_BoundBox, m_enemmslist.get(j).m_BoundBox)) {
                        //m_skilllist.remove(i);

                        m_enemmslist.remove(j);

                        System.out.println(j + " 적 미사일 삭제");
                        //SoundManager.getInstance().play(5);
                        //killcnt++;

                        return;
                    }
                }
            }
        }

        for (int i = m_pmslist.size() - 1; i >= 0; i--) {
            for (int j = m_enemlist.size() - 1; j >= 0; j--) {
                if (CollisionManager.CheckBoxToBox(m_pmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
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
                    m_rpmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;
                    return;
                }
            }
        }

        if (startskill3 == 3) {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_circle.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    m_enemlist.remove(i);
                    killcnt++;
                }
            }

            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_circle.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    m_enemmslist.remove(i);
                }
            }
        } else {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    m_enemlist.remove(i);
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    m_enemmslist.remove(i);
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
        }

        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                m_enemlist.remove(i);
                m_player.destroyPlayer();
                if (m_player.getLife() <= 0)
                    AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
            }
        }

        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                m_enemmslist.remove(i);
                m_player.destroyPlayer();
                if (m_player.getLife() <= 0)
                    AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
            }
        }

        //랜덤박스충돌
        for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
            if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_randomboxList.get(i).m_BoundBox)) {
                if (m_randomboxList.get(i).boxtype == 0) {//필살기 +1
                    SoundManager.getInstance().play(8);
                    specialSkill++;
                } else if (m_randomboxList.get(i).boxtype == 1) {//발사속도 + 100
                    SoundManager.getInstance().play(8);
                    missileSpeed -= 100;
                    nowMissileSpeed -= 0.1f;
                } else if (m_randomboxList.get(i).boxtype == 2) {//미사일 업그레이드
                    SoundManager.getInstance().play(8);
                    missileState++;
                } else if (m_randomboxList.get(i).boxtype == 3) {//생명증가
                    m_player.addLife();
                } else if (m_randomboxList.get(i).boxtype == 4) {//생명감소
                    m_player.destroyPlayer();
                } else if (m_randomboxList.get(i).boxtype == 5) {//발사속도 -20
                    SoundManager.getInstance().play(9);
                    missileSpeed += 100;
                    nowMissileSpeed += 0.1f;
                } else if (m_randomboxList.get(i).boxtype == 6) {//필살기 -1
                    SoundManager.getInstance().play(9);
                    specialSkill--;
                } else if (m_randomboxList.get(i).boxtype == 7) {//미사일 다운그레이드
                    SoundManager.getInstance().play(9);
                    missileState--;
                }
                m_randomboxList.remove(i);
                return;
            }
        }
    }
}

