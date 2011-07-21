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

package de.iritgo.aktario.framework.splash;


import de.iritgo.simplelife.string.StringTools;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;


/**
 * This is a simple splash screen for the iritgo startup.
 */
public class Splash extends JFrame implements de.iritgo.aktario.core.splash.Splash
{
	/** */
	private static final long serialVersionUID = 1L;

	private Splash splash;

	private JLabel status;

	public Splash ()
	{
		super ();
		setTitle (getAppTitle ());
		setIconImage (getIcon ().getImage ());

		//		try
		//		{
		//			UIManager.setLookAndFeel (new SubstanceRavenGraphiteGlassLookAndFeel ());
		//		}
		//		catch (UnsupportedLookAndFeelException x)
		//		{
		//		}
		try
		{
			com.jgoodies.looks.plastic.PlasticLookAndFeel
							.setPlasticTheme ((com.jgoodies.looks.plastic.PlasticTheme) Class.forName (
											"com.jgoodies.looks.plastic.theme.KDE").newInstance ());
			com.jgoodies.looks.Options.setPopupDropShadowEnabled (true);
			UIManager.put ("jgoodies.popupDropShadowEnabled", Boolean.TRUE);

			LookAndFeel lnf = (LookAndFeel) getClass ().getClassLoader ().loadClass (
							"com.jgoodies.looks.plastic.PlasticXPLookAndFeelIritgo").newInstance ();

			UIManager.setLookAndFeel (lnf);

			UIManager.getLookAndFeelDefaults ().put ("ClassLoader", getClass ().getClassLoader ());
		}
		catch (Exception x)
		{
		}

		splash = this;

		try
		{
			SwingUtilities.invokeAndWait (new Runnable ()
			{
				public void run ()
				{
					setUndecorated (true);

					ImageIcon background = getBackgroundImage ();

					int width = background.getIconWidth ();
					int height = background.getIconHeight ();

					JPanel content = (JPanel) getContentPane ();

					content.setLayout (new OverlayLayout (content));

					int borderMargin = getBorderMargin ();

					content.setBorder (new CompoundBorder (new LineBorder (getBorderColor (), getBorderWidth ()),
									new EmptyBorder (borderMargin, borderMargin, borderMargin, borderMargin)));

					JPanel infoPanel = new JPanel (null);

					infoPanel.setOpaque (false);

					JLabel version = new JLabel (getAppTitle () + " " + getVersion ());

					version.setFont (new Font ("SansSerif", Font.BOLD | Font.ITALIC, 14));
					version.setForeground (new Color (100, 100, 100));
					version.setLocation (getVersionOrigin ());
					version.setSize (width, 40);
					infoPanel.add (version);

					JLabel copyright = new JLabel ("<html>" + getCopyright ().replace ("\n", "<br>") + "</html>");

					copyright.setFont (new Font ("SansSerif", Font.BOLD | Font.ITALIC, 14));
					copyright.setForeground (new Color (100, 100, 100));
					copyright.setLocation (getCopyrightOrigin ());
					copyright.setSize (width, 40);
					infoPanel.add (copyright);

					status = new JLabel ("");
					status.setFont (new Font ("SansSerif", Font.BOLD | Font.ITALIC, 14));
					status.setForeground (new Color (100, 100, 100));
					status.setLocation (getStatusOrigin ());
					status.setSize (width, 40);
					infoPanel.add (status);

					content.add (infoPanel);

					content.add (new JLabel (background));

					Dimension screenDim = getToolkit ().getScreenSize ();

					content.setPreferredSize (new Dimension (width, height));

					setBounds ((int) (screenDim.getWidth () - width) / 2, (int) (screenDim.getHeight () - height) / 2,
									width, height);

					setVisible (true);
				}
			});
		}
		catch (InterruptedException x)
		{
		}
		catch (InvocationTargetException x)
		{
		}
	}

	protected String getAppTitle ()
	{
		if (! StringTools.isTrimEmpty (System.getProperty ("iritgo.app.title")))
		{
			return System.getProperty ("iritgo.app.title");
		}

		return "Iritgo";
	}

	protected String getCopyright ()
	{
		if (! StringTools.isTrimEmpty (System.getProperty ("iritgo.app.copyright")))
		{
			return System.getProperty ("iritgo.app.copyright");
		}

		return "(C) 2004-2007 BueroByte";
	}

	protected String getVersion ()
	{
		if (! StringTools.isTrimEmpty (System.getProperty ("iritgo.app.version.long")))
		{
			return System.getProperty ("iritgo.app.version.long");
		}

		return "1.0";
	}

	protected ImageIcon getIcon ()
	{
		return new ImageIcon (getClass ().getResource ("/resources/aktario-icon-16.png"));
	}

	protected ImageIcon getBackgroundImage ()
	{
		return new ImageIcon (getClass ().getResource ("/resources/app-splash.png"));
	}

	protected Point getVersionOrigin ()
	{
		return new Point (30, 120);
	}

	protected Point getCopyrightOrigin ()
	{
		return new Point (30, 145);
	}

	protected Point getStatusOrigin ()
	{
		return new Point (30, 170);
	}

	protected Color getBorderColor ()
	{
		return new Color (24, 123, 173);
	}

	protected int getBorderWidth ()
	{
		return 2;
	}

	protected int getBorderMargin ()
	{
		return 0;
	}

	public void setText (String text)
	{
		status.setText (text);
		status.revalidate ();
	}

	public void startCoolDown ()
	{
		Thread thread = new Thread ()
		{
			public void run ()
			{
				try
				{
					Thread.sleep (3000);
				}
				catch (Exception x)
				{
				}

				splash.setVisible (false);
				splash.dispose ();
			}
		};

		thread.start ();
	}
}
