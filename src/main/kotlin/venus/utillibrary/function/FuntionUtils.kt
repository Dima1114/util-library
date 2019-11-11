package venus.utillibrary.function

fun throwIf(condition: Boolean, thr: () -> Exception) {
    if (condition) throw thr()
}

fun <T> T.takeOrThrow(predicate: (T) -> Boolean, thr: () -> Exception): T {
    if (predicate(this)) return this
    else throw thr()
}