package com.library.railway

import arrow.core.Either
import com.library.loans.domain.ErrorMessage

typealias TwoTrack<T> = Either<ErrorMessage, T>
typealias Success<T> = Either.Right<T>
typealias Failure = Either.Left<ErrorMessage>

typealias input<T> = Success<T>
