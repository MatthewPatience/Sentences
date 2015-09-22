package com.matthewpatience.sentences.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.matthewpatience.sentences.R;
import com.matthewpatience.sentences.adapter.WordAdapter;
import com.matthewpatience.sentences.api.ApiManager;
import com.matthewpatience.sentences.model.Word;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by mpatience on 15-09-20.
 */
public class GameFragment extends Fragment implements View.OnClickListener, Callback<List<Word>> {

    private Button btnMenu;
    private FlowLayout wordBankLayout;

    private WordAdapter wordAdapter;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);

        btnMenu = (Button) rootView.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);
        wordBankLayout = (FlowLayout) rootView.findViewById(R.id.wordBankLayout);

        wordAdapter = new WordAdapter(getActivity());
        wordAdapter.setViewGroup(wordBankLayout);

        ApiManager.getInstance().getRandomWords(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMenu:

                break;
        }

    }

    @Override
    public void onResponse(Response<List<Word>> response) {

        wordAdapter.setItems(response.body());

    }

    @Override
    public void onFailure(Throwable t) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Sorry")
                .setMessage("Unable to download word bank, please restart game.\n\nError: " + t.getMessage())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                })
                .create().show();

    }
}
