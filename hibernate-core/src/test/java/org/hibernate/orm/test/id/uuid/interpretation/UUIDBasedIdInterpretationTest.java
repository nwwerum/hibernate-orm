/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.id.uuid.interpretation;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.Type;
import org.hibernate.type.UUIDBinaryType;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.DomainModelScope;
import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.orm.junit.RequiresDialect;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author Steve Ebersole
 */
@DomainModel(annotatedClasses = { UUIDBasedIdInterpretationTest.UuidIdEntity.class })
@SessionFactory
public class UUIDBasedIdInterpretationTest {

	@Test
	@JiraKey( "HHH-10564" )
	@RequiresDialect( H2Dialect.class )
	public void testH2(DomainModelScope scope) {
		checkUuidTypeUsed( scope, UUIDBinaryType.class );
	}

	@Test
	@JiraKey( "HHH-10564" )
	@RequiresDialect( value = MySQLDialect.class, version = 500 )
	public void testMySQL(DomainModelScope scope) {
		checkUuidTypeUsed( scope, UUIDBinaryType.class );
	}

	@Test
	@JiraKey( "HHH-10564" )
	@RequiresDialect( value = PostgreSQLDialect.class, version = 940 )
	public void testPostgreSQL(DomainModelScope scope) {
		checkUuidTypeUsed( scope, PostgresUUIDType.class );
	}

	@Test
	@JiraKey( "HHH-10564" )
	@RequiresDialect(H2Dialect.class)
	public void testBinaryRuntimeUsage(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			session.byId( UuidIdEntity.class ).load( UUID.randomUUID() );
		} );
	}

	private void checkUuidTypeUsed(DomainModelScope scope, Class<? extends Type> uuidTypeClass) {
		final PersistentClass entityBinding = scope.getDomainModel().getEntityBinding( UuidIdEntity.class.getName() );
		final Type idPropertyType = entityBinding.getIdentifier().getType();
		assertThat( idPropertyType, instanceOf( uuidTypeClass ) );
	}

	@Entity(name = "UuidIdEntity")
	@Table(name = "UUID_ID_ENTITY")
	public static class UuidIdEntity {
		@Id
		@GeneratedValue
		private UUID id;
	}
}