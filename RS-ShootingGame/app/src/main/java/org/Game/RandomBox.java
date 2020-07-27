package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.gameframework.AppManager;
import com.example.gameframework.SpriteAnimation;

import java.util.Random;

public class RandomBox extends SpriteAnimation {
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;
    public static final int STATE_NORMAL = 0;// 기본 상태
    public static final int STATE_OUT = 1;
    public int state = STATE_NORMAL;

    protected int boxtype;

    protected Rect m_BoundBox = new Rect();
    long LastShoot = System.currentTimeMillis();

    int speed;

    public RandomBox(Bitmap bitmap) {
        super(bitmap);
    }

    int r = new Random().nextInt(10) + 5;

    void move() {
        m_y += r;
//        m_y += speed;
        if (m_y > height * 1.03)
            state = STATE_OUT;
    }


    public void Update() {
//        if (m_y > height * 0.5)
//            state = STATE_OUT;
    }

    @Override
    public void Draw(Canvas canvas) {
        super.Draw(canvas);
    }

    @Override
    public void Update(long gameTIme) {
        super.Update(gameTIme);
        move();
    }
}
