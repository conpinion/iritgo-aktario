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


import de.iritgo.aktario.core.iobject.IObject;


/**
 * Interface for unique id generators. An id generator provides methods for
 * creating new unique ids and storing and loading of its internal state.
 */
public interface IDGenerator extends IObject
{
	/**
	 * Create a new unique id.
	 *
	 * @return The new unique id.
	 */
	public long createId();

	/**
	 * Get the value of the next id that createId() will return.
	 *
	 * @return The next id value.
	 */
	public long peekNextId();

	/**
	 * Load the last generator state.
	 */
	public void load();

	/**
	 * Store the generator state.
	 */
	public void save();
}
