/**
 * Copyright (C) 2017 Carl-Eric Menzel <cmenzel@wicketbuch.de>
 * and possibly other animatedajaxcontainer contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.wicketbuch.extensions.animatedajaxcontainer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage
{
	private static final long serialVersionUID = 1L;
	private boolean showComponent = false;

	public HomePage(final PageParameters parameters)
	{
		super(parameters);
		final AutoAnimatingContainer container =
				new AutoAnimatingContainer("container") {
					@Override
					protected void onConfigure()
					{
						super.onConfigure();
						setVisible(showComponent);
					}
				};
		this.add(container);
		this.add(new AjaxLink<Void>("toggle")
		{
			@Override
			public void onClick(AjaxRequestTarget ajax)
			{
				ajax.add(container);
				showComponent = !showComponent;
			}
		});
	}
}
