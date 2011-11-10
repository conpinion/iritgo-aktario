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
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * GetParticipantList
 *
 * @version $Id: GetParticipantList.java,v 1.6 2006/09/25 10:34:31 grappendorf Exp $
 */
public class GetParticipantList extends Command
{
	/**
	 * Create a new GetParticipantList command.
	 */
	public GetParticipantList()
	{
		super("aktario-participant.GetParticipantList");
	}

	/**
	 * Execute the command.
	 *
	 * @return The command result.
	 */
	public Object performWithResult()
	{
		List res = new LinkedList();

		for (Iterator i = Engine.instance().getBaseRegistry().iterator("ParticipantState"); i.hasNext();)
		{
			DynDataObject participantState = (DynDataObject) i.next();

			Map elem = new HashMap();

			for (Iterator j = participantState.getAttributes().entrySet().iterator(); j.hasNext();)
			{
				Map.Entry entry = (Map.Entry) j.next();

				if (entry.getValue().getClass().toString().indexOf("java.lang.") != - 1)
				{
					elem.put(entry.getKey(), entry.getValue());
				}
			}

			res.add(elem);
		}

		return res;
	}
}
