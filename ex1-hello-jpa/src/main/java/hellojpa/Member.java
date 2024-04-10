package hellojpa;


import jakarta.persistence.*;

import java.io.PipedReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "MEMBER_SEQ_GENERATOR"
    )
    private Long id;

    @Column(name = "username", insertable = false, nullable = true, unique = false, columnDefinition = "varchar(100)")
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIME)
    private Date time;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    private LocalDate localDate;

    private LocalDateTime localDateTime;

    @Lob
    private String description;

    @Lob
    private String movieStream;
    @Transient
    private Integer forMemory;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getForMemory() {
        return forMemory;
    }

    public void setForMemory(Integer forMemory) {
        this.forMemory = forMemory;
    }
}

