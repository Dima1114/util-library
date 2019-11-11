package venus.utillibrary.enumeration

interface EnumResourceService {

    fun getEnumResource(name: String) : List<EnumValue>
    fun getEnumResource(name: String, packageName: String) : List<EnumValue>
}
