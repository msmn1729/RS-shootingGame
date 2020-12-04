package org.Game.randomBox;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class RandomBox_missileSpeedDown extends RandomBox {
    public RandomBox_missileSpeedDown() {
        super(AppManager.getInstance().getBitmap(R.drawable.randombox));
        this.initSpriteData(this.m_bitmap.getWidth(), this.m_bitmap.getHeight(), 1, 1);
        speed = 10;
        boxtype = 5;
    }
    @Override
    public void Update(long gameTime) {
        super.Update(gameTime);
        m_BoundBox.set(m_x-50,m_y,m_x+this.m_bitmap.getWidth(),m_y+this.m_bitmap.getHeight());
    }
}
