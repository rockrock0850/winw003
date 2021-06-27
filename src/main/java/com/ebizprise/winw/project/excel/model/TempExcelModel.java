package com.ebizprise.winw.project.excel.model;

import com.ebizprise.project.utility.doc.excel.ExcelCell;

public class TempExcelModel {
    @ExcelCell(index = 0)
    private String a;
    @ExcelCell(index = 1)
    private String b;
    @ExcelCell(index = 3)
    private String c;
    @ExcelCell(index = 2)
    private String d;
    @ExcelCell(index = 4)
    private String e;
    @ExcelCell(index = 5)
    private String f;
    @ExcelCell(index = 6)
    private String g;
    
    public TempExcelModel(String a,String b,String c,String d,String e,String f,String g) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }
    /**
     * @return the a
     */
    public String getA() {
        return a;
    }
    /**
     * @param a the a to set
     */
    public void setA(String a) {
        this.a = a;
    }
    /**
     * @return the b
     */
    public String getB() {
        return b;
    }
    /**
     * @param b the b to set
     */
    public void setB(String b) {
        this.b = b;
    }
    /**
     * @return the c
     */
    public String getC() {
        return c;
    }
    /**
     * @param c the c to set
     */
    public void setC(String c) {
        this.c = c;
    }
    /**
     * @return the d
     */
    public String getD() {
        return d;
    }
    /**
     * @param d the d to set
     */
    public void setD(String d) {
        this.d = d;
    }
    /**
     * @return the e
     */
    public String getE() {
        return e;
    }
    /**
     * @param e the e to set
     */
    public void setE(String e) {
        this.e = e;
    }
    /**
     * @return the f
     */
    public String getF() {
        return f;
    }
    /**
     * @param f the f to set
     */
    public void setF(String f) {
        this.f = f;
    }
    /**
     * @return the g
     */
    public String getG() {
        return g;
    }
    /**
     * @param g the g to set
     */
    public void setG(String g) {
        this.g = g;
    }   
    
}
