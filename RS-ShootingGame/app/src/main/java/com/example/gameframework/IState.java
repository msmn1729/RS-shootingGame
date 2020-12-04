package com.example.gameframework;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface IState {
    void Init( );// 상태가 생성되었을 때

    void Destroy( );// 상태가 소멸될 때

    void Update( );// 지속적으로 수행할 것들

    void Render(Canvas canvas); // 그려야 할 것들

    boolean onKeyDown(int keyCode, KeyEvent event);//키 입력 처리

    boolean onTouchEvent(MotionEvent event);//터치 입력 처리
}
