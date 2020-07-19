package org.Game;

import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class R_Main extends GraphicObject {

    public R_Main() {
        super(null);
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.r_main);
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, 535, 1550, null);
    }
}
