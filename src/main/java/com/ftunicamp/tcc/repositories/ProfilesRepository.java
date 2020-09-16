package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.Profiles;
import com.ftunicamp.tcc.entities.ProfilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilesRepository extends JpaRepository<ProfilesEntity, Integer> {

    Optional<ProfilesEntity> findByName(Profiles profile);
}
