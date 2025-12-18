package se.yrgo.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @Column
//    private String preferredLanguage; // e.g., "en", "sv"
//
//    @Column
//    private String notificationPreference; // e.g., "email", "sms", "both", "none"
//
//    @Column
//    private Boolean marketingEmails;

    @Column
    private String timeZone; // e.g., "Europe/Stockholm"

    @Column
    private String theme; // e.g., "light", "dark"
}