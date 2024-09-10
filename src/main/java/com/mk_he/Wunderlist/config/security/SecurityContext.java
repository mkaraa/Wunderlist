package com.mk_he.Wunderlist.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContext {

    private static final Logger logger = LoggerFactory.getLogger(SecurityContext.class);

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "Anonymous";

        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
                logger.info("Current authenticated user: {}", userDetails);
            } else if (authentication.getPrincipal() instanceof String) {
                username = (String) authentication.getPrincipal();
            }
        } else {
            logger.warn("No authentication information found");
        }

        return username;
    }
}
