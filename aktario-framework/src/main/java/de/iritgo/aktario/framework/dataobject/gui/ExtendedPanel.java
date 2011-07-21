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

package de.iritgo.aktario.framework.dataobject.gui;


import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;


/**
 * DefaultRenderer
 */
public class ExtendedPanel extends JPanel
{
	public class ErrorItem
	{
		public JPanel panel1;

		public JPanel panel2;

		public JPanel panel3;

		public JPanel panel4;

		public ErrorItem (JPanel panel1, JPanel panel2, JPanel panel3, JPanel panel4)
		{
			this.panel1 = panel1;
			this.panel2 = panel2;
			this.panel3 = panel3;
			this.panel4 = panel4;
		}
	}

	private int row;

	private Map errorMap;

	public ExtendedPanel ()
	{
		super ();
		setLayout (new GridBagLayout ());
		row = 0;
		errorMap = new HashMap ();
	}

	public void addComp (String errorId, JComponent label, JComponent control, WidgetDescription wd)
	{
		addComp (errorId, label, control, wd, GridBagConstraints.BOTH);
	}

	public void addComp (String errorId, JComponent label, JComponent control, WidgetDescription wd, int horzfill)
	{
		JPanel panel1 = new JPanel ();
		JPanel panel2 = new JPanel ();
		JPanel panel3 = new JPanel ();
		JPanel panel4 = new JPanel ();

		panel1.setLayout (new GridBagLayout ());
		panel2.setLayout (new GridBagLayout ());
		panel3.setLayout (new GridBagLayout ());
		panel4.setLayout (new GridBagLayout ());

		panel1.setBackground (Color.WHITE);
		panel2.setBackground (Color.WHITE);
		panel3.setBackground (Color.WHITE);
		panel4.setVisible (false);

		errorMap.put (errorId, new ErrorItem (panel1, panel2, panel3, panel4));

		panel1.add (label, createConstraints (0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH,
						GridBagConstraints.NORTHWEST, new Insets (4, 8, 4, 0)));

		if (wd.isMandatoryField ())
		{
			panel2.add (new JLabel ("*"), createConstraints (0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH,
							GridBagConstraints.NORTHWEST, new Insets (4, 0, 4, 0)));
		}

		panel3.add (control, createConstraints (0, 0, 1, 1, 1.0, 1.0, horzfill, GridBagConstraints.NORTHWEST,
						new Insets (4, 4, 4, 0)));

		panel4.add (new JLabel ("Dieses Pflichtfeld m�ssen Sie ausf�llen!"), createConstraints (0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, new Insets (0, 4, 0, 0)));

		super.add (panel4, createConstraints (0, row, 3, 1, 0.1, 0.0, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.NORTHWEST, new Insets (0, 32, 0, 8)));
		++row;

		super.add (panel1, createConstraints (0, row, 1, 1, 0.1, 0.0, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.NORTHWEST, new Insets (4, 32, 0, 0)));

		super.add (panel2, createConstraints (1, row, 1, 1, 0.0, 0.0, GridBagConstraints.NONE,
						GridBagConstraints.NORTHWEST, new Insets (4, 0, 0, 0)));

		super.add (panel3, createConstraints (2, row, 1, 1, 0.9, 0.0, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.NORTHWEST, new Insets (4, 0, 0, 8)));
		++row;
	}

	public void add (Component component, Object object)
	{
		super.add (component, object);
		++row;
	}

	public void setError (String widgetId)
	{
		ErrorItem errorItem = (ErrorItem) errorMap.get (widgetId);

		if (errorItem != null)
		{
			errorItem.panel1.setBackground (Color.RED);
			errorItem.panel2.setBackground (Color.RED);
			errorItem.panel3.setBackground (Color.RED);
			((JLabel) errorItem.panel4.getComponent (0)).setText ("Dieses Pflichtfeld m�ssen Sie ausf�llen!");

			errorItem.panel4.setVisible (true);
		}
	}

	public void setNoError (String widgetId)
	{
		ErrorItem errorItem = (ErrorItem) errorMap.get (widgetId);

		if (errorItem != null)
		{
			errorItem.panel1.setBackground (Color.WHITE);
			errorItem.panel2.setBackground (Color.WHITE);
			errorItem.panel3.setBackground (Color.WHITE);
			errorItem.panel4.setVisible (false);
		}
	}

	public void close ()
	{
		errorMap.clear ();
		errorMap = null;
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param anchor The anchor.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints (int x, int y, int width, int height, double wx, double wy,
					int fill, int anchor, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints ();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		if (insets == null)
		{
			gbc.insets = new Insets (0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}
}
