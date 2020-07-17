package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Enemy_2 extends Enemy{



    public Enemy_2() {
        super(AppManager.getInstance().getBitmap(R.drawable.enemy2));
        this.initSpriteData(162, 270, 3, 6);
        hp = 10;
        speed = 2.5f;
    }

    public void Update(long GameTime){
        super.Update(GameTime);

        m_BoundBox.set(m_x, m_y, m_x + 162, m_y + 270);
    }

}
