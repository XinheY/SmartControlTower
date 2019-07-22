package com.example.smartcontroltower.Fragment_ana;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.bin.david.form.data.table.TableData;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class system_ExpandableAdapter extends BaseExpandableListAdapter {
    private static ArrayList<SmartTable<Object>> tables=new ArrayList<>();
    public String[] groupString = {"OVERALL", "CLIENT", "ISG"};


    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tables.get(groupPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupString[groupPosition]);
        return convertView;
    }
    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);
            SmartTable<Object> table= convertView.findViewById(R.id.ana_table);
            convertView.setTag(table);

        List<Object> maplist = new ArrayList<>();
        List<Object> maplist2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, String> a = new LinkedHashMap<>();
            a.put("city", "Shenyang");
            a.put("name", "Peter");
            a.put("no", "12");
            a.put("count", "2");
            a.put("no12", "1222");
            a.put("count2", "2cc");
            Map<String, String> b = new LinkedHashMap<>();
            b.put("city", "Xiamen");
            b.put("name", "Jason");
            b.put("no", "12");
            b.put("count", "2");
            b.put("no12", "1233");
            b.put("count2", "2ff");
            maplist.add(a);
            maplist.add(b);
        }

        List<HashMap<String,HashMap<String,String>>> abc=new LinkedList<>();
        Map<String,Map<String,String>> sss=new LinkedHashMap<>();
        HashMap<String,String> basd=new LinkedHashMap<>();
        basd.put("city","Shenyang");
        basd.put("Name","Cindy");
        sss.put("group",basd);


        MapTableData tableData = MapTableData.create("表格名", maplist);
        Column groupColumn = new Column("组合", tableData.getColumns().get(0), tableData.getColumns().get(1));

        List<Column> colu=new LinkedList<>();
        colu=tableData.getColumns();
        colu.add(groupColumn);
        tableData.setColumns(colu);

        table.getConfig().setFixedTitle(true);
        tableData.getColumns().get(0).setFixed(true);
        table.setZoom(true,2,1);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
//设置数据
        table.setTableData(tableData);
        table.getConfig().setTableTitleStyle(new FontStyle(50, convertView.getResources().getColor(R.color.table_gray)));
        table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(convertView.getResources().getColor(R.color.table_gray)));
        table.getConfig().setContentStyle(new FontStyle(40, convertView.getResources().getColor(R.color.table_gray)));
        table.getConfig().setColumnTitleStyle(new FontStyle(40, convertView.getResources().getColor(R.color.white)));



        tables.add(table);
        //childViewHolder.tvTitle.setText(childString[groupPosition][childPosition]);
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView tvTitle;

    }
}
