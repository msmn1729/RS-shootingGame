package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Missile_Enemy extends Missile {

    Missile_Enemy(int x, int y) {
        super(AppManager.getInstance().getBitmap(R.drawable.missile_2));
        this.setPosition(x, y);
    }

    public void Update(){
        m_y+=20;
        if(m_y > 1400)state = STATE_OUT;

        m_BoundBox.set(m_x, m_y, m_x + 143, m_y + 143);
    }
}
