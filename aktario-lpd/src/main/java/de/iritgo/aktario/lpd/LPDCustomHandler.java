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

package de.iritgo.aktario.lpd;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.event.*;
import org.apache.log4j.Logger;
import org.simoes.lpd.common.*;
import org.simoes.lpd.handler.*;
import org.simoes.util.*;
import java.io.*;
import java.util.*;


/**
 * Handles PrintJobs by writing them to Aktario.
 *
 * @author Andreas Haardt
 */
public class LPDCustomHandler implements HandlerInterface
{
	static Logger log = Logger.getLogger(FileHandler.class);

	public LPDCustomHandler()
	{
		super();
	}

	/**
	 * Writes the printJob to disk using the jobName and jobId.
	 * @param printJob the PrintJob we are processing
	 * @return the result of our work, true for success or false for non-success
	 */
	public boolean process(PrintJob printJob)
	{
		final String METHOD_NAME = "process(): ";
		boolean result = false;

		if (null != printJob && null != printJob.getControlFile() && null != printJob.getDataFile())
		{
			try
			{
				Properties properties = new Properties();

				//				if (printJob.getDataFile ().isSpooledToFile ())
				//				{
				//					// data is already in a file
				//					System.out.println (
				//						METHOD_NAME + "Data for jobName: " + printJob.getName () + " jobNumber: " +
				//						printJob.getControlFile ().getJobNumber () + " was spooled to " +
				//						printJob.getDataFile ().getFile ().getCanonicalFile ());
				//
				//					File file = printJob.getDataFile ().getFile ().getCanonicalFile ();
				//
				//					properties.setProperty (
				//						"filename",
				//						file.getAbsoluteFile () + Engine.instance ().getFileSeparator () +
				//						file.getName ());
				//					properties.setProperty ("jobname", printJob.getName ());
				//					properties.setProperty (
				//						"jobnumber", printJob.getControlFile ().getJobNumber ());
				//				}
				//				else
				//				{
				// create file name, pjb == print job
				String fileName = printJob.getName() + printJob.getControlFile().getJobNumber() + ".pjb";

				FileUtil.writeFile(printJob.getDataFile().getContents(), fileName);

				System.out.println(METHOD_NAME + "Wrote data for jobName: " + printJob.getName() + " jobNumber: "
								+ printJob.getControlFile().getJobNumber() + " to " + fileName);
				//				}
				Engine.instance().getEventRegistry().fire("lpd", new SimpleEvent("LPD", properties));
				result = true;
			}
			catch (IOException e)
			{
				log.error(METHOD_NAME + e.getMessage());
			}
		}
		else
		{
			log.error(METHOD_NAME + "The printJob or printJob.getControlFile() or printJob.getDataFile() were empty");
		}

		return result;
	}
}
