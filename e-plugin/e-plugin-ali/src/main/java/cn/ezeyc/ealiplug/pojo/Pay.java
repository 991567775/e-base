package cn.ezeyc.ealiplug.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 支付信息实体
 */
@Getter
@Setter
public class Pay {

    /**
     * id
     */
    private  String id;
    /**
     * 交易号支付宝支付回调返回
     */
    private  String tradeNo;
    /**
     * 系统订单号[自己生成]
     */
    private  String outTradeNo;
    /**
     * 支付金额
     */
    private BigDecimal totalAmount;
    /**
     * 商品信息
     */
    private  String subject;
    /**
     * 支付人
     */
    private  String buyerId;


}
