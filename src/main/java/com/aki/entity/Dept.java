package com.aki.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DEPT")
public class Dept{

 @Id
 @Column(name = "DEPTNO")
 private Integer deptno;

 @Column(name = "DNAME")
 private String dname;

 @Column(name = "LOC")
 private String loc;

 @OneToMany(mappedBy = "dept")
 private List<Emp> emp;

}
