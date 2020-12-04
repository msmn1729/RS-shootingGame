package org.Game.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class BackGround extends GraphicObject {
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;
    static final float SCROLL_SPEED = 2.0f;
    private float m_scroll = -2000 + 480;

    static final float SCROLL_SPEED_2 = 2.0f;
    private float m_scroll_2 = -2000 + 480;

    public BackGround(int backtype) {
        super(null);
        if(backtype == 0){//ready
            m_bitmap = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.back1),width, height*2, true);
        }
        else if(backtype == 1)//select
            m_bitmap = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.back2),width, height*2, true);
        else if(backtype == 2){//game
            m_bitmap = Bitmap.createScaledBitmap(AppManager.getInstance().getBitmap(R.drawable.back3), width, height*2, true);
        }
        setPosition(0, (int) m_scroll);
    }

    public void Update(long GameTime){
        m_scroll = m_scroll + SCROLL_SPEED;
        if(m_scroll >= 0)m_scroll = -2000 + 480;
        setPosition(0, (int)m_scroll);
        m_scroll_2 = m_scroll_2 + SCROLL_SPEED_2;
        if(m_scroll_2 >= 0)m_scroll_2 = -2000 + 480;
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, getX(), getY(), null);
    }
}
