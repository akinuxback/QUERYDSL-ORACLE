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
@Table(name = "BONUS")
public class Bonus implements Serializable {

 @Id
 @Column(name = "id")
 private Long id;

 @Column(name = "ENAME")
 private String ename;

 @Column(name = "JOB")
 private String job;

 @Column(name = "SAL")
 private Long sal;

 @Column(name = "COMM")
 private Long comm;

}
