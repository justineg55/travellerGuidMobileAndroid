package com.fbanseptcours.travellerguidmobileandroid.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fbanseptcours.travellerguidmobileandroid.MainActivity;
import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageButton btnHome =  view.findViewById(R.id.btn_home);
        ImageButton btnSearch =  view.findViewById(R.id.btn_search);
        ImageButton btnSettings =  view.findViewById(R.id.btn_settings);
        ImageButton btnLogout =  view.findViewById(R.id.btn_logout);

        btnHome.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        btnSearch.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });

        btnSettings.setOnClickListener((View v) -> {
            startActivity(new Intent(getActivity(), SettingsUserPreferencesActivity.class));
        });

        btnLogout.setOnClickListener((View v) -> {
            UserController.getInstance().deconnexion(
                    this.getContext(),
                    ()->startActivity(new Intent(getActivity(),FirstPageActivity.class)
                    ));
        });

        return view;

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_menu, container, false);
    }
}
