package com.ebizprise.winw.project.enums;

/**
 * 系統共用的參數值
 * @author gary.tsai 2019/6/12
 */
public enum SysCommonEnum {
    
    YES("1"),
    NO("0"),
    INSERT("I"),
    UPDATE("U"),
    DELETE("D"),
    
    /*
     * 表單資訊頁簽-需求等級對照表
     * @author adam.yeh
     */
    
    // 需求等級/問題優先順序
    ORI_DEMAND_1(1, 1, 1),
    ORI_DEMAND_2(1, 2, 2),
    ORI_DEMAND_3(1, 3, 2),
    ORI_DEMAND_4(2, 1, 1),
    ORI_DEMAND_5(2, 2, 2),
    ORI_DEMAND_6(2, 3, 3),
    ORI_DEMAND_7(3, 1, 2),
    ORI_DEMAND_8(3, 2, 3),
    ORI_DEMAND_9(3, 3, 3),

    // 事件優先順序
    INC_DEMAND_1(1, 1, 1),
    INC_DEMAND_2(1, 2, 2),
    INC_DEMAND_3(1, 3, 3),
    INC_DEMAND_4(2, 1, 2),
    INC_DEMAND_5(2, 2, 2),
    INC_DEMAND_6(2, 3, 3),
    INC_DEMAND_7(3, 1, 3),
    INC_DEMAND_8(3, 2, 3),
    INC_DEMAND_9(3, 3, 4),
    
    // 表單資訊頁簽-系統名稱清單的系統代號對應表
    SR("SR"),
    SR_C("SR"),
    Q("PROBLEM"),
    Q_C("PROBLEM"),
    INC("INCIDENT"),
    INC_C("INCIDENT"),
    
    /*
     * 表單資訊頁簽-檔案型態(用於區分FORM_FILE裡面的檔案)
     * @author adam.yeh
     */
    /** DB變更頁簽裡面的附件 */
    DB,
    /** 檔案上傳 */
    FILE;

    private static final Integer[][] ORI_DEMAND_LEVEL = new Integer[4][4];
    private static final Integer[][] INC_DEMAND_LEVEL = new Integer[4][4];
    
	private String symbol;
	private Integer effect;
	private Integer urgent;
	private Integer demand;
    
    static {
        for (SysCommonEnum e : values()) {
            if (e.name().indexOf("ORI_DEMAND") >= 0) {
                ORI_DEMAND_LEVEL[e.effect][e.urgent] = e.demand;
            }
            
            if (e.name().indexOf("INC_DEMAND") >= 0) {
                INC_DEMAND_LEVEL[e.effect][e.urgent] = e.demand;
            }
        }
    }
    
    /**
     * 取得需求等級
     * 
     * @param effect
     * @param urgent
     * @return
     * @author adam.yeh
     */
    public static Integer valueOfOriDemands (Integer effect, Integer urgent) {
        return ORI_DEMAND_LEVEL[effect][urgent];
    }
    
    public static Integer valueOfIncDemands (Integer effect, Integer urgent) {
        return INC_DEMAND_LEVEL[effect][urgent];
    }

    private SysCommonEnum () {
    }

	private SysCommonEnum (String symbol) {
		this.symbol = symbol;
	}

    private SysCommonEnum (Integer effect, Integer urgent, Integer demand) {
        this.effect = effect;
        this.urgent = urgent;
        this.demand = demand;
    }
	
    /**
     * 參數代號
     * 
     * @return
     */
	public String symbol () {
	    return this.symbol;
	}
    
}
