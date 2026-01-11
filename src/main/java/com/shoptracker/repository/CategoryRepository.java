package com.shoptracker.repository;

import com.shoptracker.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Método para verificar si existe una categoría por nombre
    boolean existsByName(String name);

}