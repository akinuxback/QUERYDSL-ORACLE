package com.aki.oraclestd;

import com.aki.entity.Emp;
import com.aki.entity.QEmp;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.hibernate.dialect.function.SQLFunctionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.aki.QDept.dept;
import static com.aki.entity.QEmp.emp;

@SpringBootTest
@Transactional(readOnly = true)
@Log4j2
public class OracleStd {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("select 기본")
    public void select_chapt_1(){
        /**
         * -- 부서의 모든 정보를 가져온다.
         * */
        queryFactory.selectFrom(dept).fetch();

        /**
        * -- 사원의 모든 정보를 가져온다.
         * */
        queryFactory.selectFrom(emp).fetchResults();

        /**
         * -- 사원의 이름과 사원번호를 가져온다.
         * */
        queryFactory.select(emp.ename, emp.empno).from(emp).fetch();

        /**
         * -- 부서 번호와 부서 이름을 가져온다.
         * */
        queryFactory.select(dept.deptno, dept.dname).from(dept).fetch();

    }

    @Test
    @DisplayName("7강 연산자 사용하기")
    public void chapt_7(){
        /**
         * ! [ 사진 연산 ]
         * -- 각 사원들의 급여액과 급여액에서 1000을 더한 값, 200을 뺀 값, 2를 곱한 값, 2로 나 눈 값을 가져온다.
         * -- 각 사원의 급여액, 커미션, 급여 + 커미션 액수를 가져 온다.
         * */
        queryFactory.select(emp.sal, emp.sal.add(1000), emp.sal.subtract(200), emp.sal.multiply(2), emp.sal.divide(2)).from(emp).fetch();
        queryFactory.select(emp.sal, emp.comm, emp.sal.add(emp.comm)).from(emp).fetch();
        // null 값 0으로 만들기, 오라클에선 nvl(컬럼, 0) -> .coalesce(0)
        queryFactory.select(emp.sal, emp.comm.coalesce(0), emp.sal.add(emp.comm)).from(emp).fetch();

        /**
         * ! [ Concat 연산자 ]
         * • 문자열을 합치는 연산자이다.
         * • 문자열 || 컬럼 || 문자열 || 컬럼
         * -- 사원들의 이름과 직무를 다음 양식으로 가져온다.
         *      -> ooo 사원의 담당 직무는 xxx 입니다.
         * */
        queryFactory.select(emp.ename.concat(" 사원의 담당 직무는 ").concat(emp.job).concat(" 입니다.")).from(emp).fetch();

        /**
         * ! [ distinct ]
         * • Select 문을 통해 가져온 모든 로우 중에서 중복된 로우를 제거하는 키워드
         * • Select distinct 컬럼명 from 테이블명
         * -- 사원들이 근무하고 있는 근무 부서의 번호를 가져온 다.
         * */
        queryFactory.select(emp.deptno).distinct().from(emp).fetch();

    }

    @Test
    @DisplayName("8강 조건절 사용하기")
    public void chapt_8(){
        /**
         * -- 근무 부서가 10번인 사원들의 사원번호, 이름, 근무 부서를 가져온다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.job, emp.deptno).from(emp).where(emp.deptno.eq(10)).fetch();

        /**
         * • 이름이 SCOTT 사원의 사원번호, 이름, 직 무, 급여를 가져온다.
         * • 직무가 SALESMAN 인 사원의 사원번호, 이름, 직무를 가져온다.
         * • 직무가 CLERK 이 아닌 사원의 사원번호, 이름, 직무를 가져온다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.job, emp.sal).from(emp).where(emp.ename.eq("SMITH")).fetchOne();
        queryFactory.select(emp.empno, emp.ename, emp.job).from(emp).where(emp.job.eq("SALESMAN")).fetch();
        queryFactory.select(emp.empno, emp.ename, emp.job).from(emp).where(emp.job.ne("CLERK")).fetch();

        /**
         * • 1982년 1월 1일 이후에 입사한 사원의 사 원번호, 이름, 입사일을 가져온다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.hiredate).from(emp).where(emp.hiredate.goe(LocalDate.parse("1982-01-01"))).fetch();

    }

    @Test
    @DisplayName("9강 논리 연산자 사용하기")
    public void chapt_9(){
        /**
         * • 10번 부서에서 근무하고 있는 직무가 MANAGER 인 사원의 사원번호, 이름, 근 무부서, 직무를 가져온다.
         * • 입사년도가 1981년인 사원중에 급여가 1500 이상인 사원의 사원번호, 이름, 급 여, 입사일을 가져온다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.deptno, emp.job).from(emp)
                .where(emp.deptno.eq(10).and(emp.job.eq("MANAGER"))).fetch();

        queryFactory
            .select(emp.empno, emp.ename, emp.sal, emp.hiredate)
            .from(emp)
            .where(
                emp.hiredate.goe(LocalDate.parse("1981-01-01"))
                .and(emp.hiredate.loe(LocalDate.parse("1981-12-31")))
                .and(emp.sal.goe(1500))
            )
        .fetch();

        /**
         * • 20번 부서에 근무하고 있는 사원 중에 급 여가 1500 이상인 사원의 사원번호, 이름, 부서번호, 급여를 가져온다.
         * • 직속상관 사원 번호가 7698번인 사원중에 직무가 CLERK 인 사원의 사원번호, 이름, 직속상관번호, 직무를 가져온다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.deptno, emp.sal).from(emp)
                .where(emp.deptno.eq(20).and(emp.sal.goe(1500))).fetch();

        queryFactory.select(emp.empno, emp.ename, emp.mgr, emp.job).from(emp)
                .where(emp.mgr.eq(7698).and(emp.job.eq("CLERK"))).fetch();
        /**
         * • 급여가 2000보다 크거나 1000보다 작은 사원의 사원번호, 이름, 급여를 가져온다.
         * • 부서번호가 20이거나 30인 사원의 사원번 호, 이름, 부서번호를 가져온다.
         * • 직무가 CLERK, SALESMAN, ANALYST 인 사원의 사원번호, 이름, 직무를 가져온다.
         * */

        queryFactory.select(emp.empno, emp.ename, emp.sal).from(emp)
                .where(emp.sal.gt(2000).or(emp.sal.lt(1000))).fetch();

        // not between - 1000 과 2000 사이가 아닌것
        queryFactory.select(emp.empno, emp.ename, emp.sal).from(emp)
                .where(emp.sal.notBetween(1000, 2000)).fetch();

        queryFactory.select(emp.empno, emp.ename, emp.deptno).from(emp)
                .where(emp.deptno.eq(20).or(emp.deptno.eq(30))).fetch();

        queryFactory.select(emp.empno, emp.ename, emp.job).from(emp)
                .where(emp.job.eq("CLERK").or(emp.job.eq("SALESMAN")).or(emp.job.eq("ANALYST"))).fetch();

        // in 사용 뭐뭐 이거나 뭐뭐 이거나 뭐뭐 인거.....
        queryFactory.select(emp.empno, emp.ename, emp.job).from(emp)
                .where(emp.job.in("CLERK", "SALESMAN", "ANALYST")).fetch();

        /**
         * • 사원 번호가 7499, 7566, 7839가 아닌 사원들의 사원번호, 이름을 가져온다.
         * */

        queryFactory.select(emp.empno, emp.ename).from(emp)
                .where(emp.empno.notIn(7499, 7566, 7839)).fetch();

    }

    @Test
    @DisplayName("10강 Like 연산자")
    public void chapt_10(){

        /**
         * • 이름이 F로 시작하는 사원의 이름과 사원 번호를 가져온다.
         * • 이름이 S로 끝나는 사원의 이름과 사원 번 호를 가져온다.
         * */

        queryFactory.selectFrom(emp).where(emp.ename.startsWith("F").or(emp.ename.endsWith("S"))).fetch();


        /**
         * • 이름에 A가 포함되어 있는 사원의 이름과 사원 번호를 가져온다.
         * • 이름의 두번째 글자가 A 인 사원의 사원 이름, 사원 번호를 가져온다.
         * • 이름이 4글자인 사원의 사원 이름, 사원 번호를 가져온다.
         * */
        queryFactory.selectFrom(emp).where(emp.ename.like("%A%")).fetch();
        queryFactory.selectFrom(emp).where(emp.ename.like("_A%")).fetch();
        queryFactory.selectFrom(emp).where(emp.ename.like("____")).fetch();

    }

    @Test
    @DisplayName("11강 null")
    public void chapt_11(){
        /**
         * ! null은 정해져 있지 않은 값 혹은 무한대의 의미를 갖는 값이다.
         * ! 이 때문에 =이나 <> 를 통해 컬럼의 값이 null 인지 연산을 할 수가 없다.
         * ! 이 때, is null 이나 is not null 을 통해 null 비교가 가능하다.null 비교
         *
         * • 사원중에 커미션을 받지 않는 사원의 사원 번호, 이름, 커미션을 가져온다.
         * • 회사 대표(직속상관이 없는 사람)의 이름 과 사원번호를 가져온다.
         * */

        queryFactory.selectFrom(emp).where(emp.comm.isNull()).fetch();
        queryFactory.selectFrom(emp).where(emp.mgr.isNull()).fetch();

    }

    @Test
    @DisplayName("12강 정렬")
    public void chapt_12(){

        /**
         * • 사원의 사원번호, 이름, 급여를 가져온다. 급여를 기준으로 오름차순 정렬을 한다.
         * • 사원의 사원번호, 이름, 급여를 가져온다. 사원번호를 기준으로 내림차순 정렬을 한 다.
         * */
        queryFactory.select(emp.empno, emp.ename, emp.sal).from(emp).orderBy(emp.sal.asc()).fetch();
        queryFactory.select(emp.empno, emp.ename, emp.sal).from(emp).orderBy(emp.empno.desc()).fetch();

        /**
         * • 사원의 사원번호, 이름을 가져온다, 사원 의 이름을 기준으로 오름차순 정렬을 한 다.
         * • 사원의 사원번호, 이름, 입사일을 가져온 다. 입사일을 기준으로 내림차순 정렬을 한다.
         * • 직무가 SALESMAN인 사원의 사원이름, 사원번호, 급여를 가져온다. 급여를 기준
         * */
        queryFactory.select(emp.empno, emp.ename).from(emp).orderBy(emp.ename.asc()).fetch();
        queryFactory.select(emp.empno, emp.ename, emp.hiredate).from(emp).orderBy(emp.hiredate.desc()).fetch();
        queryFactory.select(emp.ename, emp.empno, emp.sal).from(emp).orderBy(emp.sal.desc()).where(emp.job.eq("SALESMAN")).fetch();

        /**
         * • 1981년에 입사한 사원들의 사원번호, 사원 이름, 입사일을 가져온다. 사원 번호를 기 준으로 내림차순 정렬을 한다.
         * • 사원의 이름, 급여, 커미션을 가져온다. 커 미션을 기준으로 오름차순 정렬을 한다.
         * • 사원의 이름, 급여, 커미션을 가져온다. 커 미션을 기준으로 내임차순 정렬을 한다.
         * */

        LocalDate firstDate = LocalDate.parse("1981-01-01");
        LocalDate lastDate = LocalDate.parse("1981-12-31");
        queryFactory.select(emp.empno, emp.ename, emp.hiredate).from(emp)
                .where(emp.hiredate.between(firstDate, lastDate)).orderBy(emp.hiredate.desc()).fetch();

        queryFactory.select(emp.ename, emp.sal, emp.comm).from(emp).orderBy(emp.comm.asc()).where(emp.comm.isNotNull()).fetch();
        queryFactory.select(emp.ename, emp.sal, emp.comm.coalesce(0)).from(emp).orderBy(emp.comm.desc()).fetch();


    }

    @Test
    @DisplayName("13강 숫자함수")
    public void chapt_13() {
           /**
            * • 전직원의 급여를 2000 삭감하고 삭감한 급 여액의 절대값을 구한다.
            * • 급여가 1500 이상인 사원의 급여를 15% 삭감한다. 단 소수점 이하는 버린다.
            * • 급여가 2천 이하인 사원들의 급여를 20% 씩 인상한다. 단 10의 자리를 기준으로 반 올림한다.
            * */




        queryFactory.select(emp.sal.subtract(2000).abs()).from(emp).fetch();
        queryFactory.select(emp.sal, emp.sal.floatValue().multiply(0.85).floor().intValue()).from(emp).fetch();
        queryFactory.select(
                emp.sal,
                emp.sal.floatValue().multiply(1.2).round().intValue(),
                MathExpressions.round(emp.sal.floatValue().multiply(1.2), -2).intValue()
        ).from(emp).fetch();



    }

    @Test
    @DisplayName("14강 문자열 함수")
    public void chapt_14() {
        /**
         * • 대문자를 소문자로, 소문자를 대문자로
         * */
        
        queryFactory.select(emp.ename.lower()).from(emp).fetch();
        queryFactory.select(emp.ename.upper()).from(emp).fetch();
        
        /**
         * • 첫 글자만 대문자로, 나머지는 소문자로
         *  ???? 미해결
         * */
        queryFactory.select(emp.ename).from(emp).fetch();

        /**
         * • concat()
         * */
        queryFactory.select(emp.sal.stringValue().concat("   ,    ").concat(emp.job)).from(emp).fetch();

        /**
         * • 문자열 길이 length()
         * */
        queryFactory.select(emp.ename, emp.ename.length()).from(emp).fetch();
        
        /**
         * • 문자열 잘라내기 substr() , substrb()
         * */
        queryFactory.select(emp.ename.substring(3)).from(emp).fetch();
        queryFactory.select(emp.ename.substring(3)).from(emp).fetch();

        /**
         * • 사원의 이름 중에 A라는 글자가 두번째 이후에 나타나는 사원의 이름을 가져온다.
         * • sql 로는 where instr(ename, 'A') > 1; 이렇게 가져오는 데 queryDsl 로는 모르겠다.
         * */
        queryFactory.select(emp.ename.like("_A%")).from(emp).fetch();

        /**
         * • 특정 문자열로 채우기
         * - sql : select '문자열', lpad('문자열', 20), rpad('문자열', 20) from dual;
         *    sql 로는 이렇게 하는데 모르겠다... concat 으로 해결 되나???
         * */



        /**
         * 문자열 변경 replace()
         * */


    }

    @Test
    @DisplayName("15강 날짜 함수")
    public void chapt_15() {

    }

    @Test
    @DisplayName("16강 DECODE, CASE")
    public void chapt_16() {
        /**
         *       [ DECODE ]
         * • 각 사원의 부서 이름을 가져온다. 10 : 인사과, 20 : 개발부, 30 : 경원지원팀, 40 : 생산부
         * • 직급에 따라 인상된 급여액을 가져온다.
         *      CLERK : 10%
         *      SALESMAN : 15%
         *      PRESIDENT : 200%
         *      MANAGER : 5%
         *      ANALYST : 20%
         * */

        queryFactory.select(emp.empno, emp.ename,
                emp.deptno
                .when(10).then("인사과")
                .when(20).then("개발부")
                .when(30).then("경원지원팀")
                .when(40).then("생상분").otherwise("")
        ).from(emp).fetch();

        queryFactory.select(emp.empno, emp.ename, emp.job,
            emp.job
                .when("CLERK").then(emp.sal.floatValue().multiply(1.1).intValue())
                .when("SALESMAN").then(emp.sal.floatValue().multiply(1.15).intValue())
                .when("PRESIDENT").then(emp.sal.floatValue().multiply(2).intValue())
                .when("MANAGER").then(emp.sal.floatValue().multiply(1.05).intValue())
                .when("ANALYST").then(emp.sal.floatValue().multiply(1.2).intValue())
                .otherwise(0)
        ).from(emp).fetch();

        /**
         *      [ CASE ]
         * • 급여액 별 등급을 가져온다. 1000 미만 : C등급 1000 이상 2000미만 : B등급 2000 이상 : A등급
         * - 직원들의 급여를 다음과 같이 인상한다. 1000 이하 : 100% 1000 초과 2000미만 : 50% 2000 이상 : 200%
         * */

        // when 에 들어갈 조건이 복잡하면 -> new CaseBuilder() 을 쓴다.
        queryFactory.select(emp.empno, emp.ename, emp.sal,
            new CaseBuilder()
                .when(emp.sal.lt(1000)).then("C 등급")
                .when(emp.sal.between(1000, 1999)).then("B 등급")
                .when(emp.sal.goe(2000)).then("A 등급")
                .otherwise("기타")
        ).from(emp).fetch();

        queryFactory.select(emp.empno, emp.ename, emp.sal,
            new CaseBuilder()
                .when(emp.sal.loe(1000)).then(emp.sal.floatValue().multiply(2).intValue())
                .when(emp.sal.between(1001, 1999)).then(emp.sal.floatValue().multiply(1.5).intValue())
                .when(emp.sal.goe(2000)).then(emp.sal.floatValue().multiply(3).intValue())
                .otherwise(0)
        ).from(emp)
        .where(emp.sal.lt(1000))
        .offset(0).limit(3)
        .fetch();

    }


}
