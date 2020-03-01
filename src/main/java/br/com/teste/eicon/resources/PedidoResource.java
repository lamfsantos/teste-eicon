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

	/*
	 * O método insert recebe uma requisição via POST e como parâmetro uma Lista de pedidos via JSON ou XML
	 * em /pedidos e passa para o service aplicar as regras de negócio e enviar os objetos para serem
	 * inseridos no banco ou não.
	 * 
	 * */
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<String>> insert(@Valid @RequestBody List<Pedido> pedidos) {
		return service.insertList(pedidos);
	}
	
	/*
	 * O método find recebe uma requisição via GET em /pedidos com os parâmetros opcionais de URL
	 * numeroControle, dataCadastro e cliente, se os três parâmetros forem nulos,
	 * a requisição veio sem nenhum parâmetro, nesse caso ele chama o método findAll()
	 * e retorna uma lista com todos os pedidos cadastrados. Caso contrário, se algum dos
	 * parâmetros existir, ele chama o método findWithFilter para realizar uma busca
	 * personalizada, de acordo com os parâmetros que foram recebidos.
	 * 
	 * */

	@RequestMapping(method = RequestMethod.GET, produces = { 
			MediaType.APPLICATION_JSON_VALUE,
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
