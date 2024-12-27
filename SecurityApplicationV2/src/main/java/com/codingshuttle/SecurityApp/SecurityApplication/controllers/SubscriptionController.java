package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.SubscriptionRequest;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.Subscription;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.PlanName;
import com.codingshuttle.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.codingshuttle.SecurityApp.SecurityApplication.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> createSubscription(@RequestBody SubscriptionRequest subscriptionRequest){
        PlanName planName=PlanName.valueOf(subscriptionRequest.getPlanName().toUpperCase());
        subscriptionService.createOrUpdateSubscription(subscriptionRequest.getUserId(),planName);

        return ResponseEntity.ok("Subscribed successfully to " + planName);
    }

    @GetMapping("/access")
    @PreAuthorize("@customPermissionChecker.hasPlanAccess(authentication.name, #requiredPlan)")
    public ResponseEntity<String> checkAccess(@RequestParam String username, @RequestParam String requiredPlan) {
        try {
            PlanName planName = PlanName.valueOf(requiredPlan.toUpperCase());
            boolean hasAccess = subscriptionService.hasAccess(username, planName);

            if (hasAccess) {
                return ResponseEntity.ok("User " + username + " has access to " + requiredPlan + " plan.");
            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User " + username + " does not have access to " + requiredPlan + " plan.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid plan name: " + requiredPlan);
        }
    }
}
