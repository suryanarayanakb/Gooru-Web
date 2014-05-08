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
package org.ednovo.gooru.client.mvp.play.collection;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.client.PlaceTokens;
import org.ednovo.gooru.client.SeoTokens;
import org.ednovo.gooru.client.SimpleAsyncCallback;
import org.ednovo.gooru.client.gin.AppClientFactory;
import org.ednovo.gooru.client.gin.BasePlacePresenter;
import org.ednovo.gooru.client.mvp.authentication.SignUpPresenter;
import org.ednovo.gooru.client.mvp.home.LoginPopupUc;
import org.ednovo.gooru.client.mvp.play.collection.add.AddCollectionPresenter;
import org.ednovo.gooru.client.mvp.play.collection.body.CollectionPlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowCollectionTabWidgetEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.ShowResourceViewEvent;
import org.ednovo.gooru.client.mvp.play.collection.event.UpdateCollectionViewCountEvent;
import org.ednovo.gooru.client.mvp.play.collection.flag.CollectionFlagPresenter;
import org.ednovo.gooru.client.mvp.play.collection.info.ResourceInfoPresenter;
import org.ednovo.gooru.client.mvp.play.collection.preview.PreviewPlayerPresenter;
import org.ednovo.gooru.client.mvp.play.collection.share.CollectionSharePresenter;
import org.ednovo.gooru.client.mvp.play.collection.toc.CollectionPlayerTocPresenter;
import org.ednovo.gooru.client.mvp.play.error.CollectionNonExistView;
import org.ednovo.gooru.client.mvp.play.resource.add.AddResourceCollectionPresenter;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataPresenter;
import org.ednovo.gooru.client.mvp.play.resource.body.ResourcePlayerMetadataView;
import org.ednovo.gooru.client.mvp.play.resource.flag.ResourceFlagPresenter;
import org.ednovo.gooru.client.mvp.play.resource.narration.ResourceNarrationPresenter;
import org.ednovo.gooru.client.mvp.search.event.SetHeaderZIndexEvent;
import org.ednovo.gooru.client.mvp.search.event.UpdateSearchResultMetaDataEvent;
import org.ednovo.gooru.client.mvp.settings.CustomAnimation;
import org.ednovo.gooru.client.mvp.shelf.collection.CollectionFormInPlayPresenter;
import org.ednovo.gooru.client.mvp.shelf.event.RefreshCollectionInShelfListInPlayEvent;
import org.ednovo.gooru.client.service.PlayerAppServiceAsync;
import org.ednovo.gooru.client.util.MixpanelUtil;
import org.ednovo.gooru.client.util.PlayerDataLogEvents;
import org.ednovo.gooru.shared.model.content.ClasspageItemDo;
import org.ednovo.gooru.shared.model.content.CollectionDo;
import org.ednovo.gooru.shared.model.content.CollectionItemDo;
import org.ednovo.gooru.shared.model.content.ContentReportDo;
import org.ednovo.gooru.shared.util.AttemptedAnswersDo;
import org.ednovo.gooru.shared.util.PlayerConstants;
import org.ednovo.gooru.shared.util.StringUtil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;

public class CollectionPlayerPresenter extends BasePlacePresenter<IsCollectionPlayerView, CollectionPlayerPresenter.IsCollectionPlayerProxy> implements CollectionPlayerUiHandlers{

	@Inject
	private PlayerAppServiceAsync playerAppService;
	
    private SimpleAsyncCallback<CollectionDo> collectionDetailsAsync;
    
    private CollectionPlayerMetadataPresenter metadataPresenter;
    
    private ResourcePlayerMetadataPresenter resoruceMetadataPresenter;
    
    private CollectionPlayerTocPresenter collectionPlayerTocPresenter;
    
    private ResourceNarrationPresenter resourceNarrationPresenter;
    
    private CollectionFormInPlayPresenter collectionFormInPlayPresenter;
    
    private CollectionSharePresenter collectionSharePresenter;
    
    private ResourceInfoPresenter resourceInfoPresenter;
    
    private AddResourceCollectionPresenter addResourcePresenter;
    
    private AddCollectionPresenter addCollectionPresenter;
    
    private CollectionFlagPresenter collectionFlagPresenter;
    
    private ResourceFlagPresenter resourceFlagPresenter;
    
    private SignUpPresenter signUpViewPresenter = null;
    
    private CollectionDo collectionDo=null;
    
    private CollectionItemDo collectionItemDo=null;
    
    private String collectionMetadataId=null;
    
    private String collectionSummaryId=null;
    
    private String collectionActivityEventId=null;
    
    private String resourceActivityEventId=null;
    
    private String resourceActivityResourceId=null;
    
    private String sessionId=null;
    
    private String sessionItemId=null;
    
    private String collectionDataLogEventId=null;
    
    private String collectionNewDataLogEventId=null;
    
    private String resourceDataLogEventId;
    
    private String resourceNewDataLogEventId;
    
    private Long collectionStartTime=0L;
    
    private Long collectionEndTime=0L;
    
    private Long totalTimeSpentOnSummaryPage=0L;
    
    private String collectionEndDate="";
    
    private Long resourceStartTime=0L;
    
    private Long hintOrExplanationStartTime=0L;
    
    private String hintOrExplanationEventName=null;
    
    private String hintOrExplanationEventId=null;
    
    private String resourcePlayEventName=null;
    
    private String classpageId=null;
       
	private Map<String,AttemptedAnswersDo> attemptAnswersMap=new HashMap<String,AttemptedAnswersDo>();
	
	private List<Integer> attemptTrySequence=new ArrayList<Integer>();

	private List<Integer> attemptStatus=new ArrayList<Integer>();

	private List<Integer> answerIds=new ArrayList<Integer>();
	
	private JSONObject answerIdsObject=new JSONObject();
	
	private JSONObject hintIdsObject=new JSONObject();
	
	private JSONObject explanationIdsObject=new JSONObject();
	
	private List<Integer> attemptTrySequenceArray=new ArrayList<Integer>();
	
	private Integer resourceScore=0;
	
	private Integer collectionScore=0;
	
	private Integer attemptCount=0;

	private List<Integer> attemptStatusArray=new ArrayList<Integer>();

	private String oeQuestionAnswerText="";

	private int hintId=0;

	private boolean isExplanationUsed=false;
	
	private boolean isUserAttemptedAnswer=false;
	
	private int userAttemptedQuestionType=0;
	
	private String questionType="RES";
	
	private boolean isOpenEndedAnswerSubmited=false;
	
	public static final  Object COLLECTION_PLAYER_TOC_PRESENTER_SLOT = new Object(); 
    
    public static final  Object METADATA_PRESENTER_SLOT = new Object();
    
    private static final String RESOURCE="resource";
    
    private static final String COLLECTION_RESOURCE_THUMBS_WIDGET_MODE="COLLECTION_RESOURCE_RATING";
    
    private static final String COLLECTION_RESOURCE_ADD_WIDGET="COLLECTION_RESOURCE_ADD";
    
    private static final String COLLECTION_RESOURCE_FLAG="COLLECTION_RESOURCE_FLAG";
    
    private static final String COLLECTION_FLAG="COLLECTION_FLAG";
    
    public static final String UPDATE_HEADER="UPDATE_HEADER";

    private static final String PRIVATE="private";
    
    /**
	 * @return the answerIdsObject
	 */
	public JSONObject getAnswerIdsObject() {
		return answerIdsObject;
	}

	/**
	 * @param answerIdsObject the answerIdsObject to set
	 */
	public void setAnswerIdsObject(JSONObject answerIdsObject) {
		this.answerIdsObject = answerIdsObject;
	}

	/**
	 * @return the hintIdsObject
	 */
	public JSONObject getHintIdsObject() {
		return hintIdsObject;
	}

	/**
	 * @param hintIdsObject the hintIdsObject to set
	 */
	public void setHintIdsObject(JSONObject hintIdsObject) {
		this.hintIdsObject = hintIdsObject;
	}

	/**
	 * @return the explanationIdsObject
	 */
	public JSONObject getExplanationIdsObject() {
		return explanationIdsObject;
	}

	/**
	 * @param explanationIdsObject the explanationIdsObject to set
	 */
	public void setExplanationIdsObject(JSONObject explanationIdsObject) {
		this.explanationIdsObject = explanationIdsObject;
	}
	
	/**
	 * @return the attemptTrySequenceArray
	 */
	public List<Integer> getAttemptTrySequenceArray() {
		return attemptTrySequenceArray;
	}

	/**
	 * @param attemptTrySequenceArray the attemptTrySequenceArray to set
	 */
	public void setAttemptTrySequenceArray(List<Integer> attemptTrySequenceArray) {
		this.attemptTrySequenceArray = attemptTrySequenceArray;
	}

	/**
	 * @return the attemptStatusArray
	 */
	public List<Integer> getAttemptStatusArray() {
		return attemptStatusArray;
	}

	/**
	 * @param attemptStatusArray the attemptStatusArray to set
	 */
	public void setAttemptStatusArray(List<Integer> attemptStatusArray) {
		this.attemptStatusArray = attemptStatusArray;
	}

	/**
	 * @return the resourceScore
	 */
	public Integer getResourceScore() {
		return resourceScore;
	}

	/**
	 * @param resourceScore the resourceScore to set
	 */
	public void setResourceScore(Integer resourceScore) {
		this.resourceScore = resourceScore;
	}

	/**
	 * @return the attemptCount
	 */
	public Integer getAttemptCount() {
		return attemptCount;
	}

	/**
	 * @param attemptCount the attemptCount to set
	 */
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

	/**
	 * @return the collectionScore
	 */
	public Integer getCollectionScore() {
		return collectionScore;
	}

	/**
	 * @param collectionScore the collectionScore to set
	 */
	public void setCollectionScore(Integer collectionScore) {
		this.collectionScore = collectionScore;
	}
	
	/**
	 * @return the isOpenEndedAnswerSubmited
	 */
	public boolean isOpenEndedAnswerSubmited() {
		return isOpenEndedAnswerSubmited;
	}

	/**
	 * @param isOpenEndedAnswerSubmited the isOpenEndedAnswerSubmited to set
	 */
	public void setOpenEndedAnswerSubmited(boolean isOpenEndedAnswerSubmited) {
		this.isOpenEndedAnswerSubmited = isOpenEndedAnswerSubmited;
	}
    
	@Inject
	public CollectionPlayerPresenter(CollectionPlayerMetadataPresenter metadataPresenter,ResourcePlayerMetadataPresenter resoruceMetadataPresenter,
			CollectionPlayerTocPresenter collectionPlayerTocPresenter,CollectionSharePresenter collectionSharePresenter,
			ResourceInfoPresenter resourceInfoPresenter,ResourceNarrationPresenter resourceNarrationPresenter,
			EventBus eventBus,IsCollectionPlayerView view, IsCollectionPlayerProxy proxy, AddResourceCollectionPresenter addResourcePresenter,
     		AddCollectionPresenter addCollectionPresenter,CollectionFormInPlayPresenter collectionFormInPlayPresenter,CollectionFlagPresenter collectionFlagPresenter,
     		ResourceFlagPresenter resourceFlagPresenter,SignUpPresenter signUpViewPresenter) {
		super(view, proxy);
		getView().setUiHandlers(this);
		this.metadataPresenter=metadataPresenter;
		this.resoruceMetadataPresenter=resoruceMetadataPresenter;
		this.collectionPlayerTocPresenter=collectionPlayerTocPresenter;
		this.resourceNarrationPresenter=resourceNarrationPresenter;
		this.collectionSharePresenter=collectionSharePresenter;
		this.resourceInfoPresenter=resourceInfoPresenter;
		this.addCollectionPresenter=addCollectionPresenter;
		this.addResourcePresenter=addResourcePresenter;
		this.collectionFormInPlayPresenter=collectionFormInPlayPresenter;
		this.collectionFlagPresenter=collectionFlagPresenter;
		this.resourceFlagPresenter=resourceFlagPresenter;
		this.signUpViewPresenter=signUpViewPresenter;
		resoruceMetadataPresenter.setCollectionPlayerPresnter(this,true);
		resourceFlagPresenter.setCollectionPlayerPresenter(this);
		collectionFlagPresenter.setCollectionPlayerPresenter(this);
		metadataPresenter.setCollectionPlayerPresenter(this);
		collectionPlayerTocPresenter.setCollectionPlayerPresnter(this);
		addResourcePresenter.getAddCollectionViewButton().setVisible(false);
		addCollectionPresenter.getAddResourceViewButton().setVisible(false);
		//addResourcePresenter.getAddCollectionViewButton().addClickHandler(new showAddCollectionView());
		//addCollectionPresenter.getAddResourceViewButton().addClickHandler(new showAddResourceView());
		addResourcePresenter.getAddNewCollectionButton().addClickHandler(new ShowNewCollectionWidget());
		getView().removeStudentViewButton();
		getView().hideFlagButton(false);
	}

	@ProxyCodeSplit
	@NameToken(PlaceTokens.COLLECTION_PLAY)
	public interface IsCollectionPlayerProxy extends ProxyPlace<CollectionPlayerPresenter> {
	}

	@Override
	protected void revealInParent() {
		RevealRootPopupContentEvent.fire(this, this);
	}

	@Override
	public void onBind() {
		super.onBind();
		addRegisteredHandler(ShowResourceViewEvent.TYPE, this);
		addRegisteredHandler(UpdateCollectionViewCountEvent.TYPE, this);
		addRegisteredHandler(ShowCollectionTabWidgetEvent.TYPE, this);
		addRegisteredHandler(RefreshCollectionInShelfListInPlayEvent.TYPE, this);
	}
	@Override
	protected void onReveal() {
		super.onReveal();
		Document doc=Document.get();
		Element bodyelement = doc.getBody();
		bodyelement.getParentElement().setAttribute("style", "overflow:hidden");
	}
	@Override
	protected void onReset() {
		super.onReset();
		Document doc=Document.get();
		Element bodyelement = doc.getBody();
		bodyelement.getParentElement().setAttribute("style", "overflow:hidden");
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		getCollectionDetails();
	}

	private void getClassPageDetails(String classItemId) {
		AppClientFactory.getInjector().getClasspageService().getClassPageItem(classItemId, new SimpleAsyncCallback<ClasspageItemDo>() {
			@Override
			public void onSuccess(ClasspageItemDo classpageItemDo) { 
				metadataPresenter.setTeacherInfo(classpageItemDo);
				classpageId=classpageItemDo.getClasspageId();
				metadataPresenter.getBackToClassButton().addClickHandler(new BackToClassHandler(classpageItemDo.getClasspageId()));
			}
		});
	}
	
	public void getClassPageId(String classItemId){
		AppClientFactory.getInjector().getClasspageService().getClassPageItem(classItemId, new SimpleAsyncCallback<ClasspageItemDo>() {
			@Override
			public void onSuccess(ClasspageItemDo classpageItemDo) { 
				classpageId=classpageItemDo.getClasspageId();
				setClasspageInsightsUrl();
			}
		});
	}
	public void getCollectionDetails(){
		final String collectionId=getPlaceManager().getRequestParameter("id", null);
		final String resourceId=getPlaceManager().getRequestParameter("rid", null);
		final String tabView=getPlaceManager().getRequestParameter("tab", null);
		final String apiKey=getPlaceManager().getRequestParameter("key", null);
		final String view=getPlaceManager().getRequestParameter("view", null);
		final String rootNodeId=getPlaceManager().getRequestParameter("rootNodeId", null);
		
		if(this.collectionDo!=null){
			if(resourceId!=null&&!resourceId.equals("")){
				showResourceView(resourceId,tabView);
				showTabWidget(tabView,collectionId,resourceId,false,false);
			}
			else if(collectionId!=null && !collectionId.equalsIgnoreCase("")){
				if(view!=null&&view.equalsIgnoreCase("end")){
					showCollectionEndView(collectionId, tabView);
					showTabWidget(tabView,collectionId,resourceId,false,true);
				}else{
					showCollectionMetadataView(tabView);
					showTabWidget(tabView,collectionId,resourceId,true,false);
				}
			}
		}else{
			if(collectionId!=null && !collectionId.equalsIgnoreCase("")){
				this.playerAppService.getSimpleCollectionDetils(apiKey,collectionId,resourceId,tabView, rootNodeId, new SimpleAsyncCallback<CollectionDo>() {
					@Override
					public void onSuccess(CollectionDo collectionDo) {
						if(collectionDo.getStatusCode()!=200){
							showCollectionErrorMessage();
						}else{
							setPageTitle(collectionDo);
							showCollectionView(collectionDo,collectionId,resourceId,tabView);
							setCollectionDetails(collectionDo);
						}
					}
				});
					if(!AppClientFactory.getPlaceManager().getRequestParameter("cid","").equals("")){
						getClassPageDetails(AppClientFactory.getPlaceManager().getRequestParameter("cid")); 
					}
				
			}
		}
	}
	
	/**
	 * @function setPageTitle 
	 *  
	 * @description : To set the Window Title.
	 *  
	 * @parm(s) : @param collectionDo {@link CollectionDo}
	 * 
	*/
	
	protected void setPageTitle(CollectionDo collectionDo) {
		AppClientFactory.setBrowserWindowTitle(SeoTokens.COLLECTION_PLAYER_TITLE+collectionDo.getTitle());
	}
	
	public void showCollectionView(CollectionDo collectionDo,String collectionId,String resourceId, String tabView){
		this.collectionDo=collectionDo;
		if(collectionId==null || collectionId.equalsIgnoreCase("")){
			return;
		}
		else{
			if(resourceId==null || resourceId.equalsIgnoreCase("")){
				String view=getPlaceManager().getRequestParameter("view", null);
				if(view!=null&&view.equalsIgnoreCase("end")){
					showCollectionEndView(collectionId, tabView);
					showTabWidget(tabView,collectionId,resourceId,false,true);
				}else{
					showCollectionMetadataView(tabView);
					showTabWidget(tabView,collectionId,resourceId,true,false);
				}
			}else{
				showResourceView(resourceId,tabView);
				showTabWidget(tabView,collectionId,resourceId,false,false);
			}
		}
	}

	public PlayerAppServiceAsync getPlayerAppService() {
		return playerAppService;
	}

	public void setPlayerAppService(PlayerAppServiceAsync playerAppService) {
		this.playerAppService = playerAppService;
	}

	public SimpleAsyncCallback<CollectionDo> getCollectionDetailsAsync() {
		return collectionDetailsAsync;
	}

	public void setCollectionDetailsAsync(SimpleAsyncCallback<CollectionDo> collectionDetailsAsync) {
		this.collectionDetailsAsync = collectionDetailsAsync;
	}
	protected void setCollectionDetails(CollectionDo collectionDo){
		if(collectionDo!=null){
			getView().setResourceTitle(collectionDo.getTitle());
		}
	}
	public void showCollectionMetadataView(String tabView){
		this.collectionItemDo=null;
		this.collectionSummaryId=null;
		//getView().hideFlagButton(false);
		getView().hidePlayerButtons(true, collectionDo.getGooruOid());
		setCollectionDetails(collectionDo);
		metadataPresenter.setCollectionMetadata(collectionDo);
		//clearDashBoardIframe();
		setClassCollectionDataInsightsUrl();
		showSignupPopup();
		setOpenEndedAnswerSubmited(true);
		if(this.collectionMetadataId!=null){
			if(this.collectionMetadataId.equalsIgnoreCase(collectionDo.getGooruOid())){
				makeButtonActive(tabView);
				return;
			}
		}
		this.collectionMetadataId=collectionDo.getGooruOid();
		clearIframeContent();
		if(StringUtil.isPartnerUser(collectionDo.getUser().getUsername()))
		getProfilUserVisibility(collectionDo.getUser().getGooruUId());
//	    enablePlayerButton(true,false, isSharable, false, true,false);
		if(!AppClientFactory.isAnonymous()){
			metadataPresenter.getFlagedReport(collectionDo.getGooruOid());
		}
		metadataPresenter.setPreviewHomePresenter();
		metadataPresenter.getBackToClassButton().setVisible(false);
		makeButtonActive(tabView);
		stopResourceDataLogFromHomePage();// if resource event is not stoped , then this method 
		                                  //will call resource stop event and collection stop data log event.
		attemptAnswersMap.clear();
		setUserAttemptedQuestionTypeAndStatus(false,0);
		resetAnswerLists();
		setInSlot(METADATA_PRESENTER_SLOT, metadataPresenter,false);
	}
	
	public void showResourceView(String collectionItemId,String tabView) {
		CollectionItemDo collectionItemDo=getCollectionItemDo(collectionItemId);
		this.collectionMetadataId=null;
		this.collectionSummaryId=null;
		/** Commented to implement new study end page **/
		//		getView().hideFlagButton(true);
		
		getView().hidePlayerButtons(false, collectionDo.getGooruOid());
		showSignupPopup();
		if(this.collectionItemDo!=null){
			if(this.collectionItemDo.getCollectionItemId().equalsIgnoreCase(collectionItemDo.getCollectionItemId())){
				makeButtonActive(tabView);
				return;
			}
		}
		//TODO need to check is collection sharable or not, need to enable narration button if narration exist.
		boolean isSharable=true;
		if(PRIVATE.equalsIgnoreCase(collectionDo.getSharing())){
			isSharable=false;
		}
		if(tabView!=null&&tabView.equals("narration")){
			enablePlayerButton(true,true, isSharable, true, true,true);
			makeButtonActive(tabView);
		}else{
			enablePlayerButton(true,true, isSharable, false, true,true);
			makeButtonActive(tabView);
		}
		clearIframeContent();
		this.collectionItemDo=collectionItemDo;
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
		updateResourceViewCount(collectionItemDo.getResource().getGooruOid(),collectionItemDo.getViews().toString(),RESOURCE,collectionItemId);
		setTotalTimeSpentOnSummaryPage();
		createPlayerDataLogs();
		PlaceRequest nextResoruceRequest=getNextButtonRequestUrl(collectionItemId);
		PlaceRequest previousResoruceRequest=getPreviousButtonRequestUrl(collectionItemId);
		if(!AppClientFactory.isAnonymous()){
			getReportData(collectionItemDo.getResource().getGooruOid());
		}
		setUserAttemptedQuestionTypeAndStatus(false,0);
		resoruceMetadataPresenter.showResourceWidget(collectionItemDo,nextResoruceRequest,previousResoruceRequest);
		if(!AppClientFactory.isAnonymous()){
			resoruceMetadataPresenter.setReaction(collectionItemDo); 
		}
		setOpenEndedAnswerSubmited(true);
		setInSlot(METADATA_PRESENTER_SLOT, resoruceMetadataPresenter);
		
	}
	public void showCollectionEndView(String collectionId,String tabView) {
		this.collectionMetadataId=null;
		this.collectionItemDo=null;
		MixpanelUtil.Study_Player_Reach_End();
		getView().hidePlayerButtons(true, collectionDo.getGooruOid());
		showClasspageButton();
		setOpenEndedAnswerSubmited(true);
		getView().setResourceTitle(collectionDo.getTitle());
		metadataPresenter.setCollectionDoOnRefresh(collectionDo);
		 showSignupPopup(); 
		if(this.collectionSummaryId!=null){
			if(this.collectionSummaryId.equalsIgnoreCase(collectionDo.getGooruOid())){
				makeButtonActive(tabView);
				return;
			}
		}
		clearDashBoardIframe();
		clearIframeContent();
		if(StringUtil.isPartnerUser(collectionDo.getUser().getUsername())){
			getProfilUserVisibility(collectionDo.getUser().getGooruUId());
		}
		metadataPresenter.setStudyEndPage();
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
		stopResourceDataLog();
		resetAnswerLists();
		stopCollectionDataLog();
		//insightsScheduler.schedule(3000);
		metadataPresenter.setDataInsightsSummaryUrl(sessionId);
		updateSession(sessionId);
		setUserAttemptedQuestionTypeAndStatus(false,0);
		setInSlot(METADATA_PRESENTER_SLOT, metadataPresenter,false);
	}
	public void clearDashBoardIframe(){
		metadataPresenter.clearDashBoardIframe();
	}
	Timer insightsScheduler=new Timer(){
		@Override
		public void run() {
			setDataInsighsUrl();
		}
	};
	
	public void setClassCollectionDataInsightsUrl(){
		if(!AppClientFactory.getPlaceManager().getRequestParameter("cid","").equals("")){
			if(classpageId!=null){
				setClasspageInsightsUrl();
			}else{
				getClassPageId(AppClientFactory.getPlaceManager().getRequestParameter("cid")); 
			}
		}else{
			metadataPresenter.setDataInsightsUrl();
		}
	}
	
	public void setClasspageInsightsUrl(){
		metadataPresenter.setClasspageInsightsUrl(classpageId);
	}
	
	public void setDataInsighsUrl(){
		metadataPresenter.setDataInsightsSummaryUrl(sessionId);

	}
	private void showClasspageButton(){
		String classpageItemId=getPlaceManager().getRequestParameter("cid", null);
		if(classpageItemId!=null&&!AppClientFactory.isAnonymous()){
			PlaceRequest playerPlaceRequest=AppClientFactory.getPlaceManager().getPreviousPlayerRequestUrl();
			String viewToken=playerPlaceRequest.getNameToken();
			if(viewToken.equals(PlaceTokens.EDIT_CLASSPAGE)||viewToken.equals(PlaceTokens.STUDENT)){
				metadataPresenter.getBackToClassButton().setVisible(true);
			}else{
				metadataPresenter.getBackToClassButton().setVisible(false);
			}
		}else{
			metadataPresenter.getBackToClassButton().setVisible(false);
		}
	}
	public void makeButtonActive(String tabView){
		if(tabView!=null){
			//getView().clearActiveButtion();
			if(tabView.equalsIgnoreCase("add")){
				ResourcePlayerMetadataView.addPadding();
				//CollectionPlayerMetadataView.addPadding();
				getView().clearActiveButton(false,true, true, true, true,false);
				getView().makeButtonActive(true, false,false, false, false,false);	
			}
			else if(tabView.equalsIgnoreCase("info")){
				ResourcePlayerMetadataView.addPadding();
				//CollectionPlayerMetadataView.addPadding();
				getView().clearActiveButton(true,false, true, true, true,false);
				getView().makeButtonActive(false,true, false, false, false,false);	
			}
			else if(tabView.equalsIgnoreCase("share")){
				ResourcePlayerMetadataView.addPadding();
				//CollectionPlayerMetadataView.addPadding();
				getView().clearActiveButton(true,true, false, true, true,false);
				getView().makeButtonActive(false,false, true, false, false,false);
			}
			else if(tabView.equalsIgnoreCase("narration")){
				getView().clearActiveButton(true,true, true, false, true,false);
				getView().makeButtonActive(false,false, false, true, false,false);
			}
			else if(tabView.equalsIgnoreCase("navigation")){
				ResourcePlayerMetadataView.addPadding();
				//CollectionPlayerMetadataView.addPadding();
				getView().clearActiveButton(true,true, true, true, false,false);
				getView().makeButtonActive(false,false, false, false, true,false);
				
			}else if(tabView.equalsIgnoreCase("flag")){
				getView().clearActiveButton(true,true, true, true, true,false);
				getView().makeButtonActive(false,false, false, false, false,true);
			}
		}
	}
	@Override
	public void showResourceView(Integer collectionItemSequence, boolean isForwardDirection) {
		//getCollectionItemDo(collectionItemSequence,isForwardDirection);
	}
	
	public void scrollStudyPage(){
		getView().scrollStudyPage();
	}

	public void showTabWidget(String tabView,String collectionId,String resourceId,boolean isCollectionHome,boolean isCollectionEnd){
		if(tabView==null||tabView.equals("")){
			getView().clearActiveButton(true,true, true, true, true,true);
			new CustomAnimation(getView().getNavigationContainer()).run(400);
		}
		else if(tabView.equals("add")){
			MixpanelUtil.mixpanelEvent("Player_Click_Add");
			if(AppClientFactory.isAnonymous()){
				 clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
				String checkCallbackParam = AppClientFactory.getPlaceManager().getRequestParameter("callback");
				if(checkCallbackParam == null || checkCallbackParam.isEmpty())
				{
					showLoginPopupWidget(COLLECTION_RESOURCE_ADD_WIDGET);
				}
			}else{
				if(isCollectionHome||isCollectionEnd){
					addCollectionPresenter.getAddResourceViewButton().setVisible(false);
					setAddCollectionView(collectionId);
				}else{
					addCollectionPresenter.getAddResourceViewButton().setVisible(true);
					setAddResourcesView(collectionId, resourceId);
				}
			}
		}
		else if(tabView.equals("navigation")){
			setNavigationResourcesView(collectionId,resourceId,isCollectionHome);
		}else if(tabView.equals("share")){
			setCollectionShareView(collectionId,resourceId);
		}else if(tabView.equals("narration")){
			showNarrationPopup(resourceId);
		 }else if(tabView.equals("info")){
			 setResourceInfoView(resourceId);
		 }else if(tabView.equals("flag")){
			 if(AppClientFactory.isAnonymous()&&(isCollectionHome||isCollectionEnd)){
				 clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
					String checkCallbackParam = AppClientFactory.getPlaceManager().getRequestParameter("callback");
					if(checkCallbackParam == null || checkCallbackParam.isEmpty())
					{
					showLoginPopupWidget(COLLECTION_FLAG);
					}
			 }else if(AppClientFactory.isAnonymous()){
				 clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
					String checkCallbackParam = AppClientFactory.getPlaceManager().getRequestParameter("callback");
					if(checkCallbackParam == null || checkCallbackParam.isEmpty())
					{
				 showLoginPopupWidget(COLLECTION_RESOURCE_FLAG);
					}
			 }else{
				 if(isCollectionHome||isCollectionEnd){
					 setCollectionFlagView(collectionId);
				 }else{
					 setResourceFlagView(resourceId);
				 }
			}
	 	}else if(tabView.equals("info")){
			setResourceInfoView(resourceId);
		}
		else{
			getView().getNavigationContainer().clear();
		}

	}
	protected void showSignupPopup() {
		if (AppClientFactory.getPlaceManager().getRequestParameter("callback") != null && AppClientFactory.getPlaceManager().getRequestParameter("callback").equalsIgnoreCase("signup")) {
			//To show SignUp (Registration popup)
			if (AppClientFactory.isAnonymous()){
				Window.enableScrolling(false);
				AppClientFactory.fireEvent(new SetHeaderZIndexEvent(98, false));
				String type = AppClientFactory.getPlaceManager().getRequestParameter("type") ;
				int displayScreen =AppClientFactory.getPlaceManager().getRequestParameter("type") !=null  ? Integer.parseInt(type) : 1;
				signUpViewPresenter.displayPopup(displayScreen);
				addToPopupSlot(signUpViewPresenter);
			}
		}
	}
	public class showAddResourceView implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			final String collectionId=getPlaceManager().getRequestParameter("id", null);
			final String resourceId=getPlaceManager().getRequestParameter("rid", null);
			setAddResourcesView(collectionId,resourceId);
		}
	}
	public class showAddCollectionView implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			final String collectionId=getPlaceManager().getRequestParameter("id", null);
			setAddCollectionView(collectionId);
		}
	}
	public CollectionItemDo getCollectionItemDo(String collectionItemId){
		
		if(collectionItemId!=null&&!collectionItemId.equalsIgnoreCase("")&&collectionDo!=null&&collectionDo.getGooruOid()!=null){
			
			for(int i=0;i<collectionDo.getCollectionItems().size();i++){
				CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(i);
				if(collectionItemId.equalsIgnoreCase(collectionItemDo.getCollectionItemId())){
					collectionItemDo.setItemSequence(i+1);
					return collectionItemDo;
					
				}
			}
		}
		return null;	
	}

	public void updatCollectionViewsCount(){
		if(collectionDo!=null&&collectionDo.getGooruOid()!=null){
			String viewsCount=collectionDo.getViews();
			Integer viewsCounts=Integer.parseInt(viewsCount)+1;
			collectionDo.setViews(viewsCounts.toString());
			metadataPresenter.setViewCount(viewsCounts.toString());
			try{
	    	  	AppClientFactory.fireEvent(new UpdateSearchResultMetaDataEvent(String.valueOf(viewsCounts), collectionDo.getGooruOid(), "views"));
	         }
			catch(Exception ex){}
		}
	}
	public void updateViewCount(String collectionItemId){
		if(collectionItemId!=null&&!collectionItemId.equalsIgnoreCase("")&&collectionDo!=null&&collectionDo.getGooruOid()!=null){
			for(int i=0;i<collectionDo.getCollectionItems().size();i++){
				CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(i);
				if(collectionItemId.equalsIgnoreCase(collectionItemDo.getCollectionItemId())){
					String viewsCount=collectionItemDo.getResource().getViews();
					Integer viewsCounts=Integer.parseInt(viewsCount)+1;
					collectionItemDo.getResource().setViews(viewsCounts.toString());
					resourceInfoPresenter.updateViewsCount(viewsCounts.toString());
					return;
				}
			}
		}
	}
	public PlaceRequest getNextButtonRequestUrl(String collectionItemId){
		if(collectionItemId!=null&&!collectionItemId.equalsIgnoreCase("")&&collectionDo!=null&&collectionDo.getGooruOid()!=null){
			for(int i=0;i<collectionDo.getCollectionItems().size();i++){
				CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(i);
				if(collectionItemId.equalsIgnoreCase(collectionItemDo.getCollectionItemId())){
					if(collectionDo.getCollectionItems().size()==(i+1)){
						Map<String,String> params = new LinkedHashMap<String,String>();
						params.put("id", collectionDo.getGooruOid());
						params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
						params.put("view", "end");
						PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
						return placeRequest;
					}else{
						collectionItemDo=collectionDo.getCollectionItems().get(i+1);
						return getResourcePlaceRequest(collectionItemDo);
					}
				}
			}
		}
		return null;
	}
	public PlaceRequest getPreviousButtonRequestUrl(){
		if(collectionDo!=null&&collectionDo.getGooruOid()!=null){
			if(collectionDo.getCollectionItems().size()>0){
				CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(collectionDo.getCollectionItems().size()-1);
				return getResourcePlaceRequest(collectionItemDo);		
			}else{
				Map<String,String> params = new LinkedHashMap<String,String>();
				params.put("id", collectionDo.getGooruOid());
				params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
				return placeRequest;
			}
		}
		return null;
	}
	public PlaceRequest getPreviousButtonRequestUrl(String collectionItemId){
		if(collectionItemId!=null&&!collectionItemId.equalsIgnoreCase("")&&collectionDo!=null&&collectionDo.getGooruOid()!=null){
			for(int i=0;i<collectionDo.getCollectionItems().size();i++){
				CollectionItemDo collectionItemDo=collectionDo.getCollectionItems().get(i);
				if(collectionItemId.equalsIgnoreCase(collectionItemDo.getCollectionItemId())){
					if(i==0){
						Map<String,String> params = new LinkedHashMap<String,String>();
						params.put("id", collectionDo.getGooruOid());
						params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
						PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
						return placeRequest;
					}else{
						collectionItemDo=collectionDo.getCollectionItems().get(i-1);
						return getResourcePlaceRequest(collectionItemDo);
					}
				}
			}
		}
		return null;
	}
	public void revealResourceView(CollectionItemDo collectionItemDo){
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("id", collectionDo.getGooruOid());
		params = PreviewPlayerPresenter.setConceptPlayerParameters(params);

		if(collectionItemDo.getNarration()!=null&&!collectionItemDo.getNarration().trim().equals("")){
			params.put("rid", collectionItemDo.getCollectionItemId());
			params.put("tab", "narration");
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest);
		}else{
			params.put("rid", collectionItemDo.getCollectionItemId());
			PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
			AppClientFactory.getPlaceManager().revealPlace(false, placeRequest);
		}
	}

	public PlaceRequest getResourcePlaceRequest(CollectionItemDo collectionItemDo){
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("id", collectionDo.getGooruOid());
		params = PreviewPlayerPresenter.setConceptPlayerParameters(params);
	
		if(collectionItemDo.getNarration()!=null&&!collectionItemDo.getNarration().trim().equals("")){
			params.put("rid", collectionItemDo.getCollectionItemId());
			params.put("tab", "narration");
			return AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
		}else{
			params.put("rid", collectionItemDo.getCollectionItemId());
			return AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
		}

	}

	public void setAddResourcesView(String collectionId,String resourceId){	
		addResourcePresenter.setCollectionItemData(collectionId, getCollectionItemDo(resourceId));
		setInSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT, addResourcePresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);		
	}
	public void setAddCollectionView(String collectionId){
		addCollectionPresenter.setCollectionDo(collectionDo);
		setInSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT, addCollectionPresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);		
	}

	public void setNavigationResourcesView(String collectionId,String resourceId,boolean isCollectionHome){
		collectionPlayerTocPresenter.setNavigationResources(collectionDo);
		collectionPlayerTocPresenter.setResourceActive(collectionId, resourceId, isCollectionHome);
		setInSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT, collectionPlayerTocPresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);

	}
	public void showNarrationPopup(String resourceId){
		CollectionItemDo collectionItemDo=getCollectionItemDo(resourceId);
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
		resourceNarrationPresenter.setNarrationMetadata(collectionItemDo,collectionDo.getUser().getUsernameDisplay(),collectionDo.getUser().getGooruUId());
		try{
			if(Document.get().getElementById("playerid")!=null){
		         final Element myPlayer = Document.get().getElementById("playerid");
		         if(myPlayer.getPropertyString("src").contains("youtube")){
		        	pauseVideo(myPlayer);
		         }
			}   
		}catch(Exception e){
			
		}
		addToPopupSlot(resourceNarrationPresenter);
	}
	
	public void setResourceInfoView(String resourceId){
		CollectionItemDo collectionItemDo=getCollectionItemDo(resourceId);
		resourceInfoPresenter.setResoruceDetails(collectionItemDo);
		setInSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT, resourceInfoPresenter,false);
		new CustomAnimation(getView().getNavigationContainer()).run(400);
	}
	public void setResourceFlagView(String resourceId){
		CollectionItemDo collectionItemDo=getCollectionItemDo(resourceId);
		resourceFlagPresenter.setFlagView(collectionItemDo);
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
		addToPopupSlot(resourceFlagPresenter);
	}
	public void setCollectionFlagView(String collectionId){
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
		collectionFlagPresenter.displayCollectionFlagData(collectionDo);
		addToPopupSlot(collectionFlagPresenter);
	}
	public void setCollectionShareView(String collectionId,String resourceId){
	   collectionSharePresenter.showShareView(resourceId!=null&&!resourceId.equalsIgnoreCase("")?true:false);
       if(resourceId!=null&&!resourceId.equalsIgnoreCase("")){
           CollectionItemDo collectionItemDo=getCollectionItemDo(resourceId);
           collectionSharePresenter.setResourceShareData(collectionItemDo.getResource().getGooruOid());
           collectionSharePresenter.showResourceData(collectionItemDo);
       }else{
    	   collectionSharePresenter.setCollectionShareData(collectionDo);
       }
       setInSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT, collectionSharePresenter,false);
       new CustomAnimation(getView().getNavigationContainer()).run(400);
	}

	public void clearTabSlot(){
		clearSlot(COLLECTION_PLAYER_TOC_PRESENTER_SLOT);
	}

	public void enablePlayerButton(boolean isAddButtonEnable,boolean isInfoButtonEnable, boolean isShareButtonEnable, boolean isNarrationButtonEnable, boolean isNavigationButtonEnable,boolean isFlagButtonEnable){
		getView().enablePlayerButton(isAddButtonEnable,isInfoButtonEnable, isShareButtonEnable, isNarrationButtonEnable, isNavigationButtonEnable,isFlagButtonEnable);
	}


	@Override
	public void updateViewsCount() {
		updateResourceViewCount(collectionDo.getGooruOid(),collectionDo.getViews(),RESOURCE);
	}

	public void createPlayerDataLogs(){
		if(collectionActivityEventId!=null&&!collectionActivityEventId.isEmpty()){
			stopResourceDataLog();
			createSessionAttemptTryWhenNavigation();
			resetAnswerLists();
			createResourceDataLog();
			createSessionItem();
		}else{
			if(collectionDo!=null){
				collectionActivityEventId=GwtUUIDGenerator.uuid();
				collectionDataLogEventId=GwtUUIDGenerator.uuid();
				collectionNewDataLogEventId=GwtUUIDGenerator.uuid();
				collectionStartTime=System.currentTimeMillis();
				PlayerDataLogEvents.collectionPlayStartEvent(collectionDataLogEventId, PlayerDataLogEvents.COLLECTION_PLAY_EVENT_NAME, "", PlayerDataLogEvents.OPEN_SESSION_STATUS, collectionDo.getGooruOid(), 
						PlayerDataLogEvents.START_EVENT_TYPE, collectionStartTime, collectionStartTime, 0L, AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid());
				startPlayerActivityEvent(collectionActivityEventId, "", PlayerConstants.COLLECTION_EVENT_NAME, collectionDo.getGooruOid(), collectionDo.getGooruOid(), PlayerConstants.COLLECTION_CONTEXT+collectionDo.getGooruOid(), getUserAgent());
				createSession(collectionDo.getGooruOid());
			}	
		}
	}

	public void createResourceDataLog(){
		resourceActivityEventId=GwtUUIDGenerator.uuid();
		resourceActivityResourceId=collectionItemDo.getResource().getGooruOid();
		String resourceContext=PlayerConstants.COLLECTION_CONTEXT+collectionDo.getGooruOid()+"/"+resourceActivityResourceId;
		startPlayerActivityEvent(resourceActivityEventId, collectionActivityEventId, PlayerConstants.RESOURCE_EVENT_NAME, collectionDo.getGooruOid(), resourceActivityResourceId, resourceContext, getUserAgent());
		startResourceInsightDataLog();
	}
	
	public void createSessionAttemptTryWhenNavigation(){
		if(isUserAttemptedAnswer()){
			resoruceMetadataPresenter.createSessionAttemptTryWhenNavigation(getUserAttemptedQuestionType());
		}
	}

	public void stopResourceDataLog(){
		if(resourceActivityResourceId!=null){
			String resourceContext=PlayerConstants.COLLECTION_CONTEXT+collectionDo.getGooruOid()+"/"+resourceActivityResourceId;
			stopPlayerActivityEvent(resourceActivityEventId, collectionActivityEventId, PlayerConstants.RESOURCE_EVENT_NAME, collectionDo.getGooruOid(), resourceActivityResourceId, resourceContext, getUserAgent());
			stopResourceInsightDataLog();
			resourceActivityResourceId=null;
		}
	}
	
	public void stopResourceDataLogFromHomePage(){
		if(resourceActivityResourceId!=null){
			String resourceContext=PlayerConstants.COLLECTION_CONTEXT+collectionDo.getGooruOid()+"/"+resourceActivityResourceId;
			stopPlayerActivityEvent(resourceActivityEventId, collectionActivityEventId, PlayerConstants.RESOURCE_EVENT_NAME, collectionDo.getGooruOid(), resourceActivityResourceId, resourceContext, getUserAgent());
			stopResourceInsightDataLog();
			resourceActivityResourceId=null;
			stopCollectionDataLog();
			collectionActivityEventId=null;
			collectionEndTime=0L;
			totalTimeSpentOnSummaryPage=0L;
		}
	}

	public void stopCollectionDataLog(){
		if(collectionActivityEventId!=null){
			stopPlayerActivityEvent(collectionActivityEventId, "", PlayerConstants.COLLECTION_EVENT_NAME, collectionDo.getGooruOid(), collectionDo.getGooruOid(), PlayerConstants.COLLECTION_CONTEXT+collectionDo.getGooruOid(), getUserAgent());
			collectionEndTime=System.currentTimeMillis();
			PlayerDataLogEvents.collectionPlayStartEvent(collectionDataLogEventId, PlayerDataLogEvents.COLLECTION_PLAY_EVENT_NAME, "", PlayerDataLogEvents.OPEN_SESSION_STATUS, collectionDo.getGooruOid(), 
					PlayerDataLogEvents.STOP_EVENT_TYPE, collectionStartTime, collectionEndTime, collectionEndTime-collectionStartTime-totalTimeSpentOnSummaryPage, AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid());
			triggerCollectionNewDataLogStartStopEvent(collectionStartTime,collectionEndTime,PlayerDataLogEvents.STOP_EVENT_TYPE,0);// TODO need to implement score
		}
	}
	
	public void stopCollectionDataLogs(){
		if(this.collectionDo!=null){
			stopResourceDataLogFromHomePage();
		}
	}

	public void startResourceInsightDataLog(){
		resourceDataLogEventId=GwtUUIDGenerator.uuid();
		resourceNewDataLogEventId=GwtUUIDGenerator.uuid();
		resourceStartTime=System.currentTimeMillis();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				questionType=PlayerDataLogEvents.getQuestionType(collectionItemDo.getResource().getType());
				if(collectionItemDo.getResource().getType()==6){
					resourcePlayEventName=PlayerDataLogEvents.COLLECTION_RESOURCE_OE_EVENT_NAME;
				}else{
					resourcePlayEventName=PlayerDataLogEvents.COLLECTION_QUESTION_PLAY_EVENT_NAME;
				}
			}else{
				resourcePlayEventName=PlayerDataLogEvents.COLLECTION_RESOUCE_PLAY_EVENT_NAME;
			}
		}
		PlayerDataLogEvents.resourcePlayStartStopEvent(resourceDataLogEventId, resourcePlayEventName, collectionDataLogEventId,resourceActivityResourceId,collectionDo.getGooruOid(), PlayerDataLogEvents.START_EVENT_TYPE, resourceStartTime,
				resourceStartTime, 0L,AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length());
		triggerCollectionItemNewDataLogStartStopEvent(resourceActivityResourceId,resourceStartTime, resourceStartTime, PlayerDataLogEvents.START_EVENT_TYPE, 0,questionType);
	}

	public void stopResourceInsightDataLog(){
		stopHintOrExplanationEvent();
		Long resourceEndTime=System.currentTimeMillis();
		PlayerDataLogEvents.resourcePlayStartStopEvent(resourceDataLogEventId, resourcePlayEventName, collectionDataLogEventId,resourceActivityResourceId,collectionDo.getGooruOid(), PlayerDataLogEvents.STOP_EVENT_TYPE, resourceStartTime,
				resourceEndTime,resourceEndTime-resourceStartTime,AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length());
		triggerCollectionItemNewDataLogStartStopEvent(resourceActivityResourceId,resourceStartTime, resourceEndTime, PlayerDataLogEvents.STOP_EVENT_TYPE, 0, questionType);
	}

	public void createSessionItem(){
		if(collectionItemDo!=null){
			createSessionItem(sessionId, collectionItemDo.getCollectionItemId(), collectionItemDo.getResource().getGooruOid());
		}
	}

	public void updateResourceViewCount(String gooruId,String viewsCount,String resourceType){
		this.playerAppService.updateViewCount(gooruId, viewsCount, resourceType, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				updatCollectionViewsCount();
			}
		});
	}
	public void updateResourceViewCount(String gooruId,String viewsCount,String resourceType,final String collectionItemId){
		this.playerAppService.updateViewCount(gooruId, viewsCount, resourceType, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				updateViewCount(collectionItemId);
			}
		});
	}

	public void startPlayerActivityEvent(String activityEventId,String activityParentEventId,String eventName,String gooruOid,String resourceGooruOid,
			String context,String userAgent){
		this.playerAppService.startActivityPlayerLog(activityEventId, activityParentEventId, eventName, gooruOid, 
				resourceGooruOid, context, userAgent, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String activityEventId) {

			}
		});
	}
	public void stopPlayerActivityEvent(String activityEventId,String activityParentEventId,String eventName,String gooruOid,String resourceGooruOid,
			String context,String userAgent){
		this.playerAppService.stopActivityPlayerLog(activityEventId, activityParentEventId, eventName, gooruOid, 
				resourceGooruOid, context, userAgent, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String activityEventId) {

			}
		});
	}

	public void createSession(String collectionGooruOid){
		this.playerAppService.createSessionTracker(collectionGooruOid, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionId) {
				CollectionPlayerPresenter.this.sessionId=sessionId;
				triggerCollectionNewDataLogStartStopEvent(collectionStartTime,collectionStartTime,PlayerDataLogEvents.START_EVENT_TYPE,0);
				createResourceDataLog();
				if(collectionItemDo!=null){
					createSessionItem(sessionId, collectionItemDo.getCollectionItemId(), collectionItemDo.getResource().getGooruOid());
				}
			}
		});
	}

	public void createSessionItem(String sessionTrackerId,String collectionItemId, String resourceGooruOid){
		this.playerAppService.createSessionItemInCollection(sessionTrackerId, collectionItemId, resourceGooruOid, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {
				CollectionPlayerPresenter.this.sessionItemId=sessionItemId;
			}
		});
	}

	public void createSessionItemAttempt(int answerId, String attemptResult){
		this.playerAppService.createSessionItemAttemptTry(sessionId, sessionItemId, answerId, attemptResult, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {}
		});
	}

	public void createSessionItemAttemptOe(String answerId,String attemptStatus,String attemptAnswerResult){
		this.playerAppService.createSessionItemAttemptTryForOe(sessionId, sessionItemId,answerId, attemptStatus,attemptAnswerResult, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String sessionItemId) {
				
			}
		});
	}

	public void updateSession(String sessionTrackerId){
		if(sessionTrackerId!=null){
			this.playerAppService.updateSessionInCollection(sessionTrackerId, new SimpleAsyncCallback<String>() {
				@Override
				public void onSuccess(String sessionItemId) {}
			});
		}
	}

	public void setUserAttemptResult(String collectionItemId,AttemptedAnswersDo attemptAnswersDo){
		attemptAnswersMap.put(collectionItemId, attemptAnswersDo);
	}

	public void removeUserAttemptResult(){
		attemptAnswersMap.remove(collectionItemDo.getCollectionItemId());
	}

	public Map<String, AttemptedAnswersDo> getAttemptAnswersMap() {
		return attemptAnswersMap;
	}
	
	public void increaseUserAttemptCount(){
		setAttemptCount(getAttemptCount()+1);
	}

	public String getUserAgent(){
		return Window.Navigator.getUserAgent();
	}

	public void resetAnswerLists(){
		attemptTrySequence.clear();
		attemptStatus.clear();
		answerIds.clear();
		oeQuestionAnswerText="";
		isExplanationUsed=false;
		hintId=0;
		hintOrExplanationEventName=null;
		questionType="RES";
		hintIdsObject=new JSONObject();
		explanationIdsObject=new JSONObject();
		answerIdsObject=new JSONObject();
		attemptStatusArray.clear();
		attemptTrySequenceArray.clear();
		setResourceScore(0);
		setAttemptCount(0);
	}

	public void setAnswerAttemptSequence(int attemptSequence,int attemptStatus,int answerId){
		this.attemptTrySequence.add(attemptSequence);
		this.attemptStatus.add(attemptStatus);
		this.answerIds.add(answerId);
	}

	public void setOeQuestionAnswerText(String oeAnswerText){
		this.oeQuestionAnswerText=oeAnswerText;
	}
	public void startHintDataLogEvent(int hintId){
		stopHintOrExplanationEvent();
		this.hintId=hintId;
		hintOrExplanationStartTime=System.currentTimeMillis();
		hintOrExplanationEventId=GwtUUIDGenerator.uuid();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				if(collectionItemDo.getResource().getType()==6){
					hintOrExplanationEventName=PlayerDataLogEvents.COLLECTION_RESOURCE_OE_HINT_EVENT_NAME;
				}else{
					hintOrExplanationEventName=PlayerDataLogEvents.COLLECTION_RESOURCE_HINT_EVENT_NAME;
				}
			}
		}
		PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, hintOrExplanationEventName, resourceDataLogEventId, resourceActivityResourceId,
				collectionDo.getGooruOid(), PlayerDataLogEvents.START_EVENT_TYPE, hintOrExplanationStartTime, hintOrExplanationStartTime, 0L, AppClientFactory.getLoginSessionToken(),
				AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
	}

	public void startExplanationDataLogEvent(){
		stopHintOrExplanationEvent();
		isExplanationUsed=true;
		hintOrExplanationEventId=GwtUUIDGenerator.uuid();
		hintOrExplanationStartTime=System.currentTimeMillis();
		if(collectionItemDo!=null){
			if(collectionItemDo.getResource().getResourceType().getName().equalsIgnoreCase("assessment-question")){
				if(collectionItemDo.getResource().getType()==6){
					hintOrExplanationEventName=PlayerDataLogEvents.COLLECTION_RESOURCE_OE_EXPLANATION_EVENT_NAME;
				}else{
					hintOrExplanationEventName=PlayerDataLogEvents.COLLECTION_RESOURCE_EXPLANATION_EVENT_NAME;
				}
			}
		}
		PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, hintOrExplanationEventName, resourceDataLogEventId, resourceActivityResourceId, 
				collectionDo.getGooruOid(), PlayerDataLogEvents.START_EVENT_TYPE, hintOrExplanationStartTime,hintOrExplanationStartTime, 0L, AppClientFactory.getLoginSessionToken(),
				AppClientFactory.getGooruUid(), isExplanationUsed,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
	}

	public void saveOeQuestionAnswerDataLogEvent(){
		stopHintOrExplanationEvent();
		hintOrExplanationEventName=null;
		String submitEventId=GwtUUIDGenerator.uuid();
		Long answerEndTime=System.currentTimeMillis();
		PlayerDataLogEvents.submitOeAnswerDataLogEvent(submitEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_OE_SUBMIT_EVENT_NAME, resourceDataLogEventId,
				resourceActivityResourceId,resourceStartTime,answerEndTime, answerEndTime-resourceStartTime,
				AppClientFactory.getLoginSessionToken(), AppClientFactory.getGooruUid(),attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),collectionDo.getGooruOid());
	}

	public void stopHintOrExplanationEvent(){
		if(hintOrExplanationEventName!=null){
			Long endTime=System.currentTimeMillis();
			Long spendTime=endTime-hintOrExplanationStartTime;
			if(hintOrExplanationEventName.equals(PlayerDataLogEvents.COLLECTION_RESOURCE_EXPLANATION_EVENT_NAME)){
				PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_EXPLANATION_EVENT_NAME, resourceDataLogEventId, resourceActivityResourceId, 
						collectionDo.getGooruOid(), PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime,endTime, spendTime, AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), true,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
			}else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.COLLECTION_RESOURCE_HINT_EVENT_NAME)){
				PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_HINT_EVENT_NAME, resourceDataLogEventId, resourceActivityResourceId,
						collectionDo.getGooruOid(), PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime, endTime, spendTime,  AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
			}
			else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.COLLECTION_RESOURCE_OE_EXPLANATION_EVENT_NAME)){
				PlayerDataLogEvents.explanationButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_OE_EXPLANATION_EVENT_NAME, resourceDataLogEventId, resourceActivityResourceId, 
						collectionDo.getGooruOid(), PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime,endTime, spendTime, AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), true,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),hintId);
			}else if(hintOrExplanationEventName.equals(PlayerDataLogEvents.COLLECTION_RESOURCE_OE_HINT_EVENT_NAME)){
				PlayerDataLogEvents.hintsButtonDataLogEvent(hintOrExplanationEventId, PlayerDataLogEvents.COLLECTION_RESOURCE_OE_HINT_EVENT_NAME, resourceDataLogEventId, resourceActivityResourceId,
						collectionDo.getGooruOid(), PlayerDataLogEvents.STOP_EVENT_TYPE, hintOrExplanationStartTime, endTime, spendTime, AppClientFactory.getLoginSessionToken(),
						AppClientFactory.getGooruUid(), 0, hintId,attemptTrySequence,attemptStatus, answerIds,oeQuestionAnswerText,oeQuestionAnswerText.length(),isExplanationUsed);
			}
		}
	}

	public void getProfilUserVisibility(final String gooruUid){
		this.playerAppService.getUserProfileVisibility(gooruUid, new SimpleAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					metadataPresenter.setUserProfileName(gooruUid);
				}
			}
		});
	}

	@Override
	public String getViewToken() {
		return PlaceTokens.COLLECTION_PLAY;
	}


	@Override
	public void showLoginPopupWidget(String widgetMode){
		LoginPopupUc popup =new LoginPopupUc();
		popup.setWidgetMode(widgetMode);
		popup.setGlassEnabled(true);
	}

	public class ShowNewCollectionWidget implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			String resourceId=collectionItemDo.getResource().getGooruOid();
			addToPopupSlot(collectionFormInPlayPresenter);
			collectionFormInPlayPresenter.setResourceUid(resourceId);
			collectionFormInPlayPresenter.setPlayerType("collection");
		}
	}

	public void updateAddResourceCollectionWidget(String collectionId){
		addResourcePresenter.updateWorkSpaceLink(collectionId);
	}

	@Override
	public void refreshCollectionInShelfListInPlay(String collectionId) {
		updateAddResourceCollectionWidget(collectionId);
	}

	@Override
	public void updateResourceThumbsRating(final int userThumbsRataing) {
		String resourceGooruOid=getPlaceManager().getRequestParameter("rid", null);
		if(resourceGooruOid!=null){
			CollectionItemDo colectionItemDo=getCollectionItemDo(resourceGooruOid);
			resourceGooruOid=colectionItemDo.getResource().getGooruOid();
		}else{
			resourceGooruOid=getPlaceManager().getRequestParameter("id", null);
		}
		this.playerAppService.updateContentThumbsRating(resourceGooruOid, userThumbsRataing, new SimpleAsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				updateThumbsRatingView(userThumbsRataing);
			}
		});
	}

	public void updateThumbsRatingView(int userThumbsRataing) {
		updateResourceLikes(userThumbsRataing);
		getView().updateThumbsRatingView(userThumbsRataing);
	}

	public void updateResourceLikes(int userThumbsRataing){
		String resourceGooruOid=getPlaceManager().getRequestParameter("rid", null);
		if(resourceGooruOid!=null){
			CollectionItemDo collectionItemDo=getCollectionItemDo(resourceGooruOid);
			int resourceLikes=collectionItemDo.getRating().getVotesUp();
			int userRating=collectionItemDo.getResource().getUserRating();
			if(userThumbsRataing==1){
				resourceLikes=resourceLikes+1;
			}else if((userThumbsRataing==0||userThumbsRataing==-1)&&userRating==1){
				resourceLikes=resourceLikes-1;
			}
			resourceLikes=resourceLikes<0?0:resourceLikes;
			resourceInfoPresenter.updateLikesCount(resourceLikes);
			collectionItemDo.getResource().setUserRating(userThumbsRataing);
			collectionItemDo.getRating().setVotesUp(resourceLikes);
		}else{
			resourceGooruOid=getPlaceManager().getRequestParameter("id", null);
			int resourceLikes=collectionDo.getMetaInfo().getRating().getVotesUp();
			int userRating=collectionDo.getUserRating();
			if(userThumbsRataing==1){
				resourceLikes=resourceLikes+1;
			}else if((userThumbsRataing==0||userThumbsRataing==-1)&&userRating==1){
				resourceLikes=resourceLikes-1;
			}
			resourceLikes=resourceLikes<0?0:resourceLikes;
			metadataPresenter.updateLikesCount(resourceLikes);
			collectionDo.setUserRating(userThumbsRataing);
			collectionDo.getMetaInfo().getRating().setVotesUp(resourceLikes);
		}		
	}

	public void showTabWidget(String widgetMode,boolean isLoginRequestCancel) {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("id", collectionDo.getGooruOid());
		params = PreviewPlayerPresenter.setConceptPlayerParameters(params);

		if(isLoginRequestCancel){
			String collectionId=getPlaceManager().getRequestParameter("id", null);
			String collectionItemId=getPlaceManager().getRequestParameter("rid", null);
			if(collectionItemId!=null){
				params.put("rid", collectionItemDo.getCollectionItemId());
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			}else{
				String view=getPlaceManager().getRequestParameter("view", null);
				if(view!=null){
					params.put("view", "end");
				}
				PlaceRequest placeRequest=AppClientFactory.getPlaceManager().preparePlaceRequest(PlaceTokens.COLLECTION_PLAY, params);
				AppClientFactory.getPlaceManager().revealPlace(false, placeRequest, true);
			}
		}
		else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(COLLECTION_RESOURCE_THUMBS_WIDGET_MODE)){
			//getCollection();
		}else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(COLLECTION_RESOURCE_FLAG)){
			String collectionItemId=getPlaceManager().getRequestParameter("rid", null);
			CollectionItemDo collectionItemDo=getCollectionItemDo(collectionItemId);
			getReportData(collectionItemDo.getResource().getGooruOid());
			setResourceFlagView(collectionItemId);
			updateHeaderView();
			updateCollectionSummary();
		}else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(COLLECTION_RESOURCE_ADD_WIDGET)){
			String collectionItemId=getPlaceManager().getRequestParameter("rid", null);
			if(collectionItemId!=null){
				CollectionItemDo collectionItemDo=getCollectionItemDo(collectionItemId);
				getReportData(collectionItemDo.getResource().getGooruOid());
				setAddResourcesView(getPlaceManager().getRequestParameter("id", null), collectionItemId);
			}else{
				setAddCollectionView(getPlaceManager().getRequestParameter("id", null));
			}
			updateHeaderView();
			updateCollectionSummary();
		}else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(COLLECTION_FLAG)){
			//metadataPresenter.getFlagCollectionId(collectionDo.getGooruOid());
			setCollectionFlagView(collectionDo.getGooruOid());
			updateHeaderView();
			updateCollectionSummary();
		}else if(!isLoginRequestCancel&&widgetMode.equalsIgnoreCase(UPDATE_HEADER)){
			updateHeaderView();
			updateCollectionSummary();
		}
	}
	public void updateHeaderView(){
		getView().updateAuthorDetails();
	}
	public void updateCollectionSummary(){
		metadataPresenter.setDataInsightsUrl();
		metadataPresenter.setDataInsightsSummaryUrl(sessionId);
	}

	public void updateFlagImageOnHomeView(){
		metadataPresenter.getFlagedReport(collectionDo.getGooruOid());
	}
	public void updateFlagImageOnEndView(){
		resoruceMetadataPresenter.updateFlagImageOnEndView();
	}

	public void getCollection(){
		String collectionId=getPlaceManager().getRequestParameter("id", null);
		final String rootNodeId=getPlaceManager().getRequestParameter("rootNodeId", null);
		this.playerAppService.getSimpleCollectionDetils(null,collectionId,null,null, rootNodeId, new SimpleAsyncCallback<CollectionDo>() {
			@Override
			public void onSuccess(CollectionDo collectionDo) {
				if(collectionDo.getStatusCode()!=200){
					showCollectionErrorMessage();
				}else{
					CollectionPlayerPresenter.this.collectionDo=collectionDo;
					updateThumbsRatingView(collectionDo.getUserRating());
				}
			}
		});
	}
	
	public class BackToClassHandler implements ClickHandler{
		private String classPageId=null;
		public BackToClassHandler(String classPageId){
			this.classPageId=classPageId;
		}
		@Override
		public void onClick(ClickEvent event) {
			String page=AppClientFactory.getPlaceManager().getRequestParameter("page",null);
			revealTeachOrStudypage(page);
		}
	}
	public void revealTeachOrStudypage(String page){
		getView().showClasspage(classpageId,page);
	}
	
	/**
	 * Sets collection not exists view.
	 */
	
	protected void showCollectionErrorMessage(){
		clearSlot(METADATA_PRESENTER_SLOT);
		enablePlayerButton(false, false, false, false, false, false);
		setOpenEndedAnswerSubmited(true);
		getView().getPlayerBodyContainer().clear();
		getView().getPlayerBodyContainer().add(new CollectionNonExistView());
	}

	public void resetCollectionPlayer(){
		if(collectionDo!=null){
			stopCollectionDataLogs();
			getView().hidePlayerButtons(true,null);
			collectionDo=null;
			collectionItemDo=null;
			collectionMetadataId=null;
			collectionSummaryId=null;
			collectionActivityEventId=null;
			collectionEndTime=0L;
			totalTimeSpentOnSummaryPage=0L;
			resourceActivityEventId=null;
			resourceActivityResourceId=null;
			sessionId=null;
			sessionItemId=null;
			collectionDataLogEventId=null;
			resourceDataLogEventId=null;
			collectionNewDataLogEventId=null;
			resourceNewDataLogEventId=null;
			collectionStartTime=0L;
			resourceStartTime=0L;
			hintOrExplanationStartTime=0L;
			hintOrExplanationEventName=null;
			hintOrExplanationEventId=null;
			resourcePlayEventName=null;
			classpageId=null;
			attemptAnswersMap.clear();
			attemptTrySequence.clear();
			attemptStatus.clear();
			answerIds.clear();
			oeQuestionAnswerText="";
			hintId=0;
			questionType="RES";
			hintIdsObject=new JSONObject();
			explanationIdsObject=new JSONObject();
			answerIdsObject=new JSONObject();
			attemptStatusArray.clear();
			attemptTrySequenceArray.clear();
			setResourceScore(0);
			setAttemptCount(0);
			setCollectionScore(0);
			isExplanationUsed=false;
			getView().hideFlagButton(false);
			getView().setResourceTitle("");
			enablePlayerButton(false, false, false, false, false,false);
			getView().resetThumbsButtons();
			clearTabSlot();
			clearSlot(METADATA_PRESENTER_SLOT);
			metadataPresenter.resetMetadataFields();
			clearIframeContent();
			resoruceMetadataPresenter.resetResourceMetaData();
			resourceInfoPresenter.resetResourceInfo();
			collectionPlayerTocPresenter.clearNavigationPanel();
			setOpenEndedAnswerSubmited(true);
		}
}
	 private Timer sessionTimeoutResponseTimer = new Timer() {
	        public void run() {
	        //	setNarrationView(gblResourceId);
	    		Document doc =Document.get();
		        final Element myPlayer = doc.getElementById("playerid");
				pauseVideo(myPlayer);
	        }
	    };
	public static native void pauseVideo(Element myPlayer) /*-{
	myPlayer.pauseVideo();
	}-*/;
	public static native void playVideo(Element myPlayer) /*-{
	myPlayer.playVideo();
}-*/;

	@Override
	public void getReportData(String associatedGooruOid) {
		//getCollectionItemDo(associatedGooruOid).getResource().getGooruOid()
		playerAppService.getContentReport(associatedGooruOid, AppClientFactory.getGooruUid(), new SimpleAsyncCallback<ArrayList<ContentReportDo>>() {
			@Override
			public void onSuccess(ArrayList<ContentReportDo> result) {
				String gooruFlagId="";
				if(result!=null&&result.size()>0){
					for(int i =0;i<result.size();i++){
						gooruFlagId = gooruFlagId+result.get(i).getDeleteContentGooruOid();
						if(result.size()!=(i+1)){
							gooruFlagId=gooruFlagId+",";
						}
					}
					getView().makeFlagButtonOrange();
					resourceFlagPresenter.setContentDeleteIds(gooruFlagId);
				 }
			}
		});
		
	}
	public void clearIframeContent(){
		resoruceMetadataPresenter.clearIfrmaeContent();
	}
	
	public void setCollectionEndDate(){
		Date collectionCompleteDate = new Date();
	    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
		String[] completedDateTime = dateTimeFormat.format(collectionCompleteDate).toString().split(" ");
		collectionEndDate = completedDateTime[0] +" at "+completedDateTime[1] +" "+completedDateTime[2];
	}
	
	public String getCollectionEndDate(){
		return collectionEndDate;
	}
	
	public String getUserSpentTimeOnCollection(){
		Long spentTime=collectionEndTime-collectionStartTime;
		return spentTime.toString();
	}
	
	public void resetcollectionActivityEventId(){
		this.collectionActivityEventId=null;
		collectionEndTime=0L;
		totalTimeSpentOnSummaryPage=0L;
	}

	/**
	 * @return the isUserAttemptedAnswer
	 */
	public boolean isUserAttemptedAnswer() {
		return isUserAttemptedAnswer;
	}

	/**
	 * @param isUserAttemptedAnswer the isUserAttemptedAnswer to set
	 */
	public void setUserAttemptedAnswer(boolean isUserAttemptedAnswer) {
		this.isUserAttemptedAnswer = isUserAttemptedAnswer;
	}
	
   /**
	 * @return the userAttemptedQuestionType
	 */
	public int getUserAttemptedQuestionType() {
		return userAttemptedQuestionType;
	}

	/**
	 * @param userAttemptedQuestionType the userAttemptedQuestionType to set
	 */
	public void setUserAttemptedQuestionType(int userAttemptedQuestionType) {
		this.userAttemptedQuestionType = userAttemptedQuestionType;
	}
	
	public void setUserAttemptedQuestionTypeAndStatus(boolean isUserAttemptedThisQuestion,int questionType){
		setUserAttemptedAnswer(isUserAttemptedThisQuestion);
		setUserAttemptedQuestionType(questionType);
	}
	public void triggerSaveOeAnswerTextDataEvent(){
		String oeDataLogEventId=GwtUUIDGenerator.uuid();
		Long oeStartTime=System.currentTimeMillis();
		triggerSaveOeAnswerTextDataEvent(oeDataLogEventId,resourceActivityResourceId,oeStartTime,oeStartTime,0);
	}
	public void triggerCollectionNewDataLogStartStopEvent(Long collectionStartTime,Long collectionEndTime,String eventType,Integer score){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(collectionNewDataLogEventId));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.COLLECTION_PLAY));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(collectionStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(collectionEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		//String classpageId=AppClientFactory.getPlaceManager().getDataLogClasspageId();
		String classpageEventId=AppClientFactory.getPlaceManager().getClasspageEventId();
		String path="";
		if(classpageId!=null&&!classpageId.equals("")){
			path=classpageId+"/"+collectionDo.getGooruOid();
		}else{
			path=collectionDo.getGooruOid();
		}
		String playerMode=getPlayerMode();
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(collectionDo.getGooruOid(), classpageId, classpageEventId, eventType, playerMode,"",null,path,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(collectionEndTime-collectionStartTime-totalTimeSpentOnSummaryPage, getCollectionScore()));
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,new JSONString(new JSONObject().toString()));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public void triggerCollectionItemNewDataLogStartStopEvent(String resourceId,Long resourceStartTime,Long resourceEndTime,String eventType,Integer score,String questionType){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(resourceNewDataLogEventId));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.COLLECTION_RESOURCE_PLAY));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(resourceStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(resourceEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		String questionTypeString=questionType.equals("RES")?"resource":"question";
		//String classpageId=AppClientFactory.getPlaceManager().getDataLogClasspageId();
		String path="";
		if(classpageId!=null&&!classpageId.equals("")){
			path=classpageId+"/"+collectionDo.getGooruOid()+"/"+resourceId;
		}else{
			path=collectionDo.getGooruOid()+"/"+resourceId;
		}
		String playerMode=getPlayerMode();
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(resourceId,collectionDo.getGooruOid(), collectionNewDataLogEventId, eventType, playerMode,questionTypeString,null,path,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(resourceEndTime-resourceStartTime, getResourceScore()));
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getDataLogPayLoadObject(questionType,oeQuestionAnswerText,attemptStatusArray,attemptTrySequenceArray,answerIdsObject,hintIdsObject,explanationIdsObject,getAttemptCount()));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public void triggerSaveOeAnswerTextDataEvent(String eventId,String resourceId,Long oeStartTime,Long oeEndTime,int score){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(eventId));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(PlayerDataLogEvents.COLLECTION_RESOURCE_SAVE));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(oeStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(oeEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		//String classpageId=AppClientFactory.getPlaceManager().getDataLogClasspageId();
		String path="";
		if(classpageId!=null&&!classpageId.equals("")){
			path=classpageId+"/"+collectionDo.getGooruOid()+"/"+resourceId;
		}else{
			path=collectionDo.getGooruOid()+"/"+resourceId;
		}
		String playerMode=getPlayerMode();
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(resourceId,collectionDo.getGooruOid(), resourceNewDataLogEventId, "", playerMode,"question",null,path,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(oeEndTime-oeStartTime, 0));
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getDataLogPayLoadObject(questionType,oeQuestionAnswerText,attemptStatusArray,attemptTrySequenceArray,answerIdsObject,hintIdsObject,explanationIdsObject,getAttemptCount()));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public void triggerReactiontDataLogEvent(String resourceId,Long reactionStartTime,Long reactionEndTime,String reactionType,String eventName){
		JSONObject collectionDataLog=new JSONObject(); 
		collectionDataLog.put(PlayerDataLogEvents.EVENTID, new JSONString(GwtUUIDGenerator.uuid()));
		collectionDataLog.put(PlayerDataLogEvents.EVENTNAME, new JSONString(eventName));
		collectionDataLog.put(PlayerDataLogEvents.SESSION, PlayerDataLogEvents.getDataLogSessionObject(sessionId));
		collectionDataLog.put(PlayerDataLogEvents.STARTTIME, new JSONNumber(reactionStartTime));
		collectionDataLog.put(PlayerDataLogEvents.ENDTIME, new JSONNumber(reactionEndTime));
		collectionDataLog.put(PlayerDataLogEvents.USER, PlayerDataLogEvents.getDataLogUserObject());
		String questionTypeString=questionType.equals("RES")?"resource":"question";
		//String classpageId=AppClientFactory.getPlaceManager().getDataLogClasspageId();
		String path="";
		if(classpageId!=null&&!classpageId.equals("")){
			path=classpageId+"/"+collectionDo.getGooruOid()+"/"+resourceId;
		}else{
			path=collectionDo.getGooruOid()+"/"+resourceId;
		}
		String playerMode=getPlayerMode();
		collectionDataLog.put(PlayerDataLogEvents.CONTEXT, PlayerDataLogEvents.getDataLogContextObject(resourceId,collectionDo.getGooruOid(), resourceNewDataLogEventId, "", playerMode,questionTypeString,reactionType,path,null));
		collectionDataLog.put(PlayerDataLogEvents.VERSION,PlayerDataLogEvents.getDataLogVersionObject());
		collectionDataLog.put(PlayerDataLogEvents.METRICS,PlayerDataLogEvents.getDataLogMetricsObject(reactionEndTime-reactionStartTime, 0));
		collectionDataLog.put(PlayerDataLogEvents.PAYLOADOBJECT,PlayerDataLogEvents.getDataLogPayLoadObject(reactionType));
		PlayerDataLogEvents.collectionStartStopEvent(collectionDataLog);
	}
	public String getPlayerMode(){
		String playerMode="";
		String page=getPlaceManager().getRequestParameter("page",null);
		if(page!=null){
			if(page.equals("teach")){
				playerMode="preview";
			}else{
				playerMode="study";
			}
		}else{
			playerMode=AppClientFactory.getPlaceManager().getPlayerModeInTeach();
		}
		return playerMode;
	}
	
	public void setTotalTimeSpentOnSummaryPage(){
		if(collectionEndTime!=0L){
			Long currentTime=System.currentTimeMillis();
			totalTimeSpentOnSummaryPage=totalTimeSpentOnSummaryPage+(currentTime-collectionEndTime);
			collectionEndTime=0L;
		}
	}

}
