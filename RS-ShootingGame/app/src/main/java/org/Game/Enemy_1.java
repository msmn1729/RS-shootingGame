package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Enemy_1 extends Enemy {

    public Enemy_1() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy1));
        this.initSpriteData(185, 270, 3, 6);
        hp = 10;
        speed = 2.5f;

    }

    public void Update(long GameTime){
        super.Update(GameTime);
        m_BoundBox.set(m_x, m_y, m_x + 185, m_y + 270);
    }
}
