package org.Game.player;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;

public class Player_2 extends Player{
    public Player_2() {
        super(AppManager.getInstance().getBitmap(R.drawable.player2));

        this.initSpriteData(this.m_bitmap.getWidth()/6, this.m_bitmap.getHeight(), 5, 6);
        this.setPosition(width*0.47, height*0.8);
    }

    public void Update(long GameTime){
        super.Update(GameTime);
        m_BoundBox.set(m_x, m_y, m_x + this.m_bitmap.getWidth()/6, m_y + this.m_bitmap.getHeight());
    }
}