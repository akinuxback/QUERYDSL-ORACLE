package com.aki;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
 private Long deptno;

 @Column(name = "DNAME")
 private String dname;

 @Column(name = "LOC")
 private String loc;

}
