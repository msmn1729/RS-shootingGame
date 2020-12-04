package org.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gameframework.AppManager;
import com.example.gameframework.IState;
import com.example.gameframework.SoundManager;

import org.Game.render.R_Back;
import org.Game.render.R_BackGround;
import org.Game.render.R_Main;
import org.Game.render.R_Next;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.gameframework.MainActivity.mcontext;

public class RankState implements IState {
    int width = AppManager.getInstance().getDeviceSize().x;
    int height = AppManager.getInstance().getDeviceSize().y;
    private R_BackGround r_background;
    private R_Main r_main;
    private R_Next r_next;
    private R_Back r_back;
    public static int realflag = 1;
    int count = 0;
    String[] p_ID = new String[1000];
    String[] p_AirplaneType = new String[1000];
    String[] p_Score = new String[1000];
    String[] p_recordDate = new String[1000];
    public RequestQueue mQueue;

    public RankState()
    {

    }

    private static RankState rank = new RankState();

    public static RankState getInstance(int flag) {
        realflag = flag;
        return rank;
    }

    @Override
    public void Init() {
        jsonParse();
        r_background = new R_BackGround(0);
        r_main = new R_Main();
        r_next = new R_Next();
        r_back = new R_Back();
    }

    @Override
    public void Render(Canvas canvas) {

        //canvas.drawColor(Color.BLACK);
        r_background.Draw(canvas);
        r_main.Draw(canvas);
        if (realflag == 1) {
            r_next.Draw(canvas);
        } else if (realflag == 2) {
            r_back.Draw(canvas);
        }
        Paint p = new Paint();
        Paint p2 = new Paint();
        Paint p3 = new Paint();

        p.setTextSize((float) (width * 0.038));
        p2.setTextSize((float) (width * 0.043));
        p3.setTextSize((float) (width * 0.076));
        p.setColor(Color.WHITE);
        p2.setColor(Color.YELLOW);
        p3.setColor(Color.RED);

        int y = 30;
        canvas.drawText("! SHOW ME THE RANK !", (float) (width*0.11),
                (float) (height*0.057), p3);

        //count = 20; //숫자 크면 화면 넘어감(스크롤 기능 추가예정)
        canvas.drawText("NAME", (float) (width*0.1), (float) (height*0.1257), p2);
        canvas.drawText("TYPE", (float) (width*0.34), (float) (height*0.1257), p2);
        canvas.drawText("SCORE", (float) (width*0.495), (float) (height*0.1257), p2);
        canvas.drawText("DATE", (float) (width*0.647), (float) (height*0.1257), p2);
        int i = 0;
        if (realflag == 1) {

            for (i = 0; i < count; i++) {
                canvas.drawText(String.valueOf(i + 1), (float) (width*0.0095), (float) (height*0.1542 + y), p2);
                canvas.drawText(String.valueOf(p_ID[i]), (float) (width*0.1), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_AirplaneType[i]), (float) (width*0.34), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_Score[i]), (float) (width*0.495), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_recordDate[i]), (float) (width*0.647), (float) (height*0.1542 + y), p);
                y += height*0.0365;
                if (i >= 19) {
                    break;
                }
            }
        } else if (realflag == 2) {
            for (i = 20; i < count; i++) {
                canvas.drawText(String.valueOf(i + 1), (float) (width*0.0095), (float) (height*0.1542 + y), p2);
                canvas.drawText(String.valueOf(p_ID[i]), (float) (width*0.1), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_AirplaneType[i]), (float) (width*0.34), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_Score[i]), (float) (width*0.495), (float) (height*0.1542 + y), p);
                canvas.drawText(String.valueOf(p_recordDate[i]), (float) (width*0.647), (float) (height*0.1542 + y), p);
                y += height*0.0365;
                if (i >= 39) {
                    break;
                }
            }
        }
    }

    /**
     * 랭킹 읽어오는 메소드
     */
    public void jsonParse() {
        mQueue = Volley.newRequestQueue(mcontext);

//        String url = "https://whdgurtpqmssju.cafe24.com/post/ShootingRank.php";
        String url = "http://msmn.dothome.co.kr/GetRanking.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //"playerID""playerAirplaneType""playerScore""recordDate"
                            JSONArray jsonArray = response.getJSONArray("response");
                            String playerID, playerAirplaneType, playerScore, recordDate;
                            for (int i = 0; i < jsonArray.length(); i++) { //수정
                                JSONObject ranking = jsonArray.getJSONObject(i);

                                playerID = ranking.getString("playerID");
                                playerAirplaneType = ranking.getString("playerAirplaneType");
                                playerScore = ranking.getString("playerScore");
                                recordDate = ranking.getString("recordDate");

                                p_ID[i] = playerID;
                                p_AirplaneType[i] = playerAirplaneType;
                                p_Score[i] = playerScore;
                                p_recordDate[i] = recordDate;
                                count++;
                                if (playerScore == null) {
                                    break;
                                }
                                //  Log.d(TAG, "onResponse: 성공, rank 결과 : \n" + rank);
                                //Log.d(TAG, "onResponse: 성공, score결과\n" + score);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long GameTime = System.currentTimeMillis();
        r_background.Update(GameTime);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int _x, _y;
        _x = (int) event.getX();
        _y = (int) event.getY();
        Rect rt = new Rect();
        Rect rt2 = new Rect();
        int scoredata = count;

        //가로 300 세로 100
        rt2.set((int) (width * 0.13), (int) (height * 0.89),
                (int) (width * 0.13) + r_main.m_bitmap.getWidth(), (int) (height * 0.89) + r_main.m_bitmap.getHeight());
        if (realflag == 1) {
            if (rt2.contains(_x, _y) && event.getAction() == MotionEvent.ACTION_DOWN && scoredata > 20) {
                SoundManager.getInstance().play(4);
                AppManager.getInstance().getGameView().changeGameState
                        (RankState.getInstance(2));
                count = 0;
            } else if (rt2.contains(_x, _y) && event.getAction() == MotionEvent.ACTION_DOWN && scoredata <= 20) {
                SoundManager.getInstance().play(4);
            }
        } else if (realflag == 2) {
            if (rt2.contains(_x, _y) && event.getAction() == MotionEvent.ACTION_DOWN) {
                SoundManager.getInstance().play(4);
                AppManager.getInstance().getGameView().changeGameState
                        (RankState.getInstance(1));
                count = 0;
            }
        }
        rt.set((int) (width * 0.51), (int) (height * 0.89),
                (int) (width * 0.51) + r_main.m_bitmap.getWidth(), (int) (height * 0.89) + r_main.m_bitmap.getHeight());
        if (rt.contains(_x, _y)) {
            SoundManager.getInstance().play(4);
            AppManager.getInstance().getGameView().changeGameState
                    (new ReadyState());
        }

        return true;
    }
}







