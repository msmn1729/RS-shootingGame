package org.Game.player;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.gameframework.AppManager;
import com.example.gameframework.SoundManager;
import com.example.gameframework.SpriteAnimation;

public class Player extends SpriteAnimation {
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;
    public Rect m_BoundBox = new Rect();

    public int m_Life = 3;

    public Player(Bitmap bitmap) {
        super(bitmap);
        //this.initSpriteData(197, 290, 5, 6);
        this.initSpriteData(this.m_bitmap.getWidth()/6, this.m_bitmap.getHeight(), 5, 6);
        this.setPosition(width*0.47, height*0.8);
    }

    public void Update(long gameTime){
        super.Update(gameTime);
//        m_BoundBox.set(m_x, m_y, m_x + 197, m_y + 290);
        m_BoundBox.set((int) (m_x+width*0.11), (int) (m_y+width*0.01),
                (int) (m_x + width*0.14), (int) (m_y + width*0.25)); //플레이어 판정
//        m_BoundBox.set(m_x, m_y,
//                m_x +m_bitmap.getWidth(), m_y +m_bitmap.getHeight()); //플레이어 판정
        //System.out.println("x : " + m_x + "y : " + m_y);
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
