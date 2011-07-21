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
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.base.DataObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


/**
 * This command loads a single object of a specific type and unique id.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>type</td><td>String</td><td>The type id of the object to load.</td></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the object to load.</td></tr>
 * </table>
 */
public class LoadObject extends Command
{
	/**
	 * Create a new <code>LoadObject</code> command.
	 */
	public LoadObject ()
	{
		super ("persist.LoadObject");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "LoadObject", "Missing unique id for the object to load");

			return;
		}

		long uniqueId = ((Long) properties.get ("id")).longValue ();
		final String typeId = (String) properties.get ("type");

		if (Engine.instance ().getBaseRegistry ().get (uniqueId, typeId) != null)
		{
			Log.logVerbose ("persist", "LoadObject", "Object with id " + uniqueId + " already loaded, skipping");

			return;
		}

		if (typeId == null)
		{
			Log.logError ("persist", "LoadObject", "The type of the object to load wasn't specified");

			return;
		}

		IObject object = null;

		try
		{
			object = Engine.instance ().getIObjectFactory ().newInstance (typeId);
		}
		catch (NoSuchIObjectException ignored)
		{
			Log.logError ("persist", "LoadObject", "Attemting to load object of unknown type '" + typeId + "'");

			return;
		}

		if (! DataObject.class.isInstance (object))
		{
			Log.logError ("persist", "LoadObject", "Attemting to load object that is not persitable");

			return;
		}

		object = load (((JDBCManager) Engine.instance ().getManager ("persist.JDBCManager")).getDefaultDataSource (),
						typeId, uniqueId);

		if (object != null)
		{
			Engine.instance ().getBaseRegistry ().add ((BaseObject) object);
		}

		return;
	}

	/**
	 * Load an object.
	 *
	 * @param dataSource The data source to load from.
	 * @param typeId The type of the object to load.
	 * @param uniqueId The unique id of the object to load.
	 * @return The loaded object (already registered with the base registry).
	 */
	private DataObject load (final DataSource dataSource, final String typeId, long uniqueId)
	{
		DataObject object = null;

		try
		{
			QueryRunner query = new QueryRunner (dataSource);

			object = (DataObject) query.query ("select * from " + typeId + " where id=" + uniqueId,
							new ResultSetHandler ()
							{
								public Object handle (ResultSet rs) throws SQLException
								{
									rs.getMetaData ();

									if (rs.next ())
									{
										try
										{
											DataObject object = (DataObject) Engine.instance ().getIObjectFactory ()
															.newInstance (typeId);

											object.setUniqueId (rs.getLong ("id"));

											for (Iterator i = object.getAttributes ().entrySet ().iterator (); i
															.hasNext ();)
											{
												Map.Entry attribute = (Map.Entry) i.next ();

												if (attribute.getValue () instanceof IObjectList)
												{
													loadList (dataSource, object, object
																	.getIObjectListAttribute ((String) attribute
																					.getKey ()));
												}
												else
												{
													try
													{
														if (! object
																		.getAttribute ((String) attribute.getKey ())
																		.getClass ()
																		.equals (
																						rs
																										.getObject (
																														(String) attribute
																																		.getKey ())
																										.getClass ()))
														{
															System.out
																			.println ("********* Datastruct is not compatible with dataobject:"
																							+ object.getTypeId ()
																							+ ":"
																							+ attribute.getKey ()
																							+ " Types:"
																							+ object
																											.getAttribute (
																															(String) attribute
																																			.getKey ())
																											.getClass ()
																							+ "!="
																							+ rs
																											.getObject (
																															(String) attribute
																																			.getKey ())
																											.getClass ());
														}

														object.setAttribute ((String) attribute.getKey (), rs
																		.getObject ((String) attribute.getKey ()));
													}
													catch (NullPointerException x)
													{
														System.out.println ("LoadObject error: " + attribute.getKey ());
													}
												}
											}

											return object;
										}
										catch (NoSuchIObjectException ignored)
										{
											Log.logError ("persist", "LoadObject", "NoSuchIObjectException");
										}
									}
									else
									{
									}

									return null;
								}
							});

			if (object != null)
			{
				Log.logVerbose ("persist", "LoadObject", "Successfully loaded object " + typeId + ":" + uniqueId);
			}
			else
			{
				Log.logError ("persist", "LoadObject", "Unable to find object " + typeId + ":" + uniqueId);
			}
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "LoadObject", "Error while loading the object " + typeId + ":" + uniqueId + ": "
							+ x);
		}

		return object;
	}

	/**
	 * Load a list of iobjects.
	 *
	 * @param dataSource The data source to load from.
	 * @param owner The owner of the list.
	 * @param list the object list to load.
	 */
	public void loadList (final DataSource dataSource, DataObject owner, final IObjectList list)
	{
		try
		{
			QueryRunner query = new QueryRunner (dataSource);

			query.query ("select elemType, elemId from IritgoObjectList where id=? and attribute=?", new Object[]
			{
							new Long (owner.getUniqueId ()), list.getAttributeName ()
			}, new ResultSetHandler ()
			{
				public Object handle (ResultSet rs) throws SQLException
				{
					while (rs.next ())
					{
						DataObject element = load (dataSource, rs.getString ("elemType"), rs.getLong ("elemId"));

						if (element != null)
						{
							list.add (element);
						}
					}

					return null;
				}
			});
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "LoadObject", "Error while loading the object list "
							+ list.getOwner ().getTypeId () + "." + list.getAttributeName () + ": " + x);
		}
	}
}
