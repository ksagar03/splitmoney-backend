package com.splitmoney.common.exception

/**
 * Thrown for all authentication / registration failures.
 * The [code] is forwarded to the GraphQL error extensions so the
 * frontend can branch on it without parsing the human-readable message.
 */
class AuthException(message: String, val code: String) : RuntimeException(message)
