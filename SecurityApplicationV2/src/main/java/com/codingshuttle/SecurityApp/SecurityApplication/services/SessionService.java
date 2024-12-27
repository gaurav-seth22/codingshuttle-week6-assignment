package com.codingshuttle.SecurityApp.SecurityApplication.services;


import com.codingshuttle.SecurityApp.SecurityApplication.entities.SessionEntity;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    //private final int SESSION_LIMIT=2;

    @Transactional
    public void generateNewSession(User user, String refreshToken){
        List<SessionEntity> userSessions = sessionRepository.findByUser(user);
        if (userSessions.size() == user.getSESSION_LIMIT()) {
            userSessions.sort(Comparator.comparing(SessionEntity::getLastusedAt));

            SessionEntity leastRecentlyUsedSession = userSessions.get(0);
            sessionRepository.delete(leastRecentlyUsedSession);
        }


        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }


    public void validateSession(String refreshToken) {
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        session.setLastusedAt(LocalDateTime.now());

        sessionRepository.save(session);
    }
    @Transactional
    public void removeSession(User user) {
        sessionRepository.deleteByUser(user);
    }

    @Transactional
    public void removeSessionByToken(String refreshToken){
        sessionRepository.deleteByRefreshToken(refreshToken);
    }

    /*@Transactional
    public void createSession(Long userId,String token){
        if(sessionRepository != null && sessionRepository.existsByUserId(userId)) {
            sessionRepository.deleteByUserId(userId);
        }

        SessionEntity saveSession= new SessionEntity(userId,token);

        sessionRepository.save(saveSession);
    }

    public boolean isValidToken(String token){
         Optional<SessionEntity> session=sessionRepository.findByToken(token);
         return session.isPresent();
    }

    @Transactional
    public void removeSession(Long userId){
        sessionRepository.deleteByUserId(userId);
    }*/
}
