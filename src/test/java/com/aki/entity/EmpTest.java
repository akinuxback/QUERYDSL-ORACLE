package com.aki.entity;

import com.aki.repository.EmpRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
@Log4j2
class EmpTest {

    @Autowired
    EmpRepository empRepository;

    @Autowired
    EntityManager em;

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
    public void QEmp() throws Exception{
        JPAQueryFactory query = new JPAQueryFactory(em);
        QEmp emp = QEmp.emp;

        QueryResults<Emp> results = query.selectFrom(emp).fetchResults();

        assertThat(results.getTotal()).isEqualTo(14);
    }

}