package com.dinomobile.olxapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dinomobile.olxapp.R;

public class RecoverPasswordFragment extends Fragment {

    public RecoverPasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recover_password, container, false);

        return v;
    }
}