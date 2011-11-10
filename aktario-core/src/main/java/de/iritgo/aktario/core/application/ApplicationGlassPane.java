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

package de.iritgo.aktario.core.application;


import de.iritgo.aktario.core.gui.IGlassPane;
import de.iritgo.aktario.core.logger.Log;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ApplicationGlassPane
 *
 * @version $Id: ApplicationGlassPane.java,v 1.10 2006/10/05 15:00:42 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class ApplicationGlassPane extends IGlassPane
{
	/** Pointers */
	protected class Pointer
	{
		public int pointerId;

		public int x;

		public int y;

		public Color color;

		public Pointer(int pointerId, int x, int y)
		{
			this.pointerId = pointerId;
			this.x = x;
			this.y = y;
			color = defaultPointerColors[pointerId % defaultPointerColors.length];
		}
	}

	/**
	 * Items of the paint list.
	 */
	protected class PaintItem
	{
		public int x;

		public int y;

		public int paint;

		public PaintItem(int x, int y, int paint)
		{
			this.x = x;
			this.y = y;
			this.paint = paint;
		}
	}

	/** The disabled icon. */
	protected Image disabledIcon;

	/** The enabled icon. */
	protected Image enabledIcon;

	/** Paint icons. */
	protected Image[] paintIcons;

	/** List of paints. */
	protected List paints;

	/** List of all pointers. */
	protected List pointers;

	/** Default pointer colors. */
	protected Color[] defaultPointerColors =
	{
					Color.RED, Color.BLUE, Color.GREEN.darker(), Color.YELLOW.darker(), Color.ORANGE, Color.MAGENTA,
					Color.CYAN.darker(), Color.PINK, Color.DARK_GRAY
	};

	/**
	 * Create a new LessonDisplayGlassPane.
	 */
	public ApplicationGlassPane()
	{
		pointers = new LinkedList();
		paints = new LinkedList();
		paintIcons = new Image[WhiteBoardAction.NUM_PAINTS];

		try
		{
			disabledIcon = new ImageIcon(getClass().getResource("/resources/lesson-disabled.png")).getImage();
			enabledIcon = new ImageIcon(getClass().getResource("/resources/lesson-enabled.png")).getImage();
			paintIcons[WhiteBoardAction.PAINT_EXCLAMATION] = new ImageIcon(getClass().getResource(
							"/resources/icon-exlamation.png")).getImage();
			paintIcons[WhiteBoardAction.PAINT_INFO] = new ImageIcon(getClass().getResource("/resources/icon-info.png"))
							.getImage();
			paintIcons[WhiteBoardAction.PAINT_OK] = new ImageIcon(getClass().getResource("/resources/icon-ok.png"))
							.getImage();
			paintIcons[WhiteBoardAction.PAINT_QUESTION] = new ImageIcon(getClass().getResource(
							"/resources/icon-question.png")).getImage();
		}
		catch (Exception x)
		{
			Log.logError("client", "LessonDisplayGlassPane", x.toString());
		}
	}

	/**
	 * Paint the glass pane.
	 *
	 * @param g The graphics context.
	 */
	@Override
	public void paint(Graphics g)
	{
		for (Iterator i = paints.iterator(); i.hasNext();)
		{
			PaintItem item = (PaintItem) i.next();

			g.drawImage(paintIcons[item.paint], item.x, item.y, this);
		}

		// 		Image image = isEnabled () ? enabledIcon : disabledIcon;
		// 		g.drawImage (image, bounds.width - image.getWidth (this), 0, this);
		for (Iterator i = pointers.iterator(); i.hasNext();)
		{
			Pointer p = (Pointer) i.next();

			g.setColor(p.color);
			g.fillRect(p.x - 1, p.y - 4, 3, 9);
			g.fillRect(p.x - 4, p.y - 1, 9, 3);
		}
	}

	/**
	 * Set the pointer position.
	 *
	 * @param pointerId We can have many pointers (from other users). This specifies the
	 *   unique pointer id.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void movePointer(int pointerId, int x, int y)
	{
		Pointer pointer = null;

		for (Iterator i = pointers.iterator(); i.hasNext();)
		{
			Pointer p = (Pointer) i.next();

			if (p.pointerId == pointerId)
			{
				pointer = p;

				break;
			}
		}

		if (pointer == null)
		{
			pointer = new Pointer(pointerId, x, y);
			pointers.add(pointer);
		}

		int lastX = pointer.x;
		int lastY = pointer.y;

		pointer.x = x;
		pointer.y = y;
		repaint(lastX - 4, lastY - 4, 9, 9);
		repaint(x - 4, y - 4, 9, 9);
	}

	/**
	 * Paint.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param paint What to paint.
	 */
	public void paint(int x, int y, int paint)
	{
		if (paint == WhiteBoardAction.PAINT_ERASE)
		{
			paints.clear();
		}
		else
		{
			Image image = paintIcons[paint];

			paints.add(new PaintItem(x - (image.getWidth(this) / 2), y - (image.getHeight(this) / 2), paint));
		}

		repaint();
	}
}
