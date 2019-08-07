package com.example.smartcontroltower.Fragment_ana;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
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
import com.example.smartcontroltower.MySmartTable;
import com.example.smartcontroltower.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class clientlob_ExpandableAdapter extends BaseExpandableListAdapter {
    private static ArrayList<SmartTable<Object>> tables = new ArrayList<>();
    public String[] groupString = {"Alienware Desktops", "Alienware Notebooks", "Personal Desktops", "Personal Notebooks"
            , "Vostro Desktops", "Vostro Notebooks", "XPS Desktops", "XPS Notebooks", "Latitude", "Optiplex Desktops",
            "Fixed Workstations", "Mobile Workstations", "Chrome", "Cloud Client",
            "Internet of Things"};
    private static ArrayList<List<Object>> maplistInClExp = new ArrayList<>();
    private static int left, right,count;

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
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupString[groupPosition]);
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        system_ExpandableAdapter.ChildViewHolder childViewHolder;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        MySmartTable<Object> table = (MySmartTable<Object>) convertView.findViewById(R.id.ana_table);
        convertView.setTag(table);
        int more=15;
        if (maplistInClExp.size() != 0) {
            Log.e("SE", groupPosition + "::");
            MapTableData tableData = MapTableData.create("", maplistInClExp.get(groupPosition));

            List<Column> list4one = new ArrayList<>();
            list4one.add(tableData.getColumns().get(0));
            if(count==2){
                list4one.add(tableData.getColumns().get(1));
            }
            else if(count==3){
                list4one.add(tableData.getColumns().get(1));
                list4one.add(tableData.getColumns().get(2));
            }
            list4one.addAll(tableData.getColumns().subList(left+count-1,right+count));
            list4one.add(tableData.getColumns().get(more+count-1));
            list4one.addAll(tableData.getColumns().subList(more+left+count-1,more+count+right));
            list4one.add(tableData.getColumns().get(2*more+count-1));
            list4one.addAll(tableData.getColumns().subList(2*more+left+count-1,2*more+count+right));
            list4one.add(tableData.getColumns().get(3*more+count-1));
            tableData.setColumns(list4one);

            table.getConfig().setFixedTitle(true);
            tableData.getColumns().get(0).setFixed(true);
            if(count==2){
                tableData.getColumns().get(1).setFixed(true);
            }
            else if(count==3){
                tableData.getColumns().get(1).setFixed(true);
                tableData.getColumns().get(2).setFixed(true);
            }
            //tableData.getColumns().get(0).setTextAlign(Paint.Align.LEFT);
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);

            table.getConfig().setTableTitleStyle(new FontStyle(50, convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setContentStyle(new FontStyle(40, convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleStyle(new FontStyle(40, convertView.getResources().getColor(R.color.white)));
            table.getConfig().setVerticalPadding(10);
            table.setTableData(tableData);
            tableData.getColumns().get(0).setAutoMerge(true);
            if(count==2){
                tableData.getColumns().get(1).setAutoMerge(true);
            }
            else if(count==3){
                tableData.getColumns().get(1).setAutoMerge(true);
                tableData.getColumns().get(2).setAutoMerge(true);
            }
            table.notifyDataChanged();
            table.invalidate();
        }
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

    public void getMaplist(ArrayList<List<Object>> m, int left, int right,int count) {
        maplistInClExp.clear();
        maplistInClExp = m;
        this.left = left;
        this.right = right;
        this.count=count;
    }
}
