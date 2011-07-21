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
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.participant.ParticipantClientManager;
import de.iritgo.aktario.participant.ParticipantServerManager;


/**
 * Get the participant state dyn data object from the participant state gui pane.
 *
 * @version $Id: GetParticipantStateByName.java,v 1.5 2006/11/08 11:05:21 grappendorf Exp $
 */
public class GetParticipantStateByName extends Command
{
	/**
	 * Create a new startup command.
	 */
	public GetParticipantStateByName ()
	{
		super ("aktario-participant.GetParticipantStateByName");
	}

	/**
	 * perform command.
	 */
	public Object performWithResult ()
	{
		if (IritgoEngine.instance ().isServer ())
		{
			ParticipantServerManager participantServerManager = (ParticipantServerManager) Engine.instance ()
							.getManager ("ParticipantServerManager");

			return participantServerManager.getByName (properties.getProperty ("name"));
		}
		else
		{
			ParticipantClientManager participantClientManager = (ParticipantClientManager) Engine.instance ()
							.getManager ("ParticipantClientManager");

			return participantClientManager.getByName (properties.getProperty ("name"));
		}
	}
}
