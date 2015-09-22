package com.matthewpatience.sentences.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.matthewpatience.sentences.R;
import com.matthewpatience.sentences.model.Word;

/**
 * Created by mpatience on 15-09-22.
 */
public class WordView {

    private View view;
    private Button button;

    public WordView(LayoutInflater inflater, Word word) {

        view = inflater.inflate(R.layout.layout_word, null);
        button = (Button) view.findViewById(R.id.button);
        button.setText(word.getWord());

    }

    public View getView() {

        return view;
    }

}
