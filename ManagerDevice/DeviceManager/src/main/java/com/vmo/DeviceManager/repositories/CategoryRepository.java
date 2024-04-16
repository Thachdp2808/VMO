package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {


}
