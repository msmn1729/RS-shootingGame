package com.example.gameframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GraphicObject {
    public Bitmap m_bitmap;
    protected int m_x;
    protected int m_y;
    public Rect m_BoundBox = new Rect(); //추가한 것

    public GraphicObject(Bitmap bitmap){
        m_bitmap = bitmap;
        m_x = 0;
        m_y = 0;
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }

    public void setPosition(double x, double y){
        m_x = (int) x;
        m_y = (int) y;
    }

    public int getX(){
        return m_x;
    }

    public int getY(){
        return m_y;
    }
}
