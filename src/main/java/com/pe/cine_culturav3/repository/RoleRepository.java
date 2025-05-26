package com.pe.cine_culturav3.repository;

import com.pe.cine_culturav3.model.Role;
import com.pe.cine_culturav3.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);

    @Query("SELECT r FROM Role r WHERE r.enabled = :enabled")
    List<Role> findByEnabled(@Param("enabled") Boolean enabled);
}
