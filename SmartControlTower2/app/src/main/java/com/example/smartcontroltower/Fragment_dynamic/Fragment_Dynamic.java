package com.example.smartcontroltower.Fragment_dynamic;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.smartcontroltower.MySmartTable;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Fragment_Dynamic extends Fragment {

    public static View view;
    private static ArrayList<Object> maplist1 = new ArrayList<>();
    public static final String Tag = "Dynamic";
    private String title = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dynamic_fragment, container, false);
        if (savedInstanceState != null) {
            ArrayList<Object> maplist1old = new ArrayList<>();
            maplist1old = (ArrayList<Object>) savedInstanceState.getSerializable("map");
            refreshDate(maplist1old, savedInstanceState.getString("title"));
        } else if (maplist1.size() != 0) {
            refreshDate(maplist1, title);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("map", maplist1);
        outState.putString("title", title);
    }


    public void refreshDate(ArrayList<Object> map, String title) {
        MySmartTable<Object> table = view.findViewById(R.id.dyn_table);
        this.title = title;
        MapTableData tableData;
        if (map.size() != 0) {
            maplist1 = map;
            tableData = MapTableData.create(title, maplist1);
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
            tableData.getColumns().get(0).setWidth(300);
            tableData.getColumns().get(0).setTextAlign(Paint.Align.LEFT);
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);


            ICellBackgroundFormat<Integer> backgroundFormat = new BaseCellBackgroundFormat<Integer>() {
                @Override
                public int getBackGroundColor(Integer position) {
                    if (position % 2 == 0) {
                        return ContextCompat.getColor(view.getContext(), R.color.lightgray);
                    }
                    return TableConfig.INVALID_COLOR;
                }

                @Override
                public int getTextColor(Integer position) {
                    if (position % 2 == 0) {
                        return ContextCompat.getColor(view.getContext(), R.color.white);
                    }
                    return TableConfig.INVALID_COLOR;
                }
            };
        }
        else{
            tableData=MapTableData.create("",map);
        }

        table.getConfig().setTableTitleStyle(new FontStyle(50, R.color.table_gray));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table.getConfig().setContentStyle(new FontStyle(45, Color.rgb(115, 135, 156)));
        table.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));
        table.getConfig().setVerticalPadding(10);
        table.setTableData(tableData);
        table.notifyDataChanged();
        table.invalidate();

    }

    public void initialFragment(ArrayList<Object> map, String title) {
        maplist1.clear();
        maplist1 = map;
        this.title = title;
    }


}
