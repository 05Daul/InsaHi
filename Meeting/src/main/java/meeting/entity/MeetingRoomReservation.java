package meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import meeting.enums.ReservationStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Meeting_Room_Reservation")
public class MeetingRoomReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "reserver_name")
    private String reserverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;
}
