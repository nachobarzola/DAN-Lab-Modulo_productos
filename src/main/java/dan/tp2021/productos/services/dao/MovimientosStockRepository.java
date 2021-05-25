package dan.tp2021.productos.services.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dan.tp2021.productos.domain.MovimientosStock;

@Repository
public interface MovimientosStockRepository extends JpaRepository<MovimientosStock, Integer>{

}
