package org.Game.missile;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Missile_Enemy extends Missile {

    public Missile_Enemy(int x, int y) {
        super(AppManager.getInstance().getBitmap(R.drawable.missile_2));
        this.setPosition(x, y);
    }

    public void Update(){
        m_y+=20;

        int height = AppManager.getInstance().getDeviceSize().y;
        if(m_y > height) state = STATE_OUT;

        m_BoundBox.set(m_x, m_y, m_x + this.m_bitmap.getWidth(),
                m_y + this.m_bitmap.getHeight());
    }
}
