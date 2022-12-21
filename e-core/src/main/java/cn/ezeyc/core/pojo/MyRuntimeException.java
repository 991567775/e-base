package cn.ezeyc.core.pojo;



import java.io.Serializable;

/**
 * 自定义异常
 * @author wz
 */
public class MyRuntimeException extends RuntimeException implements Serializable {

    /**
     * 附带数据
     * */
    private Object data;

    /**
     * 数据获取
     */
    public Object data() {
        return data;
    }

    /**
     * 数据设定
     */
    public MyRuntimeException data(Object data) {
        this.data = data;
        return this;
    }


    public MyRuntimeException() {
        super();
    }

    public MyRuntimeException(Throwable cause) {
        super(cause);
    }

    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
