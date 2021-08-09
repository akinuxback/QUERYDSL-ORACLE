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
@Table(name = "SALGRADE")
public class Salgrade implements Serializable {

 @Id
 @Column(name = "GRADE")
 private Long grade;

 @Column(name = "LOSAL")
 private Long losal;

 @Column(name = "HISAL")
 private Long hisal;

}
