package org.Game.randomBox;

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

    public int boxtype;

    public Rect m_BoundBox = new Rect();

    int speed;

    public RandomBox(Bitmap bitmap) {
        super(bitmap);
    }

    int r = new Random().nextInt(10) + 5;

    void move() {
        m_y += r;
        if (m_y > height * 1.03)
            state = STATE_OUT;
    }


    public void Update() {
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
