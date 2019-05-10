package com.vk.repository;

import com.vk.entities.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Integer> {

    Audio findByAudioId(int id);

    @Query("SELECT a FROM Audio a WHERE a.audioVKID = :audioVKID AND a.audioOwnerId = :audioOwnerId")
    Audio findByAudioVKIDandOwnerId(@Param("audioVKID") int audioVKID, @Param("audioOwnerId") int audioOwnerId);

    @Query("SELECT a FROM Audio a WHERE a.audioName = :audioName")
    Audio findByAudioName(@Param("audioName") String audioName);

}
