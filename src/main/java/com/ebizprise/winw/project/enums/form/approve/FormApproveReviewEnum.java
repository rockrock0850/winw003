package com.ebizprise.winw.project.enums.form.approve;

/**
 * 表單流程-審核流程-表單送出時,根據登入者角色,顯示其對應職位的文字敘述
 * @author andrew.lee
 * @version 1.0, Created at 2020年3月23日
 */
public enum FormApproveReviewEnum {

    PIC("送交經辦","呈核副科長","呈核科長","呈核給副理","呈核給協理"),//經辦
    VSC("送交經辦","代科長審核核准","呈核科長","呈核給副理","呈核給協理"),//副科長
    SC("送交經辦","科長核准","申請核准","呈核給副理","呈核給協理"),//科長
    DIRECT1("送交經辦","送交副科","送交科長","副理簽核","呈核給協理"),//副理
    DIRECT2("送交經辦","送交副科","送交科長","送交副理","協理核准"),//協理
    
    PIC_BACK("退回經辦","返回副科長","返回給科長","返回副理","返回協理"),//退回經辦
    VSC_BACK("退回經辦","代科長退回","返回給科長","返回副理","返回協理"),//退回副科長
    SC_BACK("退回經辦","退回副科","科長退回","返回副理","返回協理"),//退回科長
    DIRECT1_BACK("退回經辦","退回副科長","退回科長","副理退回","返回協理"),//副理
    DIRECT2_BACK("退回經辦","退回副科長","退回科長","退回副理","協理退回");//協理
    
    private String pic;
    private String vsc;
    private String sc;
    private String direct1;
    private String direct2;
    
    private FormApproveReviewEnum (String pic,String vsc,String sc,String direct1,String direct2) {
        this.pic = pic;
        this.vsc = vsc;
        this.sc = sc;
        this.direct1 = direct1;
        this.direct2 = direct2;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getVsc() {
        return vsc;
    }

    public void setVsc(String vsc) {
        this.vsc = vsc;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getDirect1() {
        return direct1;
    }

    public void setDirect1(String direct1) {
        this.direct1 = direct1;
    }

    public String getDirect2() {
        return direct2;
    }

    public void setDirect2(String direct2) {
        this.direct2 = direct2;
    }
    
}
