package org.Game.skill;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;
import com.example.gameframework.SpriteAnimation;

public class Skill2_EnemyExplosion extends SpriteAnimation {
    public Skill2_EnemyExplosion(int x, int y) {
        super(AppManager.getInstance().getBitmap(R.drawable.sprite_explosion));
        this.initSpriteData(this.m_bitmap.getWidth()/6, this.m_bitmap.getHeight(), 10, 6);
        SoundManager.getInstance().play(5);
        m_x = x;
        m_y = y;
        mbReplay = false; //반복안함
    }
}
