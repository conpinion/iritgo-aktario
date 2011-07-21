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

package de.iritgo.aktario.infocenter.guinetworkdisplay;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.infocenter.guinetworkdisplay.action.InfoCenterAction;
import de.iritgo.aktario.infocenter.infocenter.InfoCenterDisplay;
import java.io.File;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 */
public class NetworkDisplay implements InfoCenterDisplay
{
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("D");

	private String infoStoreFile;

	private File file;

	private LineNumberReader reader;

	private String month = simpleDateFormat.format (new Date ());

	/**
	 * Constructor
	 *
	 */
	public NetworkDisplay ()
	{
	}

	/**
	 * Set the info store file name to read old infos
	 *
	 * @param infoStoreFile The filename
	 */
	public void setInfoStoreFile (String infoStoreFile)
	{
		this.infoStoreFile = infoStoreFile;
	}

	/**
	 * Get the Id of the Logger
	 *
	 */
	public String getId ()
	{
		return "network.display";
	}

	/**
	 * Init Logger, called any time you add a logger to any category
	 *
	 * @param category
	 */
	public void init (String category, int context, User user)
	{
		//Thats dont work, you can use different clients
		// 		String lastLoggedIn = simpleDateFormat.format (user.getLoggedOutDate ());
		// 		try
		// 		{
		// 			file = new File (infoStoreFile +  "-" + context + "-" + month + "." + category);
		// 			reader = new LineNumberReader (new FileReader (file));
		// 			String line = "";
		// 			while ((line = reader.readLine ()) != null)
		// 			{
		// 				int day = 
		// 			}
		// 	}
		// 		catch (IOException x)
		// 		{
		// 			Log.log ("system", "NetworkDisplay.init", "Cannot create or read infostorefile" + x.getMessage (), Log.FATAL);
		// 		}
	}

	/**
	 * release
	 *
	 */
	public void release ()
	{
	}

	/**
	 * Info
	 */
	public void info (User user, int context, String category, String icon, String message, String guiPaneId,
					long uniqueId, String iObjectTypeId, int level)
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());

		InfoCenterAction action = new InfoCenterAction (context, category, icon, message, guiPaneId, uniqueId,
						iObjectTypeId, level);

		action.setTransceiver (clientTransceiver);
		ActionTools.sendToClient (action);
	}
}
