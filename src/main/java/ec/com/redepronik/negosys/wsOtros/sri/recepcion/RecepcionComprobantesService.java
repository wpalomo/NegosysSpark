package ec.com.redepronik.negosys.wsOtros.sri.recepcion;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "RecepcionComprobantesService", targetNamespace = "http://ec.gob.sri.ws.recepcion", wsdlLocation = "")
public class RecepcionComprobantesService extends Service {

	private static URL __getWsdlLocation() {
		if (RECEPCIONCOMPROBANTESSERVICE_EXCEPTION != null) {
			throw RECEPCIONCOMPROBANTESSERVICE_EXCEPTION;
		}
		return RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION;
	}

	private final static URL RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION;
	private final static WebServiceException RECEPCIONCOMPROBANTESSERVICE_EXCEPTION;

	private final static QName RECEPCIONCOMPROBANTESSERVICE_QNAME = new QName(
			"http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");

	static {
		URL url = null;
		WebServiceException e = null;
		try {
			url = new URL(
					"https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl");
		} catch (MalformedURLException ex) {
			e = new WebServiceException(ex);
		}
		RECEPCIONCOMPROBANTESSERVICE_WSDL_LOCATION = url;
		RECEPCIONCOMPROBANTESSERVICE_EXCEPTION = e;
	}

	public RecepcionComprobantesService() {
		super(__getWsdlLocation(), RECEPCIONCOMPROBANTESSERVICE_QNAME);
	}

	public RecepcionComprobantesService(URL wsdlLocation) {
		super(wsdlLocation, RECEPCIONCOMPROBANTESSERVICE_QNAME);
	}

	public RecepcionComprobantesService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public RecepcionComprobantesService(URL wsdlLocation, QName serviceName,
			WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	public RecepcionComprobantesService(URL wsdlLocation,
			WebServiceFeature... features) {
		super(wsdlLocation, RECEPCIONCOMPROBANTESSERVICE_QNAME, features);
	}

	public RecepcionComprobantesService(WebServiceFeature... features) {
		super(__getWsdlLocation(), RECEPCIONCOMPROBANTESSERVICE_QNAME, features);
	}

	@WebEndpoint(name = "RecepcionComprobantesPort")
	public RecepcionComprobantes getRecepcionComprobantesPort() {
		return super.getPort(new QName("http://ec.gob.sri.ws.recepcion",
				"RecepcionComprobantesPort"), RecepcionComprobantes.class);
	}

	@WebEndpoint(name = "RecepcionComprobantesPort")
	public RecepcionComprobantes getRecepcionComprobantesPort(
			WebServiceFeature... features) {
		return super.getPort(new QName("http://ec.gob.sri.ws.recepcion",
				"RecepcionComprobantesPort"), RecepcionComprobantes.class,
				features);
	}

}
