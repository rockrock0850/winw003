package com.ebizprise.winw.project.enums.form.approve;

/**
 * 表單流程-申請流程-表單送出時,根據登入者角色,顯示其對應職位的文字敘述
 * @author andrew.lee
 * @version 1.0, Created at 2020年3月23日
 */
public enum FormApproveApplyEnum {

    PIC("送交經辦","呈核副科長","呈核科長"),//經辦
    VSC("送交經辦","代科長核准","呈核科長"),//副科長
    SC("送交經辦","科長核准","申請核准"),//科長
    
    PIC_BACK("退回經辦","返回副科長","返回給科長"),//退回經辦
    VSC_BACK("退回經辦","副科長退回","返回給科長"),//退回副科長
    SC_BACK("退回經辦","退回副科長","科長退回");//退回科長
    
    private String pic;
    private String vsc;
    private String sc;
    
    private FormApproveApplyEnum (String pic,String vsc,String sc) {
        this.pic = pic;
        this.vsc = vsc;
        this.sc = sc;
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
    
}
