// Copyright (C) 2025 Zac Sweers
// SPDX-License-Identifier: Apache-2.0
package dev.zacsweers.metro

/**
 * This annotation is used on [DependencyGraph] creators to indicate that the annotated parameter is
 * a parent graph that this graph _extends_.
 *
 * The target graph _must_ be extendable, which is denoted by the [DependencyGraph.isExtendable]
 * value.
 */
@Target(AnnotationTarget.VALUE_PARAMETER) public annotation class Extends
