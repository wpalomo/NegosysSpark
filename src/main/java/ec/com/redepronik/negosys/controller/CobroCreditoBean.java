package ec.com.redepronik.negosys.controller;

import static ec.com.redepronik.negosys.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.redepronik.negosys.utils.UtilsAplicacion.redireccionar;
import static ec.com.redepronik.negosys.utils.UtilsDate.compareTo;
import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;
import static ec.com.redepronik.negosys.utils.UtilsMath.moraTotal;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.newBigDecimal;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ec.com.redepronik.negosys.entity.Credito;
import ec.com.redepronik.negosys.entity.DetalleCredito;
import ec.com.redepronik.negosys.entity.Factura;
import ec.com.redepronik.negosys.entity.Garante;
import ec.com.redepronik.negosys.entity.PagoCredito;
import ec.com.redepronik.negosys.entity.Persona;
import ec.com.redepronik.negosys.entity.TipoPago;
import ec.com.redepronik.negosys.entityAux.CantidadesCreditoReporte;
import ec.com.redepronik.negosys.entityAux.NumeroCuotasReporte;
import ec.com.redepronik.negosys.service.CreditoService;
import ec.com.redepronik.negosys.service.FacturaService;
import ec.com.redepronik.negosys.service.LocalService;
import ec.com.redepronik.negosys.service.PersonaService;
import ec.com.redepronik.negosys.service.ProductoService;

@Controller
@Scope("session")
public class CobroCreditoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private CreditoService creditoService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private LocalService localService;

	private List<Factura> listaFacturas;

	private Factura egreso;
	private Persona cliente;
	private Garante garante;
	private Persona personaGarante;

	private List<Persona> listaGarantes;
	private Credito credito;
	private DetalleCredito detallesCredito;
	private PagoCredito pagosCredito;

	private Date fechaPagoCredito;
	private CantidadesCreditoReporte cantidadesCreditoReporte;

	private String nombreCliente;

	private Date fechaActual;

	private NumeroCuotasReporte numeroCuotas;

	private boolean bnCredito = true;

	private List<Persona> listaClienteBusqueda;
	private String criterioClienteBusqueda;
	private Integer criterioBusquedaEstado;
	private String criterioBusquedaCodigo;

	public CobroCreditoBean() {
	}

	public void activarBotonCredito() {
		bnCredito = true;
	}

	public void actualizar(ActionEvent actionEvent) {
		boolean pagadoCredito = true;
		int c = 1;
		for (DetalleCredito dc : egreso.getCredito().getDetallesCreditos()) {
			if (dc.getPagado() == false)
				pagadoCredito = false;
			dc.setOrden(c++);
			int c1 = 1;
			for (PagoCredito pc : dc.getPagosCreditos())
				pc.setOrden(c1++);
		}
		egreso.getCredito().setPagado(pagadoCredito);
		c = 1;
		for (Garante g : egreso.getCredito().getGarantes())
			g.setOrden(c++);

		if (egreso.getCredito().getPagado())
			egreso.setFechaCierre(timestamp());

		facturaService.actualizar(egreso);
		presentaMensaje(FacesMessage.SEVERITY_INFO, "ACTUALIZO EL EGRESO:"
				+ egreso.getEstablecimiento() + "-" + egreso.getPuntoEmision()
				+ "-" + egreso.getSecuencia());
	}

	public void calcularCuota() {
		if (detallesCredito.getId() != null) {
			cantidadesCreditoReporte = creditoService.calcularCuota(
					detallesCredito, cantidadesCreditoReporte,
					egreso.getFechaInicio(), fechaPagoCredito);
			bnCredito = false;
		}
	}

	public void calcularNumeroCuotasEntradasCreditos() {
		numeroCuotas = new NumeroCuotasReporte();
		numeroCuotas = facturaService.calcularNumeroCuotasEntradasCreditos(
				egreso, numeroCuotas, fechaActual);
	}

	public void cancelarCredito() {
		credito = new Credito();
		pagosCredito = new PagoCredito();
	}

	public void cancelarDetalleCredito() {
		cantidadesCreditoReporte = new CantidadesCreditoReporte();
		fechaPagoCredito = new Date();
		detallesCredito = new DetalleCredito();
	}

	public CantidadesCreditoReporte getCantidadesCreditoReporte() {
		return cantidadesCreditoReporte;
	}

	public Persona getCliente() {
		return cliente;
	}

	public Credito getCredito() {
		return credito;
	}

	public String getCriterioBusquedaCodigo() {
		return criterioBusquedaCodigo;
	}

	public Integer getCriterioBusquedaEstado() {
		return criterioBusquedaEstado;
	}

	public String getCriterioClienteBusqueda() {
		return criterioClienteBusqueda;
	}

	public DetalleCredito getDetallesCredito() {
		if (detallesCredito.getPagosCreditos() == null)
			detallesCredito.setPagosCreditos(new ArrayList<PagoCredito>());

		return detallesCredito;
	}

	public Factura getFactura() {
		return egreso;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public Date getFechaPagoCredito() {
		return fechaPagoCredito;
	}

	public Garante getGarante() {
		return garante;
	}

	public List<Persona> getListaClienteBusqueda() {
		return listaClienteBusqueda;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public List<Persona> getListaGarantes() {
		return listaGarantes;
	}

	public TipoPago[] getListaTiposPago() {
		return TipoPago.values();
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public NumeroCuotasReporte getNumeroCuotas() {
		return numeroCuotas;
	}

	public PagoCredito getPagosCredito() {
		return pagosCredito;
	}

	public Persona getPersonaGarante() {
		return personaGarante;
	}

	@PostConstruct
	public void init() {
		listaGarantes = new ArrayList<Persona>();
		listaFacturas = new ArrayList<Factura>();

		egreso = new Factura();
		cliente = new Persona();
		garante = new Garante();
		credito = new Credito();
		detallesCredito = new DetalleCredito();
		pagosCredito = new PagoCredito();

		fechaPagoCredito = new Date();
		cantidadesCreditoReporte = new CantidadesCreditoReporte();

		fechaActual = new Date();

		criterioBusquedaCodigo = new String();
	}

	public void insertarGarante() {
		personaGarante = personaService.obtenerObjeto("", new Object[] {},
				false, new Object[] {});
		// obtenerPorId(personaGarante.getId());
		boolean bn = false;
		for (Garante g : egreso.getCredito().getGarantes())
			if (g.getCliente().getNumeroDocumento()
					.compareTo(personaGarante.getNumeroDocumento()) == 0) {
				bn = true;
				presentaMensaje(FacesMessage.SEVERITY_ERROR,
						"EL GARANTE YA EXISTE");
				break;
			}
		if (!bn) {
			Garante g = new Garante();
			g.setCliente(personaGarante);
			egreso.getCredito().addGarante(g);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INSERTÓ GARANTE "
					+ personaGarante.getRazonSocial() + " CON EXITO");
		}
	}

	public boolean isBnCredito() {
		return bnCredito;
	}

	public void limpiarObjetosBusquedaCliente() {
		criterioClienteBusqueda = new String();
		listaClienteBusqueda = new ArrayList<Persona>();
	}

	public void obtener() {
		listaFacturas = facturaService.obtenerPorCredito(
				cliente.getNumeroDocumento(), criterioBusquedaEstado,
				criterioBusquedaCodigo);
		cliente = new Persona();
		criterioBusquedaEstado = 0;
		criterioBusquedaCodigo = new String();
	}

	public void obtenerClientes() {
		if (!nombreCliente.equals(null))
			listaGarantes = personaService.obtenerLista("", new Object[] {},
					false, new Object[] {});
		// obtenerGarante(nombreCliente);
	}

	public void obtenerClientesPorBusqueda() {
		listaClienteBusqueda = personaService.obtenerLista("", new Object[] {},
				false, new Object[] {});
		// obtener(criterioClienteBusqueda,
		// 0);
		criterioClienteBusqueda = new String();
	}

	public void pagarCredito() {
		BigDecimal t = redondearTotales(cantidadesCreditoReporte
				.getTotalCredito());
		BigDecimal c = redondearTotales(pagosCredito.getCuota());

		if (c.compareTo(t) > 0 || c.compareTo(newBigDecimal()) <= 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR,
					"INGRESE UN VALOR VALIDO PARA LA CUOTA");
		else {

			if (detallesCredito.getId() != null) {
				detallesCredito.setMora(newBigDecimal());
				detallesCredito.setSaldo(detallesCredito.getSaldo().add(
						detallesCredito.getMora()));
				if (compareTo(fechaPagoCredito, detallesCredito.getFechaMora()) >= 0) {
					BigDecimal mora = parametro.getInteresMora();
					BigDecimal moraTotal = moraTotal(fechaPagoCredito,
							detallesCredito.getFechaMora(), mora);
					detallesCredito.setMora(multiplicarDivide(moraTotal,
							detallesCredito.getSaldo()));
					detallesCredito.setSaldo(detallesCredito.getSaldo().add(
							detallesCredito.getMora()));
				}
			}

			pagosCredito.setFechaPago(timestamp(fechaPagoCredito));

			if (t.compareTo(c) == 0) {
				detallesCredito.setPagado(true);
				detallesCredito.setSaldo(new BigDecimal("0"));
				detallesCredito.setFechaPago(fechaPagoCredito);
			} else {
				detallesCredito.setFechaMora(fechaPagoCredito);
				detallesCredito.setSaldo(detallesCredito.getSaldo().subtract(
						pagosCredito.getCuota()));
			}
			calcularCuota();
			detallesCredito.addPagocredito(pagosCredito);
			pagosCredito = new PagoCredito();
		}
	}

	public void redirecionar() {
		redireccionar("credito.jsf");
	}

	public void setBnCredito(boolean bnCredito) {
		this.bnCredito = bnCredito;
	}

	public void setCantidadesCreditoReporte(
			CantidadesCreditoReporte cantidadesCreditoReporte) {
		this.cantidadesCreditoReporte = cantidadesCreditoReporte;
	}

	public void setCliente(Persona cliente) {
		this.cliente = cliente;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}

	public void setCriterioBusquedaCodigo(String criterioBusquedaCodigo) {
		this.criterioBusquedaCodigo = criterioBusquedaCodigo;
	}

	public void setCriterioBusquedaEstado(Integer criterioBusquedaEstado) {
		this.criterioBusquedaEstado = criterioBusquedaEstado;
	}

	public void setCriterioClienteBusqueda(String criterioClienteBusqueda) {
		this.criterioClienteBusqueda = criterioClienteBusqueda;
	}

	public void setDetallesCredito(DetalleCredito detallesCredito) {
		this.detallesCredito = detallesCredito;
	}

	public void setFactura(Factura egreso) {
		this.egreso = egreso;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public void setFechaPagoCredito(Date fechaPagoCredito) {
		this.fechaPagoCredito = fechaPagoCredito;
	}

	public void setGarante(Garante garante) {
		this.garante = garante;
	}

	public void setListaClienteBusqueda(List<Persona> listaClienteBusqueda) {
		this.listaClienteBusqueda = listaClienteBusqueda;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public void setListaGarantes(List<Persona> listaGarantes) {
		this.listaGarantes = listaGarantes;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setNumeroCuotas(NumeroCuotasReporte numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public void setPagosCredito(PagoCredito pagosCredito) {
		this.pagosCredito = pagosCredito;
	}

	public void setPersonaGarante(Persona personaGarante) {
		this.personaGarante = personaGarante;
	}

}