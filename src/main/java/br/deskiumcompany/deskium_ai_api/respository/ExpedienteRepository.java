package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Expediente;
import br.deskiumcompany.deskium_ai_api.domain.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteRepository extends JpaRepository<Expediente, DiaSemana> {
}
