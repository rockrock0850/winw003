package com.ebizprise.winw.project.excel.model;

import java.util.Date;

import com.ebizprise.project.utility.doc.excel.ExcelCell;

public class TestExcelModel {

	@ExcelCell(index = 0)
	private String a;
	@ExcelCell(index = 1)
	private String b;
	@ExcelCell(index = 2)
	private String c;
	@ExcelCell(index = 3)
	private Date d;

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	public TestExcelModel() {
	}

	public TestExcelModel(String a, String b, String c, Date d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}

	/**
	 * @param a
	 *            the a to set
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
	 * @param b
	 *            the b to set
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
	 * @param c
	 *            the c to set
	 */
	public void setC(String c) {
		this.c = c;
	}
}