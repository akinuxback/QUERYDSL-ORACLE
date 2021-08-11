package com.aki.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "EMP")
public class Emp {

 @Id
 @Column(name = "EMPNO")
 private Integer empno;

 @Column(name = "ENAME")
 private String ename;

 @Column(name = "JOB")
 private String job;

 @Column(name = "MGR")
 private Integer mgr;

 @Column(name = "HIREDATE")
 private LocalDate hiredate;

 @Column(name = "SAL")
 private Integer sal;

 @Column(name = "COMM")
 private Integer comm;

 @Column(name = "DEPTNO")
 private Integer deptno;

// @OneToOne
// @JoinColumn(name = "deptno")
// private Dept dept;

}
