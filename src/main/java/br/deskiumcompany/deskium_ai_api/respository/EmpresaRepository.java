package br.deskiumcompany.deskium_ai_api.respository;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);

    @Query("""
     SELECT e FROM Empresa e WHERE
        (:id IS NULL OR e.id = :id)
        AND (:razaoSocial IS NULL OR UPPER(e.razaoSocial) LIKE CONCAT('%', UPPER(:razaoSocial), '%'))
        AND (:cnpj IS NULL OR e.cnpj = :cnpj)
     """)
    List<Empresa> findByFiltros(
            @Param("id") Long id,
            @Param("razaoSocial") String razaoSocial,
            @Param("cnpj") String cnpj);
}
