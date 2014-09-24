/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.wildapps.push;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Card Examples
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class CardFragment extends BaseFragment {

    protected ScrollView mScrollView;
    protected TextView mTextViewSwipe;

    protected ActionMode mActionMode;
    protected Card mCardCab;
    protected CardView cardViewCab;

    @Override
    public int getTitleResourceId() {
        return R.string.card_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment_card, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActionMode!=null)
            mActionMode.finish();
    }

    private void initCards() {
        init_simple_card();
    }

    /**
     * This method builds a simple card
     */
    private void init_simple_card() {

        //Create a Card
        Card card = new Card(getActivity());

        //Create a CardHeader
        CardHeader header = new CardHeader(getActivity());

        //Set the header title
        header.setTitle("Testing");

        card.addCardHeader(header);

        //Set the card inner text
        card.setTitle("This should be the body of the text");

        //Set card in the cardView
        CardView cardView = (CardView) getActivity().findViewById(R.id.carddemo_card_id);
        cardView.setCard(card);
    }


}
