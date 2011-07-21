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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.flowcontrol.FlowControl;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.FrameworkFlowRule;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ConnectToServer;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.client.command.UserLogin;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.user.action.UserLoginFailureAction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Properties;


/**
 * UserLoginHelper
 *
 * @version $Id: UserLoginHelper.java,v 1.9 2006/09/25 10:34:31 grappendorf Exp $
 */
public class UserLoginHelper
{
	/**
	 */
	public static boolean login (final UserLoginPane loginPane, final String server, final String username,
					final String password, final boolean remember, final boolean autoLogin)
	{
		AppContext.instance ().setServerIP (server);

		connectAndGo (username, password, loginPane);

		final FlowControl flowControl = Engine.instance ().getFlowControl ();

		flowControl.add (new FrameworkFlowRule ("UserLogin", null, null)
		{
			@Override
			public void success ()
			{
				flowControl.clear ();

				AktarioGUI gui = (AktarioGUI) Client.instance ().getClientGUI ();

				new Thread (new Runnable ()
				{
					public void run ()
					{
						try
						{
							InputStream input = new URL ("http://" + server + "/autoupdate.jar").openStream ();
							String workingDirPath = Engine.instance ().getSystemDir ();

							IOUtils.copy (input, FileUtils.openOutputStream (new File (workingDirPath
											+ System.getProperty ("file.separator") + "autoupdate.jar")));
						}
						catch (Exception x)
						{
						}
					};
				}).start ();

				gui.show ();
				gui.setStatusUser (username + "@" + server);
				Engine.instance ().getSystemProperties ().setProperty ("lastlogin", username + "@" + server);

				if (loginPane != null)
				{
					if (remember)
					{
						loginPane.rememberAccount ();
					}
					else
					{
						loginPane.removeAccount ();
					}

					if (autoLogin)
					{
						loginPane.rememberAutoLogin ();
					}
				}
			}

			@Override
			public void failure (Object arg)
			{
				int failure = ((Integer) arg).intValue ();

				Client.instance ().getNetworkService ().closeAllChannels ();

				if (failure == UserLoginFailureAction.USER_ALREADY_ONLINE)
				{
					setCompleteState (false);
					Properties props = new Properties ();
					props.put ("failure", arg);
					Command cmd = new ShowDialog ("AktarioUserLoginFailureDialog");
					cmd.setProperties (props);
					CommandTools.performSimple (cmd);
					try
					{
						Thread.sleep (10000);
					}
					catch (Exception x)
					{
					}
					connectAndGo (username, password, loginPane);
					return;
				}

				if (failure == UserLoginFailureAction.LOGIN_NOT_ALLOWED)
				{
					setCompleteState (false);
					Properties props = new Properties ();
					props.put ("failure", arg);
					Command cmd = new ShowDialog ("AktarioUserLoginFailureDialog");
					cmd.setProperties (props);
					CommandTools.performSimple (cmd);
					return;
				}

				if (failure == UserLoginFailureAction.BAD_USERNAME_OR_PASSWORD)
				{
					setCompleteState (false);
					Properties props = new Properties ();
					props.put ("failure", arg);
					Command cmd = new ShowDialog ("AktarioUserLoginFailureDialog");
					cmd.setProperties (props);
					CommandTools.performSimple (cmd);
					return;
				}
			}
		});

		flowControl.add (new FrameworkFlowRule ("WrongVersion", null, null)
		{
			@Override
			public void success ()
			{
				flowControl.clear ();
				Client.instance ().getNetworkService ().closeAllChannels ();

				JOptionPane.showMessageDialog (loginPane != null ? loginPane.getPanel () : null, Engine.instance ()
								.getResourceService ().getStringWithoutException ("wrongClientVersion"), Engine
								.instance ().getResourceService ().getStringWithoutException ("systemMessage"),
								JOptionPane.OK_OPTION);

				try
				{
					String workingDirPath = Engine.instance ().getSystemDir ();

					if (workingDirPath.endsWith ("\\"))
					{
						workingDirPath = workingDirPath.substring (0, workingDirPath.length () - 1);
					}

					@SuppressWarnings("unused")
					Process proc = Runtime.getRuntime ().exec (
									"java" + " -jar \"" + workingDirPath + Engine.instance ().getFileSeparator ()
													+ "autoupdate.jar\" http://"
													+ AppContext.instance ().getServerIP () + "/update.jar" + " \""
													+ workingDirPath + "\"");
				}
				catch (Exception x)
				{
					JOptionPane.showMessageDialog (loginPane != null ? loginPane.getPanel () : null, x.toString (),
									"Iritgo", JOptionPane.OK_OPTION);
				}

				System.exit (0);
			}
		});

		return false;
	}

	static private void connectAndGo (String username, String password, final UserLoginPane loginPane)
	{
		CommandTools.performAsync (new ConnectToServer ());
		CommandTools.performAsync (new UserLogin (username, password));

		CommandTools.performAsync (new Command ()
		{
			@Override
			public void perform ()
			{
				try
				{
					SwingUtilities.invokeAndWait (new Runnable ()
					{
						public void run ()
						{
							JOptionPane.showMessageDialog (loginPane != null ? loginPane.getPanel () : null,
											Engine.instance ().getResourceService ().getString (
															"aktario.serverNotAvailable"), Engine.instance ()
															.getResourceService ().getString ("app.title"),
											JOptionPane.OK_OPTION);
						}
					});
				}
				catch (InterruptedException x)
				{
				}
				catch (InvocationTargetException x)
				{
				}

				CommandTools.performAsync (new ShowDialog ("AktarioUserLoginDialog"));
			}

			@Override
			public boolean canPerform ()
			{
				return AppContext.instance ().isConnectedWithServer () == false;
			}
		});
	}
}
