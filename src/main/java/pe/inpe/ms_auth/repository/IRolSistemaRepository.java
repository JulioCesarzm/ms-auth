package pe.inpe.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.inpe.ms_auth.entity.RolSistema;

import java.util.Optional;

@Repository
public interface IRolSistemaRepository extends JpaRepository<RolSistema, Long> {

    Optional<RolSistema> findByCodigo(String codigo);
}