package com.example.triviaquiz.data;

import com.example.triviaquiz.model.Question;

import java.util.ArrayList;

public interface AnswerListAsynResponse {
    public void processFinished(ArrayList<Question> questionArrayList);
}
