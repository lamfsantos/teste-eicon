package br.com.teste.eicon.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.teste.eicon.domain.Pedido;
import br.com.teste.eicon.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	@Autowired
	private PedidoService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido pedido = service.findExternal(id);
		return ResponseEntity.ok().body(pedido);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> insert(
			@Valid @RequestBody List<Pedido> pedidos) {
		List<String> response = new ArrayList<String>();

		if(pedidos.size()>1 && pedidos.size()<10) {
			for (Pedido pedido : pedidos) {
				if (service.find(pedido.getNumeroControle()) == null) {

					if (pedido.getQuantidade() == null) {
						pedido.setQuantidade(1);
					}

					if (pedido.getDataCadastro() == null) {
						Date date = Calendar.getInstance().getTime();
						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
						pedido.setDataCadastro(dateFormat.format(date));
					}

					pedido.setValorTotalDoPedido(pedido.getValor() * pedido.getQuantidade());

					if (pedido.getQuantidade() >= 10) {
						pedido.setValorTotalDoPedido(
								pedido.getValorTotalDoPedido() - (pedido.getValorTotalDoPedido() * 10 / 100));
					} else if (pedido.getQuantidade() > 5) {
						pedido.setValorTotalDoPedido(
								pedido.getValorTotalDoPedido() - (pedido.getValorTotalDoPedido() * 5 / 100));
					}

					service.insert(pedido);
					response.add("Pedido cadastrado: " + pedido.toString());
				} else {
					response.add("Pedido não cadastrado, Numero de Controle já cadastrado: " + pedido.toString());
				}
			}
		}else if(pedidos.size()<1){
			response.add("Nenhum pedido cadastrado, o tamanho minimo da lista de pedidos é 1.");
		}else {
			response.add("Nenhum pedido cadastrado, o tamanho máximo da lista de pedidos é 10.");
		}
		
		
		return ResponseEntity.ok().body(response);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Pedido>> findAll() {
		List<Pedido> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

}
