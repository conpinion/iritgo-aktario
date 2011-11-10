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

package de.iritgo.aktario.core.resource;


import de.iritgo.aktario.core.logger.Log;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;


/**
 *
 */
public class ImageComponent extends Canvas
{
	/** */
	private static final long serialVersionUID = 1L;

	private Image image;

	/**
	 * Standard constructor
	 */
	public ImageComponent(String fname)
	{
		super();

		image = getToolkit().getImage(fname);

		MediaTracker mt = new MediaTracker(this);

		mt.addImage(image, 0);

		try
		{
			mt.waitForAll();
		}
		catch (InterruptedException e)
		{
			Log.log("resources", "ImageComponent.ImageComponent", "Image: " + fname + " not found!", Log.WARN);
		}
	}

	/**
	 * Return the Image.
	 *
	 * @return The Image.
	 */
	public Image getImage()
	{
		return image;
	}

	@Override
	public void paint(Graphics g)
	{
		g.drawImage(image, 1, 1, this);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(image.getWidth(this), image.getHeight(this));
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(image.getWidth(this), image.getHeight(this));
	}
}
