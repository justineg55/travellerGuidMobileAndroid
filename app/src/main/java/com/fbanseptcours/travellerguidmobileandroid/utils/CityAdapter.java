package com.fbanseptcours.travellerguidmobileandroid.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fbanseptcours.travellerguidmobileandroid.model.City;

public class CityAdapter extends ArrayAdapter<City> {

    private Context context;
    private City[] citiestab;


    public CityAdapter(@NonNull Context context, int resource, @NonNull City[] citiestab) {
        super(context, resource, citiestab);
        this.context=context;
        this.citiestab=citiestab;

    }

    @Override
    public int getCount() {
        return citiestab.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //vue pour ce qui est affiché à l'intérieur quand selectionné
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView textView=(TextView) super.getView(position,convertView,parent);
        textView.setText(citiestab[position].getCityName());
        return textView;
    }

    //vues qui vont etre affichées dans la liste déroulante
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView=(TextView) super.getView(position,convertView,parent);
        textView.setText(citiestab[position].getCityName());
        return textView;
    }
}
