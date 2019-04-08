package com.vk.repository;

import com.vk.entities.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Integer> {

    Audio findByAudioId(int id);
}
