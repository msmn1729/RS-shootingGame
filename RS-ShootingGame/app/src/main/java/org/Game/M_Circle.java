package org.Game;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class M_Circle extends SpriteAnimation {
    Rect m_BoundBox = new Rect();

    public M_Circle(Bitmap bitmap) {
        super(bitmap);
        //this.initSpriteData(555, 555, 20, 9);
        this.initSpriteData(this.m_bitmap.getWidth()/16, this.m_bitmap.getWidth(), 20, 9);
        // this.setPosition(500, 1400);
    }

    public void Update(long gameTime) {
        super.Update(gameTime);
        m_BoundBox.set(m_x, m_y, m_x + this.m_bitmap.getWidth()/16, m_y + this.m_bitmap.getWidth());
    }
}
