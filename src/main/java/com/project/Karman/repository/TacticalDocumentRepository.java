package com.project.Karman.repository;

import com.project.Karman.domain.entity.TacticalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface TacticalDocumentRepository extends JpaRepository<TacticalDocument, UUID> {

    @Query(value = """
            SELECT * FROM tactical_documents
            WHERE metadata @> CAST(:metadataJson AS jsonb)
            """, nativeQuery = true)
    List<Map<String, Object>> findByMetadataJson(@Param("metadataJson") String metadataJson);
}
