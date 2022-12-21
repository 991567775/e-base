package cn.ezeyc.core.enums;

/**
 * @author wz
 */
public enum OfficeType {
    /**
     * 数据类型
     */
    PDF("pdf"),
    HTML("html");

    private String val;


    OfficeType(String val) {
        this.val = val;
    }


    public String getValue() {
        return val;
    }


}
