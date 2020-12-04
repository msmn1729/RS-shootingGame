package org.Game.render;

import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class R_Main extends GraphicObject {

    public R_Main() {
        super(null);
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.r_main);
    }

    public void Draw(Canvas canvas) {
        int width = AppManager.getInstance().getDeviceSize().x;
        int height = AppManager.getInstance().getDeviceSize().y;
        canvas.drawBitmap(m_bitmap, (int) (width * 0.51), (int) (height * 0.89), null);
    }
}
