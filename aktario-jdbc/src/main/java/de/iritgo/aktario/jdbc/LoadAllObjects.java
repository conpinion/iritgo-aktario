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
import de.iritgo.aktario.core.base.BaseRegistry;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.AbstractIObjectFactory;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.base.DataObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


/**
 * This command loads *ALL* objects of a specific type.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Description</th></tr>
 *   <tr><td>type</td><td>The type id of the objects to load.</td></tr>
 * </table>
 */
public class LoadAllObjects extends Command
{
	/**
	 * Create a new <code>LoadAllObjects</code> command.
	 */
	public LoadAllObjects ()
	{
		super ("persist.LoadAllObjects");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.getProperty ("type") == null)
		{
			Log.logError ("persist", "LoadObjects", "The type of the objects to load wasn't specified");

			return;
		}

		final String type = ((String) properties.getProperty ("type"));

		final AbstractIObjectFactory factory = (AbstractIObjectFactory) Engine.instance ().getIObjectFactory ();

		IObject sample = null;

		try
		{
			sample = factory.newInstance (type);
		}
		catch (NoSuchIObjectException ignored)
		{
			Log.logError ("persist", "LoadObjects", "Attemting to load objects of unknown type '" + type + "'");

			return;
		}

		if (! DataObject.class.isInstance (sample))
		{
			Log.logError ("persist", "LoadObjects", "Attemting to load objects that are not persitable");

			return;
		}

		final BaseRegistry registry = Engine.instance ().getBaseRegistry ();

		JDBCManager jdbcManager = (JDBCManager) Engine.instance ().getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			QueryRunner query = new QueryRunner (dataSource);

			ResultSetHandler resultSetHandler = properties.get ("resultSetHandle") != null ? (ResultSetHandler) properties
							.get ("resultSetHandler")
							: new ResultSetHandler ()
							{
								public Object handle (ResultSet rs) throws SQLException
								{
									ResultSetMetaData meta = rs.getMetaData ();

									int numObjects = 0;

									while (rs.next ())
									{
										try
										{
											DataObject object = (DataObject) factory.newInstance (type);

											object.setUniqueId (rs.getLong ("id"));

											for (Iterator i = object.getAttributes ().entrySet ().iterator (); i
															.hasNext ();)
											{
												Map.Entry attribute = (Map.Entry) i.next ();

												if (attribute.getValue () instanceof IObjectList)
												{
													// 										loadList (
													// 											dataSource, object,
													// 											object.getIObjectListAttribute (
													// 												(String) attribute.getKey ()));
												}
												else
												{
													object.setAttribute ((String) attribute.getKey (), rs
																	.getObject ((String) attribute.getKey ()));
												}
											}

											registry.add (object);
											++numObjects;
										}
										catch (NoSuchIObjectException ignored)
										{
										}
									}

									return new Integer (numObjects);
								}
							};

			Object numObjects = query.query ("select * from " + type, resultSetHandler);

			Log.logVerbose ("persist", "LoadObjects", "Successfully loaded " + numObjects + " objects of type '" + type
							+ "'");
		}
		catch (Exception x)
		{
			Log.logError ("persist", "LoadObjects", "Error while loading objects of type '" + type + "': " + x);
		}
	}
}
