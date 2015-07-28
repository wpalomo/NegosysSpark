package ec.com.redepronik.negosys.utils.documentosElectronicos;

import static ec.com.redepronik.negosys.utils.UtilsDate.fechaFormatoString;
import static ec.com.redepronik.negosys.utils.UtilsMath.multiplicarDivide;
import static ec.com.redepronik.negosys.utils.UtilsMath.parametro;
import static ec.com.redepronik.negosys.utils.UtilsMath.redondearTotales;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.infoAdicional;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.infoTributaria;
import static ec.com.redepronik.negosys.utils.documentosElectronicos.UtilsDocumentos.tipoIdentificacionComprador;

import java.util.List;

import ec.com.redepronik.negosys.entity.DetalleRetencion;
import ec.com.redepronik.negosys.entity.Local;
import ec.com.redepronik.negosys.entity.Persona;
import ec.com.redepronik.negosys.entity.Retencion;
import ec.com.redepronik.negosys.service.LocalService;
import ec.com.redepronik.negosys.service.PersonaService;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.ComprobanteRetencionE;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.Impuestos;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.Impuestos.Impuesto;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.rentencion.InfoCompRetencion;

public class UtilsRetencion {

	private static ComprobanteRetencionE armarRetencionXML(Retencion retencion,
			String cAcceso, Local local) {
		Persona p = UtilsRetencion.personaService.obtenerObjeto(
				"select p from Persona p where p.id=?1",
				new Object[] { retencion.getProveedor().getId() }, false,
				new Object[] {});

		ComprobanteRetencionE comprobanteRetencionE = new ComprobanteRetencionE();
		InfoTributaria infoTributaria = infoTributaria(cAcceso, local);
		infoTributaria.setEstab(retencion.getEstablecimiento());
		infoTributaria.setPtoEmi(retencion.getPuntoEmision());
		infoTributaria.setSecuencial(retencion.getSecuencia());
		comprobanteRetencionE.setInfoTributaria(infoTributaria);
		comprobanteRetencionE.setInfoCompRetencion(infoCompRetencion(retencion,
				p, local));
		comprobanteRetencionE.setImpuestos(impuestos(retencion
				.getDetallesRetenciones()));
		comprobanteRetencionE.setInfoAdicional(infoAdicional(p));

		return comprobanteRetencionE;
	}

	private static Impuesto impuesto(DetalleRetencion dr) {
		Impuesto impuesto = new Impuesto();
		impuesto.setCodigo(String.valueOf(dr.getImpuestoRetencion().getId()));
		impuesto.setCodigoRetencion(dr.getTarifaRetencion().getId());
		impuesto.setBaseImponible(dr.getBaseImponible());
		impuesto.setPorcentajeRetener(dr.getPorcentajeRetencion());
		impuesto.setValorRetenido(redondearTotales(multiplicarDivide(
				dr.getBaseImponible(), dr.getPorcentajeRetencion())));
		impuesto.setCodDocSustento(dr.getTipoComprobanteRetencion().getId());
		impuesto.setNumDocSustento(dr.getNumeroComprobante());
		impuesto.setFechaEmisionDocSustento(fechaFormatoString(dr
				.getFechaEmision()));
		return impuesto;
	}

	private static Impuestos impuestos(List<DetalleRetencion> list) {
		Impuestos impuestos = new Impuestos();
		for (DetalleRetencion dr : list)
			impuestos.getImpuesto().add(impuesto(dr));
		return impuestos;
	}

	public static ComprobanteRetencionE retencionXML(Retencion retencion,
			String claveAcceso, LocalService localService,
			PersonaService personaService) {
		UtilsRetencion.localService = localService;
		UtilsRetencion.personaService = personaService;
		Local local = UtilsRetencion.localService.obtenerObjeto(
				"select l from Local where l.establecimiento=?1",
				new Object[] { retencion.getEstablecimiento() }, false,
				new Object[] {});
		return armarRetencionXML(retencion, claveAcceso, local);
	}

	private static InfoCompRetencion infoCompRetencion(Retencion r, Persona p,
			Local local) {
		InfoCompRetencion infoCompRetencion = new InfoCompRetencion();
		infoCompRetencion.setFechaEmision(fechaFormatoString(r
				.getFechaEmision()));
		infoCompRetencion.setDirEstablecimiento(local.getDireccion());

		if (parametro.getCodigoContribuyente() != null
				&& parametro.getCodigoContribuyente().compareToIgnoreCase("") != 0)
			infoCompRetencion.setContribuyenteEspecial(parametro
					.getCodigoContribuyente());

		infoCompRetencion
				.setObligadoContabilidad(parametro.getContabilidad() ? "SI"
						: "NO");

		infoCompRetencion
				.setTipoIdentificacionSujetoRetenido(tipoIdentificacionComprador(p
						.getNumeroDocumento()));
		infoCompRetencion.setRazonSocialSujetoRetenido(p.getRazonSocial());
		infoCompRetencion.setIdentificacionSujetoRetenido(p
				.getNumeroDocumento());
		infoCompRetencion.setPeriodoFiscal(fechaFormatoString(
				r.getFechaEmision(), "MM/yyyy"));
		return infoCompRetencion;
	}

	private static LocalService localService;

	private static PersonaService personaService;

}
