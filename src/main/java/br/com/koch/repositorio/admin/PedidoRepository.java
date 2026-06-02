package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente_Id(Integer clienteId);

    boolean existsByCliente_IdAndProduto_IdAndAtivoTrue(Integer clienteId, Long produtoId);

    Optional<Pedido> findFirstByCliente_IdAndProduto_IdAndAtivoTrue(Integer clienteId, Long produtoId);
}
