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
 private Long empNo;

 @Column(name = "ENAME")
 private String ename;

 @Column(name = "JOB")
 private String job;

 @Column(name = "MGR")
 private Long mgr;

 @Column(name = "HIREDATE")
 private LocalDate hiredate;

 @Column(name = "SAL")
 private Long sal;

 @Column(name = "COMM")
 private Long comm;

 @Column(name = "DEPTNO")
 private Long deptno;

}
