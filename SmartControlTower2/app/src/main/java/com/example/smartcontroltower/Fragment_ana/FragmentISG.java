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


public class FragmentISG extends Fragment {

    View view;
    private ExpandableListView expandableListView=null;
    private ArrayList<List<Object>> maplistInFragIsg = new ArrayList<>();
    private ISG_ExpandableAdapter adapter = new ISG_ExpandableAdapter();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.isg_fragment, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.isg_expand_list);
        expandableListView.setAdapter(new ISG_ExpandableAdapter());
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
                int count = new ISG_ExpandableAdapter().getGroupCount();
                for (int i = 0; i < count; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }

    /**
     *从Analysis.java获取生成表格所需要的数据，再将数据传入system adapter
     * @param m  从主程序获取表格数据
     * @param left rangeseeker的左侧指针
     * @param right rangeseeker的右侧指针
     * @param count groupby中被选中个数（选中个数会影响表格的生成）
     * @param pre  submit version
     * @param comp compare version
     */
    public void setMaplistInFragIsg(ArrayList<List<Object>> m,int left,int right,int count,String pre,String comp) {
        maplistInFragIsg.clear();
        maplistInFragIsg = m;
        adapter.getMaplist(maplistInFragIsg,left,right,count,pre,comp);
        if (expandableListView != null) {
            for (int i = 0; i < new ISG_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }

    /**
     * 关闭system fragment中的所有页面
     * @param frag
     */
    public void collapse(Fragment frag) {
        if(expandableListView!=null){
            for (int i = 0; i < new ISG_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }
}
