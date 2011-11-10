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


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;


/**
 * This component is used as a glass pane in windows and dialogs to enable/disable
 * the whole window/dialog.
 */
public class IGlassPane extends JPanel
{
	/** */
	private static final long serialVersionUID = 1L;

	protected static ImageIcon waitIcon = new ImageIcon(IGlassPane.class.getResource("/resources/app-wait.gif"));

	/** If true the underlying display will be enabled. */
	protected boolean enabled;

	/** Texture image. */
	protected BufferedImage texture;

	/** Texture pain. */
	protected TexturePaint texturePaint;

	/**
	 * Create a new LessonDisplayGlassPane.
	 */
	public IGlassPane()
	{
		texture = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = (Graphics2D) texture.getGraphics();

		g2.setPaint(new Color(255, 255, 255, 64));
		g2.fillRect(0, 0, 2, 2);
		g2.setPaint(new Color(0, 0, 0, 64));
		g2.fillRect(0, 0, 1, 1);
		g2.fillRect(1, 1, 1, 1);

		texturePaint = new TexturePaint(texture, new Rectangle(0, 0, 2, 2));

		setOpaque(false);
		setLayout(new BorderLayout());

		ILabel label = new ILabel("please.wait", new ImageIcon(getClass().getResource("/resources/addressbook.png")));

		label.setHorizontalAlignment(JLabel.CENTER);

		add(label, BorderLayout.CENTER);

		setEnabled(true);
		setVisible(true);
	}

	/**
	 * Paint the glass pane.
	 *
	 * @param g The graphics context.
	 */
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		Rectangle bounds = getBounds();

		if (enabled)
		{
		}
		else
		{
			Component topLevel = getTopLevelAncestor();
			Color bgColor = topLevel.getBackground();

			g2.setPaint(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 128));
			g2.fillRect(0, 0, bounds.width, bounds.height);

			g2.setPaint(Color.WHITE);
			g2.fillRect(0, (int) bounds.getHeight() / 2 - 32, bounds.width, 64);

			g2.setPaint(Color.BLACK);
			g2.fillRect(0, (int) bounds.getHeight() / 2 - 34, bounds.width, 2);

			g2.setPaint(Color.BLACK);
			g2.fillRect(0, (int) bounds.getHeight() / 2 + 33, bounds.width, 2);
			super.paint(g);
		}
	}

	/**
	 * Enable / disable the glass pane.
	 *
	 * @param enabled If true the glass pane will be enabled.
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;

		if (enabled)
		{
			disableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
		}
		else
		{
			enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
		}

		repaint();
	}

	/**
	 * Check wether the glass pane is enabled or not.
	 *
	 * @return True if the pane is enabled.
	 */
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Set the wait icon.
	 *
	 * @param icon The new wait icon.
	 */
	public static void setWaitIcon(ImageIcon icon)
	{
		waitIcon = icon;
	}
}
