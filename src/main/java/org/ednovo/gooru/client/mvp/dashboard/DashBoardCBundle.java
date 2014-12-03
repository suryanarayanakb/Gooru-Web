/*******************************************************************************
 * Copyright 2013 Ednovo d/b/a Gooru. All rights reserved.
 * 
 *  http://www.goorulearning.org/
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.ednovo.gooru.client.mvp.dashboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;


public interface DashBoardCBundle extends ClientBundle {
	static final DashBoardCBundle INSTANCE = GWT.create(DashBoardCBundle.class);
	
	public interface  DashBoardCss extends CssResource{
		
		String clearfix();
		
		String container();
		
		String profileHeader();
		
		String rowContainer();
		
		String whiteBg();
		
		String greenBig();
		
		String greySmall();

		String smallHeader();
		
		String collectionBlock();

		String collHead();
		
		String collSmallHead();

		String resourceBlock();

		String smallHeaderIcn();

		String moreLink();

		String rating();

		String collectionsResources();
		
		String colleGrade();
		
		String treeView();

		String gradeContainer();

		String graphContainer();
		
		String graphStatistics();
		
		String statisticsBlock();
		
		String commentsContainer();
		
		String panelRatingLabels();
		
		String panelRatingValues();
		
		String ratingStars();
		
		String ratingLabel();

		String prgressBar();
		
		String bar();
		
		String count();
		
		String ratingOutBlock();
		
		String reactionOutBlock();
		
		String graphSmall();
		
		String colRight();
		
		String colLeft();
	}
	@NotStrict
	@Source("dashboard.css")
	DashBoardCss css();
}