package org.Game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class Enemy extends SpriteAnimation {
    protected int hp;
    protected float speed;

    public static final int MOVE_PATTERN_1 = 0;
    public static final int MOVE_PATTERN_2 = 1;
    public static final int MOVE_PATTERN_3 = 2;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_OUT = 1;
    public int state = STATE_NORMAL;

    Rect m_BoundBox = new Rect();

    long LastShoot = System.currentTimeMillis();

    protected int movetype;

    public Enemy(Bitmap bitmap) {
        super(bitmap);
    }

    void move(){
        if(movetype == MOVE_PATTERN_1){
            if(m_y <= 500){
                m_y += speed;
            }
            else
                m_y += speed * 2;
        }
        else if(movetype == MOVE_PATTERN_2){
            if(m_y <= 500)
                m_y += speed;
            else{
                m_x += speed;
                m_y += speed;
            }
        }
        else if(movetype == MOVE_PATTERN_3){
            if(m_y <= 500)
                m_y += speed;
            else{
                m_x -= speed;
                m_y += speed;
            }
        }
        if(m_y > 1800) state = STATE_OUT;
    }

    void attack(){
        if(System.currentTimeMillis() - LastShoot >= 1000){
            LastShoot = System.currentTimeMillis();

            GameState.getInstance().m_enemmslist.add(new Missile_Enemy(m_x + 10, m_y + 230));
        }
    }

    public void Update(long GameTime){
        super.Update(GameTime);
        attack();
        move();
    }
}
