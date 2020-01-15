package com.library.railway

import com.library.loans.domain.ErrorMessage

operator fun <T, V> TwoTrack<T>.rangeTo(other: (TwoTrack<T>) -> TwoTrack<V>): TwoTrack<V> {
    return other(this)
}

fun <T, V> TwoTrack<T>.pipe(other: (TwoTrack<T>) -> TwoTrack<V>): TwoTrack<V> {
    return other(this)
}

// take a y-shaped switch function and turn it into a two-track in/out
fun <A,B> convertToTwoTrack(a: (A) -> TwoTrack<B>): (TwoTrack<A>) -> TwoTrack<B> {
    return { input: TwoTrack<A> ->
        when(input) {
            is Success -> { a(input.b) }
            is Failure -> { input }
        }
    }
}

// take a one-track (success only) and turn it into two-track
fun <A,B> map(a: (A) -> B): (TwoTrack<A>) -> TwoTrack<B> {
    return { input: TwoTrack<A> ->
        when(input) {
            is Success -> { Success(a(input.b)) }
            is Failure -> { input }
        }
    }
}

// take a deadend no-op and turn it into one-track
fun <A> tee(a: (A) -> Unit): (A) -> A {
    return { input: A ->
        a(input)
        input
    }
}

// take a one-track that throws an exception and turn it into y-shape
fun <A,B> handle(a: (A) -> B): (A) -> TwoTrack<B> {
    return { input: A ->
        try {
            Success(a(input))
        } catch (e: Exception) {
            Failure(ErrorMessage.SystemError)
        }
    }
}