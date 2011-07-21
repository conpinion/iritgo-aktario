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

package de.iritgo.aktario.core.resource.resourcetypes;


import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceNotFoundException;
import de.iritgo.aktario.core.resource.ResourcePersistent;


/**
 *
 */
public class ResourceObject extends ResourcePersistent
{
	@SuppressWarnings("unused")
	private String string;

	/**
	 * Standard constructor
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node Name
	 */
	public ResourceObject (String name, String nodeName, String resourcePath)
	{
		super (name, nodeName, resourcePath);
	}

	/**
	 * Return the Object.
	 *
	 * @return The Object.
	 */
	public Object getClone () throws ResourceNotFoundException
	{
		Object object = null;

		try
		{
			object = ((Class) Class.forName ((String) getResourcePath ())).newInstance ();
		}
		catch (Exception e)
		{
			e.printStackTrace ();

			Log.log ("resource", "ResourceService.addNode", "ResourceLoader not found:(C)" + e.getMessage (), 5);
			throw new ResourceNotFoundException (("ClassNotFoundException: " + (String) getResourcePath ()));
		}

		return object;
	}
}
