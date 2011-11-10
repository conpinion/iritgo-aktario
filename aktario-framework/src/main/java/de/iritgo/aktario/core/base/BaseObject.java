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


import de.iritgo.aktario.core.logger.Log;


/**
 * Super class of all classes fo the Iritgo framework.
 *
 * BaseObject provides each class with a unique type id
 * which is initialized to the class name by default.
 *
 * In addition each instance is equipped with a world wide unique
 * identifier by type.
 */
public class BaseObject
{
	/** The type id of the object. */
	protected String typeId;

	/** The unique id of the object. */
	protected long uniqueId;

	/**
	 * Create a new BaseObject and initialize the
	 * type id with the class name.
	 */
	public BaseObject()
	{
		try
		{
			typeId = this.getClass().getName();
		}
		catch (Exception x)
		{
			Log.log("system", "BaseObject", "Cannot determine class.", Log.FATAL);
		}
	}

	/**
	 * Create a new BaseObject.
	 *
	 * @param typeId The type id.
	 */
	public BaseObject(String typeId)
	{
		this(typeId, 0);
	}

	/**
	 * Create a new BaseObject.
	 *
	 * @param typeId The type id.
	 * @param uniqueId The unique id.
	 */
	public BaseObject(String typeId, long uniqueId)
	{
		this.typeId = typeId;
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique id.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId()
	{
		return uniqueId;
	}

	/**
	 * Set the unique id.
	 *
	 * @param uniqueId The new unique id.
	 */
	public void setUniqueId(long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the type id.
	 *
	 * @return The type id.
	 */
	public String getTypeId()
	{
		return typeId;
	}

	/**
	 * Set the type id.
	 *
	 * @param typeId The new type id.
	 */
	public void setTypeId(String typeId)
	{
		this.typeId = typeId;
	}

	/**
	 * Check wether this is a valid object or not.
	 *
	 * @return True for a valid object.
	 */
	public boolean isValid()
	{
		return true;
	}

	/**
	 * Return a dump form the current object.
	 *
	 * @return String The current dump
	 */
	public String dump()
	{
		return toString();
	}
}
