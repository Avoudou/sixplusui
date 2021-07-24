package conversions;

import hacks.ArrayUtilsWrapper;
import hands.*;

import java.util.*;

import pokerAppUi.AdjustedRangeData;
import pokerAppUi.uiElements.HandElement;
import ranges.HandOrdering;
import ranges.SimpleLinearRange;

import comparators.HandOrderingPositionIntComparator;

import constants.SixPlusConstants;

public class RangeConversions {

    public static SimpleLinearRange convertToRange(ArrayList<HandElement> selectedHands,
                                                   AdjustedRangeData nodeData) {
        int[] selectedHandIds = new int[selectedHands.size()];
        for (int i = 0; i < selectedHands.size(); i++) {
            HandElement element = selectedHands.get(i);
            selectedHandIds[i] = HandUtils.preflop_stringToInt(element.getHandName());
        }
        HandOrdering ordering = nodeData.getOriginalRange().getHandOrdering();
        HandOrderingPositionIntComparator.sort(selectedHandIds, ordering.getHandPositions());

        int[] handOrderingIds = new int[SixPlusConstants.NUM_PREFLOP_HANDS];
        Arrays.fill(handOrderingIds, -1);
        System.arraycopy(selectedHandIds, 0, handOrderingIds, 0, selectedHandIds.length);
        int index = selectedHandIds.length;
        for (int hand : ordering.getHands()) {
            if (!ArrayUtilsWrapper.contains(handOrderingIds, hand)) {
                handOrderingIds[index++] = hand;
            }
        }
        HandOrdering newOrdering = HandOrdering.createHandOrdering(handOrderingIds);
        SimpleLinearRange newRange = new SimpleLinearRange(newOrdering, 0, selectedHandIds.length);
        return newRange;
    }

    public static ArrayList<HandElement> getSelectedHands(SimpleLinearRange range) {
        ArrayList<HandElement> selected = new ArrayList<>();
        for (int i = 0; i < range.getNumHands(); i++) {
            int handId = range.getHandOrdering().getHands()[i];
            String handString = HandUtils.preflop_intToString(handId);
            selected.add(new HandElement(handString));
        }
        return selected;
    }
    
    public static ArrayList<ArrayList<HandElement>> createSampleRangeData(SimpleLinearRange range) {
        ArrayList<ArrayList<HandElement>> rangeData = new ArrayList<ArrayList<HandElement>>();
        ArrayList<Rank> ranks = new ArrayList<Rank>(Arrays.asList(Rank.values()));
        Collections.reverse(ranks);
        for (Rank rankA : ranks) {
            ArrayList<HandElement> innerList = new ArrayList<HandElement>();
            for (Rank rankB : ranks) {
                boolean isSuited = rankA.ordinal() < rankB.ordinal();
                PreflopHand hand = new PreflopHand(rankA, rankB, isSuited);
                innerList.add(new HandElement(hand.toString()));
            }
            rangeData.add(innerList);
        }
        return rangeData;
    }
}
