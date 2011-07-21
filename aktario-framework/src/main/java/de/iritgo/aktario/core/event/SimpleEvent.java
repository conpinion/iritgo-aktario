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

package de.iritgo.aktario.core.event;


import java.util.Properties;


/**
 * A simple event class for standard events.
 */
public class SimpleEvent implements Event
{
	private String eventName;

	private Properties eventProperties;

	public SimpleEvent ()
	{
	}

	/**
	 * Default constructor
	 *
	 * @param eventName The event name for this event.
	 * @param properties The properties for this event.
	 */
	public SimpleEvent (String eventName, Properties properties)
	{
		this.eventProperties = properties;
	}

	/**
	 * Get the name for this event.
	 *
	 * @return String The event name
	 */
	public String getEventName ()
	{
		return eventName;
	}

	/**
	 * Get the event properties.
	 *
	 * @return Properties The event properties.
	 */
	public Properties getEventProperties ()
	{
		return eventProperties;
	}
}