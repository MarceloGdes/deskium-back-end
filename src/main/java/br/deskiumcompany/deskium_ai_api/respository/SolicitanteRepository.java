package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
    Optional<Solicitante> findByUsuarioId(Long id);

    @Query("""
           SELECT s FROM Solicitante s WHERE
           (:id IS NULL OR s.id = :id) AND
           (:nome IS NULL OR UPPER(s.usuario.nomeCompleto) LIKE CONCAT('%', UPPER(:nome), '%')) AND
           (:nomeEmpresa IS NULL OR UPPER(s.empresa.razaoSocial) LIKE CONCAT('%', UPPER(:nomeEmpresa), '%')))
           """)
    List<Solicitante> findByFiltros(
            @Param("id") Long id,
            @Param("nome") String nome,
            @Param("nomeEmpresa") String nomeEmpresa);
}
