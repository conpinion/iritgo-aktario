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
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.base.DataObject;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;


/**
 * This command stores the specified user to the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>dataobject</td><td>DataObject</td><td>The object to store in the database.</td></tr>
 *   <tr><td>owner</td><td>DataObject</td><td>The owner object to create the relations.</td></tr>
 *   <tr><td>listAttribute</td><td>String</td><td>The name of the IObjectList for the relation.</td></tr>
 * </table>
 */
public class StoreObject extends Command
{
	/**
	 * Create a new <code>StoreUser</code> command.
	 */
	public StoreObject()
	{
		super("persist.StoreObject");
	}

	/**
	 * Perform the command.
	 */
	public void perform()
	{
		insert((DataObject) properties.get("dataobject"), (DataObject) properties.get("owner"), (String) properties
						.get("listattribute"));
	}

	/**
	 * Insert a new data object into the database.
	 *
	 * @param object The data object to create.
	 * @param owner The data object owning this one.
	 * @param listAttribute The name of the list attribute to which the new
	 *   object belongs.
	 */
	private void insert(DataObject object, DataObject owner, String listAttribute)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
			DataSource dataSource = jdbcManager.getDefaultDataSource();

			connection = dataSource.getConnection();

			StringBuffer sqlFields = new StringBuffer("id");
			StringBuffer sqlValues = new StringBuffer("?");

			for (Iterator i = object.getAttributes().entrySet().iterator(); i.hasNext();)
			{
				Map.Entry attribute = (Map.Entry) i.next();

				if (attribute.getValue() instanceof IObjectList)
				{
					continue;
				}

				sqlFields.append(", " + (String) attribute.getKey());
				sqlValues.append(", ?");
			}

			String sql = "insert into " + object.getTypeId() + " (" + sqlFields.toString() + ") values ("
							+ sqlValues.toString() + ")";

			stmt = connection.prepareStatement(sql);
			putAttributesToStatement(object, stmt);
			stmt.execute();

			Log.logVerbose("persist", "JDBCManager", "CREATED " + object.getTypeId() + ":" + object.getUniqueId()
							+ " |" + sql + "|");

			stmt.close();

			if (owner != null)
			{
				sql = "insert into IritgoObjectList (type, id, attribute, elemType, elemId) values (?, ?, ?, ?, ?)";

				stmt = connection.prepareStatement(sql);
				stmt.setString(1, owner.getTypeId());
				stmt.setLong(2, owner.getUniqueId());
				stmt.setString(3, listAttribute);
				stmt.setString(4, object.getTypeId());
				stmt.setLong(5, object.getUniqueId());
				stmt.execute();

				Log.logVerbose("persist", "StoreObject", "CREATED REFRENCE " + owner.getTypeId() + ":"
								+ owner.getUniqueId() + " => " + object.getTypeId() + ":" + object.getUniqueId() + " |"
								+ sql + "|");
			}
		}
		catch (Exception x)
		{
			Log.logDebug("persist", "StoreObject", "Error while creating new database record: " + x);
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(connection);
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
	private void putAttributesToStatement(DataObject object, PreparedStatement stmt) throws SQLException
	{
		stmt.setLong(1, object.getUniqueId());

		int pos = 2;

		for (Iterator i = object.getAttributes().entrySet().iterator(); i.hasNext();)
		{
			Map.Entry attribute = (Map.Entry) i.next();

			if (attribute.getValue() instanceof IObjectList)
			{
				continue;
			}

			stmt.setObject(pos++, attribute.getValue());
		}
	}
}
