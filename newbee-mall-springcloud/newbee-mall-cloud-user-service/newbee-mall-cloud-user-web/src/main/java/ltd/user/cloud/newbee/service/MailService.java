package ltd.user.cloud.newbee.service;

public interface MailService {
    /**
     * 简单文本邮件
     * @param toUser 邮件接收者
     */
    void simpleMail(String toUser)throws Exception;
}
