package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.named-parameters
@Repository
public interface TicketRespository extends JpaRepository<Ticket, Long> {

    //Necessário QUERY personalizada devido a quantidade de filtros.
    //Where recebe duas operações boleanas. Caso o parametro seja nulo, irá retornar true
    // e a segunda operação não será executada, fazendo que esse filtro seja "ignorado"
    //Pela regra de negocio, um usuário nunca pode estar vinculado a um solicitante ou suporte.
    //Se o usuário for nulo, retorna tickets de todos
    @Query("""
        SELECT t
        FROM Ticket t
        INNER JOIN Acao a
            ON a.ticket = t
        WHERE (:usuario IS NULL 
           OR t.suporte.usuario = :usuario
           OR t.solicitante.usuario = :usuario)
        AND (:ticketId IS NULL OR t.id = :ticketId)
        AND (:status IS NULL OR t.status = :status)
        AND (:suporteNome IS NULL OR UPPER(t.suporte.usuario.nomeCompleto)
            LIKE concat('%', UPPER(:suporteNome), '%'))
        AND (:subStatus IS NULL OR t.subStatus = :subStatus)
        AND (:motivoId IS NULL OR t.motivo.id = :motivoId)
        AND (:categoriaId IS NULL OR t.categoria.id = :categoriaId)
        AND (:solicitanteNome IS NULL OR UPPER(t.solicitante.usuario.nomeCompleto)
            LIKE concat('%', UPPER(:solicitanteNome), '%'))
        AND (:assunto IS NULL
            OR UPPER(t.titulo) LIKE  UPPER(concat('%',:assunto, '%'))
            OR UPPER(a.textoPuro) LIKE UPPER(concat('%',:assunto, '%')))
    """)
    List<Ticket> findAll(
            @Param("usuario")Usuario usuario,
            @Param("status")Status status,
            @Param("ticketId")Long ticketId,
            @Param("assunto")String assunto,
            @Param("suporteNome")String suporteNome,
            @Param("subStatus")SubStatus subStatus,
            @Param("motivoId")Long motivoId,
            @Param("categoriaId")Long categoriaId,
            @Param("solicitanteNome") String solicitanteNome);

    Optional<Ticket> findByIdAndSolicitanteUsuarioId(Long ticketId, Long usuarioId);
}
