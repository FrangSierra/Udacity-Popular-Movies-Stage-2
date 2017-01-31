package frangsierra.popularmoviesudacity.core.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Root scope used by single instance classes.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationScope {
}
