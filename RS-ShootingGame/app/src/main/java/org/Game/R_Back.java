package org.Game;

import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class R_Back extends GraphicObject {

    public R_Back() {
        super(null);
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.r_back);
    }

    public void Draw(Canvas canvas){
        int width = AppManager.getInstance().getDeviceSize().x;
        int height = AppManager.getInstance().getDeviceSize().y;
        canvas.drawBitmap(m_bitmap, (int)(width*0.13), (int)(height*0.89), null);
        //canvas.drawBitmap(m_bitmap, 140, 1550, null);
    }
}
