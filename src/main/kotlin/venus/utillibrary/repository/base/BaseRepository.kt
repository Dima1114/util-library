package venus.utillibrary.repository.base

import venus.utillibrary.model.base.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface BaseRepository<T : BaseEntity<T>> : JpaRepository<T, Long>, QuerydslPredicateExecutor<T>