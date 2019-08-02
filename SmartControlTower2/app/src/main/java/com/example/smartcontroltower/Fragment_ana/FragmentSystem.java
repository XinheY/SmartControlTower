package com.example.smartcontroltower.Fragment_ana;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentSystem extends Fragment {

    View view;
    private static ExpandableListView expandableListView;
    private ArrayList<List<Object>> maplistInFragSys = new ArrayList<>();
    private system_ExpandableAdapter adapter = new system_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.system_fragment, container, false);
        Log.e("FragSys", "onCreateView");
        expandableListView = (ExpandableListView) view.findViewById(R.id.system_expand_list);
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

    public void setMaplistInFragSys(ArrayList<List<Object>> m) {
        maplistInFragSys.clear();
        maplistInFragSys = m;
        Log.e("FragSysSETMAP", maplistInFragSys.size() + "");
        adapter.getMaplist(maplistInFragSys);
        Log.e("setMapFragSys", (expandableListView == null) + " " + new system_ExpandableAdapter().getGroupCount());
    }

    public void collapse(Fragment frag) {
        if(expandableListView!=null) {
            for (int i = 0; i < new system_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }


}
