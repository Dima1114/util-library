package venus.utillibrary.projection.api

import org.springframework.data.rest.core.config.Projection
import venus.utillibrary.model.base.User

@Projection(name = "info", types = [User::class])
interface UserInfoProjection : BaseProjection {

    val username: String
    val email: String
}
