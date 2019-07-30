package com.example.smartcontroltower.Fragment_dynamic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.smartcontroltower.MySmartTable;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class Fragment_goal extends Fragment {

    public static View view2;
    private ArrayList<Object> maplist22 = new ArrayList<>();
    private ArrayList<Object> maplistNew = new ArrayList<>();

    public Fragment_goal() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view2 = inflater.inflate(R.layout.goal_fragment, container, false);
        if (savedInstanceState != null) {
            ArrayList<Object> maplistold = new ArrayList<>();
            maplistold = (ArrayList<Object>) savedInstanceState.getSerializable("map");
            Log.e("Change Screen", maplistold.size() + "");
            refreshDate(maplistold);
        } else if (maplistNew.size() != 0) {
            refreshDate(maplistNew);
        }
        Log.e("Tag222", "on create view");
        return view2;
    }

    public void refreshDate(ArrayList<Object> map) {
        MySmartTable<Object> table = view2.findViewById(R.id.goal_table);
        maplist22.clear();
        MapTableData tableData;
        if (map.size() != 0) {
            for (int i = 0; i < map.size(); i++) {
                LinkedHashMap<String, String> item = (LinkedHashMap<String, String>) map.get(i);
                for (String key : item.keySet()) {
                    if (((LinkedHashMap<String, String>) map.get(i)).get(key).equals("0")) {
                        ((LinkedHashMap<String, String>) map.get(i)).put(key, "-");
                    }
                }
            }
            LinkedHashMap<String, String> item2 = new LinkedHashMap<>();
            item2.put("FLAG", "ISG");
            item2.put("PRE_APJ", "--Goal--");
            item2.put("APJ", "--Act.--");
            item2.put("PRE_OPR", "--Goal--");
            item2.put("OPR", "--Act.--");
            item2.put("PRE_APCC", "--Goal--");
            item2.put("APCC", "--Act.--");
            item2.put("PRE_CCC", "--Goal--");
            item2.put("CCC", "--Act.--");
            item2.put("PRE_ICC", "--Goal--");
            item2.put("ICC", "--Act.--");
            item2.put("PRE_ODM-PP", "--Goal--");
            item2.put("ODM-PP", "--Act.--");
            item2.put("PRE_ODM", "--Goal--");
            item2.put("ODM", "--Act.--");
            item2.put("PRE_SS", "--Goal--");
            item2.put("SS", "--Act.--");
            for (int h = 0; h < map.size(); h++) {
                if (h == 2 && map.size() < 5) {
                    maplist22.add(item2);
                }
                maplist22.add(map.get(h));

            }

            tableData = MapTableData.create("", maplist22);

            List<Column> a = new LinkedList<>();
            a.add(new Column("Product Type", tableData.getColumns().get(0)));
            String[] ColumnTitle = {"APJ", "OPR", "APCC", "CCC", "ICC", "ODM-PP", "ODM", "SS"};
            int count = 0;
            for (int i = 1; i < tableData.getColumns().size() - 1; i += 2) {
                Column groupColumn = new Column(ColumnTitle[count], tableData.getColumns().get(i), tableData.getColumns().get(i + 1));
                a.add(groupColumn);
                count++;
            }
            tableData.setColumns(a);
            table.getConfig().setFixedTitle(true);
            tableData.getColumns().get(0).setFixed(true);
            tableData.getColumns().get(0).setWidth(190);
            tableData.getColumns().get(0).setTextAlign(Paint.Align.LEFT);
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);

            table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                @Override
                public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                    if (cellInfo.row % 4 == 2) {
                        paint.setColor(getResources().getColor(R.color.table_gray));
                        canvas.drawRect(rect, paint);
                    }
                }

                @Override
                public int getTextColor(CellInfo cellInfo) {
                    if (cellInfo.row % 4 == 2) {
                        return getResources().getColor(R.color.white);
                    }
                    return 0;
                }
            });
        }
        else{
            tableData = MapTableData.create("", maplist22);
        }

            table.setTableData(tableData);
            table.invalidate();
            table.getConfig().setTableTitleStyle(new FontStyle(50, R.color.table_gray));
            table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
            table.getConfig().setContentStyle(new FontStyle(40, Color.rgb(115, 135, 156)));
            table.getConfig().setColumnTitleStyle(new FontStyle(40, Color.WHITE));

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
        outState.putSerializable("map", maplist22);
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

    public void initialFragment(ArrayList<Object> map) {
        maplistNew.clear();
        maplistNew = map;
    }

}
