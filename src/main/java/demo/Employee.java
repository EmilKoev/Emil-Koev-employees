package demo;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {

    private String empId;
    private String projectId;
    private LocalDate startAt;
    private LocalDate endAt;

    Employee(String empId, String projectId, LocalDate startAt, LocalDate endAt){
        this.empId = empId;
        this.projectId = projectId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public String getEmpId() {
        return empId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(empId, employee.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    @Override
    public String toString() {
        return empId;
    }
}
