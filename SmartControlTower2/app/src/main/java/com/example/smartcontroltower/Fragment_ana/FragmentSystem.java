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

import com.example.smartcontroltower.Fragment_ana.system_ExpandableAdapter;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentSystem extends Fragment {

    View view;
    private ExpandableListView expandableListView;
    private List<Object> maplistInFragSys = new ArrayList<>();
    private system_ExpandableAdapter adapter=new system_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.system_fragment, container, false);
        Log.e("FragSys","onCreateView");
        expandableListView = (ExpandableListView) view.findViewById(R.id.system_expand_list);
        expandableListView.setAdapter(adapter);
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

    public void setMaplistInFragSys(List<Object> m){
        maplistInFragSys.clear();
        maplistInFragSys=m;
        Log.e("FragSysSETMAP",maplistInFragSys.size()+"");
        adapter.getMaplist(maplistInFragSys);
    }


}
