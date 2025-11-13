package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AcaoRepository extends JpaRepository<Acao, Long> {

    //Retorna o maior número de ação.
    @Query("""
        SELECT max(a.numAcao)
        FROM Acao a
        WHERE a.ticket = :ticket
    """)
    public int findLastNumAcao(@Param("ticket") Ticket ticket);

    public Optional<Acao> findByIdAndTicketId(Long acaoId, Long ticketId);
}
