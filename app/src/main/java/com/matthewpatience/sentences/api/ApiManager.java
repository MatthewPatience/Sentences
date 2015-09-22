package com.matthewpatience.sentences.api;

import com.matthewpatience.sentences.model.Word;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by mpatience on 15-09-20.
 */
public class ApiManager {

    private WordnikService wordnikService;

    private static ApiManager instance;

    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .build();

        wordnikService = retrofit.create(WordnikService.class);

    }

    public static ApiManager getInstance() {

        if (instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    public void getRandomWords(Callback<List<Word>> callback) {

        Call<List<Word>> request = wordnikService.getRandomWords();
        request.enqueue(callback);

    }

}
