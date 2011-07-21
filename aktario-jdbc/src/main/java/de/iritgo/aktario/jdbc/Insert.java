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
import de.iritgo.aktario.core.logger.Log;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


/**
 * This command implements a direct sql INSERT into the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>table</td><td>String</td><td>Name of the table to insert to.</td></tr>
 *   <tr><td>column.*</td><td>String</td><td>Column value.</td></tr>
 *   <tr><td>[size]</td><td>String</td><td>If set and > 0 this will be a batch insert.</td></tr>
 * </table>
 */
public class Insert extends Command
{
	/**
	 * Create a new <code>Insert</code> command.
	 */
	public Insert ()
	{
		super ("persist.Insert");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("table") == null)
		{
			Log.logError ("persist", "Insert", "Missing table name");

			return;
		}

		int size = 0;

		if (properties.get ("size") != null)
		{
			size = ((Integer) properties.get ("size")).intValue ();
		}

		JDBCManager jdbcManager = (JDBCManager) Engine.instance ().getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		Connection connection = null;
		PreparedStatement stmt = null;

		ArrayList columns = new ArrayList (8);
		ArrayList columnValues = new ArrayList (8);

		for (Iterator i = properties.entrySet ().iterator (); i.hasNext ();)
		{
			Map.Entry entry = (Map.Entry) i.next ();

			if (((String) entry.getKey ()).indexOf ("column.") == 0)
			{
				columns.add (((String) entry.getKey ()).substring (7));
				columnValues.add (entry.getValue ());
			}
		}

		int numColumns = columns.size ();

		StringBuffer sqlColumns = new StringBuffer ("(id");

		for (int i = 0; i < numColumns; ++i)
		{
			sqlColumns.append (", " + (String) columns.get (i));
		}

		sqlColumns.append (")");

		StringBuffer sqlValues = new StringBuffer ("(?");

		for (int i = 0; i < numColumns; ++i)
		{
			sqlValues.append (", ?");
		}

		sqlValues.append (")");

		String sql = "insert into " + properties.getProperty ("table") + " " + sqlColumns.toString () + " values "
						+ sqlValues.toString ();

		try
		{
			connection = dataSource.getConnection ();

			stmt = connection.prepareStatement (sql);

			if (size <= 0)
			{
				stmt.setLong (1, Engine.instance ().getPersistentIDGenerator ().createId ());

				for (int col = 0; col < numColumns; ++col)
				{
					stmt.setObject (col + 2, columnValues.get (col));
				}

				stmt.execute ();
			}
			else
			{
				for (int row = 0; row < size; ++row)
				{
					stmt.setLong (1, Engine.instance ().getPersistentIDGenerator ().createId ());

					for (int col = 0; col < numColumns; ++col)
					{
						stmt.setObject (col + 2, ((Object[]) columnValues.get (col))[row]);
					}

					stmt.execute ();
				}
			}

			stmt.close ();

			Log.logVerbose ("persist", "Insert", "INSERT |" + sql + "|");
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "Insert", "Error while executing sql |" + sql + "|: " + x);
		}
		finally
		{
			DbUtils.closeQuietly (stmt);
			DbUtils.closeQuietly (connection);
		}
	}
}
