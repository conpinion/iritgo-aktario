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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.client.AktarioClientPlugin;
import de.iritgo.aktario.client.PreferencesManager;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.user.AktarioUserPreferences;
import de.iritgo.simplelife.math.NumberTools;
import de.iritgo.simplelife.string.StringTools;
import org.swixml.SwingEngine;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * This gui pane is used to edit the user preferences.
 *
 * @version $Id: PreferencesPane.java,v 1.20 2006/10/09 22:08:43 grappendorf Exp $
 */
@SuppressWarnings("serial")
public class PreferencesPane extends SwingGUIPane
{
	/**
	 * Items of the category list.
	 */
	public class CategoryItem
	{
		public String name;

		public ImageIcon icon;

		public CategoryItem(String name, ImageIcon icon)
		{
			this.name = name;
			this.icon = icon;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}

	/**
	 * Items of the language combo box.
	 */
	private class LanguageItem
	{
		public String label;

		public String localeId;

		public LanguageItem(String label, String localeId)
		{
			this.label = label;
			this.localeId = localeId;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	/**
	 * Items of the color scheme combo box.
	 */
	private class ColorSchemeItem
	{
		public String name;

		public String className;

		public ColorSchemeItem(String name, String className)
		{
			this.name = name;
			this.className = className;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}

	/** Category list. */
	public JList categoryList;

	/** Category list model. */
	public DefaultListModel categoryModel;

	/** Category panel. */
	public JPanel category;

	/** Category title. */
	public JLabel categoryTitle;

	/** Color scheme selection. */
	public JComboBox colorScheme;

	/** Language selection. */
	public JComboBox language;

	/** Wether to always redraw the window contents. */
	public JCheckBox alwaysDrawWindowContents;

	/** Wether to start the client in minimized mode. */
	public JCheckBox startMinimized;

	/** Wether to automatically login. */
	public JCheckBox autoLogin;

	/** Auto login User name. */
	public JTextField autoLoginUser;

	/** Auto login Server name. */
	public JTextField autoLoginServer;

	/** Auto login User password. */
	public JPasswordField autoLoginPassword;

	/** Auto login User password repeated. */
	public JPasswordField autoLoginPasswordRepeat;

	/** If true, event listeners will ignore all events. */
	protected boolean ignoreEvents;

	/**
	 * Store the preferences and close the dialog.
	 */
	public Action saveAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			display.close();
			storeToObject();

			PreferencesManager preferencesManager = (PreferencesManager) Engine.instance().getManager(
							"PreferencesManager");

			preferencesManager.saveAction();
		}
	};

	/**
	 * Store the preferences.
	 */
	public Action applyAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			storeToObject();

			PreferencesManager preferencesManager = (PreferencesManager) Engine.instance().getManager(
							"PreferencesManager");

			preferencesManager.applyAction();
		}
	};

	/**
	 * Close the dialog.
	 */
	public Action cancelAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e)
		{
			display.close();

			PreferencesManager preferencesManager = (PreferencesManager) Engine.instance().getManager(
							"PreferencesManager");

			preferencesManager.cancleAction();
		}
	};

	/**
	 * Create a new PreferencesGUIPane.
	 */
	public PreferencesPane()
	{
		super("PreferencesGUIPane");
	}

	/**
	 * Initialize the gui. Subclasses should override this method to create a
	 * custom gui.
	 */
	@Override
	public void initGUI()
	{
		try
		{
			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader(AktarioClientPlugin.class.getClassLoader());

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/PreferencesPane.xml"));

			final Font categoryFont = new Font(categoryList.getFont().getFamily(), Font.BOLD, categoryList.getFont()
							.getSize());

			categoryTitle.setFont(categoryFont);

			categoryList.setCellRenderer(new DefaultListCellRenderer()
			{
				EmptyBorder border = new EmptyBorder(4, 4, 4, 4);

				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
								boolean cellHasFocus)
				{
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

					CategoryItem item = (CategoryItem) value;

					setIcon(item.icon);
					setFont(categoryFont);
					setHorizontalAlignment(JLabel.CENTER);
					setHorizontalTextPosition(JLabel.CENTER);
					setVerticalTextPosition(JLabel.BOTTOM);
					setBorder(border);

					return this;
				}
			});

			categoryModel = new DefaultListModel();
			categoryList.setModel(categoryModel);

			categoryList.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (! ignoreEvents)
					{
						selectCategory(categoryList.getSelectedIndex());
					}
				}
			});

			ResourceService resources = Engine.instance().getResourceService();

			categoryModel.addElement(new CategoryItem(resources.getString("aktario.start"), new ImageIcon(
							PreferencesPane.class.getResource("/resources/settings-start.png"))));

			categoryModel.addElement(new CategoryItem(resources.getString("aktario.appearance"), new ImageIcon(
							PreferencesPane.class.getResource("/resources/settings-appearance.png"))));

			categoryModel.addElement(new CategoryItem(resources.getString("aktario.localeSettings"), new ImageIcon(
							PreferencesPane.class.getResource("/resources/settings-locale.png"))));

			PreferencesManager preferencesManager = (PreferencesManager) Engine.instance().getManager(
							"PreferencesManager");

			preferencesManager.addPreferences(this);

			autoLogin.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					boolean enable = autoLogin.isSelected();

					autoLoginUser.setEnabled(enable);
					autoLoginServer.setEnabled(enable);
					autoLoginPassword.setEnabled(enable);
					autoLoginPasswordRepeat.setEnabled(enable);
				}
			});

			List themes = com.jgoodies.looks.plastic.PlasticLookAndFeel.getInstalledThemes();

			for (Iterator i = themes.iterator(); i.hasNext();)
			{
				MetalTheme theme = (MetalTheme) i.next();

				colorScheme.addItem(new ColorSchemeItem(theme.getName(), theme.getClass().getName()));
			}

			content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			language.addItem(new LanguageItem("Deutsch", "de"));
			language.addItem(new LanguageItem("English", "en"));

			getDisplay().putProperty("weightx", new Double(1.5));

			selectCategory(NumberTools.toInt(getDisplay().getProperties().get("category"), 0));
		}
		catch (Exception x)
		{
			Log.logError("client", "PreferencesPane.initGUI", x.toString());
			x.printStackTrace();
		}
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane()
	{
		return new PreferencesPane();
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	public IObject getSampleObject()
	{
		return new AktarioUserPreferences();
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject(IObject iobject)
	{
		AktarioUserPreferences preferences = (AktarioUserPreferences) iobject;

		for (int i = 0; i < colorScheme.getItemCount(); ++i)
		{
			if ((((ColorSchemeItem) colorScheme.getItemAt(i))).className.equals(preferences.getColorScheme()))
			{
				colorScheme.setSelectedIndex(i);

				break;
			}
		}

		alwaysDrawWindowContents.setSelected(preferences.getAlwaysDrawWindowContents());

		for (int i = 0; i < language.getItemCount(); ++i)
		{
			if (((LanguageItem) language.getItemAt(i)).localeId.equals(preferences.getLanguage()))
			{
				language.setSelectedIndex(i);

				break;
			}
		}

		Properties props = Engine.instance().getSystemProperties();

		startMinimized.setSelected(NumberTools.toBool(props.getProperty("startMinimized"), false));
		autoLogin.setSelected(NumberTools.toBool(props.getProperty("autoLogin"), false));
		autoLoginUser.setText(props.getProperty("autoLoginUser"));
		autoLoginServer.setText(props.getProperty("autoLoginServer"));
		autoLoginPassword.setText(StringTools.decode(props.getProperty("autoLoginPassword")));
		autoLoginPasswordRepeat.setText(StringTools.decode(props.getProperty("autoLoginPassword")));
	}

	/**
	 * StoreFormObject, load the Data form Object.
	 */
	@Override
	public void storeToObject(IObject iobject)
	{
		AktarioUserPreferences preferences = (AktarioUserPreferences) iobject;

		preferences.setColorScheme(((ColorSchemeItem) colorScheme.getSelectedItem()).className);
		preferences.setAlwaysDrawWindowContents(alwaysDrawWindowContents.isSelected());
		preferences.setLanguage(((LanguageItem) language.getSelectedItem()).localeId);
		preferences.update();

		Properties props = Engine.instance().getSystemProperties();

		props.put("startMinimized", startMinimized.isSelected() ? "true" : "false");
		props.put("autoLogin", autoLogin.isSelected() ? "true" : "false");

		if (autoLogin.isSelected())
		{
			props.put("autoLoginUser", autoLoginUser.getText());
			props.put("autoLoginServer", autoLoginServer.getText());
			props.put("autoLoginPassword", StringTools.encode(new String(autoLoginPassword.getPassword())));
		}

		Engine.instance().storeSystemProperties();
	}

	/**
	 * Select a category.
	 *
	 * @param index The category index.
	 */
	public void selectCategory(int index)
	{
		ignoreEvents = true;
		((CardLayout) (category.getLayout())).show(category, String.valueOf(index));
		categoryList.setSelectedIndex(index);
		categoryTitle.setText(categoryList.getSelectedValue().toString());
		ignoreEvents = false;
	}

	public void addPreferencesPane(String name, ImageIcon image, JPanel panel, int count)
	{
		categoryModel.addElement(new CategoryItem(name, image));
		category.add(panel, "" + count);
	}
}
