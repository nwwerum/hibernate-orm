/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.annotations.processing;

import org.hibernate.Incubating;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Identifies a method of an abstract class or interface as defining
 * the signature of a method which is used to execute the given
 * {@linkplain #value SQL query}, with an implementation generated
 * automatically by the Hibernate Metamodel Generator.
 * <p>
 * For example:
 * <pre>
 * public interface Books {
 *     &#64;SQL("select * from Book where isbn = :isbn")
 *     Book findBookByIsbn(String isbn);
 *
 *     &#64;SQL("select * from Book where title like ?1 order by title offset ?3 fetch first ?2 rows only")
 *     List&lt;Book&gt; findBooksByTitleWithPagination(String title, int max, int start);
 *
 *     &#64;SQL("select * from Book where title like ?1")
 *     Query findBooksByTitle(String title);
 * }
 * </pre>
 * <p>
 * The Metamodel Generator automatically creates an "implementation"
 * of these methods in the static metamodel class {@code Books_}.
 * The generated methods may be called according to the following
 * protocol:
 * <pre>
 * Book book = Books_.findBookByIsbn(session, isbn);
 * List&lt;Book&gt; books = Books_.findBooksByTitleWithPagination(session, pattern, 10, 0);
 * </pre>
 * <p>
 * Notice the extra parameter of type {@code EntityManager} at the
 * start of the parameter list.
 * <p>
 * Alternatively, the type to which the annotated method belongs may
 * also declare an abstract method with no parameters which returns
 * one of the types {@link jakarta.persistence.EntityManager},
 * {@link org.hibernate.StatelessSession},
 * or {@link org.hibernate.Session}, for example:
 * <pre>
 * EntityManager entityManager();
 * </pre>
 * In this case:
 * <ul>
 * <li>the generated method is no longer {@code static},
 * <li>the generated method will use this method to obtain the
 *     session object, instead of having a parameter of type
 *     {@code EntityManager}, and
 * <li>the generated static metamodel class will actually implement
 *     the type which declares the method annotated {@code @SQL}.
 * </ul>
 * <p>
 * Thus, the generated methods may be called according to the following
 * protocol:
 * <pre>
 * Books books = new Books_(session);
 * Book book = books.findBookByIsbn(isbn);
 * List&lt;Book&gt; books = books.findBooksByTitleWithPagination(pattern, 10, 0);
 * </pre>
 * <p>
 * This is reminiscent of traditional DAO-style repositories.
 * <p>
 * The return type of an annotated method must be:
 * <ul>
 * <li>an entity type,
 * <li>{@link java.util.List},
 * <li>{@link org.hibernate.query.Query},
 * <li>{@link jakarta.persistence.Query}, or
 * <li>{@link org.hibernate.query.NativeQuery}.
 * </ul>
 * <p>
 * The method parameters must match the parameters of the SQL query,
 * either by name or by position:
 * <ul>
 * <li>an ordinal query parameter of form {@code ?n} is matched to
 *     the <em>n</em>th parameter of the method, and
 * <li>a named query parameter of form {@code :name} is matched to
 *     the method parameter {@code name}.
 * </ul>
 *
 * @see HQL
 * @see Find
 *
 * @author Gavin King
 * @since 6.3
 */
@Target(METHOD)
@Retention(CLASS)
@Incubating
public @interface SQL {
	String value();
}
