package com.example.attandancesystem;

public class ClassItem {
    private long cid;
    private String classname;
    private String section;

    public ClassItem(String classname, String section) {
        this.classname = classname;
        this.section = section;
    }
    public ClassItem(long cid, String classname, String section) {
        this.cid = cid;
        this.classname = classname;
        this.section = section;
    }

    public String getClassname() { return classname; }
    public void setClassname(String classname) {
        this.classname = classname;
    }
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }
}