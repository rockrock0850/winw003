package com.ebizprise.winw.project.enums;

/**
 * @author gary.tsai 2019/5/31
 */
public enum UserEnum {
    
	SYSTEM("system", ""),
	ADMIN("admin", "-ADM"),
	SYS_CONFIGURATION("系統科_組態人員", "CFG"),
	DEPUTY_MANAGER("副理", "Direct1"),
	ASSISTANT_MANAGER("協理", "Direct2"),
	DIVISION_CHIEF("科長", "SC"),
	VICE_DIVISION_CHIEF("副科長", "VSC"),
    DELEGATE_MANAGER("襄理", "VSC"),
	COORDINATOR("專員", ""),
	
	PIC("經辦", "表單處理完成"),
    VSC("副科", "代科長簽核"),
    SC("科長", "科長簽核"),
    DIRECT("", ""),
    DIRECT1("副理", "副理簽核"),
    DIRECT2("協理", "協理簽核"),
    MANAGER("協/副理", "Manager"),
    
    /**
     * 資料管制科的同步符號
     */
    DC("A01421", "CN=SC-"),
    
    /**
     * 經辦
     */
    USR("", "ISWP-USR"),

    /**
     * 副科
     */
    MGR1("VSC", "ISWP-MGR1"),

    /**
     * 科長
     */
    MGR2("SC", "ISWP-MGR2"),

    /**
     * 副理
     */
    MGR3("Direct1", "ISWP-MGR3"),

    /**
     * 協理
     */
    MGR4("Direct2", "ISWP-MGR4");

	private String name;
	private String symbol;

	UserEnum(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}
	
	/**
	 * 使用者群組文字
	 * 
	 * @return
	 */
	public String wording () {
	    return this.name;
	}
	
	/**
	 * 使用者群組代號
	 * 
	 * @return
	 */
	public String symbol () {
	    return this.symbol;
	}
	
}
