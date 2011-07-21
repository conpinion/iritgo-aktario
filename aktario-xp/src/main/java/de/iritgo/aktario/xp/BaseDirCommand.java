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

package de.iritgo.aktario.xp;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.filebrowser.FileBrowser;
import de.iritgo.aktario.core.user.AktarioUserReadyServerAction;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.command.CommandTools;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.File;
import java.util.Properties;


/**
 * This command set the base dir for the aktario xp tool
 *
 * @version $Id: BaseDirCommand.java,v 1.6 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BaseDirCommand extends Command
{
	private SoftwareReviewPane sofwareReviewPane;

	/**
	 * Create a new command object.
	 */
	public BaseDirCommand ()
	{
		super ("SetBaseDirXPCommand");
	}

	/**
	 * Create a new command object.
	 */
	public BaseDirCommand (SoftwareReviewPane sofwareReviewPane)
	{
		super ("SetBaseDirXPCommand");
		this.sofwareReviewPane = sofwareReviewPane;
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		File baseDir = sofwareReviewPane.getBaseDir ();
		JPanel content = sofwareReviewPane.getContent ();
		JTextField fileNameDisplay = sofwareReviewPane.getFileNameDisplay ();
		FileBrowser fileBrowser = sofwareReviewPane.getFileBrowser ();

		AktarioUserReadyServerAction aktarioUserReadyServerAction = new AktarioUserReadyServerAction (AppContext
						.instance ().getUser (), false);

		ActionTools.sendToServer (aktarioUserReadyServerAction);

		JFileChooser chooser = new JFileChooser ();

		chooser.setDialogTitle (Engine.instance ().getResourceService ().getString ("chooseBaseDir"));

		if (baseDir != null)
		{
			chooser.setCurrentDirectory (baseDir);
		}

		chooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);

		int result = chooser.showOpenDialog (content.getTopLevelAncestor ());

		if (result == JFileChooser.APPROVE_OPTION)
		{
			baseDir = chooser.getSelectedFile ();
			sofwareReviewPane.setBaseDir (baseDir);

			fileNameDisplay.setText (baseDir.getAbsolutePath ());
			fileBrowser.setRoot (baseDir);

			Properties props = new Properties ();
			Integer role = (Integer) CommandTools.performSimple ("GetUserRole", props);

			sofwareReviewPane.setBasedirSelected (true);

			aktarioUserReadyServerAction = new AktarioUserReadyServerAction (AppContext.instance ().getUser (), true);

			ActionTools.sendToServer (aktarioUserReadyServerAction);
		}
	}
}
