package br.com.koch.repositorio;

import br.com.koch.modelo.admin.ClienteListar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteListar, Long> {
}
