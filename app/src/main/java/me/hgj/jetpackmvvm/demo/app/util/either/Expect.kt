package me.hgj.jetpackmvvm.demo.app.util.either

import java.io.Closeable
import java.util.logging.Logger


typealias Expect<R, T> = Either<R, T>
typealias Failure<R> = Either.Left<R>
typealias Success<T> = Either.Right<T>
val FAILURE: Either.Left<Nothing?> = null.failure()
val SUCCESS: Either.Right<Nothing?> = null.success()

/**
 * 构造 [RIGHT]
 */
fun <T> T.success(): Success<T> = right()
/**
 * 构造 [LEFT]
 */
fun <R> R.failure(): Failure<R> = left()

/**
 * 转换 [LEFT]
 */
inline fun <R, T, F> Expect<R, T>.failTransform(transform: (R) -> F): Expect<F, T> = mapLeft(transform)

/**
 * 带有默认值的 [RIGHT]
 */
fun <R, T : Any> Expect<R, T>.successOrDefault(defaultValue: T): T = getOrElse { defaultValue }
/**
 * 可空的 [RIGHT]
 */
fun <R, T> Expect<R, T>.successOrNull(): T? = getOrElse { null }
/**
 * 带有默认值的 [RIGHT]
 */
inline fun <R, T : Any> Expect<R, T>.successOrNull(block: (T) -> Unit): T? = successOrNull()?.apply { block(this) }
// 可能产生异常的结果
typealias MaybeOccurException<T> = Expect<Throwable, T>
/**
 * 捕获异常的代码块，返回Expect.开发阶段如果需要throw异常方便调试和跟进问题,[areExceptionsThrownAsDebug]设置true，默认为false
 */
inline fun <T> catch(areExceptionsThrownAsDebug: Boolean = false, block: () -> T): MaybeOccurException<T> = try {
    block().success()
} catch (e: Throwable) {
    e.printStackTrace()
    e.failure()
}
/**
 * 捕获use时的异常转换为Failure
 */
inline fun <T : Closeable?, R> T.useWithCatch(areExceptionsThrownAsDebug: Boolean = false, block: (T) -> R): MaybeOccurException<R> =
    catch(areExceptionsThrownAsDebug) {
        use {
            block(this)
        }
    }



/**
 * Either.Right 的 suspend 扩展
 */
suspend inline fun <A, B, C> Either<A, B>.flatMapConcat(crossinline transform: suspend (B) -> Either<A, C>): Either<A, C> =
    when (this) {
        is Either.Right -> transform(value)
        is Either.Left -> this
    }

/**
 * Either.Left 的 suspend 扩展
 */
suspend inline fun <A, B, C> Either<A, B>.flatMapLeft(crossinline transform: suspend (A) -> Either<C, B>): Either<C, B> =
    when (this) {
        is Either.Right -> this
        is Either.Left -> transform(value)
    }

/**
 * 捕获结果
 */
inline fun <A, B> Either<A, B>.onSuccess(block: (B) -> Unit): Either<A, B> {
    if (this is Either.Right) block(value)
    return this
}

/**
 * 捕获失败
 */
inline fun <A, B> Either<A, B>.onFailure(block: (A) -> Unit): Either<A, B> {
    if (this is Either.Left) block(value)
    return this
}

/**
 * 执行 block
 */
inline fun <A, B> Either<A, B>.run(block: () -> Unit): Either<A, B> {
    block()
    return this
}