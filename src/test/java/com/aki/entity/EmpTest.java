package com.aki.entity;

import com.aki.repository.EmpRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.aki.entity.QEmp.emp;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
@Log4j2
class EmpTest {

    @Autowired
    EmpRepository empRepository;

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void findAll() throws Exception{
        //given
        List<Emp> findAll = empRepository.findAll();
        //when
        Emp findFetchOne = empRepository.findByEname("SMITH");
        log.info(findFetchOne);
        log.info(findFetchOne.getEname());
        //then

    }
    
    @Test
    @DisplayName("start test")
    public void QEmp() throws Exception{

        QueryResults<Emp> results = queryFactory.selectFrom(emp).fetchResults();

        assertThat(results.getTotal()).isEqualTo(14);
    }

    @Test
    public void startQueryDsl() throws Exception{
        Emp smith = queryFactory
                .select(emp)
                .from(emp)
                .where(emp.ename.eq("SMITH"))
                .fetchOne();
        log.info(smith);

        assertThat(smith.getEname()).isEqualTo("SMITH");
    }

    @Test
    @DisplayName("검색 조건 쿼리")
    public void search(){
        Emp findEmp = queryFactory
                .selectFrom(emp)
                .where(emp.deptno.eq(20).and(emp.ename.eq("SMITH")))
                .fetchOne();

        assertThat(findEmp.getDeptno()).isEqualTo(20);
        assertThat(findEmp.getEname()).isEqualTo("SMITH");

    }

}