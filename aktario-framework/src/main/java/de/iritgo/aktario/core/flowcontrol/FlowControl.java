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


import java.util.HashMap;
import java.util.Map;


/**
 * FlowControls are used to control the execution paths through an application.
 */
public class FlowControl
{
	/** The rules of this flow control. */
	private Map rules;

	/**
	 * Create a new FlowControl.
	 */
	public FlowControl ()
	{
		rules = new HashMap ();
	}

	/**
	 * Add a rule to this flow control.
	 *
	 * @param rule The rule to add.
	 */
	public void add (FlowRule rule)
	{
		rules.put (rule.getTypeId (), rule);
	}

	/**
	 * Retrieve a rule by it's id.
	 *
	 * @param id The id of the rule to remove.
	 */
	public FlowRule get (String id)
	{
		return (FlowRule) rules.get (id);
	}

	/**
	 * Remove a rule from this flow control.
	 *
	 * @param rule The rule to remove.
	 */
	public void remove (FlowRule rule)
	{
		rules.remove (rule.getTypeId ());
	}

	/**
	 * Tell the flow control that a specific rule has succeeded.
	 *
	 * @param id The id of the rule.
	 * @param arg Optional success argument.
	 */
	public void ruleSuccess (String id, Object arg)
	{
		FlowRule rule = (FlowRule) rules.get (id);

		if (rule == null)
		{
			return;
		}

		rule.success ();
		rule.success (arg);

		if (rule.isCompleted ())
		{
			remove (rule);
		}
	}

	/**
	 * Tell the flow control that a specific rule has succeeded.
	 *
	 * @param id The id of the rule.
	 */
	public void ruleSuccess (String id)
	{
		ruleSuccess (id, null);
	}

	/**
	 * Tell the flow control that a specific rule has failed.
	 *
	 * @param id The id of the rule.
	 * @param arg Optional success argument.
	 */
	public void ruleFailure (String id, Object arg)
	{
		FlowRule rule = (FlowRule) rules.get (id);

		if (rule == null)
		{
			return;
		}

		rule.failure ();
		rule.failure (arg);

		if (rule.isCompleted ())
		{
			remove (rule);
		}
	}

	/**
	 * Tell the flow control that a specific rule has failed.
	 *
	 * @param id The id of the rule.
	 */
	public void ruleFailure (String id)
	{
		ruleFailure (id, null);
	}

	/**
	 * Check for the existence of a specific rule.
	 *
	 * @param id The id of the rule to check.
	 * @return True if the rule exists.
	 */
	public boolean ruleExists (String id)
	{
		return rules.containsKey (id);
	}

	/**
	 * Remove all rules from this flow control.
	 */
	public void clear ()
	{
		rules.clear ();
	}
}
