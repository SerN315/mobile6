package com.my.samplemusicplayertest.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.my.samplemusicplayertest.Login;
import com.my.samplemusicplayertest.R;
import com.my.samplemusicplayertest.SearchActivity;


public class FragmentHome extends Fragment implements Login.LoginCallback {
    private ImageView loginButton;
    private TextView usernameTextView;
    private Button registerButton;

    private boolean isLoggedIn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        usernameTextView = view.findViewById(R.id.username);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView searchImageView = view.findViewById(R.id.search);
        loginButton = view.findViewById(R.id.loginb);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedIn) {
                    // Navigate to the login layout
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoginSuccess(String email) {
        isLoggedIn = true;
        usernameTextView.setText(email);
    }
}