package vn.demo.jobhunter.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.demo.jobhunter.util.constant.LevelEnum;
import vn.demo.jobhunter.util.error.SecurityUtil;

@Entity
@Table(name = "jobs")
@Setter
@Getter
public class job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String name;
    private double salary;
    private int quantity;
    private LevelEnum level;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean isActive;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore // Tránh vòng lặp khi trả JSON
    @JoinTable(name = "job_skill", // Xác định bảng trung gian giữa Job và Skill, tên là job_skill.
            joinColumns = @JoinColumn(name = "job_id"), // Khóa ngoại từ bảng trung gian liên kết tới bảng job
            inverseJoinColumns = @JoinColumn(name = "skill_id")) // Khóa ngoại từ bảng trung gian liên kết tới bảng
                                                                 // skill.
    private List<Skill> skills;

    @PrePersist
    public void handleBeForeCreate() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updateAt = Instant.now();
    }

    @PreUpdate
    public void handleBeForeUpdate() {
        this.updateBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updateAt = Instant.now();
    }

}
