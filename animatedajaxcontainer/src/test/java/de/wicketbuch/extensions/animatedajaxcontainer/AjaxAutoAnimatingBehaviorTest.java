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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class AjaxAutoAnimatingBehaviorTest
{
	public static class TestPage extends WebPage
	{
		private boolean visibility = false;

		public TestPage()
		{
			final WebMarkupContainer container = new WebMarkupContainer("container")
			{
				@Override
				protected void onConfigure()
				{
					super.onConfigure();
					setVisible(visibility);
				}
			};
			container.add(new AjaxAutoAnimatingBehavior());
			this.add(container);
			this.add(new AjaxLink<Void>("showAjax")
			{
				@Override
				public void onClick(AjaxRequestTarget ajax)
				{
					ajax.add(container);
					visibility = true;
				}
			});
			this.add(new AjaxLink<Void>("hideAjax")
			{
				@Override
				public void onClick(AjaxRequestTarget ajax)
				{
					ajax.add(container);
					visibility = false;
				}
			});
			this.add(new Link<Void>("show")
			{
				@Override
				public void onClick()
				{
					visibility = true;
				}
			});
			this.add(new Link<Void>("hide")
			{
				@Override
				public void onClick()
				{
					visibility = false;
				}
			});
		}
	}

	@Test
	public void doesntActivateWithoutAjax() throws Exception
	{
		final WicketTester tester = new WicketTester();
		tester.startPage(TestPage.class);
		tester.clickLink("show", false);
		tester.assertVisible("container");
		tester.assertContainsNot("fadeIn");
	}

	@Test
	public void animatesOpeningWithAjax() throws Exception
	{
		final WicketTester tester = new WicketTester();
		tester.startPage(TestPage.class);
		tester.clickLink("showAjax", true);
		tester.assertVisible("container");
		tester.assertContains("fadeIn");
	}

	@Test
	public void doesntAnimateOpeningWithAjaxWhenAlreadyVisible() throws Exception
	{
		final WicketTester tester = new WicketTester();
		tester.startPage(TestPage.class);
		tester.clickLink("showAjax", true);
		tester.clickLink("showAjax", true);
		tester.assertVisible("container");
		tester.assertContainsNot("fadeIn");
	}

	@Test
	public void animatesClosingWithAjax() throws Exception
	{
		final WicketTester tester = new WicketTester();
		tester.startPage(TestPage.class);
		tester.clickLink("showAjax", true);
		tester.clickLink("hideAjax", true);
		tester.assertInvisible("container");
		tester.assertContains("fadeOut");
	}

	@Test
	public void doesntAnimateClosingWithAjaxWhenAlreadyInvisible() throws Exception
	{
		final WicketTester tester = new WicketTester();
		tester.startPage(TestPage.class);
		tester.clickLink("hideAjax", true);
		tester.assertInvisible("container");
		tester.assertContainsNot("fadeOut");
	}
}
