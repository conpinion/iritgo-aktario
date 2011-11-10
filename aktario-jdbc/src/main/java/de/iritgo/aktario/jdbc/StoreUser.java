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
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import org.apache.commons.dbutils.DbUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * This command stores the specified user to the database.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user to store.</td></tr>
 * </table>
 */
public class StoreUser extends Command
{
	/**
	 * Create a new <code>StoreUser</code> command.
	 */
	public StoreUser()
	{
		super("persist.StoreUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform()
	{
		if (properties.get("id") == null)
		{
			Log.logError("persist", "StoreUser", "Missing unique id for the user to store");

			return;
		}

		UserRegistry userRegistry = Server.instance().getUserRegistry();
		long userId = ((Long) properties.get("id")).longValue();
		User user = userRegistry.getUser(userId);

		if (user == null)
		{
			Log.logError("persist", "StoreUser", "Unable to find user with id " + userId);

			return;
		}

		JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource();

		Connection connection = null;
		PreparedStatement stmt = null;

		try
		{
			connection = dataSource.getConnection();

			stmt = connection.prepareStatement("delete from IritgoUser where id=?");
			stmt.setLong(1, userId);
			stmt.execute();
			stmt.close();

			String userSql = "insert into IritgoUser (id, name, password, email) " + " values (?, ?, ?, ?)";

			stmt = connection.prepareStatement(userSql);
			stmt.setLong(1, userId);
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getEmail());
			stmt.execute();
			stmt.close();

			stmt = connection.prepareStatement("delete from IritgoNamedObjects where userId=?");
			stmt.setLong(1, userId);
			stmt.execute();
			stmt.close();

			Log.logVerbose("persist", "StoreUser", "INSERT USER " + userId + " |" + userSql + "|");
		}
		catch (SQLException x)
		{
			Log.logError("persist", "StoreUser", "Error while storing user with id " + userId + ": " + x);
		}
		finally
		{
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(connection);
		}
	}
}
