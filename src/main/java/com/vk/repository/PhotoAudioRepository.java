package com.vk.repository;

import com.vk.entities.PhotoAudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoAudioRepository extends JpaRepository<PhotoAudio, Integer> {

    PhotoAudio findByPhotoAudioId(int id);

    @Query("SELECT p FROM PhotoAudio p WHERE p.photoName = :photoName")
    List<PhotoAudio> findByPhotoName(@Param("photoName") String photoName);
}
