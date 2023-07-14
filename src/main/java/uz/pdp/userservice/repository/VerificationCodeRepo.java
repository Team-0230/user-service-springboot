package uz.pdp.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.userservice.model.AbsEntity;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.model.VerificationCode;

@Repository
public interface VerificationCodeRepo extends JpaRepository<VerificationCode, AbsEntity> {

    void deleteByUser(User user);
}
