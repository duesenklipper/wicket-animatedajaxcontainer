/**
 * Copyright (C) 2017 Carl-Eric Menzel <cmenzel@wicketbuch.de> and possibly other
 * animatedajaxcontainer contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.wicketbuch.extensions.animatedajaxcontainer;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This behavior will automatically detect if the owning component's visibility has changed during
 * an ajax request. If so, it will add some Javascript to the ajax response to animate the
 * appearance or disappearance of the owning component. By default, these animations are jQuery's
 * {@code fadeIn}/{@code fadeOut} functions, but that can be customized by overriding {@link
 * #getOpenAnimation(Component)} and {@link #getCloseAnimation(Component)}.
 * <p/>
 * Any instance of this behavior can only be used on one component. Multiple components each need
 * their own instance of this behavior.
 */
public class AjaxAutoAnimatingBehavior extends Behavior
{
	private boolean previouslyVisible = false;
	private Component owner;

	@Override
	public void bind(Component component)
	{
		if (this.owner != null)
		{
			throw new IllegalStateException(
					"this behavior can only be bound to a single component per instance");
		}
		this.owner = component;
		component.setOutputMarkupId(true);
		component.setOutputMarkupPlaceholderTag(true);
	}

	@Override
	public void onConfigure(Component component)
	{
		super.onConfigure(component);
		final AjaxRequestTarget ajax = RequestCycle.get().find(AjaxRequestTarget.class);
		if (ajax != null)
		{
			if (previouslyVisible)
			{
				if (!component.isVisible())
				{
					ajax.prependJavaScript(getCloseAnimation(component));
				}
			}
			else
			{
				if (component.isVisible())
				{
					component.add(new AttributeAppender("style", "display:none;", " ")
					{
						@Override
						public boolean isTemporary(Component component)
						{
							return true;
						}
					});
					ajax.appendJavaScript(getOpenAnimation(component));
				}
			}
		}
		this.previouslyVisible = component.isVisible();
	}

	/**
	 * Generate a JavaScript string that will animate the component's appearance when it changes
	 * from invisible to visible. The component will initially be styled with {@code display:none}.
	 *
	 * @param component The component to be animated
	 * @return The JavaScript string.
	 */
	protected String getOpenAnimation(Component component)
	{
		return String.format("$('#%s').fadeIn(400);", component.getMarkupId());
	}

	/**
	 * Generate a JavaScript string that will animate the component's disappearance when it changes
	 * from visible to invisible. <strong>Note:</strong> Since this animation must run before
	 * Wicket's JS modifies the DOM, it must accept a continuation function, provided by Wicket's
	 * pipe syntax, e.g.:
	 * <pre>
	 *     continuation|$('#%s').fadeOut(400, continuation);
	 * </pre>
	 * {@code continuation|} (note the pipe symbol) tells Wicket to call the following code in an
	 * environment that contains the continuation function with the given {@code continuation} name.
	 * The animation code must call this continuation function when the animation is complete.
	 *
	 * @param component The component to be animated
	 * @return The JavaScript string.
	 */
	protected String getCloseAnimation(Component component)
	{
		return String.format("continuation|$('#%s').fadeOut(400, continuation);",
				component.getMarkupId());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response)
	{
		response.render(JavaScriptHeaderItem.forReference(Application.get()
		                                                             .getJavaScriptLibrarySettings()
		                                                             .getWicketAjaxReference()));
		response.render(JavaScriptHeaderItem.forReference(Application.get()
		                                                             .getJavaScriptLibrarySettings()
		                                                             .getJQueryReference()));
	}
}
