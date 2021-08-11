package com.aki.oraclestd;

import com.aki.QDept;
import com.aki.entity.QEmp;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.aki.QDept.dept;
import static com.aki.entity.QEmp.emp;

@SpringBootTest
@Transactional
public class OracleStd_GroupBy_Having {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;


    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("17강 그룹 함수")
    public void chapt_17(){
        /**
         * ! select 문을 통해 가져올 결과를 그룹으로 묶고 그룹 내에서 지정된 컬럼의 총합, 평 균 등을 구할 수 있는 함수
         * ! sum : 총합 • avg : 평균 • count : 로우의 수 • max : 최대 값 • min : 최소 값그룹함수
         * • 사원들의 급여 총합을 구한다.
         * • 사원들의 커미션 총합을 구한다.
         * • 급여가 1500 이상인 사원들의 급여 총합을 구한다.
         * • 20번 부서에 근부하고 있는 사원들의 급여 총합을 구한다.
         * • 직무가 SALESMAN 인 사원들의 급여 총합 을 구한다.
         * */

        queryFactory.select(emp.sal.sum(), emp.comm.sum()).from(emp).fetch();
        queryFactory.select(emp.sal.sum()).from(emp).where(emp.sal.goe(1500)).fetch();
        queryFactory.select(emp.sal.sum()).from(emp).where(emp.deptno.eq(20)).fetch();
        queryFactory.select(emp.sal.sum()).from(emp).where(emp.job.eq("SALESMAN")).fetch();

        /**
         * • 직무가 SALESMAN 인 사원들의 이름과 급여총합을 가져온다. - 문제 자체가 오류인것
         * • 전 사원의 급여 평균을 구한다.
         * • 커미션을 받는 사원들의 커미션 평균을 구한다.
         * • 전 사원의 커미션의 평균을 구한다.
         * • 커미션을 받는 사원들의 급여 평균을 구한다.
         * */
        

         /**
         * • 30번 부서에 근무하고 있는 사원들의 급여 평균을 구한다.
         * • 직무가 SALESMAN 인 사원들의 급여 + 커 미션 평균을 구한다.
         * • 사원들의 총 수를 가져온다. • 커미션을 받는 사원들의 총 수를 가져온 다.
         * • 사원들의 급여 최대, 최소값을 가져온다.
         * */

    }

    @Test
    @DisplayName("18강 Group By")
    public void chapt_18(){
        /**
         * • 각 부서별 사원들의 급여 평균을 구한다.
         * • 각 직무별 사원들의 급여 총합을 구한다.
         * • 1500 이상 급여를 받는 사원들의 부서별 급여 평균을 구한다.
         * */
        queryFactory.select(emp.deptno, emp.sal.avg().intValue()).from(emp).groupBy(emp.deptno).fetch();
        queryFactory.select(emp.job, emp.sal.sum()).from(emp).groupBy(emp.job).fetch();
        queryFactory.select(emp.deptno, emp.sal.avg().intValue()).from(emp).where(emp.sal.goe(1500))
                .groupBy(emp.deptno).fetch();
    }

    @Test
    @DisplayName("19강 Having")
    public void chapt_19(){
        /**
         * ! Group by로 묶인 각 그룹들 중에 실제 가 져올 그룹을 선택할 조건을 having 절에 작 성한다.
         * ! Having 은 Group By 절의 조건이 된다.
         *
         *      [ Having ]
         *
         * • 부서별 평균 급여가 2000이상은 부서의 급여 평균을 가져온다.
         * • 부서별 최대 급여액이 3000이하인 부서의 급여 총합을 가져온다.
         * • 부서별 최소 급여액이 1000 이하인 부서에서 직무가 CLERK 인 사원들의 급여 총합을 구한다
         * • 각 부서의 급여 최소가 900이상 최대가 10000이하인 부서의 사원들 중1500이상 의
         *      급여를 받는 사원들의 평균 급여액을 가져온다.
         * */

        queryFactory.select(emp.deptno, emp.sal.avg().intValue()).from(emp)
                .groupBy(emp.deptno)
                .having(emp.sal.avg().goe(2000))
                .fetch();

        queryFactory.select(emp.deptno, emp.sal.sum()).from(emp)
                .groupBy(emp.deptno)
                .having(emp.sal.max().loe(3000))
                .fetch();

        queryFactory.select(emp.sal.sum()).from(emp)
                .where(emp.job.eq("CLERK"))
                .groupBy(emp.deptno)
                .having(emp.sal.min().loe(1000))
                .fetch();

        queryFactory.select(emp.deptno, emp.sal.avg().intValue()).from(emp)
                .where(emp.sal.goe(1500))
                .groupBy(emp.deptno)
                .having(emp.sal.min().between(900, 10000))
                .fetch();

    }



}
