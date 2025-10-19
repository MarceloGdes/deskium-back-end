package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Suporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuporteRepository extends JpaRepository<Suporte, Long> {

    //Query busca todos os suportes com usuário ativos.
    // Faz um LEFT JOIN na tabela de tickets, filtrando apenas os com Status de aberto e
    // vincula a cada suporte
    //Suportes sem tickets irão ser apresentados também, por conta do LEFT JOIN
    //Agupa os registros por suporte e ordena de forma crescente, com base na quantidade de tickets de cada um.
    @Query("""
        SELECT s 
        FROM Suporte s
        INNER JOIN Usuario u 
            on s.usuario.id = u.id 
            and u.ativo = TRUE
        LEFT JOIN Ticket t 
            ON t.suporte.id = s.id 
            AND t.status = 'ABERTO'
        GROUP BY s.id
        ORDER BY COUNT(t.id) ASC
        """)
    List<Suporte> findSuportesOrderByTicketsAbertosAsc();

}
