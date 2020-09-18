package com.example.triviaquiz.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaquiz.controller.AppController;
import com.example.triviaquiz.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.triviaquiz.controller.AppController.TAG;

public class QuestionBank {

    private String url =  "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionList = new ArrayList<>();
    public List<Question> getQuestions(final AnswerListAsynResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("JSON: ", "onResponse: " + response);

                        for (int i=0; i<response.length() ;i++){
                            try {
                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                //ArrayList
                                questionList.add(question);
//                                Log.d("Hello", "onResponse: " + question );

//                                Log.d("JSON", "onResponse: " + response.getJSONArray(i).get(0).toString());
//                                Log.d("Json1","onResponse: " + response.getJSONArray(i).getBoolean(1));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack) callBack.processFinished(questionList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionList;

    }


}
