package com.matthewpatience.sentences.api;

import com.matthewpatience.sentences.model.Word;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by mpatience on 15-09-20.
 */
public interface WordnikService {
    @GET("/words.json/randomWords?hasDictionaryDef=true&minCorpusCount=200000&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=5&maxLength=-1&limit=50&api_key=4abdc6a76168021fcb00a05b385044306b3a58a90da77bb00")
    Call<List<Word>> getRandomWords();
}
