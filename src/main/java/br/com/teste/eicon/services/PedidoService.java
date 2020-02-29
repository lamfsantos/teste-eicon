package br.com.teste.eicon.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public Pedido findExternal(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		return pedido.orElseThrow(() -> new br.com.teste.eicon.services.exceptions.
				ObjectNotFoundException("Objeto não encontrado! Id: " + id + " Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		return repo.save(obj);
	}

	public List<Pedido> findAll() {
		return repo.findAll();
	}

}
