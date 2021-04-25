package com.ichi2.anki.services.reviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ichi2.anki.CollectionHelper;
import com.ichi2.anki.Reviewer;
import com.ichi2.libanki.sched.DeckDueTreeNode;

import java.util.List;

import timber.log.Timber;

public class ScreenOnEventReceiver extends BroadcastReceiver {

    public void onReceive(final Context context, final Intent intent) {
        Timber.i("onReceive");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (isAnyDeckHasCardsToStudy(context)) {
                Intent i = new Intent(context, Reviewer.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("AUTO_RUN_REVIEWER", true);
                context.startActivity(i);
            }
        }
    }

    private boolean isAnyDeckHasCardsToStudy(final Context context) {
        List<DeckDueTreeNode> nodes = CollectionHelper.getInstance().getCol(context).getSched().deckDueTree();
        return nodes.stream().anyMatch(n -> n.getLrnCount() + n.getNewCount() + n.getRevCount() > 0);
    }

}