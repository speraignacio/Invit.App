package app.rest.invit.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.rest.invit.entities.PostEntity;
import app.rest.invit.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
   
    @Query(value = "SELECT * FROM users p WHERE p.user_id = :userId", nativeQuery = true)
    UserEntity findByUserID(@Param("userId") String userId);
}