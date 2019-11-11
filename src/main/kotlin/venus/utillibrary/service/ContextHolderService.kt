package venus.utillibrary.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import venus.utillibrary.security.JwtUserDetails
import venus.utillibrary.model.base.User
import venus.utillibrary.security.getUser

fun getUserFromContext(): User? {
    val auth = getContext()

    return auth?.principal
            .let { it as JwtUserDetails? }
            ?.getUser()
}

fun getRolesFromContext(): List<GrantedAuthority> {
    val auth = getContext()

    return auth?.authorities?.toList() ?: emptyList()
}

private fun getContext(): Authentication? {
    return SecurityContextHolder.getContext()
            ?.authentication
            ?.takeIf { it is UsernamePasswordAuthenticationToken }
}