package org.Game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class M_Circle extends SpriteAnimation {
    Rect m_BoundBox = new Rect();

    public M_Circle(Bitmap bitmap) {
        super(bitmap);
        this.initSpriteData(555, 555, 4, 9);
        // this.setPosition(500, 1400);
    }

    public void Update(long gameTime) {
        super.Update(gameTime);
        m_BoundBox.set(m_x, m_y, m_x + 555, m_y + 555);
    }
}
