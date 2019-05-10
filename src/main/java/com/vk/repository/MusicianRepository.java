package com.vk.repository;

import com.vk.entities.Audio;
import com.vk.entities.MusicianInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicianRepository extends JpaRepository<MusicianInfo, Integer> {

    MusicianInfo findByMusicianInfoId(int id);

    @Query("SELECT m FROM MusicianInfo m WHERE m.musicianName = :musicianName")
    MusicianInfo findByMusicianName(@Param("musicianName") String musicianName);


}
