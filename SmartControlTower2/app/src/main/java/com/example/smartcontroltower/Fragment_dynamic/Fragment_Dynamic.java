package com.example.smartcontroltower.Fragment_dynamic;

import android.content.Context;
import android.graphics.Color;
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

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Fragment_Dynamic extends Fragment {

    View view;
    private ArrayList<Object> maplist = new ArrayList<>();
    public static final String Tag = "Dynamic";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        Bundle bundle = this.getArguments();//得到从Activity传来的数据

        if (bundle != null) {
//            maplist= (ArrayList<Object>) bundle.getSerializable("dynamic");
            //          Log.e("Length",maplist.size()+"");

//            SmartTable<Object> table = view.findViewById(R.id.dyn_table);
            TextView tt = view.findViewById(R.id.textte);
//            maplist= (ArrayList<Object>) bundle.getSerializable("dynamic");
//            String str=bundle.getString("string");
            tt.setText("why");
            Log.e("tt", tt.getText() + "");
//            MapTableData tableData = MapTableData.create("表格名", maplist);
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
//            table.getConfig().setFixedTitle(true);
//            tableData.getColumns().get(0).setFixed(true);
//            tableData.getColumns().get(0).setWidth(190);
//            table.setZoom(true, 2, 1);
//            table.getConfig().setShowXSequence(false);
//            table.getConfig().setShowYSequence(false);
////设置数据
//            table.setTableData(tableData);
//            table.getConfig().setContentStyle(new FontStyle(40, Color.BLACK));
//            table.getConfig().setColumnTitleStyle(new FontStyle(40, Color.BLACK));
        }
        Log.e("TAG", "oncreate view" + " " + (bundle != null));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Tag", "onattach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Tag", "oncreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Tag", "on activity create");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Tag", "on start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Tag", "on resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Tag", "on pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Tag", "on stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Tag", "des view");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tag", "destroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Tag", "detach");
    }

    public void refreshDate(ArrayList<Object> map) {
        if (map.size() != 0) {
            TextView tt = tt = view.findViewById(R.id.textte);
            SmartTable<Object> table = view.findViewById(R.id.dyn_table);
            maplist = map;
            Log.e("size", maplist.size() + "");
            MapTableData tableData = MapTableData.create("表格名", maplist);
            Column groupColumn = new Column("Factory Backlog", tableData.getColumns().get(2), tableData.getColumns().get(3), tableData.getColumns().get(4), tableData.getColumns().get(5), tableData.getColumns().get(6));
            Column groupColumn1 = new Column("APJ", tableData.getColumns().get(1));
            List<Column> a = new LinkedList<>();
            a.add(tableData.getColumns().get(0));
            a.add(groupColumn1);
            a.add(groupColumn);
            for (int i = 7; i < tableData.getColumns().size(); i++) {
                a.add(tableData.getColumns().get(i));
            }
            tableData.setColumns(a);
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
}
