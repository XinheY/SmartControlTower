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


public class FragmentISGLOB extends Fragment {

    View view;
    private ExpandableListView expandableListView;
    private ArrayList<List<Object>> maplistInDragIsgLob = new ArrayList<>();
    private ISGLOB_ExpandableAdapter adapter = new ISGLOB_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.isglob_fragment, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.isglob_expand_list);
        expandableListView.setAdapter(new ISGLOB_ExpandableAdapter());

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = new ISGLOB_ExpandableAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }

    public void setMaplistInFragIsg(ArrayList<List<Object>> m,int left,int right) {
        maplistInDragIsgLob.clear();
        maplistInDragIsgLob = m;
        Log.e("FragSysSETMAP", maplistInDragIsgLob.size() + "");
        adapter.getMaplist(maplistInDragIsgLob,left,right);
        if (expandableListView != null) {
            for (int i = 0; i < new ISGLOB_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }

    public void collapse(Fragment frag) {
        if(expandableListView!=null) {
            for (int i = 0; i < new ISGLOB_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }

}
