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

package de.iritgo.aktario.participant.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.participant.ParticipantManager;


/**
 * Add a attribute to the participantstate object.
 *
 * @version $Id: AddAttributeCommand.java,v 1.6 2006/09/25 10:34:31 grappendorf Exp $
 */
public class AddAttributeCommand extends Command
{
	/**
	 * Create a new startup command.
	 */
	public AddAttributeCommand ()
	{
		super ("AddAttributeCommand");
	}

	/**
	 *
	 */
	public void perform ()
	{
		ParticipantManager participantManager = null;

		participantManager = (ParticipantManager) Engine.instance ().getManagerRegistry ().getManager (
						"ParticipantClientManager");

		if (participantManager == null)
		{
			return;
		}

		try
		{
			participantManager.addAttributeCommand (properties.getProperty ("attribute"), (Command) properties
							.get ("command"));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "AddAttributeCommand:perform",
							"Class not found Exception, Attribute type unknown: "
											+ properties.getProperty ("attributeType"));
		}
	}
}
