package com.example.springdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "empno")
    private Integer empno;

    @Column(name = "ename")
    private String ename;

    @Column(name = "job")
    private String job;

    @Column(name = "mgr")
    private Integer mgr;

    @Column(name = "hiredate")
    private LocalDate hiredate;

    @Column(name = "sal")
    private Double sal;

    @Column(name = "comm")
    private Double comm;

    @ManyToOne
    @JoinColumn(name = "deptno")
    private Department department;
}