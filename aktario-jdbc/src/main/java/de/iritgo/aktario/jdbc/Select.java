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
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import javax.sql.DataSource;


/**
 * This command executes an sql SELECT statement.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>handler</td><td>ResultSetHandler</td><td>The result set handler.</td></tr>
 *   <tr><td>select</td><td>String</td><td>The select statement to execute.</td></tr>
 *   <tr><td>params</td><td>Object[]</td><td>The select statement to execute.</td></tr>
 * </table>
 */
public class Select extends Command
{
	/**
	 * Create a new <code>LoadAllObjects</code> command.
	 */
	public Select()
	{
		super("persist.Select");
	}

	/**
	 * Perform the command.
	 */
	public void perform()
	{
		if (properties.get("handler") == null)
		{
			Log.logError("persist", "Select", "Missing result set handler");

			return;
		}

		ResultSetHandler resultSetHandler = (ResultSetHandler) properties.get("handler");

		if (properties.get("select") == null)
		{
			Log.logError("persist", "Select", "Missing select statement");

			return;
		}

		String select = (String) properties.getProperty("select");

		Object[] params = (Object[]) properties.get("params");

		JDBCManager jdbcManager = (JDBCManager) Engine.instance().getManager("persist.JDBCManager");
		DataSource dataSource = jdbcManager.getDefaultDataSource();

		try
		{
			QueryRunner query = new QueryRunner(dataSource);

			Object res;

			if (params != null)
			{
				res = query.query(select, params, resultSetHandler);
			}
			else
			{
				res = query.query(select, resultSetHandler);
			}

			Log.logVerbose("persist", "Select", "SELECT |" + select + "|");
		}
		catch (Exception x)
		{
			Log.logError("persist", "Select", "Error while executing sql |" + select + "|: " + x);
		}
	}
}
