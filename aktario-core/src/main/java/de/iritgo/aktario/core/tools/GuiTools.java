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

package de.iritgo.aktario.core.tools;


import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;


/**
 * Various gui related utility methods.
 *
 * @version $Id: GuiTools.java,v 1.9 2006/09/25 10:34:32 grappendorf Exp $
 */
public class GuiTools
{
	/**
	 * Get the default formatter for double values without '.'
	 *
	 * @return The factory with the formatter for the JFormattedTextField.
	 */
	public static DefaultFormatterFactory getDoubleFormatter()
	{
		return new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#")));
	}
}
