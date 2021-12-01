package me.hgj.jetpackmvvm.demo.app.util.either

/**
 * [real]
 */
inline fun bind(condition: () -> Boolean) = real(condition)

/**
 * condition 为 True 返回 RIGHT
 */
inline fun real(condition: () -> Boolean): Either<Nothing?, Boolean> = if (condition()) true.right() else END

/**
 * 判断不为空
 */
inline fun <B> notNull(condition: () -> B?): Either<Nothing?, B> = condition()?.right() ?: END

/**
 * 判断集合为空
 */
inline fun emptyCollection(condition: () -> Collection<*>): Either<Nothing?, Collection<*>> {
    val rightValue = condition()
    return if (rightValue.isEmpty()) rightValue.right() else END
}
/**
 * 判断集合不为空
 */
inline fun notEmptyCollection(condition: () -> Collection<*>): Either<Nothing?, Collection<*>> {
    val rightValue = condition()
    return if (rightValue.isNotEmpty()) rightValue.right() else END
}
/**
 * 判断数组为空
 */
inline fun emptyArray(condition: () -> Array<*>): Either<Nothing?, Array<*>> {
    val rightValue = condition()
    return if (rightValue.isEmpty()) rightValue.right() else END
}
/**
 * 判断数组不为空
 */
inline fun notEmptyArray(condition: () -> Array<*>): Either<Nothing?, Array<*>> {
    val rightValue = condition()
    return if (rightValue.isNotEmpty()) rightValue.right() else END
}

/**
 * @param A 错误对象
 * @param B 正确对象
 */
sealed class Either<out A, out B> {
    /**
     * 错误
     */
    data class Left<A>(val value: A) : Either<A, Nothing>() {
        override fun toString() = "Left($value)"
    }
    /**
     * 正确
     */
    data class Right<B>(val value: B) : Either<Nothing, B>() {
        override fun toString() = "Right($value)"
    }

    /**
     * 映射正确的值
     *
     * @param C 映射后的类型
     * @param transform 映射函数
     * @return 映射后的 Either
     */
    inline fun <C> map(transform: (B) -> C): Either<A, C> = when (this) {
        is Left -> this
        is Right -> Right(transform(value))
    }

    /**
     * 映射错误的值
     *
     * @param C 映射后的类型
     * @param transform 映射函数
     * @return 映射后的 Either
     */
    inline fun <C> mapLeft(transform: (A) -> C): Either<C, B> = when (this) {
        is Left -> Left(transform(value))
        is Right -> this
    }
}

/**
 * 水平映射
 *
 * @param A 错误类型
 * @param B 映射前的正确类型
 * @param C 映射后的正确类型
 * @param transform 映射函数
 * @return 映射后的 Either
 */
inline fun <A, B, C> Either<A, B>.flatMap(transform: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Either.Right -> transform(value)
    is Either.Left -> this
}

/**
 * 获取正确的值
 */
inline fun <A, B> Either<A, B>.getOrElse(default: () -> B): B = when (this) {
    is Either.Left -> default()
    is Either.Right -> value
}

/**
 * 构造错误的 Either
 */
fun <R> R.left(): Either.Left<R> = Either.Left(this)
/**
 * 构造正确的 Either
 */
fun <T> T.right(): Either.Right<T> = Either.Right(this)

/**
 * [left]
 */
val LEFT = null.left()

/**
 * [right]
 */
val RIGHT = null.right()

/**
 * [LEFT]
 */
val END = LEFT

/**
 * [END]
 */
val BEGIN = RIGHT