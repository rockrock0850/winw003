package com.ebizprise.winw.project.enums;

/**
 * @author gary.tsai 2019/6/19
 */
public enum LDAPGroupEnum {

    OTHERS("OTHERS", "0"),
    SP_NT("SP_NT", "1"),
    SP_DC("SP_DC", "1"),
    SP_R6("SP_R6", "1"),
    SP_MQ("SP_MQ", "1"),
    SP_MVS("SP_MVS", "1"),
    SP_IMS("SP_IMS", "1"),
    SP_OP("SP_OP", "1"),
    SP_AS400MVS("SP_AS400MVS", "1"),
    ISWP_MGR("ISWP-MGR", "2"),
    ISWP_USR1("ISWP-USR", "1"),
    ISWP_USR("ISWP-USR", "經辦"),
    ISWP_MGR1("ISWP-MGR1", "副科長"),
    ISWP_MGR2("ISWP-MGR2", "科長"),
    ISWP_MGR3("ISWP-MGR3", "副理"),
    ISWP_MGR4("ISWP-MGR4", "協理");

    public String key;
    public String level;

    /**
     * key 為 LDAP 使用所屬的群組名稱
     * level 為群組的權限，1為經辦2為審核
     * @param key
     * @param level
     */
    LDAPGroupEnum (String key, String level) {
        this.key = key;
        this.level = level;
    }
    
    public String key () {
        return this.key;
    }

    public static String getLevel(String key) {
        for (LDAPGroupEnum sysGroupEnum : values()) {
            if (sysGroupEnum.key.equalsIgnoreCase(key)) {
                return sysGroupEnum.level;
            }
        }
        return "0";
    }
    
}
