package com.vk.repository;

import com.vk.entities.MusicianInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicianRepository extends JpaRepository<MusicianInfo, Integer> {

    MusicianInfo findByMusicianInfoId(int id);

}
