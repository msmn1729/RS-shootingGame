package org.Game;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RankRequest extends StringRequest {
    final static private String URL = "http://msmn.dothome.co.kr/RankingRecord.php";
    private Map<String,String> Parameters;

    public RankRequest(String playerID, String playerAirplaneType, String playerScore,
                       String recordDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Parameters = new HashMap<>();
        Parameters.put("playerID", playerID);
        Parameters.put("playerAirplaneType", playerAirplaneType);
        Parameters.put("playerScore", playerScore);
        Parameters.put("recordDate", recordDate);
    }

    @Override
    public Map<String,String> getParams()
    {
        return Parameters;
    }

}