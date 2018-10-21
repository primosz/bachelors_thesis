package com.majchrowski.piotr.inz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DailyFragment extends Fragment {
    private String date;
    DatabaseHelper myHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.daily_fragment, container, false);
        myHelper = new DatabaseHelper(getActivity());
        myHelper.open();
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getArguments()!=null){
            date = getArguments().getString("date");
            EntriesAdapter mAdapter = new EntriesAdapter(getActivity(), myHelper.getEntriesFromDay(date));
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));

        }





        return v;

    }
}
