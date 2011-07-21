/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.jdbc;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.config.DatasourceConfig;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectListEvent;
import de.iritgo.aktario.core.iobject.IObjectListListener;
import de.iritgo.aktario.core.iobject.IObjectRequestEvent;
import de.iritgo.aktario.core.iobject.IObjectRequestListener;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.uid.DefaultIDGenerator;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.IObjectCreatedEvent;
import de.iritgo.aktario.framework.base.IObjectCreatedListener;
import de.iritgo.aktario.framework.base.IObjectDeletedEvent;
import de.iritgo.aktario.framework.base.IObjectDeletedListener;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.base.IObjectModifiedListener;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The <code>JDBCManager</code> persists data objects to relational database
 * through the use of the JDBC api.
 */
public class JDBCManager extends BaseObject implements Manager, IObjectListListener, IObjectCreatedListener,
				IObjectModifiedListener, IObjectRequestListener, IObjectDeletedListener
{
	/** Configured data sources. */
	private Map dataSources;

	/** The default data source. */
	private DataSource defaultDataSource;

	/**
	 * Create a new <code>JDBCManager</code>
	 */
	public JDBCManager ()
	{
		super ("persist.JDBCManager");
	}

	/**
	 * Initialize the <code>JDBCManager</code>.
	 *
	 * This method creates the data sources specified in the server
	 * configuration.
	 */
	public void init ()
	{
		dataSources = new HashMap ();

		try
		{
			Configuration config = Engine.instance ().getConfiguration ();

			for (DatasourceConfig datasourceConfig : config.getDataSources ())
			{
				DataSource ds = (DataSource) Class.forName (datasourceConfig.getDataSourceClass ()).newInstance ();

				if (PropertyUtils.isWriteable (ds, "driverClassName"))
				{
					PropertyUtils.setProperty (ds, "driverClassName", datasourceConfig.getDriverClass ());
				}

				if (PropertyUtils.isWriteable (ds, "username"))
				{
					PropertyUtils.setProperty (ds, "username", datasourceConfig.getUser ());
				}

				if (PropertyUtils.isWriteable (ds, "password"))
				{
					PropertyUtils.setProperty (ds, "password", datasourceConfig.getPassword ());
				}

				if (PropertyUtils.isWriteable (ds, "url"))
				{
					PropertyUtils.setProperty (ds, "url", datasourceConfig.getUrl ());
				}

				dataSources.put (datasourceConfig.getId (), ds);

				if (defaultDataSource == null)
				{
					defaultDataSource = ds;
				}

				Log.logInfo ("persist", "JDBCManager", "Created datasource '" + datasourceConfig.getId () + "'");
			}

			JDBCIDGenerator persistentIdGenerator = new JDBCIDGenerator (2, 2, 1000);

			persistentIdGenerator.load ();
			Engine.instance ().installPersistentIDGenerator (persistentIdGenerator);

			DefaultIDGenerator transientIdGenerator = new DefaultIDGenerator ((long) 1, (long) 2);

			Engine.instance ().installTransientIDGenerator (transientIdGenerator);
		}
		catch (Exception x)
		{
			Log.logError ("persist", "JDBCManager", "Error while creating the datasources: " + x);
		}

		Engine.instance ().getEventRegistry ().addListener ("objectcreated", this);
		Engine.instance ().getEventRegistry ().addListener ("objectmodified", this);
		Engine.instance ().getEventRegistry ().addListener ("objectrequested", this);
		Engine.instance ().getEventRegistry ().addListener ("objectremoved", this);
	}

	/**
	 * Called when the plugin is to be unloaded.
	 *
	 * This method closes all active data sources.
	 */
	public void unload ()
	{
		for (Iterator i = dataSources.entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry entry = (Map.Entry) i.next ();

			try
			{
				try
				{
					MethodUtils.invokeMethod ((DataSource) entry.getValue (), "close", null);
				}
				catch (NoSuchMethodException x)
				{
				}
				catch (IllegalAccessException x)
				{
				}
				catch (InvocationTargetException x)
				{
				}

				Log.logInfo ("persist", "JDBCManager", "Closed datasource '" + entry.getKey () + "'");
			}
			catch (Exception x)
			{
				Log.logError ("persist", "JDBCManager", "Error during closing the datasource '" + entry.getKey ()
								+ "': " + x);
			}
		}
	}

	/**
	 * Get the default data source.
	 *
	 * @return The default data source.
	 */
	public DataSource getDefaultDataSource ()
	{
		return defaultDataSource;
	}

	/**
	 * Called when a data object was added to or removed from an
	 * object list.
	 *
	 * @param event The object list event.
	 */
	public void iObjectListEvent (IObjectListEvent event)
	{
		if (event.getType () == IObjectListEvent.ADD)
		{
			insert ((DataObject) event.getObject (), (DataObject) event.getOwnerObject (), event.getListAttribute ());
		}

		if (event.getType () == IObjectListEvent.REMOVE)
		{
			delete ((DataObject) event.getObject (), (DataObject) event.getOwnerObject (), event.getListAttribute ());
		}
	}

	/**
	 * Called when an iritgo object was created.
	 *
	 * @param event The creation event.
	 */
	public void iObjectCreatedEvent (IObjectCreatedEvent event)
	{
		insert ((DataObject) event.getCreatedObject (), (DataObject) event.getOwnerObject (), event.getListAttribute ());
	}

	/**
	 * Called when an iritgo object was created.
	 *
	 * @param event The creation event.
	 */
	public void iObjectDeletedEvent (IObjectDeletedEvent event)
	{
		delete ((DataObject) event.getDeletedObject (), (DataObject) event.getOwnerObject (), event.getListAttribute ());
	}

	/**
	 * Called when an iritgo object was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent (IObjectModifiedEvent event)
	{
		update ((DataObject) event.getModifiedObject ());
	}

	/**
	 * Called when an iritgo object is requested.
	 *
	 * @param event The reuqest event.
	 */
	public void iObjectRequestEvent (IObjectRequestEvent event)
	{
	}

	/**
	 * Insert a new data object into the database.
	 *
	 * @param object The data object to create.
	 * @param owner The data object owning this one.
	 * @param listAttribute The name of the list attribute to which the new
	 *   object belongs.
	 */
	private void insert (DataObject object, DataObject owner, String listAttribute)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlFields = new StringBuffer ("id");
			StringBuffer sqlValues = new StringBuffer ("?");

			for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
			{
				Map.Entry attribute = (Map.Entry) i.next ();

				if (attribute.getValue () instanceof IObjectList)
				{
					continue;
				}

				sqlFields.append (", " + (String) attribute.getKey ());
				sqlValues.append (", ?");
			}

			String sql = "insert into " + object.getTypeId () + " (" + sqlFields.toString () + ") values ("
							+ sqlValues.toString () + ")";

			stmt = connection.prepareStatement (sql);
			putAttributesToStatement (object, stmt);
			stmt.execute ();

			Log.logVerbose ("persist", "JDBCManager", "CREATED " + object.getTypeId () + ":" + object.getUniqueId ()
							+ " |" + sql + "|");

			stmt.close ();

			if (owner != null)
			{
				sql = "insert into IritgoObjectList (type, id, attribute, elemType, elemId) values (?, ?, ?, ?, ?)";

				stmt = connection.prepareStatement (sql);
				stmt.setString (1, owner.getTypeId ());
				stmt.setLong (2, owner.getUniqueId ());
				stmt.setString (3, listAttribute);
				stmt.setString (4, object.getTypeId ());
				stmt.setLong (5, object.getUniqueId ());
				stmt.execute ();

				Log.logVerbose ("persist", "JDBCManager", "CREATED REFRENCE " + owner.getTypeId () + ":"
								+ owner.getUniqueId () + " => " + object.getTypeId () + ":" + object.getUniqueId ()
								+ " |" + sql + "|");
			}
		}
		catch (Exception x)
		{
			// 			Log.logError (
			// 				"persist", "JDBCManager", "Error while creating new database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Delete a data object from the database.
	 *
	 * @param object The data object to create.
	 * @param owner The data object owning this one.
	 * @param listAttribute The name of the list attribute to which the new
	 *   object belongs.
	 */
	private void delete (DataObject object, DataObject owner, String listAttribute)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlFields = new StringBuffer ("id");
			StringBuffer sqlValues = new StringBuffer ("?");

			String sql = "delete from IritgoObjectList where type='" + owner.getTypeId () + "'" + " AND id="
							+ owner.getUniqueId () + " AND attribute='" + listAttribute + "'" + " AND elemType='"
							+ object.getTypeId () + "'" + " AND elemId=" + object.getUniqueId ();

			stmt = connection.prepareStatement (sql);
			stmt.execute ();

			sql = "delete from " + object.getTypeId () + " where id=" + object.getUniqueId ();

			stmt = connection.prepareStatement (sql);
			stmt.execute ();

			Log.logVerbose ("persist", "JDBCManager", "Removed " + object.getTypeId () + ":" + object.getUniqueId ()
							+ " |" + sql + "|");

			stmt.close ();
		}
		catch (Exception x)
		{
			// 			Log.logError ("persist", "JDBCManager", "Error while removed a database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Store data object changes to the database.
	 *
	 * @param object The data object to update.
	 */
	private void update (DataObject object)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = defaultDataSource.getConnection ();

			StringBuffer sqlAssigns = new StringBuffer ("id=?");

			for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
			{
				Map.Entry attribute = (Map.Entry) i.next ();

				if (attribute.getValue () instanceof IObjectList)
				{
					continue;
				}

				sqlAssigns.append (", " + (String) attribute.getKey () + "=?");
			}

			String sql = "update " + object.getTypeId () + " set " + sqlAssigns.toString () + " where id="
							+ object.getUniqueId ();

			stmt = connection.prepareStatement (sql);
			putAttributesToStatement (object, stmt);
			stmt.execute ();

			Log.logVerbose ("persist", "JDBCManager", "UPDATE " + object.getTypeId () + ":" + object.getUniqueId ()
							+ " |" + sql + "|");
		}
		catch (Exception x)
		{
			// 			Log.logError (
			// 				"persist", "JDBCManager", "Error while creating new database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}

	/**
	 * Transfer all attribute values of a data object to the specified
	 * prepared statement.
	 *
	 * @param object The data object to transfer.
	 * @param stmt The prepared statement.
	 * @throws SQLException If an attribute could not be set.
	 */
	private void putAttributesToStatement (DataObject object, PreparedStatement stmt) throws SQLException
	{
		stmt.setLong (1, object.getUniqueId ());

		int pos = 2;

		for (Iterator i = object.getAttributes ().entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry attribute = (Map.Entry) i.next ();

			if (attribute.getValue () instanceof IObjectList)
			{
				continue;
			}

			stmt.setObject (pos++, attribute.getValue ());
		}
	}
}
