package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRespository extends JpaRepository<Ticket, Long> {

    //Necessário QUERY personalizada devido a quantidade de tickets.
    //Where recebe duas operações boleanas. Caso o parametro seja nulo, irá retornar true e a segunda operação
    //Não será executada e vice-versa.
    @Query("""
        SELECT t
        FROM Ticket t
        INNER JOIN Solicitante s
            ON s.usuario = :usuario
        WHERE (:status IS NULL OR t.status = :status)
    """)
    List<Ticket> findAllBySolicitante(Usuario usuario, Status status);
}
