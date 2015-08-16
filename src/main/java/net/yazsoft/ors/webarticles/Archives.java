package net.yazsoft.ors.webarticles;

import net.yazsoft.ors.entities.WebArticles;

import java.util.List;

/**
 * Created by fec on 7/19/15.
 */
public class Archives {
    Integer month;
    Integer year;
    Long count;
    String monthname;

    @Override
    public String toString() {
        return "DateMonthYear{" +
                "month=" + month +
                ", year=" + year +
                ", count=" + count +
                '}';
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getMonthname() {
        return monthname;
    }

    public void setMonthname(String monthname) {
        this.monthname = monthname;
    }
}
