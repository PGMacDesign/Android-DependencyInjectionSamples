package com.pgmacdesign.dependencyinjectionsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pmacdowell on 2017-01-03.
 */

public class ButterKnifeFragment extends Fragment{

    //UI
    @BindView(R.id.fragment_butter_knife_tv1) TextView fragment_butter_knife_tv1;

    //Unbinder
    private Unbinder unbinder;


    public ButterKnifeFragment(){}

    public ButterKnifeFragment newInstance(){
        ButterKnifeFragment frag = new ButterKnifeFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_butter_knife, container, false);

        //ButterKnife.bind(this, view); //Not needed if using unbinder below
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
