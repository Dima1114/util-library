package venus.searchcore.validation

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component
import javax.validation.Validator


@Component
class ValidatorAddingCustomizer(private val provider: ObjectProvider<Validator>) : HibernatePropertiesCustomizer {

    override fun customize(hibernateProperties: MutableMap<String, Any>) {
        provider.ifUnique?.let { hibernateProperties["javax.persistence.validation.factory"] = it }
    }
}