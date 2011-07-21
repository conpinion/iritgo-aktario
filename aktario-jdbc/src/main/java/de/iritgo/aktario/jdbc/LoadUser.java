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
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * This command loads a single user with a specific unique id.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>id</td><td>Long</td><td>The unique id of the user load.</td></tr>
 * </table>
 */
public class LoadUser extends Command
{
	/**
	 * Create a new <code>LoadUser</code> command.
	 */
	public LoadUser ()
	{
		super ("persist.LoadUser");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		if (properties.get ("id") == null)
		{
			Log.logError ("persist", "LoadUser", "Missing unique id for the user to load");

			return;
		}

		long uniqueId = ((Long) properties.get ("id")).longValue ();

		JDBCManager jdbcManager = (JDBCManager) Engine.instance ().getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		try
		{
			QueryRunner query = new QueryRunner (dataSource);
			final User user = (User) query.query ("select * from IritgoUser where id=?", new Long (uniqueId),
							new ResultSetHandler ()
							{
								public Object handle (ResultSet rs) throws SQLException
								{
									if (rs.next ())
									{
										User user = new User (rs.getString ("name"), rs.getString ("email"), rs
														.getInt ("id"), rs.getString ("password"), 0);

										Server.instance ().getUserRegistry ().addUser (user);
										Engine.instance ().getBaseRegistry ().add (user);

										return user;
									}
									else
									{
										return null;
									}
								}
							});

			if (user == null)
			{
				Log.logError ("persist", "LoadUser", "Unable to find user with id " + uniqueId);

				return;
			}

			Log.logVerbose ("persist", "LoadUser", "Successfully loaded user " + user.getName () + ":"
							+ user.getUniqueId ());
		}
		catch (SQLException x)
		{
			Log.logError ("persist", "LoadUser", "Error while loading the users: " + x);
		}
	}
}
