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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ImageComponent;
import de.iritgo.aktario.core.resource.ResourceNotFoundException;
import de.iritgo.aktario.core.resource.ResourcePersistent;
import java.awt.Image;


/**
 *
 */
public class ResourceImage extends ResourcePersistent
{
	private ImageComponent imageComponent;

	/**
	 * Standard constructor
	 *
	 * @param name The description of the resource.
	 * @param nodeName The node Name
	 */
	public ResourceImage(String name, String nodeName, String resourcePath)
	{
		super(name, nodeName, resourcePath);
	}

	/**
	 * Return the Image.
	 *
	 * @return The Object.
	 */
	public Image getImage() throws ResourceNotFoundException
	{
		if (imageComponent == null)
		{
			Log.log("resources", "getImage", "Loading Image: " + getResourcePath(), Log.INFO);

			imageComponent = new ImageComponent(Engine.instance().getSystemDir() + "/" + (String) getResourcePath());
		}

		return imageComponent.getImage();
	}

	public ImageComponent getImageComponent() throws ResourceNotFoundException
	{
		if (imageComponent == null)
		{
			Log.log("resources", "getImage", "Loading Image: " + getResourcePath(), Log.INFO);

			imageComponent = new ImageComponent((String) getResourcePath());
		}

		return imageComponent;
	}
}
