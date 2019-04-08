package com.vk.repository;

import com.vk.entities.PhotoAudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoAudioRepository extends JpaRepository<PhotoAudio, Integer> {

    PhotoAudio findByPhotoAudioId(int id);
}
