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


import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.infocenter.infocenter.InfoCenterDisplay;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 */
public class DiskWriterDisplay implements InfoCenterDisplay
{
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMMd");

	private static SimpleDateFormat diskDateFormat = new SimpleDateFormat ("yyyy:MM:d-H:m:s");

	private String infoStoreFile;

	private File file;

	private FileWriter writer;

	private String day = simpleDateFormat.format (new Date ());

	/**
	 * Constructor
	 *
	 */
	public DiskWriterDisplay ()
	{
	}

	/**
	 * Set the info store file name to save all received informations
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
		return "diskwriter.display";
	}

	/**
	 * Init Logger, called any time you add a logger to any category
	 *
	 * @param category
	 */
	public void init (String category, int context, User user)
	{
		try
		{
			file = new File (infoStoreFile + "-" + context + "-" + day + "." + category);

			if (! file.exists ())
			{
				file.createNewFile ();
			}

			writer = new FileWriter (file, true);
		}
		catch (IOException x)
		{
			Log.log ("system", "DiskWriterDisplay.init", "Cannot create or read infostorefile" + x.getMessage (),
							Log.FATAL);
		}
	}

	/**
	 * release
	 *
	 */
	public void release ()
	{
		try
		{
			writer.flush ();
			writer.close ();
		}
		catch (IOException x)
		{
			Log.log ("system", "DiskWriterDisplay.release", "Cannot close infostorefile" + x.getMessage (), Log.FATAL);
		}
	}

	/**
	 * Info
	 */
	public void info (User user, int context, String category, String icon, String message, String guiPaneId,
					long uniqueId, String iObjectTypeId, int level)
	{
		String currentMonth = simpleDateFormat.format (new Date ());

		if (! currentMonth.equals (day))
		{
			release ();
			init (category, context, null);
		}

		try
		{
			writer.write (diskDateFormat.format (new Date ()) + "|" + category + "|" + icon + "|" + message + "|"
							+ guiPaneId + "|" + uniqueId + "|" + iObjectTypeId + "|" + level + "\r\n");

			writer.flush ();
		}
		catch (IOException x)
		{
			Log.log ("system", "DiskWriterDisplay.info", "Cannot write to infostorefile" + x.getMessage (), Log.FATAL);
		}
	}
}
