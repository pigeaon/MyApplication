package push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PUSHHISTORY")
public class PushHistory {
    //主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储的UUID
    @GeneratedValue(generator = "uuid")
    //uuid生成器的定义为uuid2，
    @GenericGenerator(name="uuid",strategy = "uuid2")
    //主键不允许更改，不允许为null
    @Column(updatable = false,nullable = false)
    private String id;

    //推送具体实现的存储是JSON字符串
    //BLOB是比TEXT更多的一个大字段类型
    @Lob
    @Column(nullable = false,columnDefinition = "BLOB")
    private String entity;

    //推送的实体类型
    @Column(nullable = false)
    private int entityType;

    //接收者
    //接收者不允许为空
    //一个接收者可以接受很多推送消息
    //FetchType。EAGER 加载信息时，加载用户信息
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Column(nullable = false,updatable = false,insertable = false)
    private String receiverId;

    //User.pushId 可为空
    @Column
    private String receiverPushId;

    //发送者者
    //发送者允许为空
    //一个发送者可以接受很多推送消息
    //FetchType。EAGER 加载信息时，加载用户信息
    @ManyToOne(optional = true,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "senderId")
    private User sender;
    @Column(nullable = false,updatable = false,insertable = false)
    private String senderId;

    //User.pushId 可为空
    @Column
    private String senerPushId;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //消息送达时间，可为空
    @Column
    private LocalDateTime arriveAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
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

    public String getReceiverPushId() {
        return receiverPushId;
    }

    public void setReceiverPushId(String receiverPushId) {
        this.receiverPushId = receiverPushId;
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

    public String getSenerPushId() {
        return senerPushId;
    }

    public void setSenerPushId(String senerPushId) {
        this.senerPushId = senerPushId;
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

    public LocalDateTime getArriveAt() {
        return arriveAt;
    }

    public void setArriveAt(LocalDateTime arriveAt) {
        this.arriveAt = arriveAt;
    }
}
