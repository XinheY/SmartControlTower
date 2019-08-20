package com.example.smartcontroltower;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用于获取初始化的所有日期信息
 */
public class InitializeInfo implements Serializable {

    private ArrayList<String> version;
    private ArrayList<String> version_addclosing;
    private ArrayList<String> version_year;
    private ArrayList<String> version_year_quar;
    private ArrayList<String> version_year_quar_week;

    public InitializeInfo(ArrayList<String> version,ArrayList<String> version_addclosing,
                          ArrayList<String> version_year,ArrayList<String> version_year_quar,ArrayList<String> version_year_quar_week){

        this.version=version;
        this.version_addclosing=version_addclosing;
        this.version_year=version_year;
        this.version_year_quar=version_year_quar;
        this.version_year_quar_week=version_year_quar_week;
    }

    public ArrayList<String> getVersion() {
        return version;
    }

    public ArrayList<String> getVersion_addclosing() {
        return version_addclosing;
    }

    public ArrayList<String> getVersion_year() {
        return version_year;
    }

    public ArrayList<String> getVersion_year_quar() {
        return version_year_quar;
    }

    public ArrayList<String> getVersion_year_quar_week() {
        return version_year_quar_week;
    }

    public void setVersion(ArrayList<String> version) {
        this.version = version;
    }

    public void setVersion_addclosing(ArrayList<String> version_addclosing) {
        this.version_addclosing = version_addclosing;
    }

    public void setVersion_year(ArrayList<String> version_year) {
        this.version_year = version_year;
    }

    public void setVersion_year_quar(ArrayList<String> version_year_quar) {
        this.version_year_quar = version_year_quar;
    }

    public void setVersion_year_quar_week(ArrayList<String> version_year_quar_week) {
        this.version_year_quar_week = version_year_quar_week;
    }
}
