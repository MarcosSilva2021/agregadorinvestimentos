package dev.mar.agregadorinvestimentos.repository;

import dev.mar.agregadorinvestimentos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {
}
