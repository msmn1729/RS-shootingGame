package org.Game.missile;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.GraphicObject;
import com.example.gameframework.SpriteAnimation;

public class Missile extends GraphicObject {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_OUT = 1;
    public int state = STATE_NORMAL;
    public int speed;

    public Missile(Bitmap bitmap) {
        super(bitmap);
    }

    public void Update(){
        if(m_y < 50)state = STATE_OUT;
    }
}
