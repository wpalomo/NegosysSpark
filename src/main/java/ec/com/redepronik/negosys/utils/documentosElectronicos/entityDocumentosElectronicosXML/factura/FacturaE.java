package ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.factura;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoAdicional;
import ec.com.redepronik.negosys.utils.documentosElectronicos.entityDocumentosElectronicosXML.InfoTributaria;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoTributaria", "infoFactura", "detalles",
		"infoAdicional" })
@XmlRootElement(name = "factura")
public class FacturaE {
	@XmlElement(required = true)
	protected InfoTributaria infoTributaria;
	@XmlElement(required = true)
	protected InfoFactura infoFactura;
	@XmlElement(required = true)
	protected Detalles detalles;
	protected InfoAdicional infoAdicional;
	@XmlAttribute
	protected String id = "comprobante";
	@XmlAttribute
	protected String version = "1.0.0";

	public Detalles getDetalles() {
		return this.detalles;
	}

	public String getId() {
		return this.id;
	}

	public InfoAdicional getInfoAdicional() {
		return this.infoAdicional;
	}

	public InfoFactura getInfoFactura() {
		return this.infoFactura;
	}

	public InfoTributaria getInfoTributaria() {
		return this.infoTributaria;
	}

	public String getVersion() {
		return this.version;
	}

	public void setDetalles(Detalles value) {
		this.detalles = value;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setInfoAdicional(InfoAdicional value) {
		this.infoAdicional = value;
	}

	public void setInfoFactura(InfoFactura value) {
		this.infoFactura = value;
	}

	public void setInfoTributaria(InfoTributaria value) {
		this.infoTributaria = value;
	}

	public void setVersion(String value) {
		this.version = value;
	}

}
