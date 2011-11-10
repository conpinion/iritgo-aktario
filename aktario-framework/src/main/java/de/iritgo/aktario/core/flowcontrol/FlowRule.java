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


import de.iritgo.aktario.core.base.BaseObject;


/**
 * FlowRules implement the application flow logic. A rule can be checked
 * for success and can be signalled to succeed or fail.
 */
public abstract class FlowRule extends BaseObject
{
	/**
	 * Create a new rule.
	 *
	 * @param id The id of the rule.
	 */
	public FlowRule(String id)
	{
		super(id);
	}

	/**
	 * Tell the rule that is has succeeded.
	 */
	public void success()
	{
	}

	/**
	 * Tell the rule that is has failed.
	 */
	public void failure()
	{
	}

	/**
	 * Tell the rule that is has succeeded.
	 *
	 * @param arg Optional success argument.
	 */
	public void success(Object arg)
	{
	}

	/**
	 * Tell the rule that is has failed.
	 *
	 * @param arg Optional failure argument.
	 */
	public void failure(Object arg)
	{
	}

	/**
	 * Check wether the rule is completed.
	 *
	 * @return True if the rule has succeeded.
	 */
	public boolean isCompleted()
	{
		return true;
	}
}
