package venus.utillibrary.enumeration

import venus.utillibrary.function.takeOrThrow
import org.reflections.Reflections
import venus.utillibrary.security.exceptions.JwtAuthenticationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.stereotype.Service
import venus.utillibrary.enumeration.exception.ResourceNotFoundException
import venus.utillibrary.service.getUserFromContext
import java.util.*

@Service
@ConditionalOnClass(EnumResource::class)
class EnumResourceServiceImpl(@Value("\${reflection.enum.root-path}") private val rootPackage: String? = null) : EnumResourceService {

    override fun getEnumResource(name: String, packageName: String): List<EnumValue> {
        val className = name.capitalize()

        val candidate = Reflections(packageName).getTypesAnnotatedWith(EnumResource::class.java)
                .asSequence()
                .filter { it.isEnum }
                .find { it.simpleName == className } ?: throw ResourceNotFoundException("Requested resource not found")

        return candidate
                .takeOrThrow(::checkPermissions) { JwtAuthenticationException("You don`t have permission to access") }
                .enumConstants
                .map { EnumValue((it as Enum<*>).ordinal, it.name,  it.name.replace("_", " ")) }
                .toList()
    }

    override fun getEnumResource(name: String): List<EnumValue> {
        rootPackage ?: throw IllegalArgumentException(
                "specify root package name as 'reflection.enum.root-path' in your property file")

        return getEnumResource(name, rootPackage)
    }

    private fun checkPermissions(clazz: Class<*>): Boolean {

        val authorities = getUserFromContext()?.roles ?: return false
        val securedBy = clazz.getAnnotation(EnumResource::class.java).secured.toList()

        return securedBy.isEmpty() || !Collections.disjoint(authorities, securedBy)
    }
}

data class EnumValue (val ordinal: Int, val name: String, val label: String)
