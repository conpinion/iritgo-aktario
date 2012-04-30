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
 * This command updates the specified user to the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>dataobject</td><td>DataObject</td><td>The object to update in the database.</td></tr>
 *   <tr><td>owner</td><td>DataObject</td><td>The owner object to create the relations.</td></tr>
 *   <tr><td>listAttribute</td><td>String</td><td>The name of the IObjectList for the relation.</td></tr>
 * </table>
 */
public class UpdateObject extends Command
{
	/**
	 * Create a new <code>UpdateObject</code> command.
	 */
	public UpdateObject()
	{
		super("persist.UpdateObject");
	}

	/**
	 * Perform the command.
	 */
	public void perform()
	{
		update((DataObject) properties.get("dataobject"));
	}

	/**
	 * Insert a new data object into the database.
	 *
	 * @param object The data object to create.
	 */
	private void update(DataObject object)
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
			DataSource dataSource = jdbcManager.getDefaultDataSource();

			connection = dataSource.getConnection();

			StringBuffer sqlAssigns = new StringBuffer("id=?");

			for (Iterator i = object.getAttributes().entrySet().iterator(); i.hasNext();)
			{
				Map.Entry attribute = (Map.Entry) i.next();

				if (attribute.getValue() instanceof IObjectList)
				{
					continue;
				}

				sqlAssigns.append(", " + (String) attribute.getKey() + "=?");
			}

			String sql = "update " + object.getTypeId() + " set " + sqlAssigns.toString() + " where id="
							+ object.getUniqueId();

			stmt = connection.prepareStatement(sql);
			putAttributesToStatement(object, stmt);
			stmt.execute();

			Log.logVerbose("persist", "JDBCManager", "UPDATE " + object.getTypeId() + ":" + object.getUniqueId() + " |"
							+ sql + "|");
		}
		catch (Exception x)
		{
			Log.logError (
					"persist", "JDBCManager", "Error while creating new database record: " + x);
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
