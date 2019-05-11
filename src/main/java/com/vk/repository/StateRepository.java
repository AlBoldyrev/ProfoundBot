package com.vk.repository;

import com.vk.entities.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    State findByStateId(int id);

    @Query("SELECT s FROM State s WHERE s.userId = :userId")
    List<State> findByUserId(@Param("userId") int userId);

    @Query("SELECT s FROM State s WHERE s.userId = :userId and s.isInfoSent = :isInfoSent")
    List<State> findByUserId(@Param("userId") int userId, @Param("isInfoSent") boolean isInfoSent);


}
