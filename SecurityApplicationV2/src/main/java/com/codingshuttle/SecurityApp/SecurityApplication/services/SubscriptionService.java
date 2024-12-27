package com.codingshuttle.SecurityApp.SecurityApplication.services;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.Subscription;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.PlanName;
import com.codingshuttle.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.SubscriptionRepository;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;


    public Subscription createOrUpdateSubscription(Long userId, PlanName planName){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find existing subscription for the user
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElse(new Subscription());

        // Update the plan name
        subscription.setPlanName(planName);
        subscription.setUser(user);



        return subscriptionRepository.save(subscription);

    }

    public PlanName getUserPlan(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return subscriptionRepository.findByUser(user)
                .map(Subscription::getPlanName)
                .orElse(PlanName.FREE); // Default to FREE plan
    }

    public boolean hasAccess(String username, PlanName requiredPlan) {
        PlanName userPlan = getUserPlan(username);
        return userPlan.getLevel() >= requiredPlan.getLevel();
    }

   /* public boolean hasAccess(String username,PlanName requiredPlan){
        Optional<User> userOptional=userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        User user=userOptional.get();
        return subscriptionRepository.existsByUserAndPlanName(user,requiredPlan);
    }*/
}
