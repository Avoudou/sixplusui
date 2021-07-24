package pokerAppUi;

import hands.*;

import java.util.*;

import pokerAppUi.uiElements.HandElement;
import ranges.SimpleLinearRange;
import solve_results.ShoveFoldNodeResults;

public class AdjustedRangeData {

    private ShoveFoldNodeResults nodeResults;
    private SimpleLinearRange originalRange;
    private SimpleLinearRange adjustedRange;

    private boolean isModified = false;

    public AdjustedRangeData(ShoveFoldNodeResults nodeResults, SimpleLinearRange originalRange) {
        this.nodeResults = nodeResults;
        this.originalRange = originalRange;
    }

    public ShoveFoldNodeResults getNodeResults() {
        return nodeResults;
    }

    public SimpleLinearRange getOriginalRange() {
        return originalRange;
    }

    public SimpleLinearRange getAdjustedRange() {
        return adjustedRange;
    }

    public SimpleLinearRange getCurrentRange() {
        return adjustedRange == null ? originalRange : adjustedRange;
    }

    public void setAdjustedRange(SimpleLinearRange adjustedRange) {
        this.adjustedRange = adjustedRange;
        isModified = true;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public ArrayList<ArrayList<HandElement>> createOriginalRangeData() {
        return createRangeData(originalRange);
    }

    /** Creates data for the original range if the range has not been modified */
    public ArrayList<ArrayList<HandElement>> createAdjustedRangeData() {
        SimpleLinearRange range = getCurrentRange();
        return createRangeData(range);
    }

    private ArrayList<ArrayList<HandElement>> createRangeData(SimpleLinearRange range) {
        ArrayList<ArrayList<HandElement>> rangeData = new ArrayList<ArrayList<HandElement>>();
        ArrayList<Rank> ranks = new ArrayList<Rank>(Arrays.asList(Rank.values()));
        Collections.reverse(ranks);
        for (Rank rankA : ranks) {
            ArrayList<HandElement> innerList = new ArrayList<HandElement>();
            for (Rank rankB : ranks) {
                boolean isSuited = rankA.ordinal() < rankB.ordinal();
                PreflopHand hand = new PreflopHand(rankA, rankB, isSuited);
                int handInt = HandUtils.preflop_stringToInt(hand.toString());
                double playedFraction = range.getFraction(handInt);
                double evDifference = nodeResults.getEvDifference(handInt);
                innerList.add(new HandElement(hand.toString(), evDifference, playedFraction > 0.5));
            }
            rangeData.add(innerList);
        }
        return rangeData;
    }

    public void resetRange() {
        adjustedRange = null;
        isModified = false;
    }

}
