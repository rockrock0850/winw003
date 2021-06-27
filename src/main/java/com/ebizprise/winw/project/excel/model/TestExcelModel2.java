package com.ebizprise.winw.project.excel.model;

import java.util.Date;

import com.ebizprise.project.utility.doc.excel.ExcelCell;

/**
 * The <code>TestExcelModel</code>
 */
public class TestExcelModel2 {

	@ExcelCell(index = 0)
	private String a;
	@ExcelCell(index = 1)
	private String b;
	@ExcelCell(index = 3)
	private int c;
	@ExcelCell(index = 2)
	private String d;
	@ExcelCell(index = 4)
	private Date f;

	public TestExcelModel2(String a, String b, int c, String d, Date f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.f = f;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public Date getF() {
		return f;
	}

	public void setF(Date f) {
		this.f = f;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
}