package application;

import org.springframework.boot.SpringApplication;

/**
 * Main entry point for the 911 Enchrichment Application
 *
 * @author James
 */
public class EmergencyEnrichApplication {

    public static void main(String[] args){
        SpringApplication.run(EmergencyEnrichConfiguration.class, args);
    }
}
