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

import org.Game.enemy.Enemy;
import org.Game.enemy.Enemy_1;
import org.Game.enemy.Enemy_2;
import org.Game.enemy.Enemy_3;
import org.Game.missile.Missile;
import org.Game.missile.Missile_Player;
import org.Game.player.Player;
import org.Game.player.Player_1;
import org.Game.player.Player_2;
import org.Game.player.Player_3;
import org.Game.render.BackGround;
import org.Game.randomBox.RandomBox;
import org.Game.randomBox.RandomBox_attackDown;
import org.Game.randomBox.RandomBox_attackUp;
import org.Game.randomBox.RandomBox_missileSpeedDown;
import org.Game.randomBox.RandomBox_missileSpeedUp;
import org.Game.randomBox.RandomBox_plusEffect;
import org.Game.randomBox.RandomBox_plusLife;
import org.Game.randomBox.RandomBox_subEffect;
import org.Game.randomBox.RandomBox_subLife;
import org.Game.skill.Skill1_SuperMissile;
import org.Game.skill.Skill2_EnemyExplosion;
import org.Game.skill.Skill3_Invincible;

import java.util.ArrayList;
import java.util.Random;


import static com.example.gameframework.MainActivity.mcontext;

public class GameState implements IState {
    public Player m_player;
    private BackGround m_background;
    private GraphicObject m_life_full_image, m_life_empty_image;
    private GraphicObject m_skill_icon, m_skill_btn_icon;
    public static int playertype; //비행기 타입
    public Skill3_Invincible skill3Invincible;

    public static int killcnt = 0; //적 처치수

    int missileSpeed;//미사일 발사 속도
    float nowMissileSpeed;//현재 발사 속도 나타내주는 변수
    int skill_cnt;//필살기 개수

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

    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;

    public ArrayList<GraphicObject> m_enemlist = new ArrayList<>();//적리스트
    public ArrayList<GraphicObject> m_pmslist = new ArrayList<>();//미사일 리스트
    public ArrayList<GraphicObject> m_lpmslist = new ArrayList<>();//왼쪽 미사일 리스트
    public ArrayList<GraphicObject> m_rpmslist = new ArrayList<>();//오른쪽 미사일 리스트
    public ArrayList<GraphicObject> m_enemmslist = new ArrayList<>();//적 미사일 리스트
    public ArrayList<RandomBox> m_randomboxList = new ArrayList<>();//랜덤 박스
    public ArrayList<GraphicObject> m_skill1_list = new ArrayList<>();//미사일 위치
    public ArrayList<GraphicObject> m_skill2_list = new ArrayList<>(); //폭발할 적 리스트
    public ArrayList<Explosion_Blue> m_explosion_blue = new ArrayList<>(); //파란 폭

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance() {
        return game;
    }

    public GameState() {
        playertype = 3;
    }

    /**
     * 적을 생성하는 메소드
     */
    public void makeEnemy() {
        if (System.currentTimeMillis() - LastRegenEnemy >= 700) { //적생성 주기(디폴트 값은 1000)
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

    /**
     * 게임 시작시 첫 화면 세팅
     */
    @Override
    public void Init() {
        allclear(); //게임시작시 모든 적을 제거, 죽인 수 초기화 후 진행

        killcnt = 0;
        missileSpeed = 1000;
        nowMissileSpeed = 1.0f;
        missileState = 1;
        score = 0;
        skill_cnt = 3; //초기 필살기 개수

        if (playertype == 0) {
            m_player = new Player_1();
            m_skill_icon = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.skill1_icon));
        } else if (playertype == 1) {
            m_player = new Player_2();
            m_skill_icon = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.skill2_icon));
        } else if (playertype == 2) {
            m_player = new Player_3();
            m_skill_icon = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.skill3_icon));
        }

        m_life_full_image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.heart_full));
        m_life_empty_image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.heart_empty));
        m_skill_btn_icon = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.skill_btn_icon));
        skill3Invincible = new Skill3_Invincible(AppManager.getInstance().getBitmap(R.drawable.circle_5));
        m_background = new BackGround(2);

        //스킬버튼 상대좌표
        m_skill_btn_icon.setPosition(0, height - m_skill_btn_icon.m_bitmap.getHeight());
    }

    @Override
    public void Destroy() {}

    /**
     * 화면에 실시간으로 업데이트 하는 메소드
     */
    @Override
    public void Update() {
        try {
            GameTime = System.currentTimeMillis();
            m_player.Update(GameTime);
            m_background.Update(GameTime);
            skill3Invincible.Update(GameTime);

            //필살기 최대, 최소 조절
            if (skill_cnt < 0) {
                skill_cnt = 0;
            }

            if (skill_cnt > 3)
                skill_cnt = 3; //필살기 최대 3개 소유가능

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
                    Missile_Player lpms = (Missile_Player) m_lpmslist.get(i);
                    lpms.Update();
                    if (lpms.state == Missile.STATE_OUT) m_lpmslist.remove(i);
                }
            }

            //3단계일떄
            if (missileState == 3) {
                for (int i = m_rpmslist.size() - 1; i >= 0; i--) {
                    Missile_Player rpms = (Missile_Player) m_rpmslist.get(i);
                    rpms.Update();
                    if (rpms.state == Missile.STATE_OUT) m_rpmslist.remove(i);
                }
            }

            //라이프가 0이될때 죽어야하므로 주기적인 체크
            if (m_player.getLife() <= 0)
                AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());

            //필살기1 포문 한 번만 진행
            for (int i = m_skill1_list.size() - 1; i >= 0; i--) {
                Skill1_SuperMissile skill1 = (Skill1_SuperMissile) m_skill1_list.get(i);
                skill1.Update();
                if (skill1.state == Missile.STATE_OUT) m_skill1_list.remove(i);
            }
            //필살기2
            for (int i = m_skill2_list.size() - 1; i >= 0; i--) {
                Skill2_EnemyExplosion skill2 = (Skill2_EnemyExplosion) m_skill2_list.get(i);
                skill2.Update(GameTime); //시간
                if (skill2.getAnimationEnd()) m_skill2_list.remove(i); //폭발애니메이션 끝나면 리스트삭제
            }
            //파란 폭발
            for (int i = m_explosion_blue.size() - 1; i >= 0; i--) {
                Explosion_Blue explosion_blue = m_explosion_blue.get(i);
                explosion_blue.Update(GameTime); //시간
                if (explosion_blue.getAnimationEnd())
                    m_explosion_blue.remove(i); //폭발애니메이션 끝나면 리스트삭제
            }

            for (int i = m_pmslist.size() - 1; i >= 0; i--) {
                Missile_Player pms = (Missile_Player) m_pmslist.get(i);
                pms.Update();
                if (pms.state == Missile.STATE_OUT) m_pmslist.remove(i);
            }
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                Enemy enem = (Enemy) m_enemlist.get(i);
                enem.Update(GameTime);
                if (enem.state == Missile.STATE_OUT) m_enemlist.remove(i);
            }
            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                Missile enemms = (Missile) m_enemmslist.get(i);
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
        } catch (Exception e) {
            System.out.println("오류");
            e.printStackTrace();
        }
    }

    /**
     * 캔버스에 그려주는 메소드
     * @param canvas
     */
    @Override
    public void Render(Canvas canvas) {

        m_background.Draw(canvas);
        if (missileState >= 2) { //좌측 보조 미사일
            for (GraphicObject lpms : m_lpmslist)
                lpms.Draw(canvas);
        }
        if (missileState == 3) { //우측 보조 미사일
            for (GraphicObject rpms : m_rpmslist)
                rpms.Draw(canvas);
        }
        try {
            for (GraphicObject pms : m_pmslist)
                pms.Draw(canvas);
            for (GraphicObject enemms : m_enemmslist)
                enemms.Draw(canvas);
            for (GraphicObject enem : m_enemlist)
                enem.Draw(canvas);
            for (GraphicObject randomBox : m_randomboxList)
                randomBox.Draw(canvas);
            for (GraphicObject skill1 : m_skill1_list)
                skill1.Draw(canvas);
            for (GraphicObject skill2 : m_skill2_list)
                skill2.Draw(canvas);
            for (Explosion_Blue explosion_blue : m_explosion_blue)
                explosion_blue.Draw(canvas);
        } catch (Exception e) {
            System.out.println("오류");
            e.printStackTrace();
        }

        m_player.Draw(canvas);
        m_skill_btn_icon.Draw(canvas);

        for (int i = 0; i < 5; i++) //하트를 그려줌
        {
            if (m_player.getLife() > i) {
                m_life_full_image.setPosition(width * 0.02 + width * 0.105 * i, height * 0.01);
                m_life_full_image.Draw(canvas);
            } else {
                m_life_empty_image.setPosition(width * 0.02 + width * 0.105 * i, height * 0.01);
                m_life_empty_image.Draw(canvas);
            }
        }

        //필살기아이콘
        if (playertype == 0) {
            m_skill_icon.setPosition(m_skill_btn_icon.getX() + m_skill_btn_icon.m_bitmap.getWidth() * 0.23,
                    m_skill_btn_icon.getY() + m_skill_btn_icon.m_bitmap.getHeight() * 0.1);
        } else if (playertype == 1) {
            m_skill_icon.setPosition(m_skill_btn_icon.getX() + m_skill_btn_icon.m_bitmap.getWidth() * 0.14,
                    m_skill_btn_icon.getY() + m_skill_btn_icon.m_bitmap.getHeight() * 0.22);
        } else if (playertype == 2) {
            m_skill_icon.setPosition(m_skill_btn_icon.getX() + m_skill_btn_icon.m_bitmap.getWidth() * 0.1,
                    m_skill_btn_icon.getY() + m_skill_btn_icon.m_bitmap.getHeight() * 0.12);
        }
        m_skill_icon.Draw(canvas);


        //필살기3을 그려줌
        if (startskill3 == 3) {
            skill3Invincible.Draw(canvas);
        }
        Paint p = new Paint();
        p.setTextSize((float) (width * 0.07));
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setColor(Color.WHITE);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("SCORE " + String.valueOf(50 * killcnt), (float) (width * 0.55), (float) (height * 0.05), p);
        p.setTextSize((float) (width * 0.06));
        canvas.drawText("KILL " + String.valueOf(killcnt), (float) (width * 0.55), (float) (height * 0.1), p);

        canvas.drawText("x" + String.valueOf(skill_cnt), (float) (width * 0.25), (float) (height * 0.98), p);

        int cooldownTime = (int) ((System.currentTimeMillis() - Skilltime) / 1000);
        p.setTextSize((float) (width * 0.05));
        canvas.drawText("Attack Speed " + String.format("%.1f", nowMissileSpeed), 0,
                (float) (m_skill_btn_icon.getY() - height * 0.01), p);
        if (cooldownTime >= 5 && skill_cnt > 0) {
            canvas.drawText("Ready!", (float) (width * 0.35), (float) (height * 0.98), p);
        } else {
            if (cooldownTime >= 5) cooldownTime = 5;
            p.setColor(Color.RED);
            canvas.drawText(cooldownTime + "/5 s", (float) (width * 0.35), (float) (height * 0.98), p);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {return true;}

    /**
     * 화면 상의 모든 적과 미사일을 삭제하는 메소드
     */
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

    /**
     * 화면을 터치하는 좌표를 따라 비행기가 이동하는 메소드
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        //플레이어 터치 후 움직임
        int difX = (int) Math.abs(x - width * 0.1 - m_player.getX()); //비행기의 중점으로 비교하기 위해
        int difY = (int) Math.abs(y - width * 0.1 - m_player.getY()); //0.0857
        if (difX < width * 0.3 && difY < width * 0.3)
            m_player.setPosition(x - width * 0.15,
                    y - width * 0.2); //터치

        Rect r_skill_btn = new Rect(m_skill_btn_icon.getX(), m_skill_btn_icon.getY(),
                m_skill_btn_icon.getX() + m_skill_btn_icon.m_bitmap.getWidth(),
                m_skill_btn_icon.getY() + m_skill_btn_icon.m_bitmap.getHeight());


        if (r_skill_btn.contains(x, y) && event.getPointerCount() > 0) {
            if (System.currentTimeMillis() - Skilltime < 5000) //딜레이 5초
            {
                return false;
            }

            if (skill_cnt > 0) {
                Skilltime = System.currentTimeMillis(); //딜레이 측정하기 위해 이전값 기억
                skill_cnt--;
                if (playertype == 0) {
                    MakeSkill1_SuperMissile();
                } else if (playertype == 1) {
                    MakeSkill2_Explosion();
                } else if (playertype == 2) {
                    startskill3 = 3;
                    fflag = 1;
                    if (startflag == 1) {
                        skill3Invincible.setPosition(m_player.getX(), m_player.getY());
                        startflag = 0;
                    }
                }
            }
        }

        if (playertype == 2) {
            skill3Invincible.setPosition(m_player.getX() - skill3Invincible.m_bitmap.getHeight() / 2.8,
                    m_player.getY() - skill3Invincible.m_bitmap.getHeight() / 4);
        }
        return true;
    }

    /**
     * 지정한 시간 간격으로 미사일을 생성하는 메소드
     */
    public void MakePlayerMissile() {// 미사일을 생성
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

    /**
     * 랜덤한 아이템박스를 생성하는 메소드
     */
    public void MakeRandomBox() {
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

    /**
     * 스킬1: 대왕 미사일 발사
     */
    public void MakeSkill1_SuperMissile() {
        //화면 중간 아래에서부터 미사일 시작
        Skill1_SuperMissile skill = new Skill1_SuperMissile(0, 1800, 20);// 미사일 위치,위치,속도
        SoundManager.getInstance().play(3);
        // 플레이어 미사일 list에 추가
        m_skill1_list.add(skill);
        SoundManager.getInstance().play(1);
    }

    /**
     * 스킬2: 화면 상의 모든 적과 미사일 폭발처
     */
    public void MakeSkill2_Explosion() {
        SoundManager.getInstance().play(3);
        for (int i = m_enemlist.size() - 1; i >= 0; i--) {
            //이 부분이 폭발처리
            m_skill2_list.add(new Skill2_EnemyExplosion(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
            m_enemlist.remove(i);
            killcnt++;
        }
        for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
            m_enemmslist.remove(i);
        }
    }

    /**
     * 스킬3: 5초동안 무적상태
     */
    public void Skill3() {
        //무적상태
        long time = GameTime - Skilltime;
        if (GameTime <= Skilltime + 5000 && GameTime >= Skilltime) {
            //음악추가
        } else {
            startskill3 = 0;
        }
    }

    /**
     * 충돌을 처리하는 메소드
     */
    public void CheckCollision() {
        //필살기1 충돌
        if (playertype == 0) {
            CheckCollisionBoxToBox(m_skill1_list, m_enemlist, true);
            //스킬1 미사일처리
            CheckCollisionBoxToBox(m_skill1_list, m_enemmslist, true);
        }

        //내 미사일과 적기 충돌시
        CheckCollisionBoxToBox(m_pmslist, m_enemlist, false);
        //왼쪽 미사일 충돌 구현
        CheckCollisionBoxToBox(m_lpmslist, m_enemlist, false);
        //오른쪽 미사일 충돌 구현
        CheckCollisionBoxToBox(m_rpmslist, m_enemlist, false);

        //필살기 스킬3은 일단 폭발처리 x
        if (startskill3 == 3) {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(skill3Invincible.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    //이 부분이 파란폭발처리
                    m_explosion_blue.add(new Explosion_Blue(m_enemlist.get(i).getX(), m_enemlist.get(i).getY()));
                    m_enemlist.remove(i);
                    killcnt++;
                }
            }

            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(skill3Invincible.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    //이 부분이 파란폭발처리
                    m_explosion_blue.add(new Explosion_Blue(m_enemmslist.get(i).getX(), m_enemmslist.get(i).getY()));
                    m_enemmslist.remove(i);
                }
            }
        } else //필살기 스킬3이 아닌 충돌
        {
            for (int i = m_enemlist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)) {
                    m_enemlist.remove(i);

                    //이 부분이 플레이어를 폭발처리 (에러발생가능성)
                    m_skill2_list.add(new Skill2_EnemyExplosion(m_player.getX(), m_player.getY()));
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
            for (int i = m_enemmslist.size() - 1; i >= 0; i--) {
                if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)) {
                    m_enemmslist.remove(i);

                    //이 부분이 플레이어를 폭발처리
                    m_skill2_list.add(new Skill2_EnemyExplosion(m_player.getX(), m_player.getY() - 30));
                    m_player.destroyPlayer();
                    if (m_player.getLife() <= 0)
                        AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
                }
            }
        }

        //랜덤박스충돌
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String randName = null;
                for (int i = m_randomboxList.size() - 1; i >= 0; i--) {
                    int random_flag = 0;
                    if (CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_randomboxList.get(i).m_BoundBox)) {
                        if (m_randomboxList.get(i).boxtype == 0) {//필살기 +1
                            skill_cnt++;
                            randName = "Skill +1";
                            random_flag = 1;
                        } else if (m_randomboxList.get(i).boxtype == 1) {//발사속도 +200
                            missileSpeed -= 500;
                            nowMissileSpeed += 0.2f;
                            randName = "Attack Speed +20%";
                            random_flag = 1;
                        } else if (m_randomboxList.get(i).boxtype == 2) {//미사일 업그레이드
                            missileState++;
                            randName = "Missile upgrade!";
                            random_flag = 1;
                        } else if (m_randomboxList.get(i).boxtype == 3) {//생명증가
                            m_player.addLife();
                            randName = "Life +1";
                            random_flag = 1;
                        } else if (m_randomboxList.get(i).boxtype == 4) {//생명감소
                            SoundManager.getInstance().play(9);
                            m_skill2_list.add(new Skill2_EnemyExplosion(m_player.getX(), m_player.getY()));
                            randName = "Life -1";
                        } else if (m_randomboxList.get(i).boxtype == 5) {//발사속도 - 100
                            missileSpeed += 100;
                            nowMissileSpeed -= 0.1f;
                            randName = "Attack Speed -10%";
                        } else if (m_randomboxList.get(i).boxtype == 6) {//필살기 -1
                            skill_cnt--;
                            randName = "Skill -1";
                        } else if (m_randomboxList.get(i).boxtype == 7) {//미사일 다운그레이드
                            missileState--;
                            randName = "Missile Downgrade";
                        }
                        if (random_flag == 1) //좋은 아이템이면 파란폭발
                        {
                            SoundManager.getInstance().play(8);
                            m_explosion_blue.add(new Explosion_Blue(m_player.getX(), m_player.getY()));
                        } else //나쁜 아이템이면
                        {
                            SoundManager.getInstance().play(9);
                            m_skill2_list.add(new Skill2_EnemyExplosion(m_player.getX(), m_player.getY()));
                        }
                        m_randomboxList.remove(i);

                        Toast myToast = Toast.makeText(mcontext, randName, Toast.LENGTH_SHORT);
                        myToast.show();

                        return;
                    }
                }
            }

        }, 1000);
    }

    /**
     * 입력 받은 두 어레이리스트의 충돌을 처리하는 메소드
     * @param box1
     * @param box2
     * @param skill1_flag
     */
    public void CheckCollisionBoxToBox(ArrayList<GraphicObject> box1, ArrayList<GraphicObject> box2, boolean skill1_flag) {
        for (int i = box1.size() - 1; i >= 0; i--) {
            for (int j = box2.size() - 1; j >= 0; j--) {
                if (CollisionManager.CheckBoxToBox(box1.get(i).m_BoundBox, box2.get(j).m_BoundBox)) {
                    //이 부분이 폭발처리
                    m_skill2_list.add(new Skill2_EnemyExplosion(box2.get(j).getX(), box2.get(j).getY()));
                    if (skill1_flag == false) box1.remove(i);
                    box2.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;
                    return;
                }
            }
        }
    }
}

