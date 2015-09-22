package com.matthewpatience.sentences.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.matthewpatience.sentences.model.Word;
import com.matthewpatience.sentences.view.WordView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpatience on 15-09-22.
 */
public class WordAdapter {

    private LayoutInflater inflater;
    private List<WordView> views;
    private List<Word> words;
    private ViewGroup viewGroup;

    public WordAdapter(Context context) {

        inflater = LayoutInflater.from(context);

    }

    public void setItems(List<Word> items) {

        this.words = items;
        populateWordViews();
        populateLayout();

    }

    public void setViewGroup(ViewGroup viewGroup) {

        this.viewGroup = viewGroup;
        populateLayout();

    }

    private void populateWordViews() {

        views = new ArrayList<>(this.words.size());
        for (int i = 0; i < this.words.size(); i++) {
            WordView view = new WordView(inflater, this.words.get(i));
            views.add(view);
        }

    }

    private void populateLayout() {

        if (views != null && viewGroup != null) {
            for (WordView view : views) {
                viewGroup.addView(view.getView());
            }
        }

    }

}
