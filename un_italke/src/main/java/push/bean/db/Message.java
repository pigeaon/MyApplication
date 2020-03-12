package push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 信息
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    public static final int TYPE_STR = 1;//字符串类型
    public static final int TYPE_PIC = 2;//图片类型
    public static final int TYPE_FILE = 3;//文件类型
    public static final int TYPE_AUDIO = 4;//语音类型

    //主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储的UUID
    //@GeneratedValue(generator = "uuid")
    //这里不自动生成主键，由客户端提供
    //uuid生成器的定义为uuid2，
    @GenericGenerator(name="uuid",strategy = "uuid2")
    //主键不允许更改，不允许为null
    @Column(updatable = false,nullable = false)
    private String id;

    //内容，类型为text
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    //附件
    @Column
    private String attach;

    //消息类型
    @Column(nullable = false)
    private int type;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //发送者 多个消息对应一个发送者
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;
    //外键
    @Column(nullable = false,updatable = false,insertable = false)
    private String senderId;

    //接收者 多个消息对应一个接收者
    @JoinColumn(name = "receiveId")
    @ManyToOne(optional = true)
    private User receiver;
    //外键
    @Column(nullable = false,updatable = false,insertable = false)
    private String receiverId;

    //一个群可以接受多个消息
    @JoinColumn(name = "groupId")
    @ManyToOne(optional = true)
    private Group group;
    //外键
    @Column(nullable = false,updatable = false,insertable = false)
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
