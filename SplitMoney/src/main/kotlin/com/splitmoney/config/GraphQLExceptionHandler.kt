package com.splitmoney.config

import com.splitmoney.common.exception.AuthException
import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

/**
 * Maps domain exceptions to proper GraphQL errors.
 *
 * Without this, Spring for GraphQL masks all RuntimeExceptions as
 * "INTERNAL_ERROR" and hides the message — the client never sees the reason.
 *
 * Returning null for unhandled types lets Spring apply its default behaviour.
 */
@Component
class GraphQLExceptionHandler : DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (ex) {
            is AuthException -> GraphqlErrorBuilder.newError(env)
                .message(ex.message ?: "Authentication error")
                .errorType(ErrorType.ValidationError)
                .extensions(mapOf("code" to ex.code))
                .build()
            else -> null  // let Spring handle everything else
        }
    }
}
