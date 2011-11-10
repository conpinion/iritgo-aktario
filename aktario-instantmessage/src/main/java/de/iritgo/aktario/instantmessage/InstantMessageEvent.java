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

package de.iritgo.aktario.instantmessage;


import de.iritgo.aktario.core.event.*;


/**
 * A simple event class for standard events.
 */
public class InstantMessageEvent implements Event
{
	private boolean incoming;

	private long timestamp;

	private String message;

	private String sourceUser;

	private String targetUser;

	public InstantMessageEvent()
	{
	}

	/**
	 * Default constructor
	 */
	public InstantMessageEvent(long timestamp, String message, String sourceUser, String targetUser, boolean incoming)
	{
		this.timestamp = timestamp;
		this.message = message;
		this.sourceUser = sourceUser;
		this.targetUser = targetUser;
		this.incoming = incoming;
	}

	/**
	 * Get timestamp.
	 *
	 * @return String The timestamp.
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Get Message.
	 *
	 * @return String The message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Get the sending user.
	 *
	 * @return String The sending user.
	 */
	public String getSourceUser()
	{
		return sourceUser;
	}

	/**
	 * Get the receiving user.
	 *
	 * @return String The receiving user.
	 */
	public String getTargetUser()
	{
		return targetUser;
	}

	/**
	 * Check if this is an incoming message.
	 *
	 * @return True for an incoming message.
	 */
	public boolean isIncoming()
	{
		return incoming;
	}
}
