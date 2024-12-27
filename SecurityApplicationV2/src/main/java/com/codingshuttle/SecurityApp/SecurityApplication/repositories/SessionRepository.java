package com.codingshuttle.SecurityApp.SecurityApplication.repositories;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity,Long> {


     List<SessionEntity> findByUser(User user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);

    void deleteByUser(User user);

    void deleteByRefreshToken(String refreshToken);
}
