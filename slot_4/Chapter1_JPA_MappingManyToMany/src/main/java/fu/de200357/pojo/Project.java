package fu.de200357.pojo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_code", unique = true, nullable = false)
    private String projectCode;

    private String projectName;

    private BigDecimal budget;

    private LocalDate startDate;

    private LocalDate endDate; // có thể null nếu chưa kết thúc

    // TODO 5.3 — inverse side
    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new HashSet<>();

    public Project() {}

    public Project(String projectCode, String projectName, BigDecimal budget, LocalDate startDate) {
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.budget = budget;
        this.startDate = startDate;
    }

    public Long getId() { return id; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Set<Employee> getEmployees() { return employees; }

    // TODO 5.4
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project p = (Project) o;
        return projectCode != null && projectCode.equals(p.projectCode);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return projectName + " (" + projectCode + ")";
    }
}