package org.Game.skill;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.gameframework.SpriteAnimation;

public class Skill3_Invincible extends SpriteAnimation {

    public Skill3_Invincible(Bitmap bitmap) {
        super(bitmap);
        this.initSpriteData(this.m_bitmap.getWidth()/16, this.m_bitmap.getHeight(), 20, 9);
    }

    public void Update(long gameTime) {
        super.Update(gameTime);
        m_BoundBox.set(m_x, m_y, m_x + this.m_bitmap.getWidth()/16, m_y + this.m_bitmap.getWidth());
    }
}
