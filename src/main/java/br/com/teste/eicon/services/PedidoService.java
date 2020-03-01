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

	/*
	 * O método find verifica se o id já foi inserido no banco de dados, é utilizado
	 * no método insert, como forma de verificar se o objeto pode ser inserido ou
	 * não. Se o método retornar null, o objeto não existe no banco de dados.
	 * 
	 */

	public Pedido find(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		return pedido.orElse(null);
	}
	
	/*
	 * O método findWIthFilter faz um filtro na lista que vai ser retornada para
	 * a requisição GET, de acordo com os parâmetros opcionais que foram
	 * recebidos. A cadeia de IFs verifica quais parâmetros foram preenchidos
	 * e quais vieram nulos, e preenche a lista com a resposta.
	 * 
	 */

	public List<Pedido> findWithFiter(Integer numeroControle, String dataCadastro, Integer cliente) {
		List<Pedido> pedidos = findAll();
		List<Pedido> response = new ArrayList<Pedido>();

		dataCadastro = putSlashInDateString(dataCadastro);

		for (Pedido p : pedidos) {
			if (numeroControle != null && dataCadastro != null && cliente != null) {
				if (numeroControle == p.getNumeroControle() && dataCadastro.equals(p.getDataCadastro())
						&& cliente == p.getCodigoCliente()) {
					response.add(p);
				}
			} else if (numeroControle == null && dataCadastro == null && cliente != null) {
				if (cliente == p.getCodigoCliente()) {
					response.add(p);
				}
			} else if (numeroControle == null && dataCadastro != null && cliente == null) {
				if (dataCadastro.equals(p.getDataCadastro())) {
					response.add(p);
				}
			} else if (numeroControle == null && dataCadastro != null && cliente != null) {
				if (dataCadastro.equals(p.getDataCadastro()) && cliente == p.getCodigoCliente()) {
					response.add(p);
				}
			} else if (numeroControle != null && dataCadastro == null && cliente != null) {
				if (numeroControle == p.getNumeroControle() && cliente == p.getCodigoCliente()) {
					response.add(p);
				}
			} else if (numeroControle != null && dataCadastro != null && cliente == null) {
				if (numeroControle == p.getNumeroControle() && dataCadastro.equals(p.getDataCadastro())) {
					response.add(p);
				}
			} else if (numeroControle != null && dataCadastro == null && cliente == null) {
				if (numeroControle == p.getNumeroControle()) {
					response.add(p);
				}
			}
		}

		return response;
	}
	
	/*
	 * O método insert envia o objeto para o repositório, para 
	 * que ele seja inserido no banco de dados.
	 * 
	 */

	public Pedido insert(Pedido obj) {
		return repo.save(obj);
	}
	
	/*
	 * Recebe a lista de pedidos que foi enviada pelo método insert da classe PedidoResource,
	 * aplica as regras de negócio e monta uma lista de strings como resposta. Cada item da 
	 * lista de resposta representa um objeto que foi recebido e informa se o pedido foi cadastrado
	 * ou não, além do motivo pelo qual ele não foi inserido no banco de dados.
	 * 
	 * As regras de negócio aplicadas são:
	 * 
	 * - A lista de pedidos deve ter tamanho entre 1 e 10
	 * - O Número de Controle, o Nome, o Valor e o Código do Cliente dos pedidos recebidos não podem
	 * ser nulos.
	 * - O Número de Controle deve ser único no banco de dados
	 * - Se a quantidade do produto não for informada, é considerado 1 produto apenas
	 * - Se a data do produto não for informada, é considerada a data atual do sistema,
	 * utilizando o formato dd-MM-yyyy
	 * - O valor total do pedido é calculado, e se a quantidade de produtos for maior que 5,
	 * esse valor recebe 5% de desconto, se a quantidade for a partir de 10, recebe 10% de desconto
	 * 
	 */
	
	

	public ResponseEntity<List<String>> insertList(List<Pedido> pedidos) {
		List<String> response = new ArrayList<String>();

		if (pedidos.size() >= 1 && pedidos.size() <= 10) {
			for (Pedido pedido : pedidos) {
				if (pedido.getNumeroControle() == null) {
					response.add("Pedido não cadastrado, o Numero de Controle não pode ser nulo: " + pedido.toString());
				} else if (pedido.getNome() == null) {
					response.add("Pedido não cadastrado, o Nome do Produto não pode ser nulo: " + pedido.toString());
				} else if (pedido.getValor() == null) {
					response.add("Pedido não cadastrado, o Valor do Produto não pode ser nulo: " + pedido.toString());
				} else if (pedido.getCodigoCliente() == null) {
					response.add("Pedido não cadastrado, o Código do Cliente não pode ser nulo: " + pedido.toString());
				} else if (find(pedido.getNumeroControle()) == null) {

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
	
	/*
	 * O método putSlashInDateString serve para colocar as barras( / ) na String da
	 * data, já que a data recebida atráves da url nontém as barras, esse método ajusta
	 * a String para que o método findWithFilter possa fazer as comparações corretamente.
	 * Caso a data recebida seja nula, o método retorna null.
	 *  
	 */

	public String putSlashInDateString(String dataCadastro) {

		if (dataCadastro == null) {
			return null;
		}

		String data = "";
		data = String.valueOf(dataCadastro.charAt(0)) + dataCadastro.charAt(1) + "/" + dataCadastro.charAt(2)
				+ dataCadastro.charAt(3) + "/" + dataCadastro.charAt(4) + dataCadastro.charAt(5)
				+ dataCadastro.charAt(6) + dataCadastro.charAt(7);
		return data;
	}
	
	/*
	 * O método findAll retorna uma lista de todos os Pedidos que 
	 * foram cadastrados
	 * 
	 */

	public List<Pedido> findAll() {
		return repo.findAll();
	}

}
