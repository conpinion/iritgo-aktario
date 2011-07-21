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

package de.iritgo.aktario.core.flowcontrol;


/**
 * A counting flow rule counts calls to it's success() and failure() methods.
 * Say you create a CountingFlowRule with an initial counter value of four.
 * Then four calls to the success() method are performed before the rule is
 * removed from the flow control.
 */
public class CountingFlowRule extends FlowRule
{
	/** Success counter. */
	private int counter;

	/**
	 * Create a new CountingFlowRule.
	 *
	 * @param id The id of the new rule.
	 * @param counter The initial success count.
	 */
	public CountingFlowRule (String id, int counter)
	{
		super (id);
		this.counter = counter;
	}

	/**
	 * Tell the rule that is has succeeded.
	 */
	@Override
	public void success ()
	{
		--counter;
	}

	/**
	 * Tell the rule that is has failed.
	 */
	@Override
	public void failure ()
	{
		++counter;
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	@Override
	public boolean isCompleted ()
	{
		return counter <= 0;
	}
}
