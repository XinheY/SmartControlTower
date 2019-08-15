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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class Fragment_goal extends Fragment {

    public static View view2;
    private static ArrayList<Object> maplist22 = new ArrayList<>();
    private static ArrayList<Object> maplistNew = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view2 = inflater.inflate(R.layout.goal_fragment, container, false);
        ArrayList<Object> maplistOld;
        maplistOld= (ArrayList<Object>) maplist22.clone();
        refreshDate(maplistOld);
        return view2;
    }

    /**
     * 创建表格然后将数据放进表格
     * @param map
     */
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
            //格式化处理数据
            LinkedHashMap<String, String> item2 = new LinkedHashMap<>();
            item2.put("FLAG", "ISG");
            item2.put("PRE_APJ", "Goal");
            item2.put("APJ", "Act.");
            item2.put("PRE_OPR", "Goal");
            item2.put("OPR", "Act.");
            item2.put("PRE_APCC", "Goal");
            item2.put("APCC", "Act.");
            item2.put("PRE_CCC", "Goal");
            item2.put("CCC", "Act.");
            item2.put("PRE_ICC", "Goal");
            item2.put("ICC", "Act.");
            item2.put("PRE_ODM-PP", "Goal");
            item2.put("ODM-PP", "Act.");
            item2.put("PRE_ODM", "Goal");
            item2.put("ODM", "Act.");
            item2.put("PRE_SS", "Goal");
            item2.put("SS", "Act.");
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

            //改变特定行的背景颜色
            table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
                @Override
                public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                    if (cellInfo.row % 4 == 2) {
                        paint.setColor(Color.rgb(115, 135, 156));
                        canvas.drawRect(rect, paint);
                    }
                }
                @Override
                public int getTextColor(CellInfo cellInfo) {
                    if (cellInfo.row % 4 == 2) {
                        return Color.WHITE;
                    }
                    return 0;
                }
            });
        } else {
            tableData = MapTableData.create("", maplist22);
        }
        table.getConfig().setTableTitleStyle(new FontStyle(50, R.color.table_gray));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(Color.rgb(115, 135, 156)));
        table.getConfig().setContentStyle(new FontStyle(45, Color.rgb(115, 135, 156)));
        table.getConfig().setColumnTitleStyle(new FontStyle(45, Color.WHITE));
        table.getConfig().setVerticalPadding(10);
        Log.e("tableData", tableData.getColumns().size() + "");
        table.setTableData(tableData);
        table.invalidate();
    }
}
