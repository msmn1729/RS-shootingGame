package org.Game.missile;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

import org.Game.missile.Missile;

public class Missile_Player extends Missile {
    public Missile_Player(int x, int y, int speed) {
        super(AppManager.getInstance().getBitmap(R.drawable.missile_1));
        this.setPosition(x, y);
        this.speed = speed;
    }

    public void Update(){
        m_y -= 30;
        if(m_y < 50)state = STATE_OUT;
        m_BoundBox.set(m_x, m_y, m_x + this.m_bitmap.getWidth(),
                m_y + this.m_bitmap.getHeight());
    }
}
