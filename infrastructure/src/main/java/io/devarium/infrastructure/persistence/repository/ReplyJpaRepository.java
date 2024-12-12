package io.devarium.infrastructure.persistence.repository;

import io.devarium.infrastructure.persistence.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {

}
