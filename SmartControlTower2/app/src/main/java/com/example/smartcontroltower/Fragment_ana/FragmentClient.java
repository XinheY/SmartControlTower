package com.example.smartcontroltower.Fragment_ana;

import android.os.Bundle;
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


public class FragmentClient extends Fragment {

    View view;
    private ExpandableListView expandableListView=null;
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
                int count = new client_ExpandableAdapter().getGroupCount();
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
    public void setMaplistInFragClient(ArrayList<List<Object>> m,int left,int right,int count,String pre,String comp){
        maplistInFragClient.clear();
        maplistInFragClient=m;
        if(expandableListView!=null){
            for(int i=0;i<new client_ExpandableAdapter().getGroupCount();i++){
                expandableListView.collapseGroup(i);}
        }
        adapter.inputdata(maplistInFragClient,left,right,count,pre,comp);

    }

    /**
     * 关闭system fragment中的所有页面
     * @param frag
     */
    public void collapse(Fragment frag) {
        if(expandableListView!=null) {
            for (int i = 0; i < new client_ExpandableAdapter().getGroupCount(); i++) {
                expandableListView.collapseGroup(i);
            }
        }
    }
}
