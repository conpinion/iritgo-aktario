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


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import org.apache.log4j.PropertyConfigurator;
import org.simoes.lpd.LPD;
import org.simoes.lpd.handler.HandlerFactory;
import org.simoes.lpd.ui.PrintJobTableModel;
import org.simoes.lpd.util.PrintQueue;
import org.simoes.lpd.util.Queues;
import java.net.URL;
import java.util.Properties;


/**
 * LPDManager
 *
 * @version $Id: LPDManager.java,v 1.5 2006/09/25 10:34:30 grappendorf Exp $
 */
public class LPDManager extends BaseObject implements Manager
{
	static
	{
		//PropertyConfigurator.configure("logConfig.ini");
		URL logConfigUrl = LPDManager.class.getResource("/resources/log.conf");

		PropertyConfigurator.configure(logConfigUrl);
	}

	private Thread lpdThread;

	/**
	 * Create a new call manager manager.
	 */
	public LPDManager()
	{
		super("LPDManager");
	}

	/**
	 * Initialize the client manager.
	 */
	public void init()
	{
		initLPDService();
	}

	private void initLPDService()
	{
		Properties properties = new Properties();

		try
		{
			properties.load(getClass().getResource("/resources/lpd.properties").openStream());
		}
		catch (Exception x)
		{
		}

		try
		{
			final String rawQueueName = "RAW";

			// Initialize data model and Print Queue
			PrintJobTableModel pjtm = new PrintJobTableModel();
			Queues queues = Queues.getInstance();

			// create the PrintQueue
			PrintQueue rawQueue = queues.createQueueWithTableModel(rawQueueName, pjtm);

			// initialize the TableModel by making it aware of it's data source, the PrintQueue
			pjtm.setPrintQueueDataModel(rawQueue);

			//			lpdThread = new Thread(LPD.getInstance (properties));
			lpdThread = new Thread(LPD.getInstance());
			//			HandlerFactory.getInstance ().setCustomHandler ("AKTARIO", new LPDCustomHandler());
			lpdThread.start();
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Free all client manager resources.
	 */
	@SuppressWarnings("deprecation")
	public void unload()
	{
		lpdThread.stop();
	}
}
