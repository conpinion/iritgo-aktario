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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.client.gui.OtherFrame;
import de.iritgo.aktario.framework.client.gui.OtherFrameCloseListener;
import java.awt.Rectangle;


/**
 *
 */
public class ShowOtherFrame extends Command
{
	private OtherFrameCloseListener closeListener;

	private String frameLabel;

	private String frameId;

	private Rectangle bounds;

	/**
	 * Standard constructor
	 */
	public ShowOtherFrame (OtherFrameCloseListener closeListener, String frameLabel, String frameId)
	{
		this.closeListener = closeListener;
		this.frameLabel = frameLabel;
		this.frameId = frameId;
	}

	/**
	 * Standard constructor
	 */
	public ShowOtherFrame (OtherFrameCloseListener closeListener, String frameLabel, String frameId, Rectangle bounds)
	{
		this (closeListener, frameLabel, frameId);
		this.bounds = bounds;
	}

	/**
	 * Standard constructor
	 */
	public ShowOtherFrame (OtherFrameCloseListener closeListener, String frameLabel)
	{
		this (closeListener, frameLabel, frameLabel);
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform ()
	{
		OtherFrame otherFrame = new OtherFrame (closeListener, frameLabel, frameId, bounds);

		otherFrame.initOtherFrame ();
	}

	public boolean canPerform ()
	{
		return true;
	}
}
