package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class RandomBox extends SpriteAnimation {

    public static final int STATE_NORMAL = 0;// 기본 상태
    public static final int STATE_OUT = 1;
    public int state = STATE_NORMAL;

    protected int boxtype;

    protected Rect m_BoundBox = new Rect();
    long LastShoot = System.currentTimeMillis();

    int speed;

    public RandomBox(Bitmap bitmap){
        super(bitmap);
    }


<<<<<<< HEAD
=======
    @Override
    public void Draw(Canvas canvas) {
        super.Draw(canvas);
    }

    @Override
    public void Update(long gameTIme) {
        super.Update(gameTIme);
        move();
    }
>>>>>>> 9dbb9974f1ae2eecd09d2a67ff6396058b743f64

    void move(){
        m_y += speed;
    }

<<<<<<< HEAD
    public void Update(){
        if(m_y > 1800)
            state = STATE_OUT;
=======
    public void Update()
    {
        if(m_y>1800)
            state=STATE_OUT;
    }

    @Override
    public void Draw(Canvas canvas) {
        super.Draw(canvas);
    }

    @Override
    public void Update(long gameTIme) {
        super.Update(gameTIme);
        move();
>>>>>>> 8d8ef0de249013aa8e97ef05cdc0228d85744ef5
    }
}
