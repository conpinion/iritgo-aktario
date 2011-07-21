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
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.UserRegistry;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * This command loads *ALL* users currently stored in the database.
 * Later this command should be replaced by a more intelligent method,
 * that loads a user on demand when he has logged into the server.
 */
public class LoadAllUsers extends Command
{
	/**
	 * Create a new <code>LoadAllUsers</code> command.
	 */
	public LoadAllUsers ()
	{
		super ("persist.LoadAllUsers");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		JDBCManager jdbcManager = (JDBCManager) Engine.instance ().getManager ("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource ();

		final UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		try
		{
			QueryRunner query = new QueryRunner (dataSource);
			List userIds = (List) query.query ("select id from IritgoUser", new ArrayListHandler ());

			for (Iterator i = userIds.iterator (); i.hasNext ();)
			{
				Long userId = (Long) ((Object[]) i.next ())[0];

				Properties props = new Properties ();

				props.put ("id", userId);
				CommandTools.performSimple ("persist.LoadUser", props);
			}

			Log.logVerbose ("persist", "LoadAllUsers", "Successfully loaded " + userIds.size () + " users");
		}
		catch (Exception x)
		{
			Log.logError ("persist", "LoadAllUsers", "Error while loading the users: " + x);
		}
	}
}
