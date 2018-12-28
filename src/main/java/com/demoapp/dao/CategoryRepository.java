package com.demoapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demoapp.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
