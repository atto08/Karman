package com.project.Karman.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tactical_documents")
public class TacticalDocument {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "vector(1536)") // pgvector 사용 시
    private float[] embedding;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
}
