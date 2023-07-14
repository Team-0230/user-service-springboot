package uz.pdp.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationCode extends AbsEntity {
    @OneToOne
    @Column(nullable = false)
    User user;
    @Column(nullable = false)
    String code;
    @Column(nullable = false)
    boolean used;
    @Column(nullable = false)
    boolean expired;
    @Column(nullable = false)
    LocalDateTime expiredDate;
    @Column(nullable = false)
    int countRequest;
    @Column(nullable = false)
    boolean blocked;

    public VerificationCode(User user, String code, LocalDateTime expiredDate) {
        this.user = user;
        this.code = code;
        this.expiredDate = expiredDate;
        this.expired = false;
        this.used = false;
        this.countRequest = 0;
        this.blocked = false;
    }
}
