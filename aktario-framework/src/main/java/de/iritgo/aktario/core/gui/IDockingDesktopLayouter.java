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

package de.iritgo.aktario.core.gui;


import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;


/**
 * This is a dockable internal frame manager.
 */
public class IDockingDesktopLayouter extends IDesktopLayouter
{
	/** */
	private static final long serialVersionUID = 1L;

	/** Client property containing the top docked window id. */
	public static final String IRITGO_DOCK_TOP = "IRITGO_DOCK_TOP";

	/** Client property containing the top docked window. */
	public static final String IRITGO_DOCK_TOP_FRAME = "IRITGO_DOCK_TOP_FRAME";

	/** Client property containing the left docked window id. */
	public static final String IRITGO_DOCK_LEFT = "IRITGO_DOCK_LEFT";

	/** Client property containing the left docked window. */
	public static final String IRITGO_DOCK_LEFT_FRAME = "IRITGO_DOCK_LEFT_FRAME";

	/** Client property containing the bottom docked window id. */
	public static final String IRITGO_DOCK_BOTTOM = "IRITGO_DOCK_BOTTOM";

	/** Client property containing the bottom docked window. */
	public static final String IRITGO_DOCK_BOTTOM_FRAME = "IRITGO_DOCK_BOTTOM_FRAME";

	/** Client property containing the right docked window id. */
	public static final String IRITGO_DOCK_RIGHT = "IRITGO_DOCK_RIGHT";

	/** Client property containing the right docked window. */
	public static final String IRITGO_DOCK_RIGHT_FRAME = "IRITGO_DOCK_RIGHT_FRAME";

	/** Property value for a window docked to the desktop. */
	public static final String IRITGO_DOCK_DESKTOP = "IRITGO_DOCK_DESKTOP";

	/** The radius of the magnetic effect. */
	protected int magnetic;

	/** The radius of the magnetic raster effect. */
	protected int rasterSize;

	/**
	 * Create a new DesktopPaneManager with default settings.
	 */
	public IDockingDesktopLayouter()
	{
		rasterSize = 20;
		magnetic = 16;
	}

	/**
	 * Get the raster size.
	 *
	 * @return The rastert size.
	 */
	public int getRasterSize()
	{
		return rasterSize;
	}

	/**
	 * Set the raster size.
	 *
	 * @param rasterSize The new raster size.
	 */
	public void setRasterSize(int rasterSize)
	{
		this.rasterSize = rasterSize;
	}

	/**
	 * Get the magnetic radius.
	 *
	 * @return The magnetic radius.
	 */
	public int getMagnetic()
	{
		return magnetic;
	}

	/**
	 * Set the magnetic radius.
	 *
	 * @param magnetic The new magnetic radius.
	 */
	public void setMagnetic(int magnetic)
	{
		this.magnetic = magnetic;
	}

	/**
	 * Removes the frame, and, if necessary, the desktopIcon, from its parent.
	 *
	 * @param frame The JInternalFrame to be removed.
	 */
	@Override
	public void closeFrame(JInternalFrame frame)
	{
		JDesktopPane desktop = frame.getDesktopPane();
		JInternalFrame[] allFrames = desktop.getAllFrames();

		for (int i = 0; i < allFrames.length; ++i)
		{
			JInternalFrame aFrame = allFrames[i];

			if ((JInternalFrame) aFrame.getClientProperty(IRITGO_DOCK_TOP_FRAME) == frame)
			{
				aFrame.putClientProperty(IRITGO_DOCK_TOP, null);
				aFrame.putClientProperty(IRITGO_DOCK_TOP_FRAME, null);
			}

			if ((JInternalFrame) aFrame.getClientProperty(IRITGO_DOCK_LEFT_FRAME) == frame)
			{
				aFrame.putClientProperty(IRITGO_DOCK_LEFT, null);
				aFrame.putClientProperty(IRITGO_DOCK_LEFT_FRAME, null);
			}

			if ((JInternalFrame) aFrame.getClientProperty(IRITGO_DOCK_BOTTOM_FRAME) == frame)
			{
				aFrame.putClientProperty(IRITGO_DOCK_BOTTOM, null);
				aFrame.putClientProperty(IRITGO_DOCK_BOTTOM_FRAME, null);
			}

			if ((JInternalFrame) aFrame.getClientProperty(IRITGO_DOCK_RIGHT_FRAME) == frame)
			{
				aFrame.putClientProperty(IRITGO_DOCK_RIGHT, null);
				aFrame.putClientProperty(IRITGO_DOCK_RIGHT_FRAME, null);
			}
		}
	}

	/**
	 * This moves the JComponent and repaints the damaged areas.
	 *
	 * @param component The component to move.
	 * @param x The new x coordinate.
	 * @param y The new y coordinate.
	 * @param width The new width.
	 * @param height The new height.
	 */
	@Override
	public void setBoundsForFrame(JComponent component, int x, int y, int width, int height)
	{
		JInternalFrame frame = (JInternalFrame) component;
		int curWidth = component.getWidth();
		int curHeight = component.getHeight();
		int curX = component.getX();
		int curY = component.getY();
		JDesktopPane desktop = frame.getDesktopPane();
		JInternalFrame[] allFrames = desktop.getAllFrames();
		int numFrames = allFrames.length;
		int deskWidth = desktop.getWidth();
		int deskHeight = desktop.getHeight();

		if (x != curX)
		{
			int newLeft = checkLeft(frame, allFrames, numFrames, x, y, height);

			if (newLeft != x)
			{
				width += x - newLeft;
				x = newLeft;
			}
		}
		else if (width != curWidth)
		{
			width = checkRight(frame, allFrames, numFrames, x, y, width, height, deskWidth) - x;
		}

		if (y != curY)
		{
			int newTop = checkTop(frame, allFrames, numFrames, x, y, width);

			if (newTop != y)
			{
				height += y - newTop;
				y = newTop;
			}
		}
		else if (height != curHeight)
		{
			height = checkBottom(frame, allFrames, numFrames, x, y, width, height, deskHeight) - y;
		}

		super.setBoundsForFrame(component, x, y, width, height);
	}

	/**
	 * @see javax.swing.DefaultDesktopManager#dragFrame(javax.swing.JComponent, int, int)
	 */
	@Override
	public void dragFrame(JComponent component, int x, int y)
	{
		JInternalFrame frame = (JInternalFrame) component;
		int width = component.getWidth();
		int height = component.getHeight();
		JDesktopPane desktop = frame.getDesktopPane();
		JInternalFrame[] allFrames = desktop.getAllFrames();
		int numFrames = allFrames.length;
		int deskWidth = desktop.getWidth();
		int deskHeight = desktop.getHeight();

		x = checkLeft(frame, allFrames, numFrames, x, y, height);
		x = checkRight(frame, allFrames, numFrames, x, y, width, height, deskWidth) - width;
		y = checkTop(frame, allFrames, numFrames, x, y, width);
		y = checkBottom(frame, allFrames, numFrames, x, y, width, height, deskHeight) - height;

		super.dragFrame(component, x, y);
	}

	/**
	 * Check and align the left frame border.
	 *
	 * @param frame The frame to align.
	 * @param allFrames An array of all the frames.
	 * @param numFrames Nubmer of frames.
	 * @param x The new x cooridnate of the frame.
	 * @param y The new x cooridnate of the frame.
	 * @param height The new frame height.
	 * @return The aligned left coordinate.
	 */
	protected int checkLeft(JInternalFrame frame, JInternalFrame[] allFrames, int numFrames, int x, int y, int height)
	{
		int newLeft = x;

		JInternalFrame dockFrame = null;

		String dockTop = (String) frame.getClientProperty(IRITGO_DOCK_TOP);

		if (dockTop != null && dockTop != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_TOP_FRAME);
		}

		String dockBottom = (String) frame.getClientProperty(IRITGO_DOCK_BOTTOM);

		if (dockBottom != null && dockBottom != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_BOTTOM_FRAME);
		}

		if (dockFrame != null && x >= dockFrame.getX() - magnetic && x <= dockFrame.getX() + magnetic)
		{
			newLeft = dockFrame.getX();
		}

		frame.putClientProperty(IRITGO_DOCK_LEFT, null);

		if (x >= 0 - magnetic && x <= 0 + magnetic)
		{
			frame.putClientProperty(IRITGO_DOCK_LEFT, IRITGO_DOCK_DESKTOP);
			newLeft = 0;
		}
		else
		{
			for (int i = 0; i < numFrames; ++i)
			{
				if (allFrames[i] == frame)
				{
					continue;
				}

				if (y <= allFrames[i].getY() + allFrames[i].getHeight() + magnetic
								&& y + height >= allFrames[i].getY() - magnetic
								&& x >= allFrames[i].getX() + allFrames[i].getWidth() - magnetic
								&& x <= allFrames[i].getX() + allFrames[i].getWidth() + magnetic)
				{
					frame.putClientProperty(IRITGO_DOCK_LEFT, allFrames[i].getName());
					frame.putClientProperty(IRITGO_DOCK_LEFT_FRAME, allFrames[i]);
					newLeft = allFrames[i].getX() + allFrames[i].getWidth();

					break;
				}
			}
		}

		return newLeft;
	}

	/**
	 * Check and align the right frame border.
	 *
	 * @param frame The frame to align.
	 * @param allFrames An array of all the frames.
	 * @param numFrames Nubmer of frames.
	 * @param x The new x cooridnate of the frame.
	 * @param y The new y cooridnate of the frame.
	 * @param width The new frame width.
	 * @param height The new frame height.
	 * @param deskWidth The desktop width.
	 * @return The aligned right coordinate.
	 */
	protected int checkRight(JInternalFrame frame, JInternalFrame[] allFrames, int numFrames, int x, int y, int width,
					int height, int deskWidth)
	{
		int newRight = x + width;

		JInternalFrame dockFrame = null;

		String dockTop = (String) frame.getClientProperty(IRITGO_DOCK_TOP);

		if (dockTop != null && dockTop != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_TOP_FRAME);
		}

		String dockBottom = (String) frame.getClientProperty(IRITGO_DOCK_BOTTOM);

		if (dockBottom != null && dockBottom != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_BOTTOM_FRAME);
		}

		if (dockFrame != null && x + width >= dockFrame.getX() + dockFrame.getWidth() - magnetic
						&& x + width <= dockFrame.getX() + dockFrame.getWidth() + magnetic)
		{
			newRight = dockFrame.getX() + dockFrame.getWidth();
		}

		frame.putClientProperty(IRITGO_DOCK_RIGHT, null);

		if (x >= deskWidth - width - magnetic && x <= deskWidth - width + magnetic)
		{
			frame.putClientProperty(IRITGO_DOCK_RIGHT, IRITGO_DOCK_DESKTOP);
			newRight = deskWidth;
		}
		else
		{
			for (int i = 0; i < numFrames; ++i)
			{
				if (allFrames[i] == frame)
				{
					continue;
				}

				if (y <= allFrames[i].getY() + allFrames[i].getHeight() + magnetic
								&& y + height >= allFrames[i].getY() - magnetic
								&& x + width >= allFrames[i].getX() - magnetic
								&& x + width <= allFrames[i].getX() + magnetic)
				{
					frame.putClientProperty(IRITGO_DOCK_RIGHT, allFrames[i].getName());
					frame.putClientProperty(IRITGO_DOCK_RIGHT_FRAME, allFrames[i]);
					newRight = allFrames[i].getX();

					break;
				}
			}
		}

		return newRight;
	}

	/**
	 * Check and align the top frame border.
	 *
	 * @param frame The frame to align.
	 * @param allFrames An array of all the frames.
	 * @param numFrames Nubmer of frames.
	 * @param x The new x cooridnate of the frame.
	 * @param y The new y cooridnate of the frame.
	 * @param width The new width of the frame.
	 * @return The aligned top coordinate.
	 */
	protected int checkTop(JInternalFrame frame, JInternalFrame[] allFrames, int numFrames, int x, int y, int width)
	{
		int newTop = y;

		JInternalFrame dockFrame = null;

		String dockRight = (String) frame.getClientProperty(IRITGO_DOCK_RIGHT);

		if (dockRight != null && dockRight != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_RIGHT_FRAME);
		}

		String dockLeft = (String) frame.getClientProperty(IRITGO_DOCK_LEFT);

		if (dockLeft != null && dockLeft != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_LEFT_FRAME);
		}

		if (dockFrame != null && y >= dockFrame.getY() - magnetic && y <= dockFrame.getY() + magnetic)
		{
			newTop = dockFrame.getY();
		}

		frame.putClientProperty(IRITGO_DOCK_TOP, null);

		if (y >= 0 - magnetic && y <= 0 + magnetic)
		{
			frame.putClientProperty(IRITGO_DOCK_TOP, IRITGO_DOCK_DESKTOP);
			newTop = 0;
		}
		else
		{
			for (int i = 0; i < numFrames; ++i)
			{
				if (allFrames[i] == frame)
				{
					continue;
				}

				if (x <= allFrames[i].getX() + allFrames[i].getWidth() + magnetic
								&& x + width >= allFrames[i].getX() - magnetic
								&& y >= allFrames[i].getY() + allFrames[i].getHeight() - magnetic
								&& y <= allFrames[i].getY() + allFrames[i].getHeight() + magnetic)
				{
					frame.putClientProperty(IRITGO_DOCK_TOP, allFrames[i].getName());
					frame.putClientProperty(IRITGO_DOCK_TOP_FRAME, allFrames[i]);
					newTop = allFrames[i].getY() + allFrames[i].getHeight();

					break;
				}
			}
		}

		return newTop;
	}

	/**
	 * Check and align the bottom frame border.
	 *
	 * @param frame The frame to align.
	 * @param allFrames An array of all the frames.
	 * @param numFrames Nubmer of frames.
	 * @param x The new x cooridnate of the frame.
	 * @param y The new y cooridnate of the frame.
	 * @param width The new frame width.
	 * @param height The new frame height.
	 * @param deskHeight The desktop height.
	 * @return The aligned bottom coordinate.
	 */
	protected int checkBottom(JInternalFrame frame, JInternalFrame[] allFrames, int numFrames, int x, int y, int width,
					int height, int deskHeight)
	{
		int newBottom = y + height;

		JInternalFrame dockFrame = null;

		String dockRight = (String) frame.getClientProperty(IRITGO_DOCK_RIGHT);

		if (dockRight != null && dockRight != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_RIGHT_FRAME);
		}

		String dockLeft = (String) frame.getClientProperty(IRITGO_DOCK_LEFT);

		if (dockLeft != null && dockLeft != IRITGO_DOCK_DESKTOP)
		{
			dockFrame = (JInternalFrame) frame.getClientProperty(IRITGO_DOCK_LEFT_FRAME);
		}

		if (dockFrame != null && y + height >= dockFrame.getY() + dockFrame.getHeight() - magnetic
						&& y + height <= dockFrame.getY() + dockFrame.getHeight() + magnetic)
		{
			newBottom = dockFrame.getY() + dockFrame.getHeight();
		}

		frame.putClientProperty(IRITGO_DOCK_BOTTOM, null);

		if (y >= deskHeight - height - magnetic && y <= deskHeight - height + magnetic)
		{
			frame.putClientProperty(IRITGO_DOCK_BOTTOM, IRITGO_DOCK_DESKTOP);
			newBottom = deskHeight;
		}
		else
		{
			for (int i = 0; i < numFrames; ++i)
			{
				if (allFrames[i] == frame)
				{
					continue;
				}

				if (x <= allFrames[i].getX() + allFrames[i].getWidth() + magnetic
								&& x + width >= allFrames[i].getX() - magnetic
								&& y + height >= allFrames[i].getY() - magnetic
								&& y + height <= allFrames[i].getY() + magnetic)
				{
					frame.putClientProperty(IRITGO_DOCK_BOTTOM, allFrames[i].getName());
					frame.putClientProperty(IRITGO_DOCK_BOTTOM_FRAME, allFrames[i]);
					newBottom = allFrames[i].getY();

					break;
				}
			}
		}

		return newBottom;
	}
}
