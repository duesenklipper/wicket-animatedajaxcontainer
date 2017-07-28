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

import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class AutoAnimatingContainer extends WebMarkupContainer
{
	private boolean previouslyVisible = false;
	private final AjaxRequestTarget.IListener ajaxListener =
			new AjaxRequestTarget.AbstractListener()
			{
				@Override
				public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget ajax)
				{
					if (map.containsValue(AutoAnimatingContainer.this))
					{
						if (AutoAnimatingContainer.this.previouslyVisible)
						{
							if (!AutoAnimatingContainer.this.isVisible())
							{
								ajax.prependJavaScript(String.format(
										"continuation|$('#%s').fadeOut(400, continuation);",
										AutoAnimatingContainer.this.getMarkupId()));
							}
						}
						else
						{
							if (AutoAnimatingContainer.this.isVisible())
							{
								AutoAnimatingContainer.this.add(new AttributeAppender("style",
										"display:none;", " ")
								{
									@Override
									public boolean isTemporary(
											Component component)
									{
										return true;
									}
								});
							}
							ajax.appendJavaScript(String.format("$('#%s').fadeIn(400);",
									AutoAnimatingContainer.this.getMarkupId()));
						}
					}
				}
			};

	public AutoAnimatingContainer(String id)
	{
		this(id, null);
	}

	public AutoAnimatingContainer(String id, IModel<?> model)
	{
		super(id, model);
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
	}

	@Override
	public void onEvent(IEvent<?> event)
	{
		if (event.getPayload() instanceof AjaxRequestTarget)
		{
			((AjaxRequestTarget) event.getPayload()).addListener(this.ajaxListener);
		}
	}

	@Override
	protected void onAfterRender()
	{
		super.onAfterRender();
		this.previouslyVisible = this.isVisible();
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.render(JavaScriptHeaderItem.forReference(Application.get()
		                                                             .getJavaScriptLibrarySettings()
		                                                             .getWicketAjaxReference()));
		response.render(JavaScriptHeaderItem.forReference(Application.get()
		                                                             .getJavaScriptLibrarySettings()
		                                                             .getJQueryReference()));
	}
}
