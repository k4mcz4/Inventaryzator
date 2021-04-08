package com.example.inventura.model;

import java.util.Date;

public class StockTakeModel {
    private long _id;
    private String startDate;
    private String endDate;

    public StockTakeModel(long _id, String startDate) {
        this._id = _id;
        this.startDate = startDate;
    }

    public StockTakeModel(long _id, String startDate, String endDate) {
        this._id = _id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public StockTakeModel(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
