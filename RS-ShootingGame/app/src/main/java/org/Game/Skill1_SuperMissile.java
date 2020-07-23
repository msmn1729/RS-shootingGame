package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Skill1_SuperMissile extends Missile {
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;
    public Skill1_SuperMissile(int x, int y, int speed) {
        super(AppManager.getInstance().getBitmap(R.drawable.super_missile));
//        this.setPosition(-(width*0.095),height + m_bitmap.getHeight());
        this.setPosition(width*0.55 - m_bitmap.getWidth()/2, height*0.1+ m_bitmap.getHeight());
        this.speed = speed;
    }

    public void Update(){
        m_y -= speed;
        if(m_y < -(m_bitmap.getHeight()))state = STATE_OUT; //미사일 끝까지

        //판정
        m_BoundBox.left = (int) (m_x + width*0.266);
        m_BoundBox.top = m_y;
        m_BoundBox.right = (int) (m_x + m_bitmap.getWidth() - width*0.361);
        m_BoundBox.bottom = m_y + m_bitmap.getHeight() ;
//        m_BoundBox.left = 0;
//        m_BoundBox.top = 0;
//        m_BoundBox.right = 2000;
//        m_BoundBox.bottom = 2000;
    }
}
