package com.aki.oraclestd;

import com.aki.QDept;
import com.aki.entity.QEmp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.aki.QDept.dept;
import static com.aki.entity.QEmp.emp;

@SpringBootTest
@Transactional
public class OracleStd_Join {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("20강 조인(join)")
    public void chapt_20(){
        /**
         * • 사원의 사원번호, 이름, 근무부서 이름을 가져온다.
         * • 사원의 사원번호, 이름, 근무지역을 가져온다.
         * • DALLAS 에 근무하고 있는 사원들의 사원번호, 이름, 직무를 가져온다.
         * • SALES 부서에 근무하고 있는 사원들의 급여 평균을 가져온다.
         * • 1982년에 입사한 사원들의 사원번호, 이름, 입사일, 근무부서이름을 가져온다.
         * • 각 사원들의 사원번호, 이름, 급여, 급여등급을 가져온다.
         * • SALES 부서에 근무하고 있는 사원의 사원 번호, 이름, 급여등급을 가져온다.
         * • 각 급여 등급별 급여의 총합과 평균, 사원 의수, 최대급여, 최소급여를 가져온다.
         * • 급여 등급이 4등급인 사원들의 사원번호, 이름, 급여, 근무부서이름, 근무지역을 가 져온다.
         * */


    }

}
