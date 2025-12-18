package se.yrgo.booking_service.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long tableId;
    private LocalDateTime bookingTime;
    private Integer numberOfGuests;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingStatus status = BookingStatus.CONFIRMED;

    private String specialRequests;

    @Column(unique = true)
    private String confirmationCode;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
