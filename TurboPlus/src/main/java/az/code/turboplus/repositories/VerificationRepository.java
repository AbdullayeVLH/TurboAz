package az.code.turboplus.repositories;

import az.code.turboplus.models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, String> {
}
