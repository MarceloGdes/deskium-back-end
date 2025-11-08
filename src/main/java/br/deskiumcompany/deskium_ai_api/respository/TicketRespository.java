package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.named-parameters
@Repository
public interface TicketRespository extends JpaRepository<Ticket, Long> {

    //Necessário QUERY personalizada devido a quantidade de filtros.
    //Where recebe duas operações boleanas. Caso o parametro seja nulo, irá retornar true
    // e a segunda operação não será executada, fazendo que esse filtro seja "ignorado"
    //Pela regra de negocio, um usuário nunca pode estar vinculado a um solicitante e um suporte.
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
            
        AND (CAST(:dataAberturaInicio AS timestamp) IS NULL
            OR CAST(:dataAberturaFim AS timestamp) IS NULL
            OR t.criadoEm BETWEEN :dataAberturaInicio AND :dataAberturaFim)
            
        AND (CAST(:dataFechamentoInicio AS timestamp) IS NULL
            OR CAST(:dataFechamentoFim AS timestamp) IS NULL
            OR t.dataResolucao BETWEEN :dataFechamentoInicio AND :dataFechamentoFim)
        
    """)
//    *Utilizado o cast para apontar para o JPQL qual o tipo do dado que estamos validando se
//    é nulo, pois ele não tem esse contexto. No comparação com o Between, o tipo do dado é
//    entendido como igual da coluna.

    List<Ticket> findAll(
            @Param("usuario")Usuario usuario,
            @Param("status")Status status,
            @Param("ticketId")Long ticketId,
            @Param("assunto")String assunto,
            @Param("suporteNome")String suporteNome,
            @Param("subStatus")SubStatus subStatus,
            @Param("motivoId")Long motivoId,
            @Param("categoriaId")Long categoriaId,
            @Param("solicitanteNome") String solicitanteNome,
            @Param("dataAberturaInicio") LocalDateTime dataAberturaInicio,
            @Param("dataAberturaFim") LocalDateTime dataAberturaFim,
            @Param("dataFechamentoInicio") LocalDateTime dataFechamentoInicio,
            @Param("dataFechamentoFim") LocalDateTime dataFechamentoFim);

    Optional<Ticket> findByIdAndSolicitanteUsuarioId(Long ticketId, Long usuarioId);
}
