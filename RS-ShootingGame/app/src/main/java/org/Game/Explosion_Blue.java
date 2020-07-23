package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;
import com.example.gameframework.SpriteAnimation;

public class Explosion_Blue extends SpriteAnimation {
    public Explosion_Blue(int x, int y) {
        //폭발 스프라이트 테스트완료
        super(AppManager.getInstance().getBitmap(R.drawable.explosion_blue));
        this.initSpriteData(this.m_bitmap.getWidth()/24, this.m_bitmap.getHeight(), 30, 24);

        SoundManager.getInstance().play(8);
        //this.initSpriteData(418, 390, 3, 6);
        m_x = x;
        m_y = y;
        mbReplay = false; //반복안함
    }
}
