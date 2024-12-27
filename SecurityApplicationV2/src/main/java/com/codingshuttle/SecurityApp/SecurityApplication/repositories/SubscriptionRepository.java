package com.codingshuttle.SecurityApp.SecurityApplication.repositories;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.Subscription;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.PlanName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    Optional<Subscription> findByUser(User user);

    boolean existsByUserAndPlanName(User user, PlanName planName);
}
