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

package de.iritgo.aktario.framework;


import de.iritgo.aktario.framework.server.Server;
import org.apache.commons.cli.Options;


/**
 * Start this class to start an Iritgo server.
 */
public class IritgoServer
{
	/**
	 * The server main method.
	 *
	 * @param args The program args.
	 */
	public static void main(String[] args)
	{
		Options options = new Options();

		IritgoEngine.create(IritgoEngine.START_SERVER, options, args);
	}

	/**
	 * Check if the server is up and running.
	 *
	 * @return True if the server is up and running.
	 */
	public static boolean isUpAndRunning()
	{
		return Server.instance().isUpAndRunning();
	}
}
