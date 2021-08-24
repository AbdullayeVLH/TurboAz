package az.code.turboplus.repositories;

import az.code.turboplus.models.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {

    @Query("SELECT model FROM Model model " +
            "WHERE model.make.id=:makeId")
    List<Model> findByMakeId(Long makeId);
}
