/**
 * Copyright (c) eBizprise, Inc All Rights Reserved.
 */
package com.ebizprise.winw.project.excel.model;

import com.ebizprise.project.utility.doc.excel.ExcelCell;

/**
 * 報表作業  匯出的值  與  Excel 列號對應 共用模組
 * A-Z 欄位   
 * 
 * @author suho.yeh
 * @version 1.0, Created at 2019年8月23日
 */
public class ExcelModel <T>{
    @ExcelCell(index = 0)
    private T columnA;
    
    @ExcelCell(index = 1)
    private T columnB;
    
    @ExcelCell(index = 2)
    private T columnC;
    
    @ExcelCell(index = 3)
    private T columnD;
    
    @ExcelCell(index = 4)
    private T columnE;
    
    @ExcelCell(index = 5)
    private T columnF;
    
    @ExcelCell(index = 6)
    private T columnG;
    
    @ExcelCell(index = 7)
    private T columnH;
    
    @ExcelCell(index = 8)
    private T columnI;
    
    @ExcelCell(index = 9)
    private T columnJ;
    
    @ExcelCell(index = 10)
    private T columnK;
    
    @ExcelCell(index = 11)
    private T columnL;
    
    @ExcelCell(index = 12)
    private T columnM;
    
    @ExcelCell(index = 13)
    private T columnN;
    
    @ExcelCell(index = 14)
    private T columnO;
    
    @ExcelCell(index = 15)
    private T columnP;
    
    @ExcelCell(index = 16)
    private T columnQ;
    
    @ExcelCell(index = 17)
    private T columnR;
    
    @ExcelCell(index = 18)
    private T columnS;
    
    @ExcelCell(index = 19)
    private T columnT;
    
    @ExcelCell(index = 20)
    private T columnU;
    
    @ExcelCell(index = 21)
    private T columnV;
    
    @ExcelCell(index = 22)
    private T columnW;
    
    @ExcelCell(index = 23)
    private T columnX;
    
    @ExcelCell(index = 24)
    private T columnY;
    
    @ExcelCell(index = 25)
    private T columnZ;

    @ExcelCell(index = 26)
    private T columnAA;
    
    @ExcelCell(index = 27)
    private T columnAB;
    
    @ExcelCell(index = 28)
    private T columnAC;
    
    @ExcelCell(index = 29)
    private T columnAD;
    
    @ExcelCell(index = 30)
    private T columnAE;
    
    @ExcelCell(index = 31)
    private T columnAF;
    
    @ExcelCell(index = 32)
    private T columnAG;
    
    @ExcelCell(index = 33)
    private T columnAH;
    
    @ExcelCell(index = 34)
    private T columnAI;
    
    @ExcelCell(index = 35)
    private T columnAJ;
    
    @ExcelCell(index = 36)
    private T columnAK;
    
    @ExcelCell(index = 37)
    private T columnAL;
    
    @ExcelCell(index = 38)
    private T columnAM;
    
    @ExcelCell(index = 39)
    private T columnAN;
    
    @ExcelCell(index = 40)
    private T columnAO;
    
    @ExcelCell(index = 41)
    private T columnAP;
    
    @ExcelCell(index = 42)
    private T columnAQ;
    
    @ExcelCell(index = 43)
    private T columnAR;
    
    @ExcelCell(index = 44)
    private T columnAS;
    
    @ExcelCell(index = 45)
    private T columnAT;
    
    @ExcelCell(index = 46)
    private T columnAU;
    
    @ExcelCell(index = 47)
    private T columnAV;
    
    @ExcelCell(index = 48)
    private T columnAW;
    
    @ExcelCell(index = 49)
    private T columnAX;
    
    @ExcelCell(index = 50)
    private T columnAY;
    
    @ExcelCell(index = 51)
    private T columnAZ;

    @SuppressWarnings("unchecked")
    public ExcelModel(T... t) {
        init(t);  
    }

    @SuppressWarnings("unchecked")
    private void init(T... t) {
        if (t != null) {
            for (int i = 0; i < t.length; i++) {
                if (i == 0)
                    this.columnA = t[i];
                if (i == 1)
                    this.columnB = t[i];
                if (i == 2)
                    this.columnC = t[i];
                if (i == 3)
                    this.columnD = t[i];
                if (i == 4)
                    this.columnE = t[i];
                if (i == 5)
                    this.columnF = t[i];
                if (i == 6)
                    this.columnG = t[i];
                if (i == 7)
                    this.columnH = t[i];
                if (i == 8)
                    this.columnI = t[i];
                if (i == 9)
                    this.columnJ = t[i];
                if (i == 10)
                    this.columnK = t[i];
                if (i == 11)
                    this.columnL = t[i];
                if (i == 12)
                    this.columnM = t[i];
                if (i == 13)
                    this.columnN = t[i];
                if (i == 14)
                    this.columnO = t[i];
                if (i == 15)
                    this.columnP = t[i];
                if (i == 16)
                    this.columnQ = t[i];
                if (i == 17)
                    this.columnR = t[i];
                if (i == 18)
                    this.columnS = t[i];
                if (i == 19)
                    this.columnT = t[i];
                if (i == 20)
                    this.columnU = t[i];
                if (i == 21)
                    this.columnV = t[i];
                if (i == 22)
                    this.columnW = t[i];
                if (i == 23)
                    this.columnX = t[i];
                if (i == 24)
                    this.columnY = t[i];
                if (i == 25)
                    this.columnZ = t[i];
                if (i == 26)
                    this.columnAA = t[i];
                if (i == 27)
                    this.columnAB = t[i];
                if (i == 28)
                    this.columnAC = t[i];
                if (i == 29)
                    this.columnAD = t[i];
                if (i == 30)
                    this.columnAE = t[i];
                if (i == 31)
                    this.columnAF = t[i];
                if (i == 32)
                    this.columnAG = t[i];
                if (i == 33)
                    this.columnAH = t[i];
                if (i == 34)
                    this.columnAI = t[i];
                if (i == 35)
                    this.columnAJ = t[i];
                if (i == 36)
                    this.columnAK = t[i];
                if (i == 37)
                    this.columnAL = t[i];
                if (i == 38)
                    this.columnAM = t[i];
                if (i == 39)
                    this.columnAN = t[i];
                if (i == 40)
                    this.columnAO = t[i];
                if (i == 41)
                    this.columnAP = t[i];
                if (i == 42)
                    this.columnAQ = t[i];
                if (i == 43)
                    this.columnAR = t[i];
                if (i == 44)
                    this.columnAS = t[i];
                if (i == 45)
                    this.columnAT = t[i];
                if (i == 46)
                    this.columnAU = t[i];
                if (i == 47)
                    this.columnAV = t[i];
                if (i == 48)
                    this.columnAW = t[i];
                if (i == 49)
                    this.columnAX = t[i];
                if (i == 50)
                    this.columnAY = t[i];
                if (i == 51)
                    this.columnAZ = t[i];
            }
        }
    }

    /**
     * @return the columnA
     */
    public T getColumnA() {
        return columnA;
    }

    /**
     * @param columnA the columnA to set
     */
    public void setColumnA(T columnA) {
        this.columnA = columnA;
    }

    /**
     * @return the columnB
     */
    public T getColumnB() {
        return columnB;
    }

    /**
     * @param columnB the columnB to set
     */
    public void setColumnB(T columnB) {
        this.columnB = columnB;
    }

    /**
     * @return the columnC
     */
    public T getColumnC() {
        return columnC;
    }

    /**
     * @param columnC the columnC to set
     */
    public void setColumnC(T columnC) {
        this.columnC = columnC;
    }

    /**
     * @return the columnD
     */
    public T getColumnD() {
        return columnD;
    }

    /**
     * @param columnD the columnD to set
     */
    public void setColumnD(T columnD) {
        this.columnD = columnD;
    }

    /**
     * @return the columnE
     */
    public T getColumnE() {
        return columnE;
    }

    /**
     * @param columnE the columnE to set
     */
    public void setColumnE(T columnE) {
        this.columnE = columnE;
    }

    /**
     * @return the columnF
     */
    public T getColumnF() {
        return columnF;
    }

    /**
     * @param columnF the columnF to set
     */
    public void setColumnF(T columnF) {
        this.columnF = columnF;
    }

    /**
     * @return the columnG
     */
    public T getColumnG() {
        return columnG;
    }

    /**
     * @param columnG the columnG to set
     */
    public void setColumnG(T columnG) {
        this.columnG = columnG;
    }

    /**
     * @return the columnH
     */
    public T getColumnH() {
        return columnH;
    }

    /**
     * @param columnH the columnH to set
     */
    public void setColumnH(T columnH) {
        this.columnH = columnH;
    }

    /**
     * @return the columnI
     */
    public T getColumnI() {
        return columnI;
    }

    /**
     * @param columnI the columnI to set
     */
    public void setColumnI(T columnI) {
        this.columnI = columnI;
    }

    /**
     * @return the columnJ
     */
    public T getColumnJ() {
        return columnJ;
    }

    /**
     * @param columnJ the columnJ to set
     */
    public void setColumnJ(T columnJ) {
        this.columnJ = columnJ;
    }

    /**
     * @return the columnK
     */
    public T getColumnK() {
        return columnK;
    }

    /**
     * @param columnK the columnK to set
     */
    public void setColumnK(T columnK) {
        this.columnK = columnK;
    }

    /**
     * @return the columnL
     */
    public T getColumnL() {
        return columnL;
    }

    /**
     * @param columnL the columnL to set
     */
    public void setColumnL(T columnL) {
        this.columnL = columnL;
    }

    /**
     * @return the columnM
     */
    public T getColumnM() {
        return columnM;
    }

    /**
     * @param columnM the columnM to set
     */
    public void setColumnM(T columnM) {
        this.columnM = columnM;
    }

    /**
     * @return the columnN
     */
    public T getColumnN() {
        return columnN;
    }

    /**
     * @param columnN the columnN to set
     */
    public void setColumnN(T columnN) {
        this.columnN = columnN;
    }

    /**
     * @return the columnO
     */
    public T getColumnO() {
        return columnO;
    }

    /**
     * @param columnO the columnO to set
     */
    public void setColumnO(T columnO) {
        this.columnO = columnO;
    }

    /**
     * @return the columnP
     */
    public T getColumnP() {
        return columnP;
    }

    /**
     * @param columnP the columnP to set
     */
    public void setColumnP(T columnP) {
        this.columnP = columnP;
    }

    /**
     * @return the columnQ
     */
    public T getColumnQ() {
        return columnQ;
    }

    /**
     * @param columnQ the columnQ to set
     */
    public void setColumnQ(T columnQ) {
        this.columnQ = columnQ;
    }

    /**
     * @return the columnR
     */
    public T getColumnR() {
        return columnR;
    }

    /**
     * @param columnR the columnR to set
     */
    public void setColumnR(T columnR) {
        this.columnR = columnR;
    }

    /**
     * @return the columnS
     */
    public T getColumnS() {
        return columnS;
    }

    /**
     * @param columnS the columnS to set
     */
    public void setColumnS(T columnS) {
        this.columnS = columnS;
    }

    /**
     * @return the columnT
     */
    public T getColumnT() {
        return columnT;
    }

    /**
     * @param columnT the columnT to set
     */
    public void setColumnT(T columnT) {
        this.columnT = columnT;
    }

    /**
     * @return the columnU
     */
    public T getColumnU() {
        return columnU;
    }

    /**
     * @param columnU the columnU to set
     */
    public void setColumnU(T columnU) {
        this.columnU = columnU;
    }

    /**
     * @return the columnV
     */
    public T getColumnV() {
        return columnV;
    }

    /**
     * @param columnV the columnV to set
     */
    public void setColumnV(T columnV) {
        this.columnV = columnV;
    }

    /**
     * @return the columnW
     */
    public T getColumnW() {
        return columnW;
    }

    /**
     * @param columnW the columnW to set
     */
    public void setColumnW(T columnW) {
        this.columnW = columnW;
    }

    /**
     * @return the columnX
     */
    public T getColumnX() {
        return columnX;
    }

    /**
     * @param columnX the columnX to set
     */
    public void setColumnX(T columnX) {
        this.columnX = columnX;
    }

    /**
     * @return the columnY
     */
    public T getColumnY() {
        return columnY;
    }

    /**
     * @param columnY the columnY to set
     */
    public void setColumnY(T columnY) {
        this.columnY = columnY;
    }

    /**
     * @return the columnZ
     */
    public T getColumnZ() {
        return columnZ;
    }

    /**
     * @param columnZ the columnZ to set
     */
    public void setColumnZ(T columnZ) {
        this.columnZ = columnZ;
    }

    /**
     * @return the columnAA
     */
    public T getColumnAA() {
        return columnAA;
    }

    /**
     * @param columnAA the columnAA to set
     */
    public void setColumnAA(T columnAA) {
        this.columnAA = columnAA;
    }

    /**
     * @return the columnAB
     */
    public T getColumnAB() {
        return columnAB;
    }

    /**
     * @param columnAB the columnAB to set
     */
    public void setColumnAB(T columnAB) {
        this.columnAB = columnAB;
    }

    /**
     * @return the columnAC
     */
    public T getColumnAC() {
        return columnAC;
    }

    /**
     * @param columnAC the columnAC to set
     */
    public void setColumnAC(T columnAC) {
        this.columnAC = columnAC;
    }

    /**
     * @return the columnAD
     */
    public T getColumnAD() {
        return columnAD;
    }

    /**
     * @param columnAD the columnAD to set
     */
    public void setColumnAD(T columnAD) {
        this.columnAD = columnAD;
    }

    /**
     * @return the columnAE
     */
    public T getColumnAE() {
        return columnAE;
    }

    /**
     * @param columnAE the columnAE to set
     */
    public void setColumnAE(T columnAE) {
        this.columnAE = columnAE;
    }

    /**
     * @return the columnAF
     */
    public T getColumnAF() {
        return columnAF;
    }

    /**
     * @param columnAF the columnAF to set
     */
    public void setColumnAF(T columnAF) {
        this.columnAF = columnAF;
    }

    /**
     * @return the columnAG
     */
    public T getColumnAG() {
        return columnAG;
    }

    /**
     * @param columnAG the columnAG to set
     */
    public void setColumnAG(T columnAG) {
        this.columnAG = columnAG;
    }

    /**
     * @return the columnAH
     */
    public T getColumnAH() {
        return columnAH;
    }

    /**
     * @param columnAH the columnAH to set
     */
    public void setColumnAH(T columnAH) {
        this.columnAH = columnAH;
    }

    /**
     * @return the columnAI
     */
    public T getColumnAI() {
        return columnAI;
    }

    /**
     * @param columnAI the columnAI to set
     */
    public void setColumnAI(T columnAI) {
        this.columnAI = columnAI;
    }

    /**
     * @return the columnAJ
     */
    public T getColumnAJ() {
        return columnAJ;
    }

    /**
     * @param columnAJ the columnAJ to set
     */
    public void setColumnAJ(T columnAJ) {
        this.columnAJ = columnAJ;
    }

    /**
     * @return the columnAK
     */
    public T getColumnAK() {
        return columnAK;
    }

    /**
     * @param columnAK the columnAK to set
     */
    public void setColumnAK(T columnAK) {
        this.columnAK = columnAK;
    }

    /**
     * @return the columnAL
     */
    public T getColumnAL() {
        return columnAL;
    }

    /**
     * @param columnAL the columnAL to set
     */
    public void setColumnAL(T columnAL) {
        this.columnAL = columnAL;
    }

    /**
     * @return the columnAM
     */
    public T getColumnAM() {
        return columnAM;
    }

    /**
     * @param columnAM the columnAM to set
     */
    public void setColumnAM(T columnAM) {
        this.columnAM = columnAM;
    }

    /**
     * @return the columnAN
     */
    public T getColumnAN() {
        return columnAN;
    }

    /**
     * @param columnAN the columnAN to set
     */
    public void setColumnAN(T columnAN) {
        this.columnAN = columnAN;
    }

    /**
     * @return the columnAO
     */
    public T getColumnAO() {
        return columnAO;
    }

    /**
     * @param columnAO the columnAO to set
     */
    public void setColumnAO(T columnAO) {
        this.columnAO = columnAO;
    }

    /**
     * @return the columnAP
     */
    public T getColumnAP() {
        return columnAP;
    }

    /**
     * @param columnAP the columnAP to set
     */
    public void setColumnAP(T columnAP) {
        this.columnAP = columnAP;
    }

    /**
     * @return the columnAQ
     */
    public T getColumnAQ() {
        return columnAQ;
    }

    /**
     * @param columnAQ the columnAQ to set
     */
    public void setColumnAQ(T columnAQ) {
        this.columnAQ = columnAQ;
    }

    /**
     * @return the columnAR
     */
    public T getColumnAR() {
        return columnAR;
    }

    /**
     * @param columnAR the columnAR to set
     */
    public void setColumnAR(T columnAR) {
        this.columnAR = columnAR;
    }

    /**
     * @return the columnAS
     */
    public T getColumnAS() {
        return columnAS;
    }

    /**
     * @param columnAS the columnAS to set
     */
    public void setColumnAS(T columnAS) {
        this.columnAS = columnAS;
    }

    /**
     * @return the columnAT
     */
    public T getColumnAT() {
        return columnAT;
    }

    /**
     * @param columnAT the columnAT to set
     */
    public void setColumnAT(T columnAT) {
        this.columnAT = columnAT;
    }

    /**
     * @return the columnAU
     */
    public T getColumnAU() {
        return columnAU;
    }

    /**
     * @param columnAU the columnAU to set
     */
    public void setColumnAU(T columnAU) {
        this.columnAU = columnAU;
    }

    /**
     * @return the columnAV
     */
    public T getColumnAV() {
        return columnAV;
    }

    /**
     * @param columnAV the columnAV to set
     */
    public void setColumnAV(T columnAV) {
        this.columnAV = columnAV;
    }

    /**
     * @return the columnAW
     */
    public T getColumnAW() {
        return columnAW;
    }

    /**
     * @param columnAW the columnAW to set
     */
    public void setColumnAW(T columnAW) {
        this.columnAW = columnAW;
    }

    /**
     * @return the columnAX
     */
    public T getColumnAX() {
        return columnAX;
    }

    /**
     * @param columnAX the columnAX to set
     */
    public void setColumnAX(T columnAX) {
        this.columnAX = columnAX;
    }

    /**
     * @return the columnAY
     */
    public T getColumnAY() {
        return columnAY;
    }

    /**
     * @param columnAY the columnAY to set
     */
    public void setColumnAY(T columnAY) {
        this.columnAY = columnAY;
    }

    /**
     * @return the columnAZ
     */
    public T getColumnAZ() {
        return columnAZ;
    }

    /**
     * @param columnAZ the columnAZ to set
     */
    public void setColumnAZ(T columnAZ) {
        this.columnAZ = columnAZ;
    }

}

