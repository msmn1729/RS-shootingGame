package com.example.gameframework;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.Game.RankRequest;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.gameframework.MainActivity.mcontext;
import static org.Game.GameState.killcnt;
import static org.Game.GameState.playertype;

public class InsertView extends AppCompatActivity {
    private EditText et_playerID;
    private Button btn_record;
    private String playerID;
    private int p_score=killcnt * 50;
    public InsertView(){
    }

    private int plainType = playertype;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.insertview);
        SoundManager.getInstance().play(2);
        //아이디 값
        et_playerID = findViewById(R.id.et_playerID);

        //버튼
        btn_record = findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                playerID = et_playerID.getText().toString();
                System.out.println("이름 : " + playerID);
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = new Date();

                String recordDate = timeFormat.format(time);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                System.out.println("랭킹 등록 성공!");
                                Toast.makeText(getApplicationContext(), "랭킹 등록 성공!", Toast.LENGTH_SHORT).show();
                                setContentView(new GameView(mcontext,2));
                            } else {
                                System.out.println("랭킹 등록에 실패했습니다.");
                                Toast.makeText(getApplicationContext(), "랭킹 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            System.out.println("오류");
                            e.printStackTrace();
                        }
                    }
                };
                String plainName = null;
                if(plainType == 0){
                    plainName = "TYPE1";
                }else if(plainType == 1){
                    plainName = "TYPE2";
                }else if(plainType == 2){
                    plainName = "TYPE3";
                }
                //Random random = new Random(); //임시 score 0~9999 랜덤값
                RankRequest rankRequest = new RankRequest(playerID, plainName,
                        p_score + "", recordDate,
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(InsertView.this); //실제 서버에 저장하는 부분(게임뷰 실행과 동시에하면 저장은 되지만 게임뷰는 실행안됨)
                queue.add(rankRequest);
                return;
            }
        });


    }

}
