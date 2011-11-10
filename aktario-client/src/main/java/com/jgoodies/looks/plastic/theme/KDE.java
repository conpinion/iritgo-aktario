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

package com.jgoodies.looks.plastic.theme;


import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import javax.swing.plaf.ColorUIResource;


public class KDE extends AbstractSkyTheme
{
	@SuppressWarnings("unused")
	private static final ColorUIResource secondary1 = new ColorUIResource(128, 128, 128);

	private static final ColorUIResource secondary2 = new ColorUIResource(225, 222, 214);

	private static final ColorUIResource secondary3 = new ColorUIResource(238, 238, 230);

	private static final ColorUIResource kdeMenuSelectedBackground = new ColorUIResource(255, 221, 118);

	@Override
	public String getName()
	{
		return "KDE";
	}

	@Override
	protected ColorUIResource getPrimary1()
	{
		return new ColorUIResource(135, 147, 129);
	}

	@Override
	protected ColorUIResource getPrimary2()
	{
		return new ColorUIResource(223, 223, 215);
	}

	@Override
	protected ColorUIResource getPrimary3()
	{
		return new ColorUIResource(162, 173, 186);
	}

	@Override
	public ColorUIResource getMenuItemSelectedBackground()
	{
		return kdeMenuSelectedBackground;
	}

	@Override
	public ColorUIResource getMenuItemSelectedForeground()
	{
		return getBlack();
	}

	@Override
	public ColorUIResource getMenuSelectedBackground()
	{
		return kdeMenuSelectedBackground;
	}

	@Override
	public ColorUIResource getFocusColor()
	{
		return PlasticLookAndFeel.getHighContrastFocusColorsEnabled() ? Colors.ORANGE_FOCUS : Colors.GRAY_DARK;
	}

	@Override
	protected ColorUIResource getSecondary3()
	{
		return secondary3;
	}

	@Override
	protected ColorUIResource getSecondary2()
	{
		return secondary2;
	}
}
