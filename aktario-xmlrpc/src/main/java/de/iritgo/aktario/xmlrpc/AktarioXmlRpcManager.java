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

package de.iritgo.aktario.xmlrpc;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.simplelife.math.NumberTools;
import de.iritgo.simplelife.string.StringTools;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcHandler;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * AktarioClientManager.
 *
 * @version $Id: AktarioXmlRpcManager.java,v 1.8 2006/09/23 00:08:45 grappendorf Exp $
 */
public class AktarioXmlRpcManager extends BaseObject implements Manager
{
	/** A map of all commands that are callable through xmlrpc. */
	protected Map<String, String> xmlRpcCommands;

	private WebServer webServer;

	private SimpleUrlServer simpleUrlServer;

	/**
	 * Create a new AktarioXmlRpcManager.
	 */
	public AktarioXmlRpcManager()
	{
		super("AktarioXmlRpcManager");
		xmlRpcCommands = new HashMap<String, String>();
	}

	/**
	 * Initialize the AktarioXmlRpcManager.
	 */
	public void init()
	{
		simpleUrlServer = new SimpleUrlServer();
		simpleUrlServer.init();

		addXmlRpcCommand("testCommand", "test.aktario-xmlrpc.testKillCommand");
		addXmlRpcCommand("kill", "aktario-xmlrpc.killCommand");

		String portString = AppContext.instance().getString("xmlrpcport");

		if (StringTools.isEmpty(portString))
		{
			return;
		}

		int port = NumberTools.toInt(portString, - 1);

		try
		{
			XmlRpc.setDebug(false);
			webServer = new WebServer(port, InetAddress.getByName("127.0.0.1"));
			webServer.setParanoid(true);
			webServer.acceptClient("127.0.0.1");
			webServer.addHandler("$default", new XmlRpcHandler()
			{
				@SuppressWarnings("unchecked")
				public Object execute(String method, Vector params)
				{
					Object res = null;

					String commandName = (String) xmlRpcCommands.get(method);

					if (commandName != null)
					{
						res = CommandTools.performAsync(commandName, params);
					}
					else
					{
						Log.logError("AktarioXmlRpcManager", "execute", "Trying to execute unknown command '"
										+ commandName + "'");
					}

					return res == null ? new Integer(1) : res;
				}
			});

			webServer.start();
		}
		catch (Exception x)
		{
			Log.logError("AktarioXmlRpcManager", "init", "Unable to initialize XML/RPC server: " + x);
		}
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload()
	{
		simpleUrlServer.shutdown();

		if (webServer != null)
		{
			webServer.shutdown();
		}
	}

	/**
	 * Add a command to the xmlrpc map.
	 *
	 * @param xmlRpcName The xmlrpc method name.
	 * @param commandName The command name.
	 */
	public void addXmlRpcCommand(String xmlRpcName, String commandName)
	{
		xmlRpcCommands.put(xmlRpcName, commandName);
	}
}
