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


import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.simplelife.string.StringTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleUrlServer
{
	// GET /number=1000 HTTP/1.0
	static private Pattern numberPattern = Pattern.compile ("GET.*number=(\\d+) .*");

	static private Pattern exitPattern = Pattern.compile ("GET.*exit=(\\w+) .*");

	private boolean shutdown = false;

	protected void start ()
	{
		ServerSocket s;

		try
		{
			s = new ServerSocket (3005);
			s.setSoTimeout (3000);
		}
		catch (Exception e)
		{
			return;
		}

		BufferedReader in = null;
		PrintWriter out = null;
		Socket remote = null;

		while (! shutdown)
		{
			try
			{
				remote = s.accept ();

				in = new BufferedReader (new InputStreamReader (remote.getInputStream ()));

				out = new PrintWriter (remote.getOutputStream ());

				String str = ".";
				String number = "";
				Boolean exitProcess = false;

				while (! str.equals (""))
				{
					str = in.readLine ();

					Matcher numberMatcher = numberPattern.matcher (str);

					if (numberMatcher.matches ())
					{
						number = numberMatcher.group (1);
						break;
					}

					Matcher exitMatcher = exitPattern.matcher (str);

					if (exitMatcher.matches ())
					{
						exitProcess = true;
						break;
					}
				}

				out.println ("HTTP/1.0 200 OK");
				out.println ("Content-Type: text/html");
				out.println ("Server: IPtell");
				out.println ("");
				// Send the HTML page
				out.println ("result ok.");
				out.flush ();

				if (StringTools.isNotTrimEmpty (number))
				{
					Properties props = new Properties ();

					props.setProperty ("number", number);
					CommandTools.performAsync ("aktario-callmanager.CallNumber", props);
				}

				if (exitProcess)
				{
					System.exit (0);
				}

			}
			catch (Exception e)
			{
			}
			finally
			{
				if (remote != null)
				{
					out.close ();

					try
					{
						remote.close ();
					}
					catch (IOException x)
					{
					}
				}
			}
		}

		Log.logInfo ("AktarioXmlRpcManager", "SimpleUrlServer", "Shutdown local network service");
	}

	public void init ()
	{
		try
		{
			new Thread (new Runnable ()
			{
				public void run ()
				{
					start ();
				}
			}).start ();
		}
		catch (Exception x)
		{
		}
	}

	public void shutdown ()
	{
		//	  Log.logInfo ("AktarioXmlRpcManager", "SimpleUrlServer", "Try to shutdown...");
		shutdown = true;
	}
}
