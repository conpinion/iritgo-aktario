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

package de.iritgo.aktario.core.base;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * All instances of BaseObjects are stored in this registry.
 *
 * This is the central point to retrieve objects by it's unique id.
 */
public class BaseRegistry
{
	/** Mapping from type ids to BaseObject map. */
	private Map typeObjectMapping;

	/**
	 * Create a new BaseRegistry.
	 */
	public BaseRegistry ()
	{
		typeObjectMapping = new HashMap ();
	}

	/**
	 * Add a BaseObject to the registry. The object is stored in the registry
	 * under it's unique id.
	 *
	 * @param object The object to add.
	 */
	synchronized public void add (BaseObject object)
	{
		String objectTypeId = object.getTypeId ();
		Map baseObjects = (Map) typeObjectMapping.get (objectTypeId);

		if (baseObjects == null)
		{
			baseObjects = new HashMap ();
			typeObjectMapping.put (objectTypeId, baseObjects);
		}

		baseObjects.put (new Long (object.getUniqueId ()), object);
	}

	/**
	 * Retrieve a BaseObject by it's unique id.
	 *
	 * @param uniqueId The unique of the object to retrieve.
	 * @param typeId The type id of the object to retrieve.
	 * @return The object or null if it wasn't found.
	 */
	public BaseObject get (long uniqueId, String typeId)
	{
		Map baseObjects = (Map) typeObjectMapping.get (typeId);

		if (baseObjects == null)
		{
			return null;
		}

		BaseObject baseObject = (BaseObject) baseObjects.get (new Long (uniqueId));

		if (baseObject == null)
		{
			return null;
		}

		return baseObject;
	}

	/**
	 * Objects in the registry.
	 *
	 * @return object The object to add.
	 */
	synchronized public int size ()
	{
		int size = 0;

		for (Iterator i = typeObjectMapping.values ().iterator (); i.hasNext ();)
		{
			Map baseObjects = (Map) i.next ();

			size += baseObjects.size ();
		}

		return size;
	}

	/**
	 * Remove a BaseObject from the registry.
	 *
	 * @param object The object to remove.
	 */
	synchronized public void remove (BaseObject object)
	{
		Map baseObjects = (Map) typeObjectMapping.get (object.getTypeId ());

		if (baseObjects == null)
		{
			return;
		}

		baseObjects.remove (new Long (object.getUniqueId ()));
	}

	/**
	 * Get an iterator over all objects in this registry.
	 *
	 * @return The object iterator.
	 */
	synchronized public Iterator iterator ()
	{
		return null;

		// Map baseObjects = typeObjectMapping.get (typeId);
		// 		if (baseObjects == null)
		// 			return;
		// 		return baseObjects.values ().iterator ();
	}

	/**
	 * Get an iterator over a type id from the objects in this registry.
	 *
	 * @return The object iterator.
	 */
	synchronized public Iterator iterator (String typeId)
	{
		Map baseObjects = (Map) typeObjectMapping.get (typeId);

		if (baseObjects == null)
		{
			return new LinkedList ().iterator ();
		}

		List tmpList = new LinkedList (baseObjects.values ());

		return tmpList.iterator ();
	}

	/**
	 * Remove all base objects from this registry.
	 */
	synchronized public void clear ()
	{
		for (Iterator i = typeObjectMapping.values ().iterator (); i.hasNext ();)
		{
			((Map) i.next ()).clear ();
		}

		typeObjectMapping.clear ();
	}
}
