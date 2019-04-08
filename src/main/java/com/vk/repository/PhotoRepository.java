package com.vk.repository;

import com.vk.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    Photo findByPhotoId(int id);
}
