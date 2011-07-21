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

package de.iritgo.aktario.participant;


import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.framework.base.FrameworkPlugin;
import de.iritgo.aktario.participant.command.AddAttributeCommand;
import de.iritgo.aktario.participant.command.AddAttributeEditorCommand;
import de.iritgo.aktario.participant.command.AddAttributeRenderCommand;
import de.iritgo.aktario.participant.command.AddAttributeToParticipantStateCommand;
import de.iritgo.aktario.participant.command.AddContentCommand;
import de.iritgo.aktario.participant.command.GetParticipantList;
import de.iritgo.aktario.participant.command.GetParticipantState;
import de.iritgo.aktario.participant.command.GetParticipantStateByName;
import de.iritgo.aktario.participant.gui.ParticipantStatePane;


/**
 * ParticipantClientPlugin.
 *
 * @version $Id: ParticipantPlugin.java,v 1.9 2006/09/25 10:34:30 grappendorf Exp $
 */
public class ParticipantPlugin extends AktarioPlugin
{
	protected void registerDataObjects ()
	{
	}

	protected void registerActions ()
	{
	}

	protected void registerGUIPanes ()
	{
		registerGUIPane (new ParticipantStatePane ());
	}

	protected void registerManagers ()
	{
		registerManager (FrameworkPlugin.CLIENT, new ParticipantClientManager ());
		registerManager (FrameworkPlugin.SERVER, new ParticipantServerManager ());
	}

	protected void registerConsoleCommands ()
	{
	}

	protected void registerCommands ()
	{
		registerCommand (new AddAttributeToParticipantStateCommand ());
		registerCommand (new AddAttributeRenderCommand ());
		registerCommand (new AddAttributeEditorCommand ());
		registerCommand (new AddAttributeCommand ());
		registerCommand (new AddContentCommand ());
		registerCommand (new GetParticipantState ());
		registerCommand (new GetParticipantList ());
		registerCommand (new GetParticipantStateByName ());
	}
}
