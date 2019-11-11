package venus.utillibrary.entity

import venus.utillibrary.enumeration.EnumResource
import venus.utillibrary.model.base.Role

@EnumResource(secured = [Role.ROLE_READ])
enum class TestEnum2{
    GET, POST
}