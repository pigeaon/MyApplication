package push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_GROUP")
public class Group {
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

    //群名称
    @Column(nullable = false)
    private String name;
    //群描述
    @Column(nullable = false)
    private String description;
    //图片
    @Column
    private String picture;


    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //群的创建者
    //optional  必须有一个创建者
    //fetch：加载方式为FecthType.EAGER,急加载
    //意味着加载群信息的时候必须加载owner的信息
    //cascade:联级级别为ALL，所有的更改都将进行关系更新
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;
    private String ownerId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}