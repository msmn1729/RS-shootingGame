package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Skill1_SuperMissile extends Missile {

    public Skill1_SuperMissile(int x, int y, int speed) {
        super(AppManager.getInstance().getBitmap(R.drawable.super_missile));
        this.setPosition(-100,y);
        this.speed = speed;
    }

    public void Update(){
        m_y -= 10;
        if(m_y < -1800)state = STATE_OUT; //미사일 끝까지

        m_BoundBox.left = m_x;
        m_BoundBox.top = m_y;
        m_BoundBox.right = m_x + 1000;
        m_BoundBox.bottom = m_y + 1000;
//        m_BoundBox.left = 0;
//        m_BoundBox.top = 0;
//        m_BoundBox.right = 2000;
//        m_BoundBox.bottom = 2000;
    }
}
