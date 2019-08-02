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


public class FragmentClientLob extends Fragment {

    View view;
    private ExpandableListView expandableListView;
    private ArrayList<List<Object>> maplistInFragcl = new ArrayList<>();
    private clientlob_ExpandableAdapter adapter = new clientlob_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.clientlob_fragment, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.clientlob_expand_list);
        expandableListView.setAdapter(new clientlob_ExpandableAdapter());
//        //设置分组的监听
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(view.getContext(), groupString[groupPosition], Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//        //设置子项布局监听
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(view.getContext(), childString[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
//                return true;
//
//            }
//        });
//        //控制他只能打开一个组
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = new clientlob_ExpandableAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }


    public void setMaplistInFragcl(ArrayList<List<Object>> m,int left,int right) {
        maplistInFragcl.clear();
        maplistInFragcl = m;
        Log.e("FragSysSETMAP", maplistInFragcl.size() + "");
        adapter.getMaplist(maplistInFragcl,left,right);
        Log.e("FragSysSET", (expandableListView == null) + " " + new system_ExpandableAdapter().getGroupCount());
        if (expandableListView != null) {
            for (int i = 0; i < new clientlob_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }

    public void collapse(Fragment frag) {
        if (expandableListView != null) {
            for (int i = 0; i < new clientlob_ExpandableAdapter().getGroupCount(); i++) {
                this.expandableListView.collapseGroup(i);
            }
        }

    }

}
