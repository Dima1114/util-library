package venus.utillibrary.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface TestBaseRepository<T> : JpaRepository<T, Long>, QuerydslPredicateExecutor<T>