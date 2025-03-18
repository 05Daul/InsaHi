package meeting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Meeting_Room")
public class MeetingRoom  {
    @Id
    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "photo")
    private String photo;  //이미지 데이터 저장
}
