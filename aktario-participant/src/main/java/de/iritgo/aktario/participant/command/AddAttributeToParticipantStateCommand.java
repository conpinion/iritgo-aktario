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
 * @version $Id: AddAttributeToParticipantStateCommand.java,v 1.6 2006/09/25 10:34:31 grappendorf Exp $
 */
public class AddAttributeToParticipantStateCommand extends Command
{
	/**
	 * Create a new startup command.
	 */
	public AddAttributeToParticipantStateCommand ()
	{
		super ("AddAttributeToParticipantState");
	}

	/**
	 *
	 */
	public void perform ()
	{
		ParticipantManager participantManager = null;

		participantManager = (ParticipantManager) Engine.instance ().getManagerRegistry ().getManager (
						"ParticipantServerManager");

		if (participantManager == null)
		{
			participantManager = (ParticipantManager) Engine.instance ().getManagerRegistry ().getManager (
							"ParticipantClientManager");
		}

		try
		{
			participantManager.addAttribute (properties.getProperty ("attribute"), properties.get ("attributeType"));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "AddAttributeToParticipantCommand:perform",
							"Class not found Exception, Attribute type unknown: "
											+ properties.getProperty ("attributeType"));
		}
	}
}
