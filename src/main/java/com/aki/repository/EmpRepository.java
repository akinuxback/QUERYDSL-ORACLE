package com.aki.repository;

import com.aki.entity.Emp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpRepository extends JpaRepository<Emp, Long> {

    @Query("select e from Emp e where e.ename = :ename")
    public Emp findByEname(String ename);
}
