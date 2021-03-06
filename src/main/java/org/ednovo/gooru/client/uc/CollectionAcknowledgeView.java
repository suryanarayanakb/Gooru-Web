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
package org.ednovo.gooru.client.uc;


import java.util.List;

import org.ednovo.gooru.application.client.gin.AppClientFactory;
import org.ednovo.gooru.application.shared.model.content.CollectionDo;
import org.ednovo.gooru.application.shared.model.content.CollectionItemDo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class CollectionAcknowledgeView extends Composite implements HasClickHandlers{

	
	@UiField FlowPanel acknowledgeContainer;
	
	private static BottomButtonViewUiBinder uiBinder = GWT.create(BottomButtonViewUiBinder.class);

	interface BottomButtonViewUiBinder extends UiBinder<Widget, CollectionAcknowledgeView> {
	}
	
	public CollectionAcknowledgeView(){
		initWidget(uiBinder.createAndBindUi(this));
		acknowledgeContainer.getElement().setId("fpnlAcknowledgeContainer");
	}
	@UiConstructor
	public CollectionAcknowledgeView(CollectionDo collectionDo){
		initWidget(uiBinder.createAndBindUi(this));
		acknowledgeContainer.getElement().setId("fpnlAcknowledgeContainer");
		PlayerBundle.INSTANCE.getPlayerStyle().ensureInjected();
		renderAcknowledgeResource(collectionDo);
	}
	
	public void renderAcknowledgeResource(CollectionDo collectionDo){
		if(collectionDo!=null&&collectionDo.getGooruOid()!=null){
			List<String> acknowldegeList=collectionDo.getMetaInfo().getAcknowledgement();
			for(int i=0;i<acknowldegeList.size();i++){
				String sourceName=acknowldegeList.get(i);
				sourceName=sourceName!=null?sourceName.trim():"";
				final FlowPanel resourceSourcesContainer=new FlowPanel();
				final FlowPanel resourceSourcesWidget=new FlowPanel();
				for(int itemsCount=0;itemsCount<collectionDo.getCollectionItems().size();itemsCount++){
					CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(itemsCount);
					String attribution=collectionItemDo.getResource().getResourceSource()!=null?collectionItemDo.getResource().getResourceSource().getAttribution():null;
					if(attribution!=null&&attribution.trim().equalsIgnoreCase(sourceName)){
						if((!collectionItemDo.getResource().getUrl().startsWith("https://docs.google.com"))&&(!collectionItemDo.getResource().getUrl().startsWith("http://docs.google.com"))){
							Label sourceNameLabel=getResourceNameLabel(sourceName);
							resourceSourcesContainer.add(sourceNameLabel);
							TocResourceView ackResourceView=new TocResourceView(collectionItemDo,itemsCount+1,false,false);
						ackResourceView.setResourceTitleColor();
						//ackResourceView.addClickHandler(new PreviewResourceView(collectionItemDo, collectionDo.getGooruOid()));
						resourceSourcesWidget.add(ackResourceView);	
							}
					}
				}
				resourceSourcesContainer.add(resourceSourcesWidget);
				acknowledgeContainer.add(resourceSourcesContainer);
				
			}
		}
	}
	
	private Label getResourceNameLabel(String sourceName){
		Label sourceNameText=new Label(sourceName);
		sourceNameText.setStyleName(PlayerBundle.INSTANCE.getPlayerStyle().resourceSourceName());
		return sourceNameText;
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	public class PreviewResourceView implements ClickHandler{
		private CollectionItemDo collectionItemDo;
		private String collectionId;
		public PreviewResourceView(CollectionItemDo collectionItemDo,String collectionId){
			this.collectionItemDo=collectionItemDo;
			this.collectionId=collectionId;
		}
		@Override
		public void onClick(ClickEvent event) {
			if(this.collectionItemDo.getNarration()!=null&&!this.collectionItemDo.getNarration().trim().equals("")){
				PlaceRequest request=new PlaceRequest("p").with("id", collectionId)
						.with("rid", this.collectionItemDo.getCollectionItemId())
						.with("tab", "narration");
				AppClientFactory.getPlaceManager().revealPlace(false,request);
			}else{
				PlaceRequest request=new PlaceRequest("p").with("id", collectionId).with("rid", this.collectionItemDo.getCollectionItemId());
				AppClientFactory.getPlaceManager().revealPlace(false,request);
			}
		}
	}
	
}
