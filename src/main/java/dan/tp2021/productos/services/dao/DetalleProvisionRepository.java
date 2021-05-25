package dan.tp2021.productos.services.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.tp2021.productos.domain.DetalleProvision;

@Repository
public interface DetalleProvisionRepository extends JpaRepository<DetalleProvision, Integer>{

}
