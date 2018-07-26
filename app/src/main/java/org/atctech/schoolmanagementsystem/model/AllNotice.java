package org.atctech.schoolmanagementsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jacky on 4/1/2018.
 */

public class AllNotice {

    @SerializedName("file")
    @Expose
    private String file;

    @SerializedName("size")
    @Expose
    private int size;

    @SerializedName("subject")
    @Expose
    private String subject;


    @SerializedName("date")
    @Expose
    private String date;


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
