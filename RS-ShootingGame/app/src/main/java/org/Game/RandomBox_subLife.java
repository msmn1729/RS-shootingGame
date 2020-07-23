package org.Game;

import android.graphics.Bitmap;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

import org.Game.RandomBox;

public class RandomBox_subLife extends RandomBox {
    public RandomBox_subLife() {
        super(AppManager.getInstance().getBitmap(R.drawable.randombox));
        this.initSpriteData(this.m_bitmap.getWidth(), this.m_bitmap.getHeight(), 1, 1);
        speed = 10;
        boxtype = 4;
    }

    @Override
    public void Update(long gameTime) {
        super.Update(gameTime);
        m_BoundBox.set(m_x-50,m_y,m_x+this.m_bitmap.getWidth(),m_y+this.m_bitmap.getHeight());
    }
}
