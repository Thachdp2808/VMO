package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    User findByRole(Erole role);
    Optional<User> findById(int id);



}
