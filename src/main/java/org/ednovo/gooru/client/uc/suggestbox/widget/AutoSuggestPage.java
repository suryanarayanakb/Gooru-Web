package org.ednovo.gooru.client.uc.suggestbox.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
/**
 * 
 * @fileName : AutoSuggestPage.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Jan 29, 2014
 *
 * @Author Gooru Team
 *
 * @Reviewer:
 */
public class AutoSuggestPage extends DialogBox {
    FlowPanel container;

    public AutoSuggestPage() {
        super(false, true);
        setText("");
        container = new FlowPanel();
        container.setStyleName("auto_suggest");

        container.add(new AutoSuggestForm());
        add(container);
        center();
        show();
    }

    public class AutoSuggestForm extends Composite {
        FlowPanel form;

        public AutoSuggestForm() {
            form = new FlowPanel();
            form.setStyleName("form");
            initWidget(form);
            form.add(new InputListWidget());
        }

        public void onSubmit(DomEvent<EventHandler> event) {
            // no-op
        }
    }

    public class InputListWidget extends Composite {
        List<String> itemsSelected = new ArrayList<String>();

        public InputListWidget() {
            FlowPanel panel = new FlowPanel();
            initWidget(panel);

            final BulletList list = new BulletList();
            list.setStyleName("token-input-list-gooru");
            final ListItem item = new ListItem();
            item.setStyleName("token-input-input-token-gooru");
            final TextBox itemBox = new TextBox();
            itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
            final SuggestBox suggestBox = new SuggestBox(getSuggestions(), itemBox);
            suggestBox.getElement().setId("suggestion_box");
            item.add(suggestBox);
            list.add(item);

            // this needs to be on the itemBox rather than box, or backspace will get executed twice
            itemBox.addKeyDownHandler(new KeyDownHandler() {
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                        if ("".equals(itemBox.getValue().trim())) {
                            ListItem li = (ListItem) list.getWidget(list.getWidgetCount() - 2);
                            Paragraph p = (Paragraph) li.getWidget(0);
                            if (itemsSelected.contains(p.getText())) {
                                itemsSelected.remove(p.getText());
                                GWT.log("Removing selected item '" + p.getText() + "'", null);
                                GWT.log("Remaining: " + itemsSelected, null);
                            }
                            list.remove(li);
                            itemBox.setFocus(true);
                        }
                    }
                }
            });

            suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
                public void onSelection(SelectionEvent selectionEvent) {
                    deselectItem(itemBox, list);
                }
            });

            panel.add(list);

            panel.getElement().setAttribute("onclick", "document.getElementById('suggestion_box').focus()");
            suggestBox.setFocus(true);
        }

        public void deselectItem(final TextBox itemBox, final BulletList list) {
            if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {

                final ListItem displayItem = new ListItem();
                displayItem.setStyleName("token-input-token-gooru");
                Paragraph p = new Paragraph(itemBox.getValue());

                displayItem.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent clickEvent) {
                        displayItem.addStyleName("token-input-selected-token-gooru");
                    }
                });

                Span span = new Span("x");
                span.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent clickEvent) {
                        removeListItem(displayItem, list);
                    }
                });

                displayItem.add(p);
                displayItem.add(span);
                // hold the original value of the item selected

                itemsSelected.add(itemBox.getValue());

                list.insert(displayItem, list.getWidgetCount() - 1);
                itemBox.setValue("");
                itemBox.setFocus(true);
            }
        }

        public void removeListItem(ListItem displayItem, BulletList list) {
            itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
            list.remove(displayItem);
        }
    }
    public MultiWordSuggestOracle getSuggestions() {
    	//TODO this needs to be populated from API
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("Amy Kesic");
        oracle.add("Jason Weston");
        oracle.add("Dave Johnson");
        oracle.add("Paul Hammant");
        oracle.add("Jesse Kuhnert");
        oracle.add("Ben Alex");
        oracle.add("Tom Bender");
        oracle.add("Alexandru Popescu");
        oracle.add("Kaveh Arabfakhry");
        oracle.add("Steven Hong");
        oracle.add("Jason van Zyl");
        oracle.add("Alex Vauthey");
        oracle.add("Kiran Karnati");
        oracle.add("Kalpana Nagireddy");
        oracle.add("Ramnivas Laddad");
        oracle.add("Amy Anne Rasberry");
        oracle.add("Vincent Stoessel");
        oracle.add("Steven Leija");
        oracle.add("Brian Burke");
        oracle.add("John Ipson");
        oracle.add("Candy Chastain Mielke");
        oracle.add("Scott Mark");
        oracle.add("Dov B. Katz");
        oracle.add("Alef Arendsen");
        oracle.add("David Jencks");
        oracle.add("Alexey Belikov");
        oracle.add("Bryan Vial");
        oracle.add("Dror Bereznitsky");
        oracle.add("David Moskowitz");
        oracle.add("Oscar Chan");
        oracle.add("Sergey Sundukovskiy");
        oracle.add("John Newton");
        oracle.add("Chris Buzzetta");
        oracle.add("Peter Svensson");
        oracle.add("Riccardo Ferretti");
        oracle.add("Christian Parker");
        oracle.add("Ann (Jaksa) Skaehill");
        oracle.add("Justin Blue");
        oracle.add("Sean Dawson");
        oracle.add("Devaraj NS");
        oracle.add("Robert Gadd");
        oracle.add("Diego Campodonico");
        oracle.add("Bryan Field-Elliot");
        oracle.add("Scott Delap");
        oracle.add("Kevin Koster");
        oracle.add("Fernand Galiana");
        oracle.add("Christopher Shuler");
        oracle.add("Geir Magnusson Jr");
        oracle.add("Tyler Hansen");
        oracle.add("Olivier Lamy");
        oracle.add("J. Thomas Richardson");
        oracle.add("Russell Beattie");
        oracle.add("Martin Ouellet");
        oracle.add("Scott Ferguson");
        oracle.add("Guillaume Laforge");
        oracle.add("Eric Weidner");
        oracle.add("Troy McKinnon");
        oracle.add("Max Hays");
        oracle.add("Phillip Rhodes");
        oracle.add("Eugene Kulechov");
        oracle.add("Bob Johnson");
        oracle.add("Richard Tucker, PMP");
        oracle.add("Mats Henricson");
        oracle.add("Floyd Marinescu");
        oracle.add("Ed Burns");
        oracle.add("Michael Root");
        oracle.add("Dana Busch");
        oracle.add("Borislav Roussev");
        oracle.add("Harris Tsim");
        oracle.add("Jason Thrasher");
        oracle.add("Soo-il Kim");
        oracle.add("Lindsey Bowman");
        oracle.add("Ganesh Hariharan");
        oracle.add("Judy Herilla");
        oracle.add("Jevgeni Kabanov");
        oracle.add("Craig Whitacre");
        oracle.add("Paul M. Garvey");
        oracle.add("Jeremy Whitlock");
        oracle.add("Fabrizio Giustina");
        oracle.add("Todd Fredrich");
        oracle.add("Matt Stine");
        oracle.add("Yassine Hinnach");
        oracle.add("Chris Huston");
        oracle.add("Jodi Behrens-Stark");
        oracle.add("John Greenhill");
        oracle.add("Roy Porter");
        oracle.add("Paul Tuckey");
        oracle.add("Arjun Ram");
        oracle.add("Merrill Bennett");
        oracle.add("James Richards");
        oracle.add("Franz Garsombke");
        oracle.add("Kimberly Horan");
        oracle.add("Hani Suleiman");
        oracle.add("Thomas Dudziak");
        oracle.add("Andrew Penrose");
        oracle.add("Igor Polyakov");
        oracle.add("Steve Runkel");

        return oracle;
    }

}