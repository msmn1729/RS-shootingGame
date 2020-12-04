package org.Game.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class R_BackGround extends GraphicObject {

    static final float SCROLL_SPEED = 0.2f;
    private float m_scroll = -2000 + 480;

    Bitmap m_layer;

    public R_BackGround() {
        super(null);
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.r_background);
        m_layer = AppManager.getInstance().getBitmap(R.drawable.r_background);
        setPosition(0, (int) m_scroll);
    }

    public void Update(){
        m_scroll = m_scroll + SCROLL_SPEED;
        if(m_scroll >= 0)m_scroll = 0;
        setPosition(0, (int)m_scroll);
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, getX(), getY(), null);
    }
}
