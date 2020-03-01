package br.com.teste.eicon.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.teste.eicon.domain.Pedido;
import br.com.teste.eicon.repositories.PedidoRepository;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository repo;

	public Pedido find(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		return pedido.orElse(null);
	}
	
	public Boolean isNotInTheList(List<Pedido> pedidos, Pedido pedido) {
		for (Pedido p : pedidos) {
			if(p.equals(pedido)) {
				return false;
			}
		}
		return true;
	}

	public List<Pedido> findWithFiter(Integer numeroControle, String dataCadastro, Integer cliente) {
		List<Pedido> pedidos = findAll();
		List<Pedido> response = new ArrayList<Pedido>();

		for (Pedido p : pedidos) {

			if (numeroControle != null) {
				if (numeroControle == p.getNumeroControle()) {
					if(isNotInTheList(response, p)) {
						response.add(p);
					}
				}
			}

			if (dataCadastro != null) {
				String data = "";
				data = String.valueOf(dataCadastro.charAt(0)) + dataCadastro.charAt(1) + "/" + dataCadastro.charAt(2)
						+ dataCadastro.charAt(3) + "/" + dataCadastro.charAt(4) + dataCadastro.charAt(5)
						+ dataCadastro.charAt(6) + dataCadastro.charAt(7);
				if (data.equals(p.getDataCadastro())) {
					if(isNotInTheList(response, p)) {
						response.add(p);
					}
				}
			}

			if (cliente != null) {
				if (cliente == p.getCodigoCliente()) {
					if(isNotInTheList(response, p)) {
						response.add(p);
					}
				}
			}

		}

		return response;
	}

	public Pedido findByData() {
		return null;
	}

	public Pedido findByCliente() {
		return null;
	}

	public Pedido findExternal(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		return pedido.orElseThrow(() -> new br.com.teste.eicon.services.exceptions.ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + " Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		return repo.save(obj);
	}

	public ResponseEntity<List<String>> insertList(List<Pedido> pedidos) {
		List<String> response = new ArrayList<String>();

		if (pedidos.size() >= 1 && pedidos.size() <= 10) {
			for (Pedido pedido : pedidos) {
				if (find(pedido.getNumeroControle()) == null) {

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

					insert(pedido);
					response.add("Pedido cadastrado: " + pedido.toString());
				} else {
					response.add("Pedido não cadastrado, Numero de Controle já cadastrado: " + pedido.toString());
				}
			}
		} else if (pedidos.size() < 1) {
			response.add("Nenhum pedido cadastrado, o tamanho minimo da lista de pedidos é 1.");
		} else {
			response.add("Nenhum pedido cadastrado, o tamanho máximo da lista de pedidos é 10.");
		}

		return ResponseEntity.ok().body(response);
	}

	public List<Pedido> findAll() {
		return repo.findAll();
	}

}
