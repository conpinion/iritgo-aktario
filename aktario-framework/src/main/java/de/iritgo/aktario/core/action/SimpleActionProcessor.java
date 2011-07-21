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

package de.iritgo.aktario.core.action;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.base.Transceiver;


/**
 * This is an action processor that directly executes actions.
 */
public class SimpleActionProcessor extends BaseObject implements ActionProcessor
{
	public SimpleActionProcessor ()
	{
		super ("SimpleActionProcessor");
	}

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform (Action action)
	{
		if (action.canPerform ())
		{
			action.perform ();
		}
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		perform (action);
	}

	/**
	 * Clone a new instance from this processor
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public Object clone ()
	{
		return new SimpleActionProcessor ();
	}

	/**
	 * Close this action processor
	 */
	public void close ()
	{
	}
}
