package cn.ezeyc.core.enums;

/**
 * @author wz
 */
public enum ResultEnum {

    /**
     * 系统错误,请联系管理员
     */
    error(001,"系统错误,请联系管理员"),
    /**
     * 证书错误,请联系管理员
     */
    license(002,"证书错误,请联系管理员"),
    /**
     * 操作失败
     */
    OperateField(100,"操作失败"),
    /**
     * 操作成功
     */
    OK(200, "操作成功"),
    /**
     * 没有访问权限
     */
    noAuth(401,"没有访问权限"),
    /**
     * 权限不足,禁止访问
     */
    forbidden(403,"权限不足,禁止访问"),
    /**
     * 找不到请求
     */
    notfound(404,"找不到请求"),
    /**
     * 请求程序报错
     */

    actionError(500,"请求程序报错"),
    /**
     * 用户认证错误
     */
    //登录
    loginError(1001,"用户认证错误"),
    /**
     * 密码错误
     */
    loginPwdError(1002,"密码错误"),
    /**
     * 用户不存在
     */
    loginNameError(1003,"用户不存在"),
    /**
     * 缺少token
     */
    Authorization(1004,"缺少token"),
    /**
     * token失效
     */
    tokenError(1005,"token失效"),
    /**
     * sso服务地址未配置
     */
    ssoError(1100,"sso服务地址未配置"),
    /**
     * 验证码为空
     */
    picNull(2001,"验证码为空"),
    /**
     * 验证码错误
     */
    picError(2002,"验证码错误"),
    /**
     * 验证码失效
     */
    picTimeOut(2003,"验证码失效"),
    /**
     * 参数校验错误
     */
    verify(3001,"参数校验错误");

    private int code;
    private String message;

    ResultEnum() {
    }

     ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultEnum getResultEnum(int code) {
        for (ResultEnum type : ResultEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return error;
    }

    public static ResultEnum getResultEnum(String message) {
        for (ResultEnum type : ResultEnum.values()) {
            if (type.getMessage().equals(message)) {
                return type;
            }
        }
        return error;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}