package com.example.smartcontroltower.Fragment_ana;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentClient extends Fragment {

    View view;
    private static ExpandableListView expandableListView;
    private ArrayList<List<Object>> maplistInFragClient = new ArrayList<>();
    private client_ExpandableAdapter adapter = new client_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_fragment, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.client_expand_list);
        expandableListView.setAdapter(adapter);

        //控制他只能打开一个组
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = new system_ExpandableAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });


        return view;
    }


    public void setMaplistInFragClient(ArrayList<List<Object>> m){
        maplistInFragClient.clear();
        maplistInFragClient=m;
        Log.e("FragClient",maplistInFragClient.size()+"");
        adapter.inputdata(maplistInFragClient);
        if(expandableListView!=null){
            for(int i=0;i<new system_ExpandableAdapter().getGroupCount();i++){
                expandableListView.collapseGroup(i);}
        }
    }
}
