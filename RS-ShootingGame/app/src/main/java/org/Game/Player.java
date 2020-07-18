package org.Game;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.SoundManager;
import com.example.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {
    Rect m_BoundBox = new Rect();

    int m_Life = 3;

    public Player(Bitmap bitmap) {
        super(bitmap);
        this.initSpriteData(162, 270, 3, 1);
        this.setPosition(500, 1400);
    }

    public void Update(long gameTime){
        super.Update(gameTime);

//        if(Move){
//            this.m_x += _dirX;
//            this.m_y += _dirY;
//        }
        m_BoundBox.set(m_x, m_y, m_x + 162, m_y + 160);
    }

    public int getLife(){
        return m_Life;
    }
    public void addLife(){
        m_Life++;
    }
    public void destroyPlayer()
    {
        SoundManager.getInstance().play(7);
        m_Life--;
    }
}
