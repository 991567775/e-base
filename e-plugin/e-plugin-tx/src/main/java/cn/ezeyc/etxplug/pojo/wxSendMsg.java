package cn.ezeyc.etxplug.pojo;


import lombok.Getter;
import lombok.Setter;
import org.noear.snack.ONode;

/**
 * 微信消息推送实体
 */
@Getter
@Setter
public class wxSendMsg {
    /**
     * token
     */
    private String accessToken;
    private String touser;
    private String templateId;
    private String page;
    private String lang="zh_CN";
    private String miniprogramState="formal";
    private ONode data;//推送文字

  }
