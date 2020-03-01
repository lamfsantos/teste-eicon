package br.com.teste.eicon.resources;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.teste.eicon.domain.Pedido;
import br.com.teste.eicon.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	@Autowired
	private PedidoService service;

//	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
//			MediaType.APPLICATION_XML_VALUE })
//	@ResponseBody
//	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
//		Pedido pedido = service.findExternal(id);
//		return ResponseEntity.ok().body(pedido);
//	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> insert(@Valid @RequestBody List<Pedido> pedidos) {
		return service.insertList(pedidos);
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public ResponseEntity<List<Pedido>> find(
			@RequestParam(value = "numeroControle", required = false) Integer numeroControle,
			@RequestParam(value = "dataCadastro", required = false) String dataCadastro,
			@RequestParam(value = "cliente", required = false) Integer cliente) {
		List<Pedido> list = new ArrayList<Pedido>();
		
		if (numeroControle == null && dataCadastro == null && cliente == null) {
			list = service.findAll();
		} else {
			list = service.findWithFiter(numeroControle, dataCadastro, cliente);
		}

		return ResponseEntity.ok().body(list);
	}
}
