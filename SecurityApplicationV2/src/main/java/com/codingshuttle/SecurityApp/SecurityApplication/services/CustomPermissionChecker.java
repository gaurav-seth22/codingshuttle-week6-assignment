package com.codingshuttle.SecurityApp.SecurityApplication.services;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.PlanName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("customPermissionChecker")
@Slf4j  // Add Lombok logging
public class CustomPermissionChecker {

    @Autowired
    private SubscriptionService subscriptionService;

    public boolean hasPlanAccess(String username, String requiredPlan) {
        try {

            log.debug("Checking access for user: {} and plan: {}", username, requiredPlan);
            PlanName requiredPlanName = PlanName.valueOf(requiredPlan.toUpperCase());
            PlanName userPlan = subscriptionService.getUserPlan(username);

            log.debug("User plan: {}, Required plan: {}", userPlan, requiredPlanName);
            // If user has no plan, only allow FREE access
            if (userPlan == null) {
                log.debug("User has no plan, allowing only FREE access");
                return requiredPlanName == PlanName.FREE;
            }

            // Check if user's plan level is sufficient
            boolean hasAccess = userPlan.getLevel() >= requiredPlanName.getLevel();
            log.debug("Access decision: {}", hasAccess);
            return hasAccess;
        } catch (Exception e) {
            log.error("Error checking plan access", e);
            return false;
        }
    }
}
