package venus.utillibrary.model.base

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_READ,
    ROLE_WRITE;

    override fun getAuthority(): String {
        return this.name
    }
}
