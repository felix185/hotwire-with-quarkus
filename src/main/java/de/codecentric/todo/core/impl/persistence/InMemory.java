package de.codecentric.todo.core.impl.persistence;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Qualifier for in-memory repository adapter.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface InMemory {
}
