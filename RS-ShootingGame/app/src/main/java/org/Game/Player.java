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
        this.initSpriteData(197, 290, 3, 6);
        this.setPosition(500, 1400);
    }

    public void Update(long gameTime){
        super.Update(gameTime);
        m_BoundBox.set(m_x, m_y, m_x + 197, m_y + 290);
    }

    public int getLife(){
        return m_Life;
    }
    public void addLife(){
        SoundManager.getInstance().play(10);
        m_Life++;
    }
    public void destroyPlayer()
    {
        SoundManager.getInstance().play(7);
        m_Life--;
    }
}
