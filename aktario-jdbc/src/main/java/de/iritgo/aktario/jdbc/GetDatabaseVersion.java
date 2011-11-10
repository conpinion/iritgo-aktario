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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


/**
 * Get the name and version of the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 */
public class GetDatabaseVersion extends Command
{
	/** The chached version. */
	String version;

	/**
	 * Create a new <code>GetDatabaseVersion</code> command.
	 */
	public GetDatabaseVersion()
	{
		super("persist.GetDatabaseVersion");
	}

	/**
	 * Perform the command.
	 *
	 * @return The database name and version.
	 */
	public Object performWithResult()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource();

		Connection connection = null;

		try
		{
			if (version == null)
			{
				connection = dataSource.getConnection();

				DatabaseMetaData meta = connection.getMetaData();

				version = meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion();
			}

			return version;
		}
		catch (SQLException x)
		{
			Log.logError("persist", "Insert", "Unable to get database meta data: " + x);
		}
		finally
		{
			DbUtils.closeQuietly(connection);
		}

		return null;
	}
}
