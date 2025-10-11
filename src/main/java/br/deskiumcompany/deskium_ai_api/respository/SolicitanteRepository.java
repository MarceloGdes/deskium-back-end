package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
}
