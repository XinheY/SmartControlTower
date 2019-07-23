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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dynamic_fragment, container, false);

        Bundle bundle = this.getArguments();//得到从Activity传来的数据
        if (bundle != null) {
            SmartTable<Object> table = view.findViewById(R.id.dyn_table);
            TextView tt=view.findViewById(R.id.textte);
            maplist= (ArrayList<Object>) bundle.getSerializable("dynamic");
            String str=bundle.getString("string");
            tt.setText(str);
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



        return view;
    }


}
