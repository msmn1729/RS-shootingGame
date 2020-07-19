package org.Game;

import android.graphics.Canvas;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.R;

public class R_Next extends GraphicObject {

    public R_Next() {
        super(null);
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.r_next);
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, 140, 1550, null);
    }
}
