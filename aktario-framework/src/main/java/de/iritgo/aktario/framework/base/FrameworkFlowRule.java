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

package de.iritgo.aktario.framework.base;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.flowcontrol.FlowRule;
import de.iritgo.aktario.framework.IritgoEngine;


/**
 *
 */
public class FrameworkFlowRule extends FlowRule
{
	private Command successCommand;

	private Command failureCommand;

	private boolean completeState;

	/**
	 * Constructor
	 *
	 * @param id the rule id.
	 * @param successCommand successCommand id.
	 * @param failureCommand failureCommand id.
	 */
	public FrameworkFlowRule (String id, Command successCommand, Command failureCommand)
	{
		super (id);
		this.successCommand = successCommand;
		this.failureCommand = failureCommand;
		completeState = true;
	}

	@Override
	public void success ()
	{
		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (successCommand);
	}

	@Override
	public void failure ()
	{
		IritgoEngine.instance ().getAsyncCommandProcessor ().perform (failureCommand);
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	public void setCompleteState (Boolean state)
	{
		completeState = state;
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	@Override
	public boolean isCompleted ()
	{
		return completeState;
	}
}
