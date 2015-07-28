package ec.com.redepronik.negosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "bitacoras")
public class Bitacora implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Timestamp fecha;
	private String metodo;
	private Persona persona;

	public Bitacora() {
	}

	public Bitacora(Timestamp fecha, String metodo, Persona persona) {
		this.fecha = fecha;
		this.metodo = metodo;
		this.persona = persona;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bitacora other = (Bitacora) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(nullable = false)
	public Timestamp getFecha() {
		return this.fecha;
	}

	@Id
	@SequenceGenerator(allocationSize = 1, name = "BITACORAS_ID_GENERATOR", sequenceName = "BITACORAS_ID_SEQ")
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BITACORAS_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	@Column(nullable = false, length = 100)
	public String getMetodo() {
		return this.metodo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persona", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setBitacoraid(Long id) {
		this.id = id;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}