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

package de.iritgo.aktario.core.uid;


import de.iritgo.aktario.core.base.SystemProperties;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;


/**
 * This is an id generator that stores it's state in a property object.
 */
public class PropertiesIDGenerator extends DefaultIDGenerator
{
	/** The properties in which the generator state is stored. */
	protected SystemProperties properties;

	/** The property key under which the generator state is stored. */
	protected String propertyKey;

	/**
	 * Create a new id generator.
	 */
	public PropertiesIDGenerator ()
	{
		super ("PropertiesIDGenerator", 1, 1);
	}

	/**
	 * Create a new id generator.
	 *
	 * @param properties The properties in which to store the current state.
	 * @param propertyKey The key under which to store the current state.
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public PropertiesIDGenerator (SystemProperties properties, String propertyKey, long start, long step)
	{
		super ("PropertiesIDGenerator", start, step);
		this.properties = properties;
		this.propertyKey = propertyKey;
	}

	/**
	 * Load the last generator state.
	 */
	@Override
	public void load ()
	{
		id = properties.getLong (propertyKey, id);

		Log.logDebug ("persist", "PropertiesIDGenerator", "Successfully loaded the generator state (id=" + id + ")");
	}

	/**
	 * Store the generator state.
	 */
	@Override
	public void save ()
	{
		properties.put (propertyKey, Long.toString (id));

		Log.logDebug ("persist", "PropertiesIDGenerator", "Successfully saved the generator state (id=" + id + ")");
	}

	/**
	 * Create a new instance of the id generator.
	 *
	 * @return The fresh instance.
	 */
	@Override
	public IObject create ()
	{
		return new PropertiesIDGenerator ();
	}
}
