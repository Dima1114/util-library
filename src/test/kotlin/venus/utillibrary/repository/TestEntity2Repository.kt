package venus.utillibrary.repository

import venus.utillibrary.entity.TestEntity
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface TestEntity2Repository : TestBaseRepository<TestEntity>