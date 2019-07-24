package com.example.smartcontroltower.Fragment_dynamic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Fragment_goal extends Fragment {

    public static View view2;
    private ArrayList<Object> maplist22=new ArrayList<>();


    public Fragment_goal() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view2 = inflater.inflate(R.layout.goal_fragment, container, false);
        Log.e("Tag222", "on create view");
        return view2;
    }

    public void refreshDate(ArrayList<Object> map) {
        if (map.size() != 0) {
            SmartTable<Object> table = view2.findViewById(R.id.goal_table);
            maplist22 = map;
            Log.e("size", maplist22.size() + "");
            MapTableData tableData = MapTableData.create("表格名", maplist22);
//            Column groupColumn = new Column("Factory Backlog", tableData.getColumns().get(2), tableData.getColumns().get(3), tableData.getColumns().get(4), tableData.getColumns().get(5), tableData.getColumns().get(6));
//            Column groupColumn1 = new Column("APJ", tableData.getColumns().get(1));
//            List<Column> a = new LinkedList<>();
//            a.add(tableData.getColumns().get(0));
//            a.add(groupColumn1);
//            a.add(groupColumn);
//            for (int i = 7; i < tableData.getColumns().size(); i++) {
//                a.add(tableData.getColumns().get(i));
//            }
//            tableData.setColumns(a);
            table.getConfig().setFixedTitle(true);
            tableData.getColumns().get(0).setFixed(true);
            tableData.getColumns().get(0).setWidth(190);
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);
//设置数据
            table.setTableData(tableData);
            table.getConfig().setContentStyle(new FontStyle(40, Color.BLACK));
            table.getConfig().setColumnTitleStyle(new FontStyle(40, Color.BLACK));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Tag222", "onattach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Tag222", "oncreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Tag222", "on activity create");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Tag222", "onSaveInstance");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Tag222", "on start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Tag222", "on resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Tag222", "on pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Tag222", "on stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Tag222", "des view");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tag222", "destroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Tag222", "detach");
    }




}
