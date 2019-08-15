package com.example.smartcontroltower.Fragment_ana;

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
import java.util.List;

public class system_ExpandableAdapter extends BaseExpandableListAdapter {
    private static ArrayList<SmartTable<Object>> tables = new ArrayList<>();
    public String[] groupString = {"OVERALL", "CLIENT", "ISG"};//顺序与网页版相同
    private static ArrayList<List<Object>> maplistInSysExp = new ArrayList<>();//表格数据
    private static int leftIndicator = 0;
    private static int rightIndicator = 0;
    private static int groupbyCount = 1;
    private static String submitVersionTitle = "";
    private static String compareVersionTitle = "";

    @Override
    public int getGroupCount() {
        return groupString.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return tables.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

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
        //  Log.e("SE", "getGroupView");
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
     *
     */

    //取得显示给定分组的表格数据
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        MySmartTable<Object> table = convertView.findViewById(R.id.ana_table);
        convertView.setTag(table);
        int more = 15;//每一组会有15列

        if (maplistInSysExp.size() != 0) {
            MapTableData tableData = null;

                tableData = MapTableData.create("", maplistInSysExp.get(groupPosition));
                List<Column> list4one = new ArrayList<>();//没有groupColumn的list
                List<Column> list4one2 = new ArrayList<>();//完成groupColumn的list
                //根据所选的range来选取需要被显示在表格中的数据
                list4one.addAll(tableData.getColumns().subList(leftIndicator + groupbyCount - 1, rightIndicator + groupbyCount));
                list4one.add(tableData.getColumns().get(more + groupbyCount - 1));
                list4one.addAll(tableData.getColumns().subList(more + leftIndicator + groupbyCount - 1, more + groupbyCount + rightIndicator));
                list4one.add(tableData.getColumns().get(2 * more + groupbyCount - 1));
                list4one.addAll(tableData.getColumns().subList(2 * more + leftIndicator + groupbyCount - 1, 2 * more + groupbyCount + rightIndicator));
                list4one.add(tableData.getColumns().get(3 * more + groupbyCount - 1));
                int size1 = list4one.size() / 3;
                Column one = new Column(submitVersionTitle, list4one.subList(0, size1));//submit version
                Column two = new Column(compareVersionTitle, list4one.subList(size1, 2 * size1));//compareVersionTitleare version
                Column three = new Column("Delta", list4one.subList(2 * size1, 3 * size1));//delta
                list4one2.add(tableData.getColumns().get(0));
                if (groupbyCount == 2) {
                    list4one2.add(tableData.getColumns().get(1));
                } else if (groupbyCount == 3) {
                    list4one2.add(tableData.getColumns().get(1));
                    list4one2.add(tableData.getColumns().get(2));
                }
                list4one2.add(one);
                list4one2.add(two);
                list4one2.add(three);
                tableData.setColumns(list4one2);


            table.getConfig().setFixedTitle(true);
            //根据groupby所选项数来判断固定的列数
            tableData.getColumns().get(0).setFixed(true);
            if (groupbyCount == 2) {
                tableData.getColumns().get(1).setFixed(true);
            } else if (groupbyCount == 3) {
                tableData.getColumns().get(1).setFixed(true);
                tableData.getColumns().get(2).setFixed(true);
            }
            table.setZoom(true, 2, 1);
            table.getConfig().setShowXSequence(false);
            table.getConfig().setShowYSequence(false);
            table.getConfig().setTableTitleStyle(new FontStyle(50, convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setContentStyle(new FontStyle(40, convertView.getResources().getColor(R.color.table_gray)));
            table.getConfig().setColumnTitleStyle(new FontStyle(40, convertView.getResources().getColor(R.color.white)));
            table.getConfig().setVerticalPadding(10);
            table.setTableData(tableData);

            //根据groupby所选项数来判断需要自动合并的列数
            tableData.getColumns().get(0).setAutoMerge(true);
            if (groupbyCount == 2) {
                tableData.getColumns().get(1).setAutoMerge(true);
            } else if (groupbyCount == 3) {
                tableData.getColumns().get(1).setAutoMerge(true);
                tableData.getColumns().get(2).setAutoMerge(true);
            }
            table.notifyDataChanged();
            table.invalidate();
        }
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

    /**
     * 从system fragment获取数据
     * @param m 表格数据
     * @param leftIndicator range左侧指标
     * @param rightIndicator range右侧指标
     * @param groupbyCount groupby中被选中的数量
     * @param submitVersionTitle submit version
     * @param compareVersionTitle compareVersionTitleare version
     */
    public void getMaplist(ArrayList<List<Object>> m, int leftIndicator, int rightIndicator, int groupbyCount, String submitVersionTitle, String compareVersionTitle) {
        maplistInSysExp.clear();
        maplistInSysExp = m;
        this.leftIndicator = leftIndicator;
        this.rightIndicator = rightIndicator;
        this.groupbyCount = groupbyCount;
        this.submitVersionTitle = submitVersionTitle;
        this.compareVersionTitle = compareVersionTitle;
    }
}
