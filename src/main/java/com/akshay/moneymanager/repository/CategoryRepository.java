package com.akshay.moneymanager.repository;

import com.akshay.moneymanager.entity.CategoryEntity;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    // select * from category_entity where profile_id = ?1
    List<CategoryEntity> findByProfileId(Long profileId);

    // select * from  category_entity where id = ?1 and profile_id = ?2
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

    // select * from category_entity where type = ?1 and profile_id =?2
    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);

    Boolean existsByNameAndProfileId(String name, Long profileId);

}
