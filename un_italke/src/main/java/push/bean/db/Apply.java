package push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 申请记录表
 */
@Entity
@Table(name = "TB_APPLY")
public class Apply {
    private static final int TYPE_ADD_USER = 1;//添加用户
    private static final int TYPE_ADDGROUP = 2;//添加群

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

    //申请描述
    @Column(nullable = false)
    private String description;

    //附件
    @Column(columnDefinition = "TEXT")
    private String attach;

    //当前申请类型
    @Column(nullable = false)
    private int type;

    //目标ID
    //User.id 或 Group.id
    @Column(nullable = false)
    private String targetId;

    //申请人
    //为空时为系统人员
    @ManyToOne()
    @JoinColumn(name = "applicantId")
    private User applicant;
    @Column(updatable = false,insertable = false)
    private String applicantId;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
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
