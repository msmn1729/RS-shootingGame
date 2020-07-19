package org.Game;

import com.example.gameframework.AppManager;
import com.example.gameframework.R;
import com.example.gameframework.SpriteAnimation;

public class Skill2_Enemy_Explosion extends SpriteAnimation {
    public Skill2_Enemy_Explosion(int x, int y) {
        //폭발 스프라이트 테스트완료
        super(AppManager.getInstance().getBitmap(R.drawable.sprite_explosion));
        this.initSpriteData(209, 195, 10, 6);
        m_x = x;
        m_y = y;

        mbReplay = false; //반복안함
    }
}
