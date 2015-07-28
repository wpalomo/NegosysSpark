package ec.com.redepronik.negosys.service;

import static ec.com.redepronik.negosys.utils.UtilsDate.timestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.redepronik.negosys.dao.TraspasoDao;
import ec.com.redepronik.negosys.entity.DetalleTraspaso;
import ec.com.redepronik.negosys.entity.EstadoKardex;
import ec.com.redepronik.negosys.entity.Kardex;
import ec.com.redepronik.negosys.entity.Traspaso;

@Service
public class TraspasoServiceImpl extends GenericServiceImpl<Traspaso, Integer>
		implements TraspasoService {

	@Autowired
	private TraspasoDao traspasoDao;

	@Autowired
	private KardexService kardexService;

	@Autowired
	private ParametroService parametroService;

	public String insertar(Traspaso traspaso) {
		String mensaje = "SAVE";
		List<DetalleTraspaso> listaDetalleTraspasos = new ArrayList<DetalleTraspaso>();
		for (DetalleTraspaso dt : traspaso.getDetallesTraspasos()) {

			Kardex saldoProductoBodegaOrigen = kardexService
					.obtenerSaldoActual(dt.getProducto().getCodigo(), traspaso
							.getLocalOrigen().getId());
			if (dt.getCantidad() <= saldoProductoBodegaOrigen.getCantidad()) {
				listaDetalleTraspasos.add(dt);

				if (saldoProductoBodegaOrigen != null) {
					saldoProductoBodegaOrigen.setActivo(false);
					kardexService.actualizar(saldoProductoBodegaOrigen);
				}

				Kardex kardex = new Kardex();
				kardex.setEstadoKardex(EstadoKardex.ST);
				kardex.setProducto(dt.getProducto());
				kardex.setLocal(traspaso.getLocalOrigen());
				kardex.setNota("HACIA "
						+ traspaso.getLocalDestino().getNombre());
				kardex.setFecha(timestamp());
				kardex.setCantidad(dt.getCantidad());
				kardex.setPrecio(saldoProductoBodegaOrigen.getPrecio());
				kardex.setActivo(false);
				kardexService.insertar(kardex);
				kardexService.insertar(kardexService.generarKardexSaldo(kardex,
						saldoProductoBodegaOrigen.getCantidad(),
						kardex.getPrecio(), false));

				kardex = new Kardex();
				kardex.setEstadoKardex(EstadoKardex.ET);
				kardex.setProducto(dt.getProducto());
				kardex.setLocal(traspaso.getLocalDestino());
				kardex.setNota("DESDE " + traspaso.getLocalOrigen().getNombre());
				kardex.setFecha(timestamp());
				kardex.setCantidad(dt.getCantidad());
				kardex.setPrecio(saldoProductoBodegaOrigen.getPrecio());
				kardex.setActivo(false);
				kardexService.insertar(kardex);

				Kardex saldoProductoBodegaDestino = kardexService
						.obtenerSaldoActual(dt.getProducto().getCodigo(),
								traspaso.getLocalDestino().getId());
				int cantidad = 0;
				BigDecimal precio = saldoProductoBodegaOrigen.getPrecio();
				if (saldoProductoBodegaDestino != null) {
					cantidad = saldoProductoBodegaDestino.getCantidad();
					precio = saldoProductoBodegaDestino.getPrecio();
					saldoProductoBodegaDestino.setActivo(false);
					kardexService.actualizar(saldoProductoBodegaDestino);
				}

				kardexService.insertar(kardexService.generarKardexSaldo(kardex,
						cantidad, precio, true));

			} else
				mensaje = "STOCK";
		}

		if (!listaDetalleTraspasos.isEmpty()) {
			traspaso.setDetallesTraspasos(listaDetalleTraspasos);
			traspaso.setEstablecimiento(traspaso.getLocalOrigen()
					.getCodigoEstablecimiento());
			traspaso.setPuntoEmision("0");
			traspaso.setSecuencia("0");
			traspasoDao.insertar(traspaso);
		} else
			mensaje = "NULL";
		return mensaje;
	}

	public List<Traspaso> obtener() {
		List<Traspaso> list = traspasoDao.obtenerLista("", new Object[] {},
				false, new Object[] {});
		// obtenerPorHql(
		// "select distinct t from Traspaso t "
		// + "inner join fetch t.detalleTraspasos "
		// + "inner join fetch t.local1 l1 "
		// + "inner join fetch t.local2 l2 " + "order by t.fecha",
		// new Object[] {});
		return list;
	}

}