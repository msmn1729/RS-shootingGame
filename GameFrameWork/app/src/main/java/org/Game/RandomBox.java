package org.Game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class RandomBox extends SpriteAnimation {

    public static final int STATE_NORMAL = 0;// 기본 상태
    public static final int STATE_OUT = 1;
    public int state = STATE_NORMAL;

    protected int boxtype;

    Rect m_BoundBox = new Rect();
    long LastShoot = System.currentTimeMillis();

    protected float speed;

    public RandomBox(Bitmap bitmap){
        super(bitmap);
        this.initSpriteData(100,100,1,1);
        speed = 3.0f;
    }

    void move(){
        m_y += speed;
    }

    public void Update(long gameTime){
        super.Update(gameTime);
        m_BoundBox.set(m_x, m_y, m_x + 100, m_y + 100);
        if(m_y > 1800)
            state = STATE_OUT;
    }
}
