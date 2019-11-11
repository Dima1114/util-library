package venus.utillibrary.repository.base

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.transaction.annotation.Transactional
import venus.utillibrary.model.base.User
import java.util.*

@RepositoryRestResource
interface UserRepository : BaseRepository<User> {

    @EntityGraph(attributePaths = ["roles"])
    fun findByUsername(username: String): Optional<User>

    @Modifying
    @RestResource(exported = false)
    @Transactional
    @Query("update User u set u.refreshToken = :token where u.username =:username")
    fun updateRefreshToken(@Param("username") username: String, @Param("token") refreshToken: String?): Int?
}
