package dan.tp2021.productos.domain;


import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Provision {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //valor autonumerico
	@Column(name="ID_PROVISION")
	private Integer id;
	
	private Instant fechaProvision;
	
	@OneToMany(mappedBy = "provision")
	private List<DetalleProvision> detalle;
	
	
	
	public Provision(Instant fechaProvision, List<DetalleProvision> detalle) {
		super();
		this.fechaProvision = fechaProvision;
		this.detalle = detalle;
	}
	
	
	public Provision() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Instant getFechaProvision() {
		return fechaProvision;
	}
	public void setFechaProvision(Instant fechaProvision) {
		this.fechaProvision = fechaProvision;
	}
	public List<DetalleProvision> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<DetalleProvision> detalle) {
		this.detalle = detalle;
	}

	
}
