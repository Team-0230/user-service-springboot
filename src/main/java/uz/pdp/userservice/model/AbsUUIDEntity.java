package uz.pdp.userservice.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public class AbsUUIDEntity {
    @Id
    @GeneratedValue(generator = "uuid_my_id")
    @GenericGenerator(name = "uuid_my_id", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
