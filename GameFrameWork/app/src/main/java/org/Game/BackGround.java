package org.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class BackGround extends GraphicObject {

    static final float SCROLL_SPEED = 0.2f;
    private float m_scroll = -2000 + 480;

    Bitmap m_layer2;
    static final float SCROLL_SPEED_2 = 0.2f;
    private float m_scroll_2 = -2000 + 480;

    public BackGround(int backtype) {
        super(null);
        if(backtype == 0){//ready
            m_bitmap = AppManager.getInstance().getBitmap(R.drawable.back1);
        }
        else if(backtype == 1)//select
            m_bitmap = AppManager.getInstance().getBitmap(R.drawable.back2);
        else if(backtype == 2){//game
            m_bitmap = AppManager.getInstance().getBitmap(R.drawable.back3);
        }

//        m_layer2 = AppManager.getInstance().getBitmap(R.drawable.background_2);
        setPosition(0, (int) m_scroll);
    }

    void Update(long GameTime){
        m_scroll = m_scroll + SCROLL_SPEED;
        if(m_scroll >= 0)m_scroll = 0;
        setPosition(0, (int)m_scroll);
        m_scroll_2 = m_scroll_2 + SCROLL_SPEED_2;
        if(m_scroll_2 >= 0)m_scroll_2 = 0;
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, getX(), getY(), null);
//        canvas.drawBitmap(m_layer2, getX(), getY(), null);
    }
}
