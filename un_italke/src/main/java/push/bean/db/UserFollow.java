package push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系的model
 * 用于用户直接进行好友关系的实现
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {
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

    //定义一个发起者用户，用户关注某人，此处的origin即为此用户
    //多对一，用户关注很多人，每一次关注为一条记录
    //即User对应多个UserFollow
    @ManyToOne
    //定义关联度表字段名为originId，对应的是User.id
    @JoinColumn(name = "originId")
    private User origin;
    @Column(nullable = false,updatable = false,insertable = false)
    private String originId;

    //定义关注的目标
    //多对一，用户可以被很多人，每一次关注为一条记录
    @ManyToOne
    //定义关联的表字段名为target，对应的是User.id
    private User target;
    //此列提取到Model中
    @Column(nullable = false,updatable = false,insertable = false)
    private String targetId;

    //备注
    @Column
    private String alias;

    //定义创建时间戳 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    //更新创建时间戳 更新时写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

}
